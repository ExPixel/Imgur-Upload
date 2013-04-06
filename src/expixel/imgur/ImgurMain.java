package expixel.imgur;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import expixel.imgur.ui.ImgurAppFrame;
import expixel.imgur.ui.ImgurAppFrameModel;


public class ImgurMain {
	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		Imgur.prepareDatabase();

		ImgurAppFrame appFrame = new ImgurAppFrame();
		appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		new ImgurAppFrameModel(appFrame);
		appFrame.setVisible(true);
	}
}
