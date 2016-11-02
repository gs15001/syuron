/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import javax.swing.JButton;
import org.jeditor.navi.InputMyDialog;
import org.jeditor.navi.NaviManager;

public class Navi_t extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_t(NaviManager mgr) {
		super(mgr, "t", 4, true);

		indexLabel.setText("デバッグプロセスナビゲーション");
		questionLabel.setText("");
		descriptLabel.setText("");

		JButton button = buttons.get(0);
		button.setText("ステップ1");
		button = buttons.get(1);
		button.setText("ステップ2");
		button = buttons.get(2);
		button.setText("ステップ3");
		button = buttons.get(3);
		button.setText("ステップ4");

		dialog[3] = new InputMyDialog(InputMyDialog.VARIABLE);
		setSamplePane(new sample(mgr));
	}

	@Override
	public void setInput(String notice) {
		super.setInput(notice);
		noticeLabel.setText("利用なし");
	}
}

class sample extends AbstractSamplePane {

	private static final long serialVersionUID = 1L;

	public sample(NaviManager mgr) {
		super(mgr);
	}

}
