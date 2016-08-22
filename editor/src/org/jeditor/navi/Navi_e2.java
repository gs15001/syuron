/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navi;

import javax.swing.JButton;

public class Navi_e2 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_e2(NaviManager mgr) {
		super(mgr, "e2", 1);
		//@formatter:off
		indexLabel.setText("エラー");
		questionLabel.setText("<html>エラーを見つけることができませんでした。<br>"
				+ "自信のない回答をした箇所があれば、そこに戻り、もう１度考えてみましょう。<br>"
				+ "それでも見つけられない場合は友達やTAに聞いてみましょう。</html>");
		descriptLabel.setText("");
		//@formatter:on
		JButton button = buttons.get(0);
		button.setText("終了");
	}

}
