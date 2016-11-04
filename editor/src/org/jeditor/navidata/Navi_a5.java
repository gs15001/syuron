/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import javax.swing.JButton;
import org.jeditor.navi.InputMyDialog;
import org.jeditor.navi.NaviManager;

public class Navi_a5 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_a5(NaviManager mgr) {
		super(mgr, "a5", 3);
		//@formatter:off
		indexLabel.setText("条件分岐・繰り返し文の調査");
		questionLabel.setText("<html>現在着目している範囲にある条件文(if)や繰り返し文(while,for)を選択してください。<br>"
				+ "複数存在している場合は、外側(コードの上のほう）から選択してください。</html>");
		descriptLabel.setText("<html>誤っている変数があるにもかかわらず、代入している処理が"
				+ "ない場合、誤る原因は条件文や繰り返し文にあると考えられます。<br>"
				+ "着目している範囲にある条件文や繰り返し文に誤りがないか確認します。</html>");
		//@formatter:on
		JButton button = buttons.get(0);
		button.setText("if文");
		button = buttons.get(1);
		button.setText("for文");
		button = buttons.get(2);
		button.setText("次へ");
		dialog[0] = new InputMyDialog(InputMyDialog.IF);
		dialog[1] = new InputMyDialog(InputMyDialog.FOR);
	}

	@Override
	public void setInput(String notice) {
		super.setInput(notice);
		noticeLabel.setText("着目している処理　：　" + notice + " 行目");
		inputTmp = getIndex() + "-" + notice + "-";
	}
}
