/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import org.jeditor.navi.InputMyDialog;
import org.jeditor.navi.NaviManager;

public class Navi_b2 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_b2(NaviManager mgr) {
		super(mgr, "b2", 2);
		//@formatter:off
		indexLabel.setText("条件に使用する変数の値の調査");
		questionLabel.setText("<html>この条件文(if)の条件に使用する変数の値を確認しましょう。<br>"
				+ "確認した値は正しいですか。</html>");
		descriptLabel.setText("<html>実行する分岐先が正解とは異なるため、この条件文に誤りがあります。その誤りの原因は、<br>"
				+ "・使用する変数が誤っている<br>"
				+ "・条件式が誤っている<br>"
				+ "のどちらかになります。<br>"
				+ "まず、前者の確認を行います。</html>");
		//@formatter:on
		JButton button = buttons.get(0);
		button.setText("正しい");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				preInput = notice;
			}
		});
		
		button = buttons.get(1);
		button.setText("誤り");

		dialog[1] = new InputMyDialog(InputMyDialog.VARIABLE);
	}

	@Override
	public void setInput(String notice) {
		super.setInput(notice);
		noticeLabel.setText("着目している条件文　：　" + notice + " 行目");
		parent.setNoticeLine(notice);
	}

}
