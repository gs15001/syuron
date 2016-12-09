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

public class Navi_r3 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_r3(NaviManager mgr) {
		super(mgr, "r3", 1, true);
		//@formatter:off
		indexLabel.setText("繰り返し処理の部分に誤りがあります");
		questionLabel.setText("<html>繰り返し回数を数えるための変数の初期値、<br>"
				+ "繰り返し文の条件、<br>"
				+ "繰り返し回数を数えるための処理<br>"
				+ "のいずれかに誤りがあります。順に確認しましょう。</html>");
		descriptLabel.setText("<html>一定の回数繰り返すタイプで繰り返し回数が正解と異なる場合、<br>"
				+ "その原因は、上記の3つのいずれかになります。<br>"
				+ "サンプルも参考にしながら確認しましょう。</html>");
		//@formatter:on
		JButton button = buttons.get(0);
		button.setText("終了");

		setSamplePane(new r3sample(mgr));
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
		noticeLabel.setText("着目している繰り返し文　：　" + notices[0] + " 行目");
		parent.setNoticeLine(notices[0]);
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
		noticeLabel.setText("着目している繰り返し文　：　" + preInput + " 行目");
	}
}

class r3sample extends AbstractSamplePane {

	private static final long serialVersionUID = 1L;

	public r3sample(NaviManager mgr) {
		super(mgr);
		JPanel pane = new JPanel();
		pane.setBackground(new Color(224, 224, 224));
		JLabel label = new JLabel(new ImageIcon("./res/r3.png"));
		((FlowLayout) pane.getLayout()).setVgap(50);;
		pane.add(label);
		addMainPane(pane);
	}
}
