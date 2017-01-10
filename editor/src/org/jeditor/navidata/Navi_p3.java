/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JButton;
import org.jeditor.navi.InputMyDialog;
import org.jeditor.navi.NaviManager;
import org.jeditor.navidata.AbstractNaviPane.MyButton;

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
		MyButton button2 = (MyButton) button;
		button2.removeActionListener(button2.getActionListeners()[0]);
		button2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				preInput = String.valueOf(startEnd[0]) + "-" + String.valueOf(startEnd[1]) + "-";
				postInput = parent.getPartitionLine();
				input = "";
				int tmp = 0;
				if(dialog[button2.i] != null) {
					input = dialog[button2.i].showInputDialog(parent);
				}
				if(input != null || dialog[button2.i] == null) {
					tmp = Integer.parseInt(input);
					parent.clreaAll();
					selected = button2.getText();
					naviManager.changeNavi(getIndex(), button2.getText(), preInput + (tmp - 1) + "-" + tmp + postInput);
				}
			}
		});

		button = buttons.get(2);
		button.setText("次へ");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				preInput = "";
				postInput = parent.getPartition();
			}
		});

		dialog[0] = new InputMyDialog(InputMyDialog.PARTITION);
		dialog[1] = new InputMyDialog(InputMyDialog.METHOD);
		dialog[2] = new InputMyDialog("誤っていた変数名を入力してください\n配列の場合は「[]」は入力しないでください", "誤っていた変数名の入力", -1);
	}

	private void refreshLabel(int pattern) {
		noticeLabel.setText("着目しているまとまり：" + startEndString[0] + "から" + startEndString[1]);
		// @formatter:off
		questionLabel.setText("<html>" + startEndString[0] + "から" + startEndString[1]
				+ "までのまとまりをさらに2つのまとまりに分割しましょう。<br>" + "境目となる行を入力してください。<br>"
				+ "メソッド呼び出しがある場合は「メソッド」を選択してください。<br>"
				+ "まとまりが10行程度になったり、もう分割できなくなったら、<br>"
				+ "「次へ」を選択してください。<br>"
				+ "「次へ」を選択した場合、最後に確認した誤っている変数名の入力を促されます。</html>");

		if(pattern == 1) {
			descriptLabel.setText("<html>" + (startEnd[0]) + "行目までのまとまりは正しく動いており、バグが無いことが確認できました。<br>"
					+ "そのため、バグは" + startEndString[0] + "から" + startEndString[1] + "までのまとまりに潜んでいることが分かります。<br>"
					+ startEndString[0] + "から" + startEndString[1] + "までのまとまりに同様のことを繰り返し、バグの潜む範囲<br>を絞り込んでいきます。<br>"
					+ "分割できないくらいまで絞り込めたら、その範囲を順に調べていきます。</html>");
		} else if(pattern == 2) {
			descriptLabel.setText("<html>" + startEndString[0] + "から" + startEndString[1] + "までのまとまりが正しく動いていないため、"
					+ "バグは" + startEndString[0] + "から<br>" + startEndString[1] + "までのまとまりに潜んでいることが分かりました。<br>"
					+ startEndString[0] + "から" + startEndString[1] + "までのまとまりに同様のことを繰り返し、バグの潜む範囲を<br>絞り込んでいきます。<br>"
					+ "ある程度まで絞り込めたら、その範囲を順に調べていきます。</html>");
		}else{
			questionLabel.setText("<html>" + startEndString[0] + "から" + startEndString[1]
					+ "までのまとまりをさらに2つのまとまりに分割しましょう。<br>" + "境目となる行を入力してください。<br>"
					+ "メソッド呼び出しがある場合は「メソッド」を選択してください。<br>"
					+ "もう分割できないと判断したなら、「次へ」を選択してください。<br>"
					+ "「次へ」を選択した場合、最後に確認した誤っている変数名の入力を促されます。<br>"
					+ "メソッドの返り値が誤っていたので、returnしている変数名を入力しましょう。</html>");
			descriptLabel.setText("<html>" + startEndString[0] + "から" + startEndString[1] + "までのまとまりのメソッドが正しく動いていないため、"
					+ "バグは" + startEndString[0] + "から<br>" + startEndString[1] + "までのまとまりに潜んでいることが分かりました。<br>"
					+ startEndString[0] + "から" + startEndString[1] + "までのまとまりに同様のことを繰り返し、バグの潜む範囲を<br>絞り込んでいきます。<br>"
					+ "ある程度まで絞り込めたら、その範囲を順に調べていきます。</html>");
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
	public void updateData(List<Integer> noticeLine, String returnLine, int[] partition, Set<Integer> partitionLines) {
		startEnd[0] = partition[0];
		startEndString[0] = (startEnd[0] + 1) + "行目";
		startEnd[1] = partition[1];
		startEndString[1] = startEnd[1] + "行目";
		refreshLabel(startEnd[2]);
	}
}
