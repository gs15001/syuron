/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navi;

import javax.swing.JButton;

public class Navi_a5 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_a5(NaviManager mgr) {
		super(mgr, "a5", 3);
		//@formatter:off
		indexLabel.setText("条件分岐・繰り返し文の調査");
		questionLabel.setText("");
		descriptLabel.setText("");
		//@formatter:on
		JButton button = buttons.get(0);
		button.setText("if文");
		button = buttons.get(1);
		button.setText("for文");
		button = buttons.get(2);
		button.setText("次へ");
	}

}
