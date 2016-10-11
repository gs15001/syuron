/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import javax.swing.JButton;
import org.jeditor.navi.NaviManager;

public class Navi_e8 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_e8(NaviManager mgr) {
		super(mgr, "e8", 1);

		indexLabel.setText("日付データ関連の修正");
		//@formatter:off
		questionLabel.setText("<html>日付データを扱っている部分にバグがあります。<br>"
				+ "日付データの形式などに注意して日付データを扱っている部分をもう一度見直して見ましょう。</html>");
				
		descriptLabel.setText("<html></html>");
		//@formatter:on

		JButton button = buttons.get(0);
		button.setText("終了");
	}

	@Override
	public void setInput(String notice) {
		super.setInput(notice);
		noticeLabel.setText("利用なし");
	}
}
