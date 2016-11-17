/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import javax.swing.JButton;
import org.jeditor.navi.NaviManager;

public class Navi_a0 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_a0(NaviManager mgr) {
		super(mgr, "a0", 1);

		indexLabel.setText("ステップ4 バグの特定");
		//@formatter:off
		questionLabel.setText("<html>ステップ3で、バグの潜む範囲を絞り込みました。<br>"
				+ "ステップ4では、誤っている変数を手がかりにその範囲を細かく調べ、<br>バグを特定していきます。</html>");
		
		descriptLabel.setText("<html>以降、「左辺」と「右辺」という言葉がでてきます。<br>"
				+ "数学の方程式と同様に、「左辺」は「=」の左側、「右辺」は「=」の右側を<br>示します。</html>");
		//@formatter:on

		JButton button = buttons.get(0);
		button.setText("次へ");
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

		noticeLabel.setText("誤っている変数　：　" + notices[0]);
		parent.setVariable(notices[0]);
		preInput = notices[0];
		postInput = parent.getPartition();
	}
}
