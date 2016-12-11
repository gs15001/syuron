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

public class Navi_a2 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_a2(NaviManager mgr) {
		super(mgr, "a2", 2, true);
		// @formatter:off
		indexLabel.setText("条件分岐・繰り返しの確認");
		questionLabel.setText("<html>着目している処理は条件文(if)や繰り返し文(while,for)のブロック（{})内に"
				+ "<br>存在しますか。<br>"
				+ "複数行着目している場合は、いずれかの行がブロック内に存在していれば<br>"
				+ "「存在する」を選択してください。</html>");
		descriptLabel.setText("<html>着目している処理が条件文や繰り返し文のブロック内に存在する場合、<br>"
				+ "条件文や繰り返し文の誤りによって結果が誤ることがあるため、確認します。</html>");
		// @formatter:on
		JButton button = buttons.get(0);
		button.setText("存在する");

		button = buttons.get(1);
		button.setText("存在しない");
		setSamplePane(new a2sample(mgr));
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
}

class a2sample extends AbstractSamplePane {

	private static final long serialVersionUID = 1L;

	public a2sample(NaviManager mgr) {
		super(mgr);
		JPanel pane = new JPanel();
		pane.setBackground(new Color(224, 224, 224));
		JLabel label = new JLabel(new ImageIcon("./res/a2.png"));
		((FlowLayout) pane.getLayout()).setVgap(20);
		pane.add(label);
		addMainPane(pane);
	}
}
