/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import javax.swing.JButton;
import org.jeditor.navi.InputMyDialog;
import org.jeditor.navi.NaviManager;

public class Navi_a0 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_a0(NaviManager mgr) {
		super(mgr, "a0", 1);

		indexLabel.setText("ステップ4 バグの特定");
		//@formatter:off
		questionLabel.setText("<html>ステップ3で、バグの潜む範囲を絞り込みました。<br>"
				+ "ステップ4では、その範囲を細かく調べ、バグを特定していきます。</html>");
		
		descriptLabel.setText("<html></html>");
		//@formatter:on

		JButton button = buttons.get(0);
		button.setText("次へ");

		dialog[0] = new InputMyDialog(InputMyDialog.VARIABLE);
	}

	@Override
	public void setInput(String notice) {
		super.setInput(notice);
		noticeLabel.setText("利用なし");
	}
}
