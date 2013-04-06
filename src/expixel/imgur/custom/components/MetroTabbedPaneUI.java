package expixel.imgur.custom.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.plaf.basic.BasicTabbedPaneUI;

public class MetroTabbedPaneUI extends BasicTabbedPaneUI {

	Color tabRollOverColor;

	int curRollOverTab = -1;

	@Override
	protected void installDefaults() {
		super.installDefaults();

		this.tabRollOverColor = new Color(224, 224, 224);

		((MetroTabbedPane)this.tabPane).setSelectedTabTextColor(new Color(38, 115, 236));
		((MetroTabbedPane)this.tabPane).setUnselectedTabTextColor(Color.black);

		this.tabPane.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) {
				MetroTabbedPaneUI.this.mouseLocChanged(e.getX(), e.getY());
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				MetroTabbedPaneUI.this.mouseLocChanged(e.getX(), e.getY());
			}});
	}

	public void mouseLocChanged(int x, int y) {
		Point p = new Point(x, y);
		int last = this.curRollOverTab;
		int tin = -1;
		for(int i = 0; i < this.tabPane.getTabCount(); i++) {
			if(this.getTabBounds(this.tabPane, i).contains(p)) {
				tin = i;
			}
		}
		this.curRollOverTab = tin;
		if(last != this.curRollOverTab) this.tabPane.repaint();
	}


	@Override
	protected void paintTabArea(Graphics g, int tabPlacement, int selectedIndex) {
		g.setColor(Color.white);
		g.fillRect(0, 0, this.tabPane.getWidth(), this.maxTabHeight);
		g.setColor(Color.LIGHT_GRAY);
		g.drawLine(0, this.maxTabHeight + 3, this.tabPane.getWidth(), this.maxTabHeight + 3);
		super.paintTabArea(g, tabPlacement, selectedIndex);
	}

	@Override
	protected void paintTabBackground(Graphics g, int tabPlacement,
			int tabIndex, int x, int y, int w, int h, boolean isSelected) {
		if(this.curRollOverTab == tabIndex && !isSelected){
			g.setColor(this.tabRollOverColor);
		}
		else g.setColor(Color.white);
		g.fillRect(x, y, w, h);
	}

	@Override
	protected void paintFocusIndicator(Graphics g, int tabPlacement,
			Rectangle[] rects, int tabIndex, Rectangle iconRect,
			Rectangle textRect, boolean isSelected) {
	}

	@Override
	protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex,
			int x, int y, int w, int h, boolean isSelected) {
		g.setColor(Color.LIGHT_GRAY);
		if(!isSelected) {
			g.drawLine(0, y + h, x + w, y + h);
		} else {
			g.drawLine(x, y, x, y + h - 2);
			g.drawLine(x, y, x + w, y);
			g.drawLine(x + w, y, x + w, y + h - 2);
		}
	}

	@Override
	protected void paintText(Graphics g, int tabPlacement, Font font,
			FontMetrics metrics, int tabIndex, String title,
			Rectangle textRect, boolean isSelected) {
		Color tCol;
		if(isSelected) {
			tCol = ((MetroTabbedPane)this.tabPane).getSelectedTabTextColor();
		} else {
			tCol = ((MetroTabbedPane)this.tabPane).getUnselectedTabTextColor();
		}
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, // Anti-alias!
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setFont(font);
		g.setColor(tCol);
		g.drawString(title, textRect.x, textRect.y + textRect.height - 2);
	}


	@Override
	protected void paintContentBorder(Graphics g, int tabPlacement,
			int selectedIndex) {
	}

}