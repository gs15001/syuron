/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navi;

import javax.swing.JButton;

public class Navi_a2 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_a2(NaviManager mgr) {
		super(mgr, "a2", 2);

		indexLabel.setText("条件分岐・繰り返しの確認");
		questionLabel.setText("");
		descriptLabel.setText("");
		
		JButton button = buttons.get(0);
		button.setText("存在する");

		button = buttons.get(1);
		button.setText("存在しない");
	}

}
