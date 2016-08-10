package org.jeditor.navi;

import javax.swing.JButton;

public class Navi_a4 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_a4(NaviManager mgr) {
		super(mgr, "a4", 2);

		JButton button = buttons.get(0);
		button.setText("正しい");
		button = buttons.get(1);
		button.setText("誤り");
	}

}
