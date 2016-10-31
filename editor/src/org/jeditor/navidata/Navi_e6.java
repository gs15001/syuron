/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import javax.swing.JButton;
import org.jeditor.navi.NaviManager;

public class Navi_e6 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_e6(NaviManager mgr) {
		super(mgr, "e6", 1);

		indexLabel.setText("入力待ち・無限ループの可能性");
		//@formatter:off
		questionLabel.setText("<html>プログラムが最後まで実行されない原因は、<br>"
				+ "・入力待ち      ・無限ループ　　　の2つが考えられます。<br>"
				+"それぞれについて確認しましょう。</html>");
				
		descriptLabel.setText("<html>入力待ちでないことを確認するためは、Enterを<br>"
				+ "コンソールに入力することで確認できます。<br>"
				+ "何回かEnterを入力しても反応しない場合は、無限ループに陥っています。<br>"
				+ "プログラムの繰り返し文を見直してみましょう。</html>");
		//@formatter:on

		JButton button = buttons.get(0);
		button.setText("終了");
	}

	@Override
	public void setInput(String notice) {
		super.setInput(notice);
		noticeLabel.setText("利用なし");
	}
}
