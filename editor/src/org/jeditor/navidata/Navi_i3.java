/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import javax.swing.JButton;
import org.jeditor.navi.NaviManager;

public class Navi_i3 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_i3(NaviManager mgr) {
		super(mgr, "i3", 2);

		indexLabel.setText("出力の確認");
		//@formatter:off
		questionLabel.setText("<html>結果（出力）は常に誤っていますか。<br>"
				+ "実行毎に結果が異なるプログラムの場合、何度も実行し、<br>"
				+ "正しい結果(出力)になる場合がないか確認しましょう。</html>");
			
		descriptLabel.setText("<html>入力の無いプログラムは特定の場合を除き、結果(出力)は毎回同じになります。<br>"
				+ "入力の無いプログラムで実行毎に結果(出力)が変化し、一部正しい結果(出力)になる場合、バグをかなり絞り込むことができます。</html>");
		//@formatter:on

		JButton button = buttons.get(0);
		button.setText("全て誤り");
		button = buttons.get(1);
		button.setText("一部正しい");
	}

	@Override
	public void setInput(String notice) {
		super.setInput(notice);
		noticeLabel.setText("利用なし");
	}
}
