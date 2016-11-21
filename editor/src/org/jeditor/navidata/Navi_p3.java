/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JButton;
import org.jeditor.navi.InputMyDialog;
import org.jeditor.navi.NaviManager;

public class Navi_p3 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;
	private int[] startEnd = new int[3];
	private String[] startEndString = new String[3];
	private Set<Integer> partitionLineSet = null;

	public Navi_p3(NaviManager mgr) {
		super(mgr, "p3", 3);

		indexLabel.setText("さらなるまとまり分割");

		questionLabel.setText("");
		descriptLabel.setText("");

		JButton button = buttons.get(0);
		button.setText("分割");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				preInput = String.valueOf(startEnd[0]) + "-" + String.valueOf(startEnd[1]) + "-";
				postInput = parent.getPartitionLine();
			}
		});

		button = buttons.get(1);
		button.setText("メソッド");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				preInput = String.valueOf(startEnd[0]) + "-" + String.valueOf(startEnd[1]) + "-";
				postInput = parent.getPartitionLine();
			}
		});

		button = buttons.get(2);
		button.setText("次へ");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				postInput = parent.getPartition();
			}
		});

		dialog[0] = new InputMyDialog(InputMyDialog.PARTITION);
		dialog[1] = new InputMyDialog(InputMyDialog.METHOD);
		dialog[2] = new InputMyDialog("誤っていた変数名を入力してください\n入力なしでスキップ", "誤っていた変数名の入力");
	}

	private void refreshLabel(int pattern) {
		noticeLabel.setText("着目しているまとまり：" + startEndString[0] + "から" + startEndString[1]);
		// @formatter:off
		questionLabel.setText("<html>" + startEndString[0] + "から" + startEndString[1]
				+ "までのまとまりをさらに2つのまとまりに分割しましょう。<br>" + "境目となる行を入力してください。<br>" + "メソッド呼び出しがある場合は「メソッド」を選択してください。<br>"
				+ "境目になるようなキリのいいところがなければ、「次へ」を選択してください。</html>");

		if(pattern == 1) {
			descriptLabel.setText("<html>" + startEndString[0] + "までのまとまりは正しく動いており、バグが無いことが確認できました。<br>"
					+ startEndString[0] + "から" + startEndString[1] + "までのまとまりに同様のことを繰り返し、バグの潜む範囲<br>を絞り込んでいきます。<br>"
					+ "ある程度、絞り込めたら、その範囲を細かく調べていきます。</html>");
		} else {
			descriptLabel.setText("<html>" + startEndString[0] + "から" + startEndString[1] + "までのまとまりは正しく動いていないため、"
					+ "バグは" + startEndString[0] + "から<br>" + startEndString[1] + "までのまとまりに潜んでいることがわかりました。<br>"
					+ startEndString[0] + "から" + startEndString[1] + "までのまとまりに同様のことを繰り返し、バグの潜む範囲を<br>絞り込んでいきます。<br>"
					+ "ある程度、絞り込めたら、その範囲を細かく調べていきます。</html>");
		}
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
				if(i <= 2) {
					startEnd[i] = tmp;
					if(i == 0) {
						startEndString[i] = (startEnd[0] + 1) + "行目";
					} else {
						startEndString[i] = notices[i] + "行目";
					}
				}
				if(i != 2 && i != 0) {
					// エディターにライン表示のための値を渡す
					// i=0のものは後で渡す
					partitionLineSet.add(tmp);
				}
			} catch (NumberFormatException e) {
				if(i <= 2) {
					startEnd[i] = -1;
					startEndString[i] = "エラー行目";
				}
			}
		}
		// 遷移元からの調整　1:正しいから遷移 2:誤りから遷移 3：メソッドの誤りから遷移
		if(startEnd[2] != 2) {
			startEndString[0] = startEnd[0] + "行目";
			startEnd[0]--;
			partitionLineSet.add(startEnd[0]);
		} else {
			partitionLineSet.add(startEnd[0]);
		}
		// 値の確認 問題なければエディターに必要な値を渡す
		if(!(startEnd[0] <= startEnd[1])) {
			startEnd[0] = startEnd[1] = -1;
			startEndString[0] = startEndString[1] = "エラー行目";
		} else {
			parent.setPartition(startEnd[0], startEnd[1]);
			parent.setPartitionLine(partitionLineSet);
		}
		noticeLabel.setText("着目しているまとまり：" + startEndString[0] + "から" + startEndString[1]);
		refreshLabel(startEnd[2]);
	}

	@Override
	public void updateData(int noticeLine, int returnLine, int[] partition, Set<Integer> partitionLines) {
		startEnd[0] = partition[0];
		startEndString[0] = (startEnd[0] + 1) + "行目";
		startEnd[1] = partition[1];
		startEndString[1] = startEnd[1] + "行目";
		refreshLabel(startEnd[2]);
	}
}
