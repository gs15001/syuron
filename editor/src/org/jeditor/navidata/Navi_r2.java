/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import org.jeditor.navi.NaviManager;

public class Navi_r2 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	private String called;
	private String calledLine;

	public Navi_r2(NaviManager mgr) {
		super(mgr, "r2", 2);
		//@formatter:off
		indexLabel.setText("繰り返し回数の調査");
		questionLabel.setText("<html>この繰り返し文(while,for)の繰り返し回数は正しいですか。<br>"
				+ "繰り返し文のブロック({})の中にprint文を挿入し、繰り返し回数を数えてみましょう。</html>");
		descriptLabel.setText("<html>一定の回数繰り返すタイプの繰り返し文では、その回数分繰り返しているかを確認します。</html>");
		//@formatter:on
		JButton button = buttons.get(0);
		button.setText("正しい");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				inputTmp = calledLine;
			}
		});

		button = buttons.get(1);
		button.setText("誤り");
	}
	
	public String getCalled() {
		return called;
	}

	@Override
	public void setInput(String notice) {
		super.setInput(notice);
		String[] notices = notice.split("-", 3);
		called = notices[0];
		calledLine = notices[1];

		noticeLabel.setText("着目している繰り返し文　：　" + notices[2] + " 行目");
		inputTmp = notices[2];
	}
}
