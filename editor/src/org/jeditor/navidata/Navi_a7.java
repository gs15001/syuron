/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import javax.swing.JButton;
import org.jeditor.navi.NaviManager;

public class Navi_a7 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_a7(NaviManager mgr) {
		super(mgr, "a7", 2);
		//@formatter:off
		indexLabel.setText("使用する変数の調査");
		questionLabel.setText("<html>右辺に出てきた誤っている変数の変数名は正しいですか。<br>"
				+ "使用する変数を間違えてはいませんか。</html>");
		descriptLabel.setText("<html>似たような変数名の変数があると使用する変数を間違えてしまうことがあります。念の為にも確認しましょう。</html>");
		//@formatter:on
		JButton button = buttons.get(0);
		button.setText("正しい");
		button = buttons.get(1);
		button.setText("誤り");
	}

	public void setInput(String notice) {
		super.setInput(notice);
		String[] notices = notice.split("-");

		try {
			parent.setPartition(Integer.parseInt(notices[1]), Integer.parseInt(notices[2]));
		} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
			parent.setPartition(-1, -1);
		}
		noticeLabel.setText("誤っている変数　：　" + notices[0]);
		parent.setVariable(notices[0]);
		preInput = notices[0];
		postInput = parent.getPartition();
	}
}
