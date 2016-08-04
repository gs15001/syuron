package org.jeditor.navi;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

public class NaviSecond extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public NaviSecond(NaviManager mgr) {
		super(mgr, "s2", 3);
		
		JButton button = buttons.get(0);
		button.setText("1");

		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				naviManager.changeNavi(getIndexLabel(),button.getText());
			}
		});
	}
}
