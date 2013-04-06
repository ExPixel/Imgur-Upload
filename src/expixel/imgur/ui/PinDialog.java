package expixel.imgur.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;
import expixel.imgur.custom.components.MetroButton;

public class PinDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	protected JTextField textField;

	boolean okay = false;
	private MetroButton mtrbtnGetYourPin;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			PinDialog dialog = new PinDialog(null);
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public PinDialog(Frame frame) {
		super(frame);
		this.setModal(true);
		this.setTitle("Imgur Pin");
		this.getContentPane().setBackground(Color.WHITE);
		this.setBounds(100, 100, 355, 155);
		this.getContentPane().setLayout(new BorderLayout());
		this.contentPanel.setBackground(Color.WHITE);
		this.contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.getContentPane().add(this.contentPanel, BorderLayout.CENTER);
		this.contentPanel.setLayout(new MigLayout("", "[][grow]", "[][][][]"));
		{
			this.mtrbtnGetYourPin = new MetroButton();
			this.mtrbtnGetYourPin.setText("Get Your Pin Number");
			this.contentPanel.add(this.mtrbtnGetYourPin, "cell 0 0 2 1");
		}
		{
			JLabel lblEnterThePin = new JLabel("Enter the pin provided by imgur in the text box below.");
			this.contentPanel.add(lblEnterThePin, "cell 0 1 2 1");
		}
		{
			JLabel lblPin = new JLabel("PIN:");
			this.contentPanel.add(lblPin, "cell 0 3,alignx trailing");
		}
		{
			this.textField = new JTextField();
			this.contentPanel.add(this.textField, "cell 1 3,growx");
			this.textField.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(Color.WHITE);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			this.getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				MetroButton mtrbtnOkay = new MetroButton();
				mtrbtnOkay.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						PinDialog.this.okay = true;
						PinDialog.this.setVisible(false);
					}
				});
				mtrbtnOkay.setText("Okay");
				buttonPane.add(mtrbtnOkay);
			}
			{
				MetroButton mtrbtnCancel = new MetroButton();
				mtrbtnCancel.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						PinDialog.this.okay = false;
						PinDialog.this.setVisible(false);
					}
				});
				mtrbtnCancel.setText("Cancel");
				buttonPane.add(mtrbtnCancel);
			}
		}
	}

	public JTextField getPinTextField() {
		return this.textField;
	}

	public boolean okayPressed() {
		return this.okay;
	}
	public MetroButton getPinNumberGetButton() {
		return this.mtrbtnGetYourPin;
	}
}
