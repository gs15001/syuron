/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import javax.swing.JButton;
import org.jeditor.navi.NaviManager;

public class Navi_p2 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_p2(NaviManager mgr) {
		super(mgr, "p2", 2);

		indexLabel.setText("まとまりの動作確認");
		//@formatter:off
		questionLabel.setText("<html>n行目までのまとまりが正しく動作しているか確認しましょう。<br>"
				+ "境目であるn行目にprint文を挿入して、必要な変数の値を確認しましょう。<br>"
				+ "確認した値は正しいですか。</html>");
		
		descriptLabel.setText("<html>プログラムをまとまりに分割できたなら、次はまとまりごとに正しく動作しているか確認します。<br>"
				+ "正しく動作しているかは、変数の値を確認することで確かめることができます。<br>"
				+ "確認するべき変数は、n行目以降のまとまりで使用している変数です。</html>");
		//@formatter:on

		JButton button = buttons.get(0);
		button.setText("正しい");
		button = buttons.get(1);
		button.setText("誤り");
	}

	@Override
	public void setInput(String notice) {
		super.setInput(notice);
		noticeLabel.setText("利用なし");
	}
}
