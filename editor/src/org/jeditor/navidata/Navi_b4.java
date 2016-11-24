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

public class Navi_b4 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_b4(NaviManager mgr) {
		super(mgr, "b4", 2, true);
		// @formatter:off
		indexLabel.setText("条件の条件式の調査");
		questionLabel.setText("<html>この条件文(if)の条件式を確認しましょう。<br>" + "複雑な計算を行っている場合は、分解して1つ1つ確認しましょう。<br>"
				+ "確認した結果、条件式は正しいですか。</html>");
		descriptLabel.setText("<html>条件文の誤りの原因のうち、「使用する変数が誤っている」は<br>先ほど正しいことを確認しました。<br>"
				+ "残る「条件式が誤っている」の確認を行います。</html>");
		// @formatter:on
		JButton button = buttons.get(0);
		button.setText("正しい");
		button = buttons.get(1);
		button.setText("誤り");
		
		setSamplePane(new b4sample(mgr));
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
		postInput = parent.getPartition();
	}

	@Override
	public void updateData(int noticeLine, int returnLine, int[] partition, Set<Integer> partitionLines) {
		postInput = parent.getPartition();
		preInput = noticeLine + "";
		noticeLabel.setText("着目している条件文　：　" + noticeLine + " 行目");
	}
}

class b4sample extends AbstractSamplePane {

	private static final long serialVersionUID = 1L;

	public b4sample(NaviManager mgr) {
		super(mgr);
		JPanel pane = new JPanel();
		pane.setBackground(new Color(224, 224, 224));
		JLabel label = new JLabel(new ImageIcon("./res/b4.png"));
		((FlowLayout) pane.getLayout()).setVgap(50);;
		pane.add(label);
		addMainPane(pane);
	}
}