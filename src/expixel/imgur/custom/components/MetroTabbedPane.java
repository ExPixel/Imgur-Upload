package expixel.imgur.custom.components;

import java.awt.Color;

import javax.swing.JTabbedPane;

public class MetroTabbedPane extends JTabbedPane {

	Color selectedTabTextColor;
	Color unselectedTabTextColor;

	public void initMetro() {
		if(MetroConstants.METRO)
			this.setUI(new MetroTabbedPaneUI());
	}

	/**
	 *
	 */
	public MetroTabbedPane() {
		super();
		this.initMetro();
	}

	/**
	 * @param tabPlacement
	 * @param tabLayoutPolicy
	 */
	public MetroTabbedPane(int tabPlacement, int tabLayoutPolicy) {
		super(tabPlacement, tabLayoutPolicy);
		this.initMetro();
	}

	/**
	 * @param tabPlacement
	 */
	public MetroTabbedPane(int tabPlacement) {
		super(tabPlacement);
		this.initMetro();
	}

	/**
	 * @return the selectedTabTextColor
	 */
	public Color getSelectedTabTextColor() {
		return this.selectedTabTextColor;
	}

	/**
	 * @param selectedTabTextColor the selectedTabTextColor to set
	 */
	public void setSelectedTabTextColor(Color selectedTabTextColor) {
		this.selectedTabTextColor = selectedTabTextColor;
	}

	/**
	 * @return the unselectedTabTextColor
	 */
	public Color getUnselectedTabTextColor() {
		return this.unselectedTabTextColor;
	}

	/**
	 * @param unselectedTabTextColor the unselectedTabTextColor to set
	 */
	public void setUnselectedTabTextColor(Color unselectedTabTextColor) {
		this.unselectedTabTextColor = unselectedTabTextColor;
	}

}
