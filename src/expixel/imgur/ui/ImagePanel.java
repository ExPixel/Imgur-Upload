package expixel.imgur.ui;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class ImagePanel extends JPanel {
	Image image;

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Image drawimg = this.image;
		if(this.image != null) {
			if(this.image.getWidth(null) > this.getWidth() || this.image.getHeight(null) > this.getHeight())
				drawimg = this.image.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH);
			int xoffset = (int)((double)this.getWidth() / 2 - this.image.getWidth(null) / 2);
			int yoffset = (int)((double)this.getHeight() / 2 - this.image.getHeight(null) / 2);
			g.drawImage(drawimg, xoffset, yoffset, null);
		}
	}

	/**
	 * @return the image
	 */
	public Image getImage() {
		return this.image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(Image image) {
		this.image = image;
	}


}
