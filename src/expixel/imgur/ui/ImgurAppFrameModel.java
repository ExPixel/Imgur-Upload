package expixel.imgur.ui;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Stack;

import javax.imageio.ImageIO;

import net.iharder.dnd.FileDrop;
import expixel.imgur.Imgur;
import expixel.imgur.database.ImgurDatabase;
import expixel.imgur.database.ImgurImage;

public class ImgurAppFrameModel {
	ImgurAppFrame appFrame;
	PinDialog pinDialog;
	LimitReachedDialog limitDialog;

	volatile Stack<File> filesToUpload = new Stack<File>();
	volatile File currentlyUploading;

	public ImgurAppFrameModel(ImgurAppFrame appFrame) {
		this.bind(appFrame);
	}

	public void bind(ImgurAppFrame frame) {
		this.appFrame = frame;
		this.bindDropModel();
		this.bindLoginModel();

		this.loadImages();
	}

	private void bindDropModel() {
		new FileDrop(this.appFrame.getImageDropPanel(), new FileDrop.Listener() {
			@Override
			public void filesDropped(final File[] files) {
				if(Imgur.uploadLimitReached()) {
					ImgurAppFrameModel.this.showLimitReachedDialog();
					return;
				}
				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						for(File file : files)
							ImgurAppFrameModel.this.addFileToUploadStack(file);
					}
				});
				t.start();
			}
		});
	}

	public void addFileToUploadStack(File file) {
		this.filesToUpload.add(file);
		this.consumeUploadStack();
	}

	public void consumeUploadStack() {
		if(this.currentlyUploading != null || this.filesToUpload.isEmpty()) {
			this.appFrame.setTitle("Imgur Upload");
			return;
		} else {
			this.currentlyUploading = this.filesToUpload.pop();
			Runnable r = new Runnable() {
				@Override
				public void run() {
					ImgurAppFrameModel.this.uploadCurrentFile();
				}
			};
			new Thread(r).start();
		}
	}

	public void uploadCurrentFile() {
		this.appFrame.setTitle(String.format("Imgur Upload - (%d remaining) Uploading %s",
				this.filesToUpload.size(), this.currentlyUploading.getName()));
		ImgurImage img = Imgur.uploadFile(this.currentlyUploading);
		this.addUploadImage(img);
		this.refreshUploadsPanel();
		this.currentlyUploading = null;
		this.consumeUploadStack();
	}

	private void bindLoginModel() {
		this.logInStatus();
		this.appFrame.getLoginButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ImgurAppFrameModel.this.attemptLogin();
			}
		});
	}

	public void attemptLogin() {
		if(Imgur.isLoggedIn()) {
			Imgur.logOut();
		} else {
			PinDialog pdialog = this.getPinDialog();
			pdialog.setVisible(true);
			if(pdialog.okayPressed()) {
				String pin = pdialog.getPinTextField().getText();
				Imgur.auth(pin);
			}
		}
		this.logInStatus();
	}

	public void logInStatus() {
		if(Imgur.isLoggedIn()) {
			this.statusLoggedIn();
		} else {
			this.statusNotLoggedIn();
		}
	}

	public void statusLoggedIn() {
		this.appFrame.getLoginButton().setText("Logout");
		this.appFrame.getLoginButton().initRedTheme();
		this.appFrame.getLoggedInLabel().setText("Logged in as " + Imgur.getUsername());
		this.appFrame.getTabbedPane().addTab("Uploads", this.appFrame.getUploadsTabPanel());
	}

	public void statusNotLoggedIn() {
		this.appFrame.getLoginButton().setText("Login");
		this.appFrame.getLoginButton().initBlueTheme();
		this.appFrame.getLoggedInLabel().setText("");
		this.removeTabComponent(this.appFrame.getUploadsTabPanel());
	}

	public void removeTabComponent(Component c) {
		this.appFrame.getTabbedPane().remove(c);
	}

	public PinDialog getPinDialog() {
		if(this.pinDialog == null) {
			this.pinDialog = new PinDialog(this.appFrame);
			this.pinDialog.getPinNumberGetButton().addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								System.out.println("Browsing...");
								URL url = new URL(Imgur.authLink());
								Desktop.getDesktop().browse(url.toURI());
							} catch (IOException | URISyntaxException e1) {
								e1.printStackTrace();
							}
						}
					}).start();
				}
			});
		}
		return this.pinDialog;
	}

	public LimitReachedDialog getLimitReachedDialog() {
		if(this.limitDialog == null) {
			this.limitDialog = new LimitReachedDialog(this.appFrame);
		}
		return this.limitDialog;
	}

	public void showLimitReachedDialog() {
		this.getLimitReachedDialog().setVisible(true);
	}

	public void loadImages() {
		ImgurImage[] imgs = ImgurDatabase.getImgurImages(0, -1);

		for(ImgurImage img : imgs) {
			this.addUploadImage(img);
		}

		this.refreshUploadsPanel();
	}

	public void refreshUploadsPanel() {
		this.appFrame.getUploadsList().revalidate();
		this.appFrame.getUploadsList().repaint();
	}

	public void addUploadImage(ImgurImage img) {
		if(img == null) return;
		ImgurImagePanel panel = this.createImagePanel(img);
		this.appFrame.getUploadsList().add(panel, 0); // Add it to the front.
	}

	private ImgurImagePanel createImagePanel(ImgurImage img) {
		if(img == null) return null;

		System.out.println("Loading Panel: " + img.getId());

		ImgurImagePanel panel = new ImgurImagePanel();
		panel.getLinkField().setText(img.getLink());
		panel.getImagePanel().setImage(this.getImageFor(img));

		panel.setImgurImage(img);

		this.attachPanelDeleteEvent(panel);
		return panel;
	}

	private BufferedImage getImageFor(ImgurImage i) {
		if(i == null) return null;
		if(i.getImageFile() != null && i.getImageFile().exists()) {
			try {
				return ImageIO.read(i.getImageFile());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public void attachPanelDeleteEvent(final ImgurImagePanel panel) {
		panel.getDeleteButton().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ImgurAppFrameModel.this.deleteImgurImagePanel(panel);
			}
		});
	}

	public void deleteImgurImagePanel(final ImgurImagePanel panel) {

		if(panel.getImgurImage() != null) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					ImgurDatabase.deleteID(panel.getImgurImage().getId());
					Imgur.deleteImage(panel.getImgurImage());
				}
			}).start();
		}
		this.appFrame.getUploadsList().remove(panel);
		this.refreshUploadsPanel();
	}
}
