package org.jeditor.navi;

import javax.swing.JButton;

public class NaviTop extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public NaviTop(NaviManager mgr) {
		super(mgr, "s1", 1);

		JButton button = buttons.get(0);
		button.setText("1");

	}
}
