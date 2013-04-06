package expixel.imgur.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import expixel.imgur.custom.components.MetroButton;

public class LimitReachedDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();


	/**
	 * Create the dialog.
	 */
	public LimitReachedDialog(Frame frame) {
		super(frame);
		this.setModal(true);
		this.getContentPane().setBackground(Color.WHITE);
		this.setTitle("Upload Limit Reached");
		this.setBounds(100, 100, 320, 125);
		this.getContentPane().setLayout(new BorderLayout());
		this.contentPanel.setBackground(Color.WHITE);
		this.contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.getContentPane().add(this.contentPanel, BorderLayout.CENTER);
		this.contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBorder(null);
			this.contentPanel.add(scrollPane, BorderLayout.CENTER);
			{
				JTextArea txtrYouHaveReached = new JTextArea();
				txtrYouHaveReached.setEditable(false);
				txtrYouHaveReached.setFont(new Font("Consolas", Font.PLAIN, 13));
				txtrYouHaveReached.setLineWrap(true);
				txtrYouHaveReached.setWrapStyleWord(true);
				txtrYouHaveReached.setText("You have reached your daily upload limit of 1000 uploads!");
				scrollPane.setViewportView(txtrYouHaveReached);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(Color.WHITE);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			this.getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				MetroButton mtrbtnClose = new MetroButton();
				mtrbtnClose.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						LimitReachedDialog.this.setVisible(false);
					}
				});
				mtrbtnClose.setText("Close");
				buttonPane.add(mtrbtnClose);
			}
		}
	}

}
