/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navi;

import javax.swing.JButton;

public class Navi_a4 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_a4(NaviManager mgr) {
		super(mgr, "a4", 2);
		//@formatter:off
		indexLabel.setText("誤っている変数の右辺の式を調査");
		questionLabel.setText("<html>見つけた処理の右辺の式を確認しましょう。<br>"
				+ "複雑な計算を行っている場合は、分解して1つ1つ確認しましょう。<br>"
				+ "確認した結果、式は正しいですか。</html>");
		descriptLabel.setText("<html>代入する値が誤る原因のうち、「使用する変数が誤っている」は<br>"
				+ "先ほど正しいことを確認しました。<br>"
				+ "残る「式が誤っている」の確認を行います。</html>");
		//@formatter:on
		JButton button = buttons.get(0);
		button.setText("正しい");
		button = buttons.get(1);
		button.setText("誤り");
	}

}
