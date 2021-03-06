/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import javax.swing.JButton;
import org.jeditor.navi.NaviManager;

public class Navi_e3 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_e3(NaviManager mgr) {
		super(mgr, "e3", 1);

		indexLabel.setText("コンパイルエラーの修正");
		//@formatter:off
		questionLabel.setText("<html>表示されているコンパイルエラーを見て修正しましょう。</html>");
			
		descriptLabel.setText("<html>コンパイルエラーの見方</html>");
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
