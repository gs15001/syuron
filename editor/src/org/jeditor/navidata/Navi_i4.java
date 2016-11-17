/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import javax.swing.JButton;
import org.jeditor.navi.NaviManager;

public class Navi_i4 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_i4(NaviManager mgr) {
		super(mgr, "i4", 3);

		indexLabel.setText("乱数・日付データの確認");
		//@formatter:off
		questionLabel.setText("<html>プログラムの中で乱数や日付データなどを扱ってはいませんか。</html>");
			
		descriptLabel.setText("<html>入力の無いプログラムで実行毎に結果(出力)が変化し、一部正しい結果(出力)に<br>"
				+ "なる場合、バグの原因は乱数や日付データを扱う部分にある場合がほとんどです。<br>"
				+ "乱数や日付データを扱っているか確認します。</html>");
		//@formatter:on

		JButton button = buttons.get(0);
		button.setText("乱数");
		button = buttons.get(1);
		button.setText("日付データ");
		button = buttons.get(2);
		button.setText("どちらもない");
	}

	@Override
	public void setInput(String notice) {
		super.setInput(notice);
		noticeLabel.setText("利用なし");
	}
}
