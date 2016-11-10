/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import javax.swing.JButton;
import org.jeditor.navi.NaviManager;

public class Navi_r3 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_r3(NaviManager mgr) {
		super(mgr, "r3", 1);
		//@formatter:off
		indexLabel.setText("繰り返し処理の部分に誤りがあります");
		questionLabel.setText("<html>繰り返し回数を数えるための変数の初期値、<br>"
				+ "繰り返し文の条件、<br>"
				+ "繰り返し回数を数えるための処理<br>"
				+ "のいずれかに誤りがあります。順に確認しましょう。</html>");
		descriptLabel.setText("<html>一定の回数繰り返すタイプで繰り返し回数が正解と異なる場合、その原因は、上記の3つのいずれかになります。</html>");
		//@formatter:on
		JButton button = buttons.get(0);
		button.setText("終了");
	}

	@Override
	public void setInput(String notice) {
		super.setInput(notice);
		noticeLabel.setText("着目している繰り返し文　：　" + notice + " 行目");
		parent.setNoticeLine(notice);
	}

}
