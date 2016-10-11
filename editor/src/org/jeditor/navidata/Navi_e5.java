/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import javax.swing.JButton;
import org.jeditor.navi.NaviManager;

public class Navi_e5 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_e5(NaviManager mgr) {
		super(mgr, "e5", 1);

		indexLabel.setText("ランタイムエラーの修正");
		//@formatter:off
		questionLabel.setText("<html>表示されているランタイムエラーを見て修正しましょう。</html>");
					
		descriptLabel.setText("<html>ランタイムエラーの見方</html>");
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
