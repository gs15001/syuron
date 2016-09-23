/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navi;

import javax.swing.JButton;

public class Navi_a7 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_a7(NaviManager mgr) {
		super(mgr, "a7", 2);
		//@formatter:off
		indexLabel.setText("使用する変数の調査");
		questionLabel.setText("<html>右辺に出てきた誤っている変数の変数名は正しいですか。<br>"
				+ "使用する変数を間違えてはいませんか。</html>");
		descriptLabel.setText("<html>似たような変数名の変数があると使用する変数を間違えてしまうことがあります。念の為にも確認しましょう。</html>");
		//@formatter:on
		JButton button = buttons.get(0);
		button.setText("正しい");
		button = buttons.get(1);
		button.setText("誤り");
	}

	public void setInput(String input) {
		super.setInput(input);
		noticeLabel.setText("着目している変数　：　" + input);
		this.input = input;
	}
}
