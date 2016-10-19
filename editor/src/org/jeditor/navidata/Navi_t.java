/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import javax.swing.JButton;
import org.jeditor.navi.InputMyDialog;
import org.jeditor.navi.NaviManager;

public class Navi_t extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_t(NaviManager mgr) {
		super(mgr, "t", 4);

		indexLabel.setText("デバッグプロセスナビゲーション");
		questionLabel.setText("");
		descriptLabel.setText("");

		JButton button = buttons.get(0);
		button.setText("a");
		button = buttons.get(1);
		button.setText("b");
		button = buttons.get(2);
		button.setText("c");
		button = buttons.get(3);
		button.setText("d");

		dialog[3] = new InputMyDialog(InputMyDialog.VARIABLE);
	}

	@Override
	public void setInput(String notice) {
		super.setInput(notice);
		noticeLabel.setText("利用なし");
	}
}
