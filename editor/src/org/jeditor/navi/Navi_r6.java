package org.jeditor.navi;

import javax.swing.JButton;

public class Navi_r6 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_r6(NaviManager mgr) {
		super(mgr, "r6", 2);

		JButton button = buttons.get(0);
		button.setText("正しい");
		button = buttons.get(1);
		button.setText("誤り");
	}

}
