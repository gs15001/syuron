/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jeditor.navi.InputMyDialog;
import org.jeditor.navi.NaviManager;

public class Navi_a1 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_a1(NaviManager mgr) {
		super(mgr, "a1", 2, true);

		indexLabel.setText("誤っている変数の原因を探す");
		// @formatter:off
		questionLabel.setText("<html>誤っている変数に代入している処理を探しましょう。<br>" + "変数に代入している処理とは、その変数が左辺にある処理です。<br>"
				+ "複数ある場合は一番下にある処理を対象とします。</html>");

		descriptLabel.setText("<html>誤っている変数を見つけたなら、次はその原因を探します。<br>" + "変数は代入された時だけ、値が変更されます。<br>"
				+ "つまり、変数の値が誤っている場合、代入した値が誤っていることになるため、<br>その部分を探します。</html>");
		// @formatter:on

		JButton button = buttons.get(0);
		button.setText("ある");
		button = buttons.get(1);
		button.setText("ない");

		dialog[0] = new InputMyDialog(InputMyDialog.ROW);
		setSamplePane(new a1sample(mgr));
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
		noticeLabel.setText("誤っている変数　：　" + notices[0]);
		parent.setVariable(notices[0]);
		postInput = parent.getPartition();
	}

	@Override
	public void updateData(int noticeLine, int returnLine, int[] partition, Set<Integer> partitionLines) {
		postInput = parent.getPartition();
	}
}

class a1sample extends AbstractSamplePane {

	private static final long serialVersionUID = 1L;

	public a1sample(NaviManager mgr) {
		super(mgr);
		JPanel pane = new JPanel();
		pane.setBackground(new Color(224, 224, 224));
		JLabel label = new JLabel(new ImageIcon("./res/a1.png"));
		((FlowLayout) pane.getLayout()).setVgap(50);;
		pane.add(label);
		addMainPane(pane);
	}
}
