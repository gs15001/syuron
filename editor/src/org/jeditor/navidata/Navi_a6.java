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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.jeditor.navi.InputMyDialog;
import org.jeditor.navi.NaviManager;

public class Navi_a6 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_a6(NaviManager mgr) {
		super(mgr, "a6", 3, true);
		//@formatter:off
		indexLabel.setText("条件文・繰り返し文の調査");
		questionLabel.setText("<html>着目している処理が存在している条件文(if)や繰り返し文(while,for)を"
				+ "<br>選択してください。<br>"
				+ "複数存在している場合は、外側(コードの上のほう）から選択してください。<br>"
				+ "条件文・繰り返し文を調べ終わると再度この画面に戻ってきます。<br>"
				+ "存在している条件文・繰り返し文を全て調べ終えた場合は、<br>"
				+ "「次へ」を選択してください。</html>");
		descriptLabel.setText("<html>着目している処理が存在している条件文や繰り返し文に誤りがないか<br>"
				+ "を調べていきます。<br>"
				+ "外側にある条件文や繰り返し文の方が結果に大きく影響することが多いため、"
				+ "<br>外側から順に調べていきます。</html>");
		//@formatter:on
		JButton button = buttons.get(0);
		button.setText("条件文");
		button = buttons.get(1);
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//@formatter:off
				String m = "以降、繰り返し文のブロック({})内でprint文を挿入する場合、繰り返し回数分出力がされます。\n"
						+ "全部を確認する必要はなく、一部だけを確認し、正しいかどうか判断しましょう。\n\n"
						+ "ただし、確認しなかった部分に誤りがあるかもしれないことを忘れないようにしましょう。";
				//@formatter:on
				JOptionPane.showMessageDialog(parent, m, "注意", JOptionPane.WARNING_MESSAGE);
			}
		});
		button.setText("繰り返し文");
		button = buttons.get(2);
		button.setText("次へ");
		dialog[0] = new InputMyDialog(InputMyDialog.IF);
		dialog[1] = new InputMyDialog(InputMyDialog.FOR);

		setSamplePane(new a6sample(mgr));
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
		preInput = getIndex() + "-" + notices[0] + "-";
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
		
		
		preInput = getIndex() + "-" + preInput2 + "-";
		noticeLabel.setText("着目している処理　：　" + preInput2 + " 行目");
	}
}

class a6sample extends AbstractSamplePane {

	private static final long serialVersionUID = 1L;

	public a6sample(NaviManager mgr) {
		super(mgr);
		JPanel pane = new JPanel();
		pane.setBackground(new Color(224, 224, 224));
		JLabel label = new JLabel(new ImageIcon("./res/a6.png"));
		((FlowLayout) pane.getLayout()).setVgap(50);;
		pane.add(label);
		addMainPane(pane);
	}
}
