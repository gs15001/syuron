package org.jeditor.navi;

import javax.swing.JButton;

public class Navi_r1 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_r1(NaviManager mgr) {
		super(mgr, "r1", 2);

		JButton button = buttons.get(0);
		button.setText("はい");
		button = buttons.get(1);
		button.setText("いいえ");
	}

}
