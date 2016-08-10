package org.jeditor.navi;

import javax.swing.JButton;


public class Navi_t extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_t(NaviManager mgr) {
		super(mgr, "t", 1);
		
		JButton button = buttons.get(0);
		button.setText("スタート");
	}

}
