/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jeditor.navi.InputMyDialog;
import org.jeditor.navi.NaviManager;

public class Navi_p4 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;
	private int[] startEnd = new int[3];
	private String[] startEndString = new String[3];
	private Set<Integer> partitionLineSet = null;
	private boolean oneLine = true;

	public Navi_p4(NaviManager mgr) {
		super(mgr, "p4", 2, true);

		indexLabel.setText("メソッドの動作確認");

		questionLabel.setText("");
		descriptLabel.setText("");

		JButton button = buttons.get(0);
		button.setText("正しい");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				preInput = String.valueOf(startEnd[1] + 1) + "-" + String.valueOf(startEnd[2]) + "-1";
				postInput = parent.getPartitionLine();
			}
		});

		button = buttons.get(1);
		button.setText("誤り");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				preInput = "";
				postInput = "-3" + parent.getPartitionLine();
			}
		});

		dialog[1] = new InputMyDialog("メソッドが定義されている行番号を入力してください。\n「開始行-終了行」の形式で入力", "メソッドの定義されている行番号の入力", 6);

		setSamplePane(new p4sample(mgr));
	}

	private void refreshLabel() {
		if(oneLine) {
			noticeLabel.setText("着目しているまとまり：" + startEndString[1] + "のメソッド");
			//@formatter:off
		questionLabel.setText("<html>" + startEndString[1] + "のメソッドが正しく動作しているか確認しましょう。<br>"
				+ "メソッド呼び出し後の" + (startEnd[1] + 1) + "行目にprint文を挿入して、必要な変数の値を確認しましょう。<br>"
				+ "確認した値は正しいですか。<br>"
				+ "※「誤り」を選択する場合、メソッドが定義されている行数を入力します。<br>"
				+ "別ファイルにメソッドが定義されている場合は、別ファイルを開いた状態で<br>"
				+ "「誤り」を選択し、行数を入力してください。</html>");

		descriptLabel.setText("<html>" + startEnd[0] + "行目までのまとまりは正しく動いており、バグが無いことが確認できました。<br>"
				+ "続いて、" + startEndString[1] + "のメソッドにバグが無いか確認します。<br>"
				+ "確認するべき変数は、" + (startEnd[1] + 1) + "行目以降のまとまりでも使用している変数です。<br>"
				+ "基本的にはメソッドの返り値を確認します。<br>"
				+ "正しい変数の値はプログラムの過程を紙などに書いて求めましょう。</html>");
		//@formatter:on
		} else {
			noticeLabel.setText("着目しているまとまり：" + (startEnd[0] + 1) + "～" + (startEndString[1]) + "内のメソッド");
			//@formatter:off
		questionLabel.setText("<html>" + (startEnd[0] + 1) + "～" + (startEndString[1]) + "内のメソッドが正しく動作しているか確認しましょう。<br>"
				+ "メソッド呼び出し後の" + (startEnd[1] + 1) + "行目にprint文を挿入して、必要な変数の値を確認しましょう。<br>"
				+ "確認した値は正しいですか。<br>"
				+ "※「誤り」を選択する場合、メソッドが定義されている行数を入力します。<br>"
				+ "別ファイルにメソッドが定義されている場合は、別ファイルを開いた状態で<br>"
				+ "「誤り」を選択し、行数を入力してください。</html>");

		descriptLabel.setText("<html>" + startEnd[0] + "行目までのまとまりは正しく動いており、バグが無いことが確認できました。<br>"
				+ "続いて、" + (startEnd[0] + 1) + "～" + (startEndString[1]) + "内のメソッドにバグが無いか確認します。<br>"
				+ "確認するべき変数は、" + (startEnd[1] + 1) + "行目以降のまとまりでも使用している変数です。<br>"
				+ "基本的にはメソッドの返り値を確認します。<br>"
				+ "正しい変数の値はプログラムの過程を紙などに書いて求めましょう。</html>");
		//@formatter:on
		}
	}

	@Override
	public void setInput(String notice) {
		super.setInput(notice);
		partitionLineSet = new HashSet<>();

		String[] notices = notice.split("-");
		for (int i = 0; i < notices.length; i++) {
			try {
				int tmp = Integer.parseInt(notices[i]);
				// エディターにライン表示のための値を渡す
				partitionLineSet.add(tmp);
				if(i <= 2) {
					startEnd[i] = tmp;
					startEndString[i] = notices[i] + "行目";
				}
				if(i == 1) {
					if(startEnd[1] - startEnd[0] == 1) {
						oneLine = true;
					} else {
						oneLine = false;
					}
				}
			} catch (NumberFormatException e) {
				if(i <= 2) {
					startEnd[i] = -1;
					startEndString[i] = "エラー行目";
				}
			}
		}
		// 値の確認 問題なければエディターに必要な値を渡す
		if(!(startEnd[0] <= startEnd[1])) {
			startEnd[0] = startEnd[1] = -1;
			startEndString[0] = startEndString[1] = "エラー行目";
		} else {
			parent.setPartition(startEnd[0], startEnd[1]);
			parent.setPartitionLine(partitionLineSet);
		}
		refreshLabel();
	}

	@Override
	public void updateData(List<Integer> noticeLine, String returnLine, int[] partition, Set<Integer> partitionLines) {
		// 終点を探す
		TreeSet<Integer> set = new TreeSet<>(partitionLines);
		Iterator<Integer> ite = set.iterator();
		while (ite.hasNext()) {
			if(ite.next().intValue() == partition[1]) {
				startEnd[2] = ite.next().intValue();
				break;
			}
		}

		startEnd[0] = partition[0];
		startEnd[1] = partition[1];
		startEndString[0] = startEnd[0] + "行目";
		startEndString[1] = startEnd[1] + "行目";

		if(startEnd[1] - startEnd[0] == 1) {
			oneLine = true;
		} else {
			oneLine = false;
		}
		refreshLabel();
	}
}

class p4sample extends AbstractSamplePane {

	private static final long serialVersionUID = 1L;

	public p4sample(NaviManager mgr) {
		super(mgr);
		JPanel pane = new JPanel();
		pane.setBackground(new Color(224, 224, 224));

		ClassLoader classLoader = this.getClass().getClassLoader();
		URL resUrl = classLoader.getResource("res/p4.png");
		JLabel label = new JLabel(new ImageIcon(resUrl));

		// JLabel label = new JLabel(new ImageIcon("./res/p4.png"));
		((FlowLayout) pane.getLayout()).setVgap(5);
		pane.add(label);
		addMainPane(pane);
	}
}