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

public class Navi_a4 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_a4(NaviManager mgr) {
		super(mgr, "a4", 2, true);
		//@formatter:off
		indexLabel.setText("誤っている変数の右辺の式を調査");
		questionLabel.setText("<html>見つけた処理の右辺の式を確認しましょう。<br>"
				+ "複雑な計算を行っている場合は、分解して1つ1つ確認しましょう。<br>"
				+ "確認した結果、式は正しいですか。</html>");
		descriptLabel.setText("<html>代入する値が誤る原因のうち、「使用する変数が誤っている」は先ほど正しいことを確認しました。<br>"
				+ "残る「式が誤っている」の確認を行います。</html>");
		//@formatter:on
		JButton button = buttons.get(0);
		button.setText("正しい");
		button = buttons.get(1);
		button.setText("誤り");

		setSamplePane(new a4sample(mgr));
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
		postInput = parent.getPartition();
	}

	@Override
	public void updateData(int noticeLine, int returnLine, int[] partition, Set<Integer> partitionLines) {
		postInput = parent.getPartition();
		preInput = noticeLine + "";
		noticeLabel.setText("着目している処理　：　" + noticeLine + " 行目");
	}
}

class a4sample extends AbstractSamplePane {

	private static final long serialVersionUID = 1L;

	public a4sample(NaviManager mgr) {
		super(mgr);
		JPanel pane = new JPanel();
		pane.setBackground(new Color(224, 224, 224));
		JLabel label = new JLabel(new ImageIcon("./res/a4.png"));
		((FlowLayout) pane.getLayout()).setVgap(50);;
		pane.add(label);
		addMainPane(pane);
	}
}
