/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import java.awt.Color;
import java.awt.FlowLayout;
import java.util.List;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jeditor.navi.NaviManager;

public class Navi_a8 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_a8(NaviManager mgr) {
		super(mgr, "a8", 2, true);

		indexLabel.setText("配列の添字の確認");
		// @formatter:off
		questionLabel.setText("<html>配列の添字は正しいですか。<br>"
				+ "添字に変数を使用している場合は、その変数の値を確認しましょう。<br>"
				+ "※着目している処理が繰り返し文のブロック({})内にある場合は、print文の出力が<br>"
				+ "　繰り返し回数分出力されます。全ての出力を確認してもいいですが、一部だけの<br>"
				+ "　確認で正しいか判断しても問題ありません。<br><br>"
				+ "ただし、確認しなかった部分に誤りがある可能性を忘れないようにしましょう。</html>");

		descriptLabel.setText("<html>誤っている変数が配列の場合は、まず添字の確認を行います。<br>"
				+ "代入している値を確認する前に、代入する場所（配列の位置）を確認します。<br>"
				+ "特に、添字に変数を使用している場合は確認を怠らないようにしましょう。</html>");
		// @formatter:on

		JButton button = buttons.get(0);
		button.setText("正しい");
		button = buttons.get(1);
		button.setText("誤り");

		setSamplePane(new a8sample(mgr));
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

	class a8sample extends AbstractSamplePane {

		private static final long serialVersionUID = 1L;

		public a8sample(NaviManager mgr) {
			super(mgr);
			JPanel pane = new JPanel();
			pane.setBackground(new Color(224, 224, 224));
			JLabel label = new JLabel(new ImageIcon("./res/a8.png"));
			((FlowLayout) pane.getLayout()).setVgap(50);
			pane.add(label);
			addMainPane(pane);
		}
	}
}
