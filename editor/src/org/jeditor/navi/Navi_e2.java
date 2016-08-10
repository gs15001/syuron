package org.jeditor.navi;

import javax.swing.JButton;

public class Navi_e2 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_e2(NaviManager mgr) {
		super(mgr, "e2", 1);

		JButton button = buttons.get(0);
		button.setText("終了");
	}

}
