/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import javax.swing.JButton;
import org.jeditor.navi.NaviManager;

public class Navi_d1 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_d1(NaviManager mgr) {
		super(mgr, "d1", 2);

		indexLabel.setText("コンパイルエラーの確認");
		//@formatter:off
		questionLabel.setText("<html>コンパイルを行い、コンパイルエラーが発生しているかを確認しましょう。</html>");
		
		descriptLabel.setText("<html>プログラムを実行するためにはコンパイルをする必要があります。<br>"
				+ "コンパイルに失敗するとコンパイルエラーが発生するのでそれを確認します。</html>");
		//@formatter:on

		JButton button = buttons.get(0);
		button.setText("発生している");
		button = buttons.get(1);
		button.setText("発生していない");
	}

	@Override
	public void setInput(String notice) {
		super.setInput(notice);
		noticeLabel.setText("利用なし");
	}
}
