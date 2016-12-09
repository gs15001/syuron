/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import java.util.List;
import java.util.Set;
import javax.swing.JButton;
import org.jeditor.navi.InputMyDialog;
import org.jeditor.navi.NaviManager;

public class Navi_a5 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_a5(NaviManager mgr) {
		super(mgr, "a5", 3, true);
		//@formatter:off
		indexLabel.setText("条件分岐・繰り返し文の調査");
		questionLabel.setText("<html>現在着目している範囲にある条件文(if)や繰り返し文(while,for)を選択してください。<br>"
				+ "複数存在している場合は、外側(コードの上のほう）から選択してください。<br>"
				+ "全て調べ終えた場合は、「次へ」を選択してください。</html>");
		descriptLabel.setText("<html>誤っている変数があるにもかかわらず、代入している処理が"
				+ "ない場合、誤る原因は条件文や繰り返し文にあると考えられます。<br>"
				+ "着目している範囲にある条件文や繰り返し文に誤りがないか確認します。</html>");
		//@formatter:on
		JButton button = buttons.get(0);
		button.setText("条件文");
		button = buttons.get(1);
		button.setText("繰り返し文");
		button = buttons.get(2);
		button.setText("次へ");
		dialog[0] = new InputMyDialog(InputMyDialog.IF);
		dialog[1] = new InputMyDialog(InputMyDialog.FOR);
		
		setSamplePane(new a6sample(mgr));
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

	@Override
	public void updateData(List<Integer> noticeLine, String returnLine, int[] partition, Set<Integer> partitionLines) {
		postInput = parent.getPartition();
		preInput = "";
		for (int i = 0; i < noticeLine.size(); i++) {
			preInput += (noticeLine.get(i) + 1) + ",";
		}
		preInput = preInput.substring(0, preInput.length());
		noticeLabel.setText("着目している処理　：　" + preInput + " 行目");
	}
}
