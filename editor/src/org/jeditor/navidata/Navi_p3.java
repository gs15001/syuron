/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import javax.swing.JButton;
import org.jeditor.navi.NaviManager;

public class Navi_p3 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_p3(NaviManager mgr) {
		super(mgr, "p3", 2);

		indexLabel.setText("さらなるまとまり分割");
		//@formatter:off
		questionLabel.setText("<html>n行目からm行目までのまとまりをさらに2つのまとまりに分割しましょう。<br>"
				+ "境目となる行を入力してください。<br>"
				+ "境目になるようなキリのいいところがなければ、「次へ」を選択してください。</html>");
		
		descriptLabel.setText("<html>確認の結果、バグの潜む範囲はn行目からm行目のまとまり内まで絞り込むことができました。<br>"
				+ "同様のことを繰り返し、バグの潜む範囲を絞り込んでいきます。ある程度、絞り込めたら、その範囲を細かく調べていきます。</html>");
		//@formatter:on

		JButton button = buttons.get(0);
		button.setText("分割");
		button = buttons.get(1);
		button.setText("次へ");
	}

	@Override
	public void setInput(String notice) {
		super.setInput(notice);
		noticeLabel.setText("利用なし");
	}
}
