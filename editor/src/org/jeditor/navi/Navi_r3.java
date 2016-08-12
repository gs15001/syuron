/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navi;

import javax.swing.JButton;

public class Navi_r3 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_r3(NaviManager mgr) {
		super(mgr, "r3", 1);
		
		indexLabel.setText("繰り返し処理の部分に誤りがあります");
		questionLabel.setText("");
		descriptLabel.setText("");

		JButton button = buttons.get(0);
		button.setText("終了");
	}

}
