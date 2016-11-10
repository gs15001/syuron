/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import javax.swing.JButton;
import org.jeditor.navi.InputMyDialog;
import org.jeditor.navi.NaviManager;

public class Navi_a1 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_a1(NaviManager mgr) {
		super(mgr, "a1", 2);

		indexLabel.setText("誤っている変数の原因を探す");
		//@formatter:off
		questionLabel.setText("<html>誤っている変数に代入している処理を探しましょう。<br>"
				+ "変数に代入している処理とは、その変数が左辺にある処理です。<br>"
				+ "複数ある場合は一番最後の処理を対象とします。</html>");
		
		descriptLabel.setText("<html>誤っている変数を見つけたなら、次はその原因を探します。<br>"
				+ "変数は代入された時だけ、保持する値が変更されます。<br>"
				+ "つまり、変数の値が誤っている場合、代入した値が誤っていることになるため、その部分を探します。</html>");
		//@formatter:on

		JButton button = buttons.get(0);
		button.setText("ある");
		button = buttons.get(1);
		button.setText("ない");

		dialog[0] = new InputMyDialog(InputMyDialog.ROW);
	}

	@Override
	public void setInput(String notice) {
		super.setInput(notice);
		noticeLabel.setText("着目している変数　：　" + notice);
		parent.setVariable(notice);
	}
}
