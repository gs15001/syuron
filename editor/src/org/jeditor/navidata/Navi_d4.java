/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import javax.swing.JButton;
import org.jeditor.navi.NaviManager;

public class Navi_d4 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_d4(NaviManager mgr) {
		super(mgr, "d4", 2);

		indexLabel.setText("最後まで処理されているのか");
		//@formatter:off
		questionLabel.setText("<html>プログラムの最後にprint文を挿入し、そのprint文が出力されるか確認しましょう。</html>");
			
		descriptLabel.setText("<html>エラー関連は発生していないことが確認できたので、次にプログラムが<br>"
				+ "最後まで処理されているかを確認します。<br>"
				+ "print文を用いて出力することで、そのprint文まで処理されている<br>"
				+ "ということを確認することができます。</html>");
		//@formatter:on

		JButton button = buttons.get(0);
		button.setText("表示される");
		button = buttons.get(1);
		button.setText("表示されない");
	}

	@Override
	public void setInput(String notice) {
		super.setInput(notice);
		noticeLabel.setText("利用なし");
	}
}