package org.jeditor.navi;

import javax.swing.JButton;

public class Navi_a1 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_a1(NaviManager mgr) {
		super(mgr, "a1", 2);

		JButton button = buttons.get(0);
		button.setText("ある");
		button = buttons.get(1);
		button.setText("ない");
	}

}
