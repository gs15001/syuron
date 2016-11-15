/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import org.jeditor.navi.NaviManager;

public class Navi_b1 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	private String called;
	private String calledLine;

	public Navi_b1(NaviManager mgr) {
		super(mgr, "b1", 2);
		//@formatter:off
		indexLabel.setText("分岐先の調査");
		questionLabel.setText("<html>この条件文(if)によってどの分岐先が実行されているか確認しましょう。確認した分岐先は正しいですか。</html>");
		descriptLabel.setText("<html>条件文に誤りがないかを確認します。<br>"
				+ "条件文に誤りがある場合、正解とは異なる分岐先を実行している場合が多いです。<br>"
				+ "そのため、まずどの分岐先が実行されているか確認します。</html>");
		//@formatter:on
		JButton button = buttons.get(0);
		button.setText("正しい");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				preInput = calledLine;
			}
		});

		button = buttons.get(1);
		button.setText("誤り");
	}

	public String getCalled() {
		return called;
	}

	@Override
	public void setInput(String notice) {
		super.setInput(notice);
		String[] notices = notice.split("-");
		called = notices[0];
		calledLine = notices[1];

		try {
			parent.setPartition(Integer.parseInt(notices[3]), Integer.parseInt(notices[4]));
		} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
			parent.setPartition(-1, -1);
		}

		noticeLabel.setText("着目している条件文　：　" + notices[2] + " 行目");
		parent.setNoticeLine(notices[2]);
		preInput = notices[2];
		postInput = parent.getPartition();
	}
}
