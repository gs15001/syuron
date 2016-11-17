/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import org.jeditor.navi.InputMyDialog;
import org.jeditor.navi.NaviManager;

public class Navi_a3 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_a3(NaviManager mgr) {
		super(mgr, "a3", 2);
		//@formatter:off
		indexLabel.setText("誤っている変数の右辺の値を調査");
		questionLabel.setText("<html>誤っている処理の右辺に出てくる変数の値を確認しましょう。<br>"
				+ "確認した値は正しいですか。</html>");
		descriptLabel.setText("<html>代入する値は右辺の式を評価（計算）した結果になります。<br>"
				+ "代入する値が誤る原因は、<br>"
				+ "・右辺の式で使用する変数が誤っている<br>"
				+ "・右辺の式が誤っている<br>"
				+ "のどちらかになります。<br>"
				+ "まず、前者の確認を行います。</html>");
		//@formatter:on
		JButton button = buttons.get(0);
		button.setText("正しい");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				preInput = notice;
				postInput = "";
			}
		});

		button = buttons.get(1);
		button.setText("誤り");
		dialog[1] = new InputMyDialog(InputMyDialog.VARIABLE);
	}

	@Override
	public void setInput(String notice) {
		super.setInput(notice);
		String[] notices = notice.split("-");

		try {
			parent.setPartition(Integer.parseInt(notices[1]), Integer.parseInt(notices[2]));
		} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
			parent.setPartition(-1, -1);
		}
		noticeLabel.setText("着目している処理　：　" + notices[0] + " 行目");
		parent.setNoticeLine(notices[0]);
		postInput = parent.getPartition();
	}
}
