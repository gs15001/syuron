/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JButton;
import org.jeditor.navi.InputMyDialog;
import org.jeditor.navi.NaviManager;

public class Navi_p4 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;
	private int[] startEnd = new int[3];
	private String[] startEndString = new String[3];

	public Navi_p4(NaviManager mgr) {
		super(mgr, "p4", 2);

		indexLabel.setText("メソッドの動作確認");

		questionLabel.setText("");
		descriptLabel.setText("");

		JButton button = buttons.get(0);
		button.setText("正しい");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				preInput = String.valueOf(startEnd[0] + 1) + "-" + String.valueOf(startEnd[1]) + "-1";
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

		dialog[1] = new InputMyDialog("メソッドが定義されている行番号を入力してください。\n「開始行-終了行」の形式で入力", "メソッドの定義されている行番号の入力");
	}

	private void refreshLabel() {
		//@formatter:off
		questionLabel.setText("<html>" + startEnd[0] + "行目のメソッドが正しく動作しているか確認しましょう。"
				+ "メソッド呼び出し後の" + (startEnd[0] + 1) + "行目にprint文を挿入して、必要な変数の値を確認しましょう。"
				+ "確認した値は正しいですか。</html>");
		
		descriptLabel.setText("<html>" + (startEnd[0] - 1) + "行目までのまとまりは正しく動いており、バグが無いことが確認できました。<br>"
				+ "続いて、" + startEnd[0] + "行目のメソッドにバグが無いか確認します。<br>"
				+ "確認するべき変数は、" + (startEnd[0] + 1) + "行目以降で使用している変数です。</html>");
		//@formatter:on

	}

	@Override
	public void setInput(String notice) {
		super.setInput(notice);
		Set<Integer> partitionLineSet = new HashSet<>();

		String[] notices = notice.split("-");
		for (int i = 0; i < notices.length; i++) {
			try {
				int tmp = Integer.parseInt(notices[i]);
				// エディターにライン表示のための値を渡す
				partitionLineSet.add(tmp);
				if(i < 2) {
					startEnd[i] = tmp;
					if(startEnd[i] == 0) {
						startEndString[i] = "最初";
					} else if(startEnd[i] == 999) {
						startEndString[i] = "最後";
					} else {
						startEndString[i] = notices[i] + "行目";
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
			parent.setPartition(startEnd[0] - 1, startEnd[0]);
			parent.setPartitionLine(partitionLineSet);
		}
		noticeLabel.setText("着目しているまとまり：" + startEndString[0] + "のメソッド");
		refreshLabel();
	}
}
