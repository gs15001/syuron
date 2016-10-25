/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import org.jeditor.navi.InputMyDialog;
import org.jeditor.navi.NaviManager;

public class Navi_p3 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;
	private int[] startEnd = new int[3];
	private String[] startEndString = new String[3];

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
				inputTmp = String.valueOf(startEnd[0]) + "-" + String.valueOf(startEnd[1]) + "-";
			}
		});

		button = buttons.get(1);
		button.setText("メソッド");
		button = buttons.get(2);
		button.setText("次へ");

		dialog[0] = new InputMyDialog(InputMyDialog.PARTITION);
		dialog[1] = new InputMyDialog(InputMyDialog.METHOD);
		dialog[2] = new InputMyDialog("誤っていた変数名を入力してください\n入力なしでスキップ", "誤っていた変数名の入力");
	}

	private void refreshLabel(int pattern) {
		//@formatter:off
		questionLabel.setText("<html>" + startEndString[0] + "から" + startEndString[1] + "までのまとまりをさらに2つのまとまりに分割しましょう。<br>"
				+ "境目となる行を入力してください。<br>"
				+ "メソッド呼び出しがある場合は「メソッド」を選択してください。<br>"
				+ "境目になるようなキリのいいところがなければ、「次へ」を選択してください。</html>");
	
		if(pattern == 1) {
			descriptLabel.setText("<html>" + startEndString[0] + "までのまとまりは正しく動いており、バグが無いことが確認できました。<br>"
					+ startEndString[0] + "から" + startEndString[1] + "までのまとまりに同様のことを繰り返し、バグの潜む範囲を絞り込んでいきます。"
					+ "ある程度、絞り込めたら、その範囲を細かく調べていきます。</html>");
		}else{
			descriptLabel.setText("<html>" + startEndString[0] + "から" + startEndString[1] + "までのまとまりは正しく動いていないため、"
					+ "バグは" + startEndString[0] + "から" + startEndString[1] + "までのまとまりに潜んでいることがわかりました。<br>"
					+ startEndString[0] + "から" + startEndString[1] + "までのまとまりに同様のことを繰り返し、バグの潜む範囲を絞り込んでいきます。"
					+ "ある程度、絞り込めたら、その範囲を細かく調べていきます。</html>");
		}
		//@formatter:on
	}

	@Override
	public void setInput(String notice) {
		super.setInput(notice);

		String[] notices = notice.split("-", 3);
		for (int i = 0; i < notices.length; i++) {
			try {
				startEnd[i] = Integer.parseInt(notices[i]);
				if(startEnd[i] == 0) {
					startEndString[i] = "最初";
				} else if(startEnd[i] == 999) {
					startEndString[i] = "最後";
				} else {
					startEndString[i] = notices[i] + "行目";
				}
			} catch (NumberFormatException e) {
				startEnd[i] = -1;
				startEndString[i] = "エラー行目";
			}
		}
		if(!(startEnd[0] <= startEnd[1])) {
			startEnd[0] = startEnd[1] = -1;
			startEndString[0] = startEndString[1] = "エラー行目";
		}
		noticeLabel.setText("着目しているまとまり：" + startEndString[0] + "から" + startEndString[1]);
		refreshLabel(startEnd[2]);

	}
}
