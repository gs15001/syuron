/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import org.jeditor.navi.InputMyDialog;
import org.jeditor.navi.NaviManager;

public class Navi_p3 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;
	int[] startEnd = new int[3];

	public Navi_p3(NaviManager mgr) {
		super(mgr, "p3", 2);

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
		button.setText("次へ");

		dialog[0] = new InputMyDialog(InputMyDialog.PARTITION);
		dialog[1] = new InputMyDialog("誤っていた変数名を入力してください\n入力なしでスキップ", "誤っていた変数名の入力");
	}

	private void refreshLabel() {
		//@formatter:off
		questionLabel.setText("<html>" + startEnd[0] + "行目から" + startEnd[1] + "行目までのまとまりをさらに2つのまとまりに分割しましょう。<br>"
				+ "境目となる行を入力してください。<br>"
				+ "境目になるようなキリのいいところがなければ、「次へ」を選択してください。</html>");
	
		descriptLabel.setText("<html>確認の結果、バグの潜む範囲は" + startEnd[0] + "行目から" 
				+ startEnd[1] + "行目のまとまり内まで絞り込むことができました。<br>"
				+ "同様のことを繰り返し、バグの潜む範囲を絞り込んでいきます。ある程度、絞り込めたら、その範囲を細かく調べていきます。</html>");
		//@formatter:on
	}

	@Override
	public void setInput(String notice) {
		super.setInput(notice);

		String[] notices = notice.split("-", 3);
		for (int i = 0; i < notices.length; i++) {
			try {
				startEnd[i] = Integer.parseInt(notices[i]);
			} catch (NumberFormatException e) {
				startEnd[i] = -1;
			}
		}
		noticeLabel.setText("着目しているまとまり：" + startEnd[0] + "行目から" + startEnd[1] + "行目");
		refreshLabel();
	}
}
