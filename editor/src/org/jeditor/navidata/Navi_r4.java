/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jeditor.navi.InputMyDialog;
import org.jeditor.navi.NaviManager;

public class Navi_r4 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;
	private String[] notices;

	public Navi_r4(NaviManager mgr) {
		super(mgr, "r4", 2, true);
		//@formatter:off
		indexLabel.setText("条件に使用する変数の値の調査");
		questionLabel.setText("<html>着目している繰り返し文(while,for)の条件に使用する変数の値を確認しましょう。<br>"
				+ "確認した値は正しいですか。</html>");
		descriptLabel.setText("<html>一定の条件を満たすまで繰り返すタイプの繰り返し文では、条件の部分に誤りがないかを確認します。<br>"
				+ "条件の部分が誤る原因は、<br>"
				+ "・使用する変数の値が誤っている<br>"
				+ "・条件式が誤っている<br>"
				+ "のどちらかになります。<br>"
				+ "まず、前者の確認を行います。</html>");
		//@formatter:on
		JButton button = buttons.get(0);
		button.setText("正しい");

		button = buttons.get(1);
		button.setText("誤り");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				preInput = "";
			}
		});

		dialog[1] = new InputMyDialog(InputMyDialog.VARIABLE);

		setSamplePane(new r4sample(mgr));
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
	public void updateData(List<Integer> noticeLine, String returnLine, int[] partition, Set<Integer> partitionLines) {
		postInput = parent.getPartition();
		String preInput2 = "";
		for (int i = 0; i < noticeLine.size(); i++) {
			preInput2 += (noticeLine.get(i) + 1) + ",";
		}
		preInput2 = preInput2.substring(0, preInput2.length());
		notices[1] = returnLine;
		preInput = notices[0] + "-" + notices[1] + "-" + preInput2;
		noticeLabel.setText("着目している繰り返し文　：　" + preInput2 + " 行目");
	}
}

class r4sample extends AbstractSamplePane {

	private static final long serialVersionUID = 1L;

	public r4sample(NaviManager mgr) {
		super(mgr);
		JPanel pane = new JPanel();
		pane.setBackground(new Color(224, 224, 224));
		JLabel label = new JLabel(new ImageIcon("./res/r4.png"));
		((FlowLayout) pane.getLayout()).setVgap(50);
		pane.add(label);
		addMainPane(pane);
	}
}
