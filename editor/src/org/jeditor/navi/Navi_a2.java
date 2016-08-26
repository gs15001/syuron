/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navi;

import javax.swing.JButton;

public class Navi_a2 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_a2(NaviManager mgr) {
		super(mgr, "a2", 2);
		//@formatter:off
		indexLabel.setText("条件分岐・繰り返しの確認");
		questionLabel.setText("<html>見つけた処理は条件文(if)や繰り返し文(while,for)の<br>"
				+ "ブロック（{})内に存在しますか。</html>");
		descriptLabel.setText("<html>見つけた処理が条件文や繰り返し文のブロック内に<br>"
				+ "存在する場合、条件文や繰り返し文の誤りによって結果が<br>"
				+ "誤ることがあるため、確認します。</html>");
		//@formatter:on
		JButton button = buttons.get(0);
		button.setText("存在する");

		button = buttons.get(1);
		button.setText("存在しない");
	}

}
