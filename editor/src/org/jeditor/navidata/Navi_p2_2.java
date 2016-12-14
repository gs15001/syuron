/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jeditor.navi.NaviManager;

public class Navi_p2_2 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;
	private int[] startEnd = new int[4];
	private String[] startEndString = new String[4];
	private Set<Integer> partitionLineSet = null;

	public Navi_p2_2(NaviManager mgr) {
		super(mgr, "p2_2", 2, true);

		indexLabel.setText("まとまりの動作確認");

		questionLabel.setText("");
		descriptLabel.setText("");

		JButton button = buttons.get(0);
		button.setText("正しい");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				preInput = String.valueOf(startEnd[2]) + "-" + String.valueOf(startEnd[3]) + "-"
						+ String.valueOf(startEnd[1]);
				postInput = parent.getPartitionLine();
			}
		});

		button = buttons.get(1);
		button.setText("誤り");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				preInput = String.valueOf(startEnd[0]) + "-" + String.valueOf(startEnd[2]) + "-2";
				postInput = parent.getPartitionLine();
			}
		});

		// 変数名を入力させるか
		// dialog[1] = new InputMyDialog(InputMyDialog.VARIABLE);

		setSamplePane(new p2_2sample(mgr));
	}

	private void refreshLabel() {
		noticeLabel.setText("着目しているまとまり：" + startEndString[0] + "から" + startEndString[2]);
		// @formatter:off
		questionLabel.setText("<html>" + startEndString[0] + "から" + startEndString[2]
				+ "までのまとまりが正しく動作しているか確認しましょう。<br>"
				+ "境目である" + (startEnd[2]+1) + "行目にprint文を挿入して、必要な変数の値を確認しましょう。<br>"
				+ "確認した値は正しいですか。</html>");

		descriptLabel.setText("<html>プログラムをまとまりに分割したので、次はまとまりごとに正しく動作しているか確認します。<br>"
				+ "正しく動作しているかは、変数の値を確認することで確かめることができます。<br>"
				+ "確認するべき変数は、" + startEndString[2] + "までのまとまりと" + (startEnd[3]+1) + "行目以降のまとまりで<br>"
				+ "共通している変数、もしくは、メソッドの引数に使用されている変数です。<br>"
				+ "正しい変数の値はプログラムの過程を紙などに書いて求めましょう。</html>");
		// @formatter:on

	}

	@Override
	public void setInput(String notice) {
		super.setInput(notice);
		partitionLineSet = new HashSet<>();

		String[] notices = notice.split("-");
		for (int i = 0; i < notices.length; i++) {
			try {
				int tmp = Integer.parseInt(notices[i]);
				// エディターにライン表示のための値を渡す準備
				partitionLineSet.add(tmp);
				if(i < 2) {
					startEnd[i] = tmp;
					if(i == 0) {
						startEndString[i] = (startEnd[0] + 1) + "行目";
					} else {
						startEndString[i] = notices[i] + "行目";
					}
				} else if(i < 4) {
					startEnd[i] = tmp;
					startEndString[i] = startEnd[i] + "行目";
					// エディターにライン表示のための値を渡す準備
					partitionLineSet.add(startEnd[i]);
				}
			} catch (NumberFormatException e) {
				if(i <= 2) {
					startEnd[i] = -1;
					startEndString[i] = "エラー行目";
				}
			}
		}
		// 値の確認 問題なければエディターに必要な値を渡す
		if(!(startEnd[0] <= startEnd[2] && startEnd[2] <= startEnd[1])) {
			startEnd[0] = startEnd[1] = startEnd[2] = -1;
			startEndString[0] = startEndString[1] = startEndString[2] = "エラー行目";
		} else {
			parent.setPartition(startEnd[0], startEnd[2]);
			parent.setPartitionLine(partitionLineSet);
		}
		noticeLabel.setText("着目しているまとまり：" + startEndString[0] + "から" + startEndString[2]);
		refreshLabel();
	}

	@Override
	public void updateData(List<Integer> noticeLine, String returnLine, int[] partition, Set<Integer> partitionLines) {
		// 終点を探す
		TreeSet<Integer> set = new TreeSet<>(partitionLines);
		Iterator<Integer> ite = set.iterator();
		while (ite.hasNext()) {
			if(ite.next().intValue() == partition[1]) {
				startEnd[3] = ite.next().intValue();// メソッドの終わりを指しているやつ
				startEndString[3] = startEnd[3] + "行目";
				startEnd[1] = ite.next().intValue();// 次のまとまりの終点
				break;
			}
		}

		startEnd[0] = partition[0];
		startEndString[0] = (startEnd[0] + 1) + "行目";
		startEnd[2] = partition[1];
		startEndString[2] = startEnd[2] + "行目";
		refreshLabel();
	}
}

class p2_2sample extends AbstractSamplePane {

	private static final long serialVersionUID = 1L;

	public p2_2sample(NaviManager mgr) {
		super(mgr);
		JPanel pane = new JPanel();
		pane.setBackground(new Color(224, 224, 224));
		JLabel label = new JLabel(new ImageIcon("./res/p2_2.png"));
		((FlowLayout) pane.getLayout()).setVgap(5);
		pane.add(label);
		addMainPane(pane);
	}
}
