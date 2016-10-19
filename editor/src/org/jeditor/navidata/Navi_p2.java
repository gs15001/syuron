/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import org.jeditor.navi.InputMyDialog;
import org.jeditor.navi.NaviManager;

public class Navi_p2 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;
	int[] startEnd = new int[3];

	public Navi_p2(NaviManager mgr) {
		super(mgr, "p2", 2);

		indexLabel.setText("まとまりの動作確認");

		questionLabel.setText("");
		descriptLabel.setText("");

		JButton button = buttons.get(0);
		button.setText("正しい");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				input = String.valueOf(startEnd[2]) + "-" + String.valueOf(startEnd[1]);
			}
		});

		button = buttons.get(1);
		button.setText("誤り");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				input = String.valueOf(startEnd[0]) + "-" + String.valueOf(startEnd[2]);
			}
		});

		// 変数名を入力させるか
		// dialog[1] = new InputMyDialog(InputMyDialog.VARIABLE);
	}

	private void refreshLabel() {
		//@formatter:off
		questionLabel.setText("<html>" + startEnd[0] + "行目から" + startEnd[2] + "行目までのまとまりが正しく動作しているか確認しましょう。<br>"
				+ "境目である" + startEnd[2] + "行目にprint文を挿入して、必要な変数の値を確認しましょう。<br>"
				+ "確認した値は正しいですか。</html>");
		
		descriptLabel.setText("<html>プログラムをまとまりに分割できたなら、次はまとまりごとに正しく動作しているか確認します。<br>"
				+ "正しく動作しているかは、変数の値を確認することで確かめることができます。<br>"
				+ "確認するべき変数は、" + startEnd[2] + "行目以降のまとまりで使用している変数です。</html>");
		//@formatter:on

	}

	@Override
	public void setInput(String notice) {
		super.setInput(notice);
		System.out.println(notice);

		String[] notices = notice.split("-", 3);
		for (int i = 0; i < notices.length; i++) {
			try {
				startEnd[i] = Integer.parseInt(notices[i]);
			} catch (NumberFormatException e) {
				startEnd[i] = -1;
			}
		}
		noticeLabel.setText("着目しているまとまり：" + startEnd[0] + "行目から" + startEnd[2] + "行目");
		refreshLabel();
	}
}
