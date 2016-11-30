/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import javax.swing.JButton;
import org.jeditor.navi.NaviManager;

public class Navi_i5 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_i5(NaviManager mgr) {
		super(mgr, "i5", 1);

		indexLabel.setText("");
		//@formatter:off
		questionLabel.setText("<html>解説を読み、「次へ」を選択してください。</html>");
		descriptLabel.setText("<html>一部だけ正しい結果になる場合、<br>"
				+ "・入力の違いを手がかりにしてデバッグを進める方法<br>"
				+ "・誤った入力だけに着目してデバッグを進める方法<br>"
				+ "の２種類があります。<br>"
				+ "後者の方が経験などを必要とせず行えるため、後者の方法で進めていきます。<br>"
				+ "以降、プログラムの入力には「誤った結果になる入力」をしてください。</html>");
		//@formatter:on

		JButton button = buttons.get(0);
		button.setText("次へ");
	}

	@Override
	public void setInput(String notice) {
		super.setInput(notice);
		noticeLabel.setText("利用なし");
	}
}
