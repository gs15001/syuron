/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import org.jeditor.navi.InputMyDialog;
import org.jeditor.navi.NaviManager;

public class Navi_p2_2 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;
	private int[] startEnd = new int[3];
	private String[] startEndString = new String[3];

	public Navi_p2_2(NaviManager mgr) {
		super(mgr, "p2_2", 2);

		indexLabel.setText("まとまりの動作確認");

		questionLabel.setText("");
		descriptLabel.setText("");

		JButton button = buttons.get(0);
		button.setText("正しい");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				inputTmp = String.valueOf(startEnd[2] + 1) + "-" + String.valueOf(startEnd[1]);
			}
		});

		button = buttons.get(1);
		button.setText("誤り");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				inputTmp = String.valueOf(startEnd[0]) + "-" + String.valueOf(startEnd[2] + "-2");
			}
		});

		// 変数名を入力させるか
		// dialog[1] = new InputMyDialog(InputMyDialog.VARIABLE);
	}

	private void refreshLabel() {
		//@formatter:off
		questionLabel.setText("<html>" + startEndString[0] + "から" + startEndString[2] + "までのまとまりが正しく動作しているか確認しましょう。<br>"
				+ "境目である" + startEndString[2] + "にprint文を挿入して、必要な変数の値を確認しましょう。<br>"
				+ "確認した値は正しいですか。</html>");
		
		descriptLabel.setText("<html>プログラムをまとまりに分割したので、次はまとまりごとに正しく動作しているか確認します。<br>"
				+ "正しく動作しているかは、変数の値を確認することで確かめることができます。<br>"
				+ "確認するべき変数は、" + startEndString[2] + "以降、またはメソッドで使用している変数です。</html>");
		//@formatter:on

	}

	@Override
	public void setInput(String notice) {
		super.setInput(notice);

		String[] notices = notice.split("-", 3);
		for (int i = 0; i < notices.length; i++) {
			try {
				startEnd[i] = Integer.parseInt(notices[i]);
				if(i == 2) {
					// メソッドの行数を指定しているので-1
					startEnd[i]--;
					// エディターにライン表示のための値を渡す
					parent.setPartitionLine(startEnd[i]);
					parent.setPartitionLine(startEnd[i] + 1);
				}
				if(startEnd[i] == 0) {
					startEndString[i] = "最初";
				} else if(startEnd[i] == 999) {
					startEndString[i] = "最後";
				} else {
					startEndString[i] = startEnd[i] + "行目";
				}
			} catch (NumberFormatException e) {
				startEnd[i] = -1;
				startEndString[i] = "エラー行目";
			}
		}
		if(!(startEnd[0] <= startEnd[2] && startEnd[2] <= startEnd[1])) {
			startEnd[0] = startEnd[1] = startEnd[2] = -1;
			startEndString[0] = startEndString[1] = startEndString[2] = "エラー行目";
		} else {
			parent.setPartition(startEnd[0], startEnd[2]);
		}
		noticeLabel.setText("着目しているまとまり：" + startEndString[0] + "から" + startEndString[2]);
		refreshLabel();
	}
}
