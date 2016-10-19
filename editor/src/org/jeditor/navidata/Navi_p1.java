/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import javax.swing.JButton;
import org.jeditor.navi.InputMyDialog;
import org.jeditor.navi.NaviManager;

public class Navi_p1 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_p1(NaviManager mgr) {
		super(mgr, "p1", 1);

		indexLabel.setText("まとまり分割");
		//@formatter:off
		questionLabel.setText("<html>プログラムをサンプルのように2つのまとまりに分割しましょう。<br>"
				+ "境目となる行を入力してください。<br>"
				+ "※2等分にする必要はなく、中央付近でキリのいいところを境にしましょう。</html>");
		
		descriptLabel.setText("<html>これまでにどういう時にバグが発生しているのかを確認してきました。<br>"
				+ "ここから実際にプログラムの中からバグを探していきます。<br>"
				+ "バグを探すために、プログラムを上から1行1行見て、確認するのでは効率が悪く、大きなプログラムになるほど大変になっていきます。<br>"
				+ "そのため、プログラムをまとまりに分割し、まとまり毎に確認し、バグの潜む範囲を大雑把に絞り込んでいきます。</html>");
		//@formatter:on

		JButton button = buttons.get(0);
		button.setText("分割");

		dialog[0] = new InputMyDialog(InputMyDialog.PARTITION);
	}

	@Override
	public void setInput(String notice) {
		super.setInput(notice);
		noticeLabel.setText("着目している変数　：　" + notice);
		this.input = "0-999-";
	}
}
