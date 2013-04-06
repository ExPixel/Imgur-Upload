package expixel.imgur.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import net.miginfocom.swing.MigLayout;
import expixel.imgur.custom.components.MetroButton;
import expixel.imgur.database.ImgurImage;

public class ImgurImagePanel extends JPanel {
	private JPanel panel;
	private JTextField linkField;
	private MetroButton copyButton;
	private MetroButton deleteButton;
	private ImagePanel imagePanel;

	ImgurImage imgurImage;

	/**
	 * Create the panel.
	 */
	public ImgurImagePanel() {
		this.setBorder(new LineBorder(Color.LIGHT_GRAY, 2));
		this.setBackground(new Color(245, 245, 245));
		this.setLayout(new BorderLayout(0, 0));
		this.setPreferredSize(new Dimension(196, 164));
		this.add(this.getPanel(), BorderLayout.SOUTH);
		this.add(this.getImagePanel(), BorderLayout.CENTER);

	}

	public JPanel getPanel() {
		if (this.panel == null) {
			this.panel = new JPanel();
			this.panel.setOpaque(false);
			this.panel.setLayout(new MigLayout("", "[grow][]", "[][]"));
			this.panel.add(this.getLinkField(), "cell 0 0 2 1,growx");
			this.panel.add(this.getCopyButton(), "cell 0 1");
			this.panel.add(this.getDeleteButton(), "cell 1 1");
		}
		return this.panel;
	}
	public JTextField getLinkField() {
		if (this.linkField == null) {
			this.linkField = new JTextField();
			this.linkField.setEditable(false);
			this.linkField.setColumns(10);
		}
		return this.linkField;
	}
	public MetroButton getCopyButton() {
		if (this.copyButton == null) {
			this.copyButton = new MetroButton();
			this.copyButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ImgurImagePanel.this.copyToClipboard();
				}
			});
			this.copyButton.setIcon(new ImageIcon(ImgurImagePanel.class.getResource("/expixel/imgur/ui/assets/clipboard-paste-document-text.png")));
			this.copyButton.setText("Copy");
			this.copyButton.initGreenTheme();
		}
		return this.copyButton;
	}
	public MetroButton getDeleteButton() {
		if (this.deleteButton == null) {
			this.deleteButton = new MetroButton();
			this.deleteButton.setIcon(new ImageIcon(ImgurImagePanel.class.getResource("/expixel/imgur/ui/assets/bin--arrow.png")));
			this.deleteButton.setText("Delete");
			this.deleteButton.initRedTheme();
		}
		return this.deleteButton;
	}
	public ImagePanel getImagePanel() {
		if (this.imagePanel == null) {
			this.imagePanel = new ImagePanel();
			this.imagePanel.setOpaque(false);
		}
		return this.imagePanel;
	}

	public void copyToClipboard() {
		String text = this.getLinkField().getText();
		StringSelection ss = new StringSelection(text);
		Clipboard board = Toolkit.getDefaultToolkit ().getSystemClipboard ();
		board.setContents(ss, null);
	}

	/**
	 * @return the imgurImage
	 */
	public ImgurImage getImgurImage() {
		return this.imgurImage;
	}

	/**
	 * @param imgurImage the imgurImage to set
	 */
	public void setImgurImage(ImgurImage imgurImage) {
		this.imgurImage = imgurImage;
	}
}
