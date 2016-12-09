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

public class Navi_b2 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_b2(NaviManager mgr) {
		super(mgr, "b2", 2, true);
		// @formatter:off
		indexLabel.setText("条件に使用する変数の値の調査");
		questionLabel.setText("<html>着目している条件文(if)の条件に使用する変数の値を確認しましょう。<br>"
				+ "確認した値は正しいですか。</html>");
		descriptLabel.setText("<html>実行する分岐先が正解とは異なるため、この条件文に誤りがあります。<br>"
				+ "その誤りの原因は、<br>"
				+ "・使用する変数の値が誤っている<br>"
				+ "・条件式が誤っている<br>"
				+ "のどちらかになります。<br>"
				+ "まず、前者の確認を行います。</html>");
		// @formatter:on
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

		setSamplePane(new b2sample(mgr));
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
		noticeLabel.setText("着目している条件文　：　" + notices[0] + " 行目");
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
		noticeLabel.setText("着目している条件文　：　" + preInput + " 行目");
	}
}

class b2sample extends AbstractSamplePane {

	private static final long serialVersionUID = 1L;

	public b2sample(NaviManager mgr) {
		super(mgr);
		JPanel pane = new JPanel();
		pane.setBackground(new Color(224, 224, 224));
		JLabel label = new JLabel(new ImageIcon("./res/b2.png"));
		((FlowLayout) pane.getLayout()).setVgap(50);;
		pane.add(label);
		addMainPane(pane);
	}
}