/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navi;

import javax.swing.JButton;

public class Navi_a6 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_a6(NaviManager mgr) {
		super(mgr, "a6", 3);
		//@formatter:off
		indexLabel.setText("条件分岐・繰り返し文の調査");
		questionLabel.setText("<html>見つけた処理が存在している条件文(if)や繰り返し文(while,for)を<br>"
				+ "選択してください。<br>"
				+ "複数存在している場合は、外側(コードの上のほう）から<br>"
				+ "選択してください。</html>");
		descriptLabel.setText("<html>見つけた処理が属している条件文(if)や繰り返し文(while,for)に誤りが<br>"
				+ "ないかを調べていきます。<br>"
				+ "外側にある条件文や繰り返し文の方が結果に大きく影響することが<br>"
				+ "多いため、外側から調べていきます。</html>");
		//@formatter:on
		JButton button = buttons.get(0);
		button.setText("if文");
		button = buttons.get(1);
		button.setText("for文");
		button = buttons.get(2);
		button.setText("次へ");
	}

}
