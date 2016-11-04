/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import org.jeditor.navi.InputMyDialog;
import org.jeditor.navi.NaviManager;

public class Navi_r4 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_r4(NaviManager mgr) {
		super(mgr, "r4", 2);
		//@formatter:off
		indexLabel.setText("条件に使用する変数の値の調査");
		questionLabel.setText("<html>この繰り返し文(while,for)の条件に使用する変数の値を確認しましょう。<br>"
				+ "確認した値は正しいですか。</html>");
		descriptLabel.setText("<html>一定の条件を満たすまで繰り返すタイプの繰り返し文では条件の部分に誤りがないかを確認します。<br>"
				+ "条件の部分が誤る原因は、<br>"
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
				inputTmp = notice;
			}
		});

		button = buttons.get(1);
		button.setText("誤り");

		dialog[1] = new InputMyDialog(InputMyDialog.VARIABLE);
	}

	@Override
	public void setInput(String notice) {
		super.setInput(notice);
		String[] notices = notice.split("-", 3);

		noticeLabel.setText("着目している繰り返し文　：　" + notices[2] + " 行目");
	}

}
