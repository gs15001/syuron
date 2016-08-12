/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navi;

import javax.swing.JButton;

public class Navi_a3 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_a3(NaviManager mgr) {
		super(mgr, "a3", 2);

		indexLabel.setText("誤っている変数の右辺の値を調査");
		questionLabel.setText("");
		descriptLabel.setText("");
		
		JButton button = buttons.get(0);
		button.setText("正しく");
		button = buttons.get(1);
		button.setText("誤り");
	}

}
