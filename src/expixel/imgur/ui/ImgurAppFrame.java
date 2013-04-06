package expixel.imgur.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import expixel.imgur.custom.components.MetroButton;
import expixel.imgur.custom.components.MetroTabbedPane;

public class ImgurAppFrame extends JFrame {

	private JPanel contentPane;
	private MetroTabbedPane tabbedPane;
	private JPanel fileDropTabPanel;
	private JPanel imageDropPanel;
	private JLabel lblDropFilesHere;
	private JPanel panel_2;
	private MetroButton loginButton;
	private JPanel uploadsTabPanel;
	private JScrollPane scrollPane;
	private JPanel uploadsList;
	private JLabel loggedInLabel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					ImgurAppFrame frame = new ImgurAppFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ImgurAppFrame() {
		this.setTitle("Imgur Uploader");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 450, 300);
		this.contentPane = new JPanel();
		this.contentPane.setBackground(Color.WHITE);
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setContentPane(this.contentPane);
		this.contentPane.setLayout(new BorderLayout(0, 0));
		this.contentPane.add(this.getTabbedPane(), BorderLayout.CENTER);
		this.contentPane.add(this.getPanel_2(), BorderLayout.SOUTH);
	}

	public MetroTabbedPane getTabbedPane() {
		if (this.tabbedPane == null) {
			this.tabbedPane = new MetroTabbedPane();
			this.tabbedPane.setFont(new Font("Tahoma", Font.PLAIN, 11));
			this.tabbedPane.setBackground(Color.WHITE);
			this.tabbedPane.addTab("File Drop", null, this.getFileDropTabPanel(), null);
			this.tabbedPane.addTab("Uploads", null, this.getUploadsTabPanel(), null);
		}
		return this.tabbedPane;
	}
	public JPanel getFileDropTabPanel() {
		if (this.fileDropTabPanel == null) {
			this.fileDropTabPanel = new JPanel();
			this.fileDropTabPanel.setBackground(Color.WHITE);
			this.fileDropTabPanel.setLayout(new BorderLayout(0, 0));
			this.fileDropTabPanel.add(this.getImageDropPanel(), BorderLayout.CENTER);
		}
		return this.fileDropTabPanel;
	}
	public JPanel getImageDropPanel() {
		if (this.imageDropPanel == null) {
			this.imageDropPanel = new JPanel();
			this.imageDropPanel.setBackground(Color.WHITE);
			this.imageDropPanel.setBorder(BorderFactory.createDashedBorder(Color.lightGray, 4, 8, 4, true));
			this.imageDropPanel.setLayout(new BorderLayout(0, 0));
			this.imageDropPanel.add(this.getLblDropFilesHere(), BorderLayout.CENTER);
		}
		return this.imageDropPanel;
	}
	public JLabel getLblDropFilesHere() {
		if (this.lblDropFilesHere == null) {
			this.lblDropFilesHere = new JLabel("Drop Images Here");
			this.lblDropFilesHere.setForeground(Color.LIGHT_GRAY);
			this.lblDropFilesHere.setBackground(Color.WHITE);
			this.lblDropFilesHere.setFont(new Font("Tahoma", Font.PLAIN, 36));
			this.lblDropFilesHere.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return this.lblDropFilesHere;
	}
	public JPanel getPanel_2() {
		if (this.panel_2 == null) {
			this.panel_2 = new JPanel();
			this.panel_2.setBackground(Color.WHITE);
			this.panel_2.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
			this.panel_2.add(this.getLoggedInLabel());
			this.panel_2.add(this.getLoginButton());
		}
		return this.panel_2;
	}
	public MetroButton getLoginButton() {
		if (this.loginButton == null) {
			this.loginButton = new MetroButton();
			this.loginButton.setText("Login");
			this.loginButton.initBlueTheme();
		}
		return this.loginButton;
	}
	public JPanel getUploadsTabPanel() {
		if (this.uploadsTabPanel == null) {
			this.uploadsTabPanel = new JPanel();
			this.uploadsTabPanel.setBackground(Color.WHITE);
			this.uploadsTabPanel.setLayout(new BorderLayout(0, 0));
			this.uploadsTabPanel.add(this.getScrollPane(), BorderLayout.CENTER);
			this.uploadsTabPanel.setVisible(false);
		}
		return this.uploadsTabPanel;
	}
	public JScrollPane getScrollPane() {
		if (this.scrollPane == null) {
			this.scrollPane = new JScrollPane();
			this.scrollPane.setBackground(Color.WHITE);
			this.scrollPane.setViewportView(this.getUploadsList());
			this.scrollPane.getVerticalScrollBar().setUnitIncrement(16);
			this.scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
		}
		return this.scrollPane;
	}
	public JPanel getUploadsList() {
		if (this.uploadsList == null) {
			this.uploadsList = new JPanel();
			this.uploadsList.setBackground(Color.WHITE);
			ModifiedFlowLayout mfl_uploadsList = new ModifiedFlowLayout();
			mfl_uploadsList.setAlignment(FlowLayout.LEADING);
			this.uploadsList.setLayout(mfl_uploadsList);
		}
		return this.uploadsList;
	}
	public JLabel getLoggedInLabel() {
		if (this.loggedInLabel == null) {
			this.loggedInLabel = new JLabel("");
		}
		return this.loggedInLabel;
	}
}
