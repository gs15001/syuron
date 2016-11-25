/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import java.awt.Color;
import java.awt.FlowLayout;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jeditor.navi.NaviManager;

public class Navi_r1 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;
	private String[] notices;

	public Navi_r1(NaviManager mgr) {
		super(mgr, "r1", 2, true);
		// @formatter:off
		indexLabel.setText("繰り返し文の種類を調査");
		questionLabel.setText("<html>この繰り返し文(while,for)の繰り返し回数は分かりますか。</html>");
		descriptLabel.setText("<html>繰り返し文の種類を確認します。<br>" + "繰り返し文には主に、<br>" + "・一定の回数繰り返すタイプ<br>"
				+ "・一定の条件を満たすまで繰り返すタイプ<br>" + "の2種類があります。種類に応じて、確認すべき部分が異なるため、<br>まず種類を確認します。</html>");
		// @formatter:on
		JButton button = buttons.get(0);
		button.setText("はい");
		button = buttons.get(1);
		button.setText("いいえ");

		setSamplePane(new r1sample(mgr));
	}

	@Override
	public void setInput(String notice) {
		super.setInput(notice);
		notices = notice.split("-");

		try {
			parent.setPartition(Integer.parseInt(notices[3]), Integer.parseInt(notices[4]));
		} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
			parent.setPartition(-1, -1);
		}
		noticeLabel.setText("着目している繰り返し文　：　" + notices[2] + " 行目");
		parent.setNoticeLine(notices[2]);
		parent.setReturnLine(notices[1]);
		preInput = notices[0] + "-" + notices[1] + "-" + notices[2];
		postInput = parent.getPartition();
	}

	@Override
	public void updateData(int noticeLine, int returnLine, int[] partition, Set<Integer> partitionLines) {
		postInput = parent.getPartition();
		notices[2] = noticeLine + "";
		notices[1] = returnLine + "";
		preInput = notices[0] + "-" + notices[1] + "-" + notices[2];
		noticeLabel.setText("着目している繰り返し文　：　" + noticeLine + " 行目");
	}
}

class r1sample extends AbstractSamplePane {

	private static final long serialVersionUID = 1L;

	public r1sample(NaviManager mgr) {
		super(mgr);
		JPanel pane = new JPanel();
		pane.setBackground(new Color(224, 224, 224));
		JLabel label = new JLabel(new ImageIcon("./res/r1.png"));
		((FlowLayout) pane.getLayout()).setVgap(50);;
		pane.add(label);
		addMainPane(pane);
	}
}
