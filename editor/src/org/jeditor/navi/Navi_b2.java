/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navi;

import javax.swing.JButton;

public class Navi_b2 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_b2(NaviManager mgr) {
		super(mgr, "b2", 2);
		//@formatter:off
		indexLabel.setText("条件に使用する変数の値の調査");
		questionLabel.setText("<html>この条件文(if)の条件に使用する変数の値を確認しましょう。<br>"
				+ "確認した値は正しいですか。</html>");
		descriptLabel.setText("<html>実行する分岐先が正解とは異なるため、この条件文に誤りがあります。<br>"
				+ "その誤りの原因は、<br>"
				+ "・使用する変数が誤っている<br>"
				+ "・条件式が誤っている<br>"
				+ "のどちらかになります。<br>"
				+ "まず、前者の確認を行います。</html>");
		//@formatter:on
		JButton button = buttons.get(0);
		button.setText("正しい");
		button = buttons.get(1);
		button.setText("誤り");
	}

}
