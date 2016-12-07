/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jeditor.navi.NaviManager;

public class Navi_r2 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	private String called;

	public Navi_r2(NaviManager mgr) {
		super(mgr, "r2", 2, true);
		// @formatter:off
		indexLabel.setText("繰り返し回数の調査");
		questionLabel.setText("<html>着目している繰り返し文(while,for)の繰り返し回数は正しいですか。<br>"
				+ "繰り返し文のブロック({})の中にprint文を挿入し、繰り返し回数を<br>数えてみましょう。</html>");
		descriptLabel.setText("<html>一定の回数繰り返すタイプの繰り返し文では、正しい回数 繰り返しているかを<br>"
				+ "確認します。</html>");
		// @formatter:on
		JButton button = buttons.get(0);
		button.setText("正しい");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				preInput = parent.getReturnLine() + "";
			}
		});

		button = buttons.get(1);
		button.setText("誤り");

		setSamplePane(new r2sample(mgr));
	}

	public String getCalled() {
		return called;
	}

	@Override
	public void setInput(String notice) {
		super.setInput(notice);
		String[] notices = notice.split("-");
		called = notices[0];
		parent.setReturnLine(notices[1]);

		try {
			parent.setPartition(Integer.parseInt(notices[3]), Integer.parseInt(notices[4]));
		} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
			parent.setPartition(-1, -1);
		}
		noticeLabel.setText("着目している繰り返し文　：　" + notices[2] + " 行目");
		parent.setNoticeLine(notices[2]);
		preInput = notices[2];
		postInput = parent.getPartition();
	}

	@Override
	public void updateData(int noticeLine, int returnLine, int[] partition, Set<Integer> partitionLines) {
		postInput = parent.getPartition();
		preInput = noticeLine + "";
		noticeLabel.setText("着目している繰り返し文　：　" + noticeLine + " 行目");
	}
}

class r2sample extends AbstractSamplePane {

	private static final long serialVersionUID = 1L;

	public r2sample(NaviManager mgr) {
		super(mgr);
		JPanel pane = new JPanel();
		pane.setBackground(new Color(224, 224, 224));
		JLabel label = new JLabel(new ImageIcon("./res/r2.png"));
		((FlowLayout) pane.getLayout()).setVgap(50);;
		pane.add(label);
		addMainPane(pane);
	}
}
