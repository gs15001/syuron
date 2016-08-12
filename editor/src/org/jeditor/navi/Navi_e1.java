/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navi;

import javax.swing.JButton;

public class Navi_e1 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_e1(NaviManager mgr) {
		super(mgr, "e1", 1);

		indexLabel.setText("現在調べている部分に誤りがあります");
		questionLabel.setText("");
		descriptLabel.setText("");
		
		JButton button = buttons.get(0);
		button.setText("終了");
	}

}
