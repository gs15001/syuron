/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import javax.swing.JButton;
import org.jeditor.navi.NaviManager;

public class Navi_i0 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_i0(NaviManager mgr) {
		super(mgr, "i0", 1);

		indexLabel.setText("ステップ2 再現性の確認");
		//@formatter:off
		questionLabel.setText("<html>ステップ1では、バグの存在を確認しました。<br>"
				+ "ステップ2では、どんな時に誤った結果(出力)になるかを確認し、<b>誤った結果(出力)<br>を再現できるようにします。</b><br>"
				+ "誤った結果を確実に出力できる場合はトップに戻り、「ステップ3」に<br>進みましょう。</html>");
			
		descriptLabel.setText("<html>以降、「誤った結果(出力)を再現できるようにすること」を<b>「再現性の確認」</b><br>と言うこととします。<br>"
				+ "再現性の確認は、複雑なプログラムになるほど重要になります。</html>");
		//@formatter:on

		JButton button = buttons.get(0);
		button.setText("次へ");
	}

	@Override
	public void setInput(String notice) {
		super.setInput(notice);
		noticeLabel.setText("全体");
	}
}
