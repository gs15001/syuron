/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.List;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jeditor.navi.NaviManager;

public class Navi_b1 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	private String called;

	public Navi_b1(NaviManager mgr) {
		super(mgr, "b1", 2, true);
		//@formatter:off
		indexLabel.setText("分岐先の調査");
		questionLabel.setText("<html>着目している条件文(if)によってどの分岐先が実行されているか確認しましょう。<br>"
				+ "実行されている分岐先は正しいですか。</html>");
		descriptLabel.setText("<html>条件文に誤りがないかを確認します。<br>"
				+ "条件文に誤りがある場合、正解とは異なる分岐先の処理を実行してしまいます。<br>"
				+ "そのため、まずどの分岐先が実行されているか確認します。<br>"
				+ "分岐先の確認はそれぞれに分岐先にprint文を挿入し、出力を確認することで<br>"
				+ "できます。</html>");
		//@formatter:on
		JButton button = buttons.get(0);
		button.setText("正しい");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				preInput = parent.getReturnLine();
				parent.clearReturnLine();
			}
		});

		button = buttons.get(1);
		button.setText("誤り");

		setSamplePane(new b1sample(mgr));
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

		noticeLabel.setText("着目している条件文　：　" + notices[2] + " 行目");
		parent.setNoticeLine(notices[2]);
		preInput = notices[2];
		postInput = parent.getPartition();
	}

	@Override
	public void updateData(List<Integer> noticeLine, String returnLine, int[] partition, Set<Integer> partitionLines) {
		postInput = parent.getPartition();
		preInput = "";
		for (int i = 0; i < noticeLine.size(); i++) {
			preInput += (noticeLine.get(i) + 1) + ",";
		}
		preInput = preInput.substring(0, preInput.length() - 1);
		noticeLabel.setText("着目している条件文　：　" + preInput + " 行目");
	}
}

class b1sample extends AbstractSamplePane {

	private static final long serialVersionUID = 1L;

	public b1sample(NaviManager mgr) {
		super(mgr);
		JPanel pane = new JPanel();
		pane.setBackground(new Color(224, 224, 224));

		ClassLoader classLoader = this.getClass().getClassLoader();
		URL resUrl = classLoader.getResource("res/b1.png");
		JLabel label = new JLabel(new ImageIcon(resUrl));

		// JLabel label = new JLabel(new ImageIcon("./res/b1.png"));
		((FlowLayout) pane.getLayout()).setVgap(50);
		pane.add(label);
		addMainPane(pane);
	}
}
