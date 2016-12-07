/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import javax.swing.JButton;
import org.jeditor.navi.NaviManager;

public class Navi_d2 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_d2(NaviManager mgr) {
		super(mgr, "d2", 2);

		indexLabel.setText("結果の確認");
		//@formatter:off
		questionLabel.setText("<html>実行結果（出力）は理想通りの結果ですか。<br>"
				+ "入力があるプログラムの場合は色々な入力を試してみましょう。</html>");
		
		descriptLabel.setText("<html>コンパイルに成功し、プログラムが実行できるようになったので、<br>"
				+ "実際に実行し、結果を確認します。<br>"
				+ "入力がある場合は、色々な入力を試し、どの入力に対しても理想通りの<br>"
				+ "結果になるか確認します。</html>");
		//@formatter:on

		JButton button = buttons.get(0);
		button.setText("誤り");
		button = buttons.get(1);
		button.setText("理想通り");
	}

	@Override
	public void setInput(String notice) {
		super.setInput(notice);
		noticeLabel.setText("全体");
	}
}
