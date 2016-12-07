/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import javax.swing.JButton;
import org.jeditor.navi.NaviManager;

public class Navi_d0 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_d0(NaviManager mgr) {
		super(mgr, "d0", 1);

		indexLabel.setText("ステップ1 バグの確認");
		//@formatter:off
		questionLabel.setText("<html>ステップ1では、プログラムが正しく動作するかを確認し、<b>バグの存在を<br>"
				+ "確認します。</b><br>"
				+ "既に実行結果(出力)が誤っており、バグの存在を確認しているなら、トップに<br>"
				+ "戻り、「ステップ2」に進みましょう。</html>");
		
		descriptLabel.setText("<html></html>");
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
