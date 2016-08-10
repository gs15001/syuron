package org.jeditor.navi;

import javax.swing.JButton;

public class Navi_a5 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_a5(NaviManager mgr) {
		super(mgr, "a5", 3);

		JButton button = buttons.get(0);
		button.setText("if文");
		button = buttons.get(1);
		button.setText("for文");
		button = buttons.get(2);
		button.setText("次へ");
	}

}
