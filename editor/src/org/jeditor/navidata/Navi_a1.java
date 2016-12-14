/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.net.URL;
import java.util.List;
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
		super(mgr, "a1", 3, true);

		indexLabel.setText("誤っている変数の原因を探す");
		// @formatter:off
		questionLabel.setText("<html>誤っている変数に代入している処理を探しましょう。<br>"
				+ "変数に代入している処理とは、その変数が左辺にある処理です。<br>"
				+ "複数ある場合は一番下にある処理を対象とします。<br><br>"
				+ "※誤っている変数が配列の場合は、「配列」を選択してください。<br>"
				+ "配列に代入している処理全てを対象とします。</html>");

		descriptLabel.setText("<html>まずは、変数の値が誤っている原因を探します。<br>"
				+ "変数は代入された時だけ、値が変更されます。<br>"
				+ "つまり、変数の値が誤っている場合、代入する部分（右辺）が誤っている<br>"
				+ "ということになるため、その部分を探します。</html>");
		// @formatter:on

		JButton button = buttons.get(0);
		button.setText("ある");
		button = buttons.get(1);
		button.setText("配列");
		button = buttons.get(2);
		button.setText("ない");

		dialog[0] = new InputMyDialog(InputMyDialog.ROW);
		dialog[1] = new InputMyDialog("配列に代入している処理の行番号を全て入力してください\n行番号は、「,」で区切って複数入力してください", "代入している処理の行番号の入力");
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
	public void updateData(List<Integer> noticeLine, String returnLine, int[] partition, Set<Integer> partitionLines) {
		postInput = parent.getPartition();
	}
}

class a1sample extends AbstractSamplePane {

	private static final long serialVersionUID = 1L;

	public a1sample(NaviManager mgr) {
		super(mgr);
		JPanel pane = new JPanel();
		pane.setBackground(new Color(224, 224, 224));

		ClassLoader classLoader = this.getClass().getClassLoader();
		URL resUrl = classLoader.getResource("res/a1.png");
		JLabel label = new JLabel(new ImageIcon(resUrl));

		// JLabel label = new JLabel(new ImageIcon("./res/a1.png"));
		((FlowLayout) pane.getLayout()).setVgap(5);
		pane.add(label);
		addMainPane(pane);
	}
}
