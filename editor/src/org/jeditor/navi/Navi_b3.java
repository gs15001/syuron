/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navi;

import javax.swing.JButton;

public class Navi_b3 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_b3(NaviManager mgr) {
		super(mgr, "b3", 2);

		JButton button = buttons.get(0);
		button.setText("正しい");
		button = buttons.get(1);
		button.setText("誤り");
	}

}
