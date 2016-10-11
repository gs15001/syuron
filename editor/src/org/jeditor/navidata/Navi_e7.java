/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import javax.swing.JButton;
import org.jeditor.navi.NaviManager;

public class Navi_e7 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_e7(NaviManager mgr) {
		super(mgr, "e7", 1);

		indexLabel.setText("乱数関連の修正");
		//@formatter:off
		questionLabel.setText("<html>乱数を扱っている部分にバグがあります。<br>"
				+ "乱数の生成部分や乱数を使用した条件文あたりをもう一度見直して見ましょう。</html>");
				
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
