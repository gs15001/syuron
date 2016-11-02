/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import javax.swing.JButton;
import org.jeditor.navi.NaviManager;

public class Navi_d3 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_d3(NaviManager mgr) {
		super(mgr, "d3", 2);

		indexLabel.setText("実行中のエラーの確認");
		//@formatter:off
		questionLabel.setText("<html>実行中にエラーが発生していないか確認しましょう。</html>");
		
		descriptLabel.setText("<html>プログラムの実行中にもエラーが発生することがあり、<br>"
				+ "このエラーをランタイムエラーと言います。<br>"
				+ "コンパイルエラーと同様に、修正する必要があります。</html>");
		//@formatter:on

		JButton button = buttons.get(0);
		button.setText("発生していない");
		button = buttons.get(1);
		button.setText("発生している");
	}

	@Override
	public void setInput(String notice) {
		super.setInput(notice);
		noticeLabel.setText("利用なし");
	}
}
