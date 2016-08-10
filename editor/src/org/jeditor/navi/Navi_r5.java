package org.jeditor.navi;

import javax.swing.JButton;

public class Navi_r5 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_r5(NaviManager mgr) {
		super(mgr, "r5", 2);

		JButton button = buttons.get(0);
		button.setText("正しい");
		button = buttons.get(1);
		button.setText("誤り");
	}

}
