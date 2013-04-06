package expixel.imgur.custom.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

public class MetroButton extends JButton {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	Color rollOverBackground;
	Color rollOverForeground;
	Color rollOverBorderColor;
	Color borderColor;
	Color disabledBackground;
	Color disabledForeground;
	Color disabledBorderColor;

	@Override
	public void paintComponent(Graphics g) {

		if(!MetroConstants.METRO) {
			super.paintComponent(g);
			return;
		}

		Color bgColor;
		Color fgColor;
		Color bColor;


		if(this.getModel().isEnabled()) {
			if(this.getModel().isPressed() || this.getModel().isArmed()) {
				bgColor = this.getRollOverBackground();
				fgColor = this.getRollOverForeground();
				bColor = this.getRollOverBorderColor();
				if(this.getModel().isPressed() || this.getModel().isArmed()) {
					if(bgColor != null) bgColor = bgColor.darker();
					if(fgColor != null) fgColor = fgColor.brighter();
					if(bColor != null) bColor = bColor.darker();
				}
			} else if(this.getModel().isRollover() || this.getModel().isSelected()) {
				bgColor = this.getRollOverBackground();
				fgColor = this.getRollOverForeground();
				bColor = this.getRollOverBorderColor();
			} else {
				if(this.isDefaultButton()) {
					bgColor = this.defaultRollOverBackground();
					fgColor = this.getRollOverForeground();
					bColor = this.getRollOverBorderColor();
				} else {
					bgColor = this.getBackground();
					fgColor = this.getForeground();
					bColor = this.getBorderColor();
				}
			}
		} else {
			bgColor	= this.getDisabledBackground();
			fgColor	= this.getDisabledForeground();
			bColor	= this.getDisabledBorderColor();
		}

		int bx = (int) this.getBounds().getX();
		int by = (int) this.getBounds().getY();
		int bw = (int) this.getBounds().getWidth();
		int bh = (int) this.getBounds().getHeight();

		/* Paint Background: */
		g.setColor(bgColor);
		g.fillRect(0, 0, bw, bh);

		int iconXPush = 0;

		/* Paint Icon: */
		if(this.getIcon() != null) {
			if(	this.getText() != null && this.getText().length() > 0 )
				iconXPush = this.getIcon().getIconWidth()/2 + this.getIconTextGap() - 4;
			else
				iconXPush = this.getIcon().getIconWidth() / 2;
			this.getIcon().paintIcon(this, g, iconXPush, this.centerY(this.getIcon().getIconHeight()));
		}

		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, // Anti-alias!
				RenderingHints.VALUE_ANTIALIAS_ON);

		/* Paint Text: */
		Rectangle2D stringRect = g.getFontMetrics().getStringBounds(this.getText(), g);
		int string_x = this.CenterX((int)stringRect.getWidth());
		int string_y = this.centerY((int)stringRect.getHeight()) + (int)(stringRect.getHeight() / 1.5) + 2;
		g.setColor(fgColor);
		g.drawString(this.getText(), string_x, string_y);
	}

	public Color defaultRollOverBackground() {
		Color c = this.getRollOverBackground().brighter();
		return new Color(
				Math.max(c.getRed() - 15, 0),
				Math.max(c.getGreen() - 15, 0),
				Math.max(c.getBlue() - 15, 0)
				);
	}

	public int CenterX(int w) {
		return (int)((double)this.getWidth() / 2 - w / 2);
	}

	public int centerY(int h) {
		return (int)((double)this.getHeight() / 2 - h / 2);
	}

	public void initGreenTheme() {
		this.setBorderPainted(false);

		this.setBackground(new Color(224, 224, 224));
		this.setRollOverBackground(new Color(115, 236, 38).darker());
		this.setDisabledBackground(new Color(195, 195, 195));

		this.setForeground(Color.black);
		this.setRollOverForeground(Color.white);
		this.setDisabledForeground(new Color(224, 224, 224).brighter());

		this.invalidate();
	}

	public void initBlueTheme() {
		this.setBorderPainted(false);

		this.setBackground(new Color(224, 224, 224));
		this.setRollOverBackground(new Color(38, 115, 236));
		this.setDisabledBackground(new Color(195, 195, 195));

		this.setForeground(Color.black);
		this.setRollOverForeground(Color.white);
		this.setDisabledForeground(new Color(224, 224, 224).brighter());

		this.invalidate();
	}

	public void initOrangeTheme() {
		this.setBorderPainted(false);

		this.setBackground(new Color(224, 224, 224));
		this.setRollOverBackground(new Color(236, 115, 38));
		this.setDisabledBackground(new Color(195, 195, 195));

		this.setForeground(Color.black);
		this.setRollOverForeground(Color.white);
		this.setDisabledForeground(new Color(224, 224, 224).brighter());

		this.invalidate();
	}

	public void initRedTheme() {
		this.setBorderPainted(false);

		this.setBackground(new Color(224, 224, 224));
		this.setRollOverBackground(new Color(225, 0, 0));
		this.setDisabledBackground(new Color(195, 195, 195));

		this.setForeground(Color.black);
		this.setRollOverForeground(Color.white);
		this.setDisabledForeground(new Color(224, 224, 224).brighter());

		this.invalidate();
	}

	public void initColors() {
		if(MetroConstants.METRO) {
			this.setFont(new Font("Consolas", Font.PLAIN, 12));
			this.initBlueTheme();
		}
	}


	/**
	 *
	 */
	public MetroButton() {
		super();
		this.initColors();
	}




	/**
	 * @param a
	 */
	public MetroButton(Action a) {
		super(a);
		this.initColors();
	}




	/**
	 * @param icon
	 */
	public MetroButton(Icon icon) {
		super(icon);
		this.initColors();
	}




	/**
	 * @param text
	 * @param icon
	 */
	public MetroButton(String text, Icon icon) {
		super(text, icon);
		this.initColors();
	}




	/**
	 * @param name
	 */
	public MetroButton(String name) {
		super(name);
		this.initColors();
	}


	/**
	 * @return the rollOverBackground
	 */
	public Color getRollOverBackground() {
		return this.rollOverBackground;
	}


	/**
	 * @param rollOverBackground the rollOverBackground to set
	 */
	public void setRollOverBackground(Color rollOverBackground) {
		this.rollOverBackground = rollOverBackground;
	}


	/**
	 * @return the rollOverForeground
	 */
	public Color getRollOverForeground() {
		return this.rollOverForeground;
	}


	/**
	 * @param rollOverForeground the rollOverForeground to set
	 */
	public void setRollOverForeground(Color rollOverForeground) {
		this.rollOverForeground = rollOverForeground;
	}


	/**
	 * @return the rollOverBorderColor
	 */
	public Color getRollOverBorderColor() {
		return this.rollOverBorderColor;
	}


	/**
	 * @param rollOverBorderColor the rollOverBorderColor to set
	 */
	public void setRollOverBorderColor(Color rollOverBorderColor) {
		this.rollOverBorderColor = rollOverBorderColor;
	}


	/**
	 * @return the borderColor
	 */
	public Color getBorderColor() {
		return this.borderColor;
	}


	/**
	 * @param borderColor the borderColor to set
	 */
	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}


	/**
	 * @return the disabledBackground
	 */
	public Color getDisabledBackground() {
		return this.disabledBackground;
	}


	/**
	 * @param disabledBackground the disabledBackground to set
	 */
	public void setDisabledBackground(Color disabledBackground) {
		this.disabledBackground = disabledBackground;
	}


	/**
	 * @return the disabledForeground
	 */
	public Color getDisabledForeground() {
		return this.disabledForeground;
	}


	/**
	 * @param disabledForeground the disabledForeground to set
	 */
	public void setDisabledForeground(Color disabledForeground) {
		this.disabledForeground = disabledForeground;
	}


	/**
	 * @return the disabledBorderColor
	 */
	public Color getDisabledBorderColor() {
		return this.disabledBorderColor;
	}


	/**
	 * @param disabledBorderColor the disabledBorderColor to set
	 */
	public void setDisabledBorderColor(Color disabledBorderColor) {
		this.disabledBorderColor = disabledBorderColor;
	}

}
