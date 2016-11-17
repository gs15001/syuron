/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import javax.swing.JButton;
import org.jeditor.navi.InputMyDialog;
import org.jeditor.navi.NaviManager;

public class Navi_a6 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_a6(NaviManager mgr) {
		super(mgr, "a6", 3);
		//@formatter:off
		indexLabel.setText("条件分岐・繰り返し文の調査");
		questionLabel.setText("<html>着目している処理が存在している条件文(if)や繰り返し文(while,for)を<br>選択してください。<br>"
				+ "複数存在している場合は、外側(コードの上のほう）から選択してください。<br>"
				+ "全て調べ終えた場合は、「次へ」を選択してください。</html>");
		descriptLabel.setText("<html>着目している処理が存在している条件文(if)や繰り返し文(while,for)に誤りが<br>ないかを調べていきます。<br>"
				+ "外側にある条件文や繰り返し文の方が結果に大きく影響することが多いため、<br>外側から調べていきます。</html>");
		//@formatter:on
		JButton button = buttons.get(0);
		button.setText("条件文");
		button = buttons.get(1);
		button.setText("繰り返し文");
		button = buttons.get(2);
		button.setText("次へ");
		dialog[0] = new InputMyDialog(InputMyDialog.IF);
		dialog[1] = new InputMyDialog(InputMyDialog.FOR);
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
		preInput = getIndex() + "-" + notices[0] + "-";
		postInput = parent.getPartition();
	}
}
