/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import javax.swing.JButton;
import org.jeditor.navi.NaviManager;

public class Navi_a2 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_a2(NaviManager mgr) {
		super(mgr, "a2", 2);
		//@formatter:off
		indexLabel.setText("条件分岐・繰り返しの確認");
		questionLabel.setText("<html>着目している処理は条件文(if)や繰り返し文(while,for)のブロック（{})内に<br>存在しますか。</html>");
		descriptLabel.setText("<html>着目している処理が条件文や繰り返し文のブロック内に存在する場合、<br>"
				+ "条件文や繰り返し文の誤りによって結果が誤ることがあるため、確認します。</html>");
		//@formatter:on
		JButton button = buttons.get(0);
		button.setText("存在する");

		button = buttons.get(1);
		button.setText("存在しない");
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
		preInput = notices[0];
		postInput = parent.getPartition();
	}
}
