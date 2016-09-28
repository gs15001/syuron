/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import javax.swing.JButton;
import org.jeditor.navi.NaviManager;


public class Navi_r2 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_r2(NaviManager mgr) {
		super(mgr, "r2", 2);
		//@formatter:off
		indexLabel.setText("繰り返し回数の調査");
		questionLabel.setText("<html>この繰り返し文(while,for)の繰り返し回数は正しいですか。</html>");
		descriptLabel.setText("<html>一定の回数繰り返すタイプの繰り返し文では、その回数分繰り返しているかを確認します。</html>");
		//@formatter:on
		JButton button = buttons.get(0);
		button.setText("正しい");
		button = buttons.get(1);
		button.setText("誤り");
	}

	@Override
	public void setInput(String notice) {
		super.setInput(notice);
		noticeLabel.setText("着目している繰り返し文　：　" + notice + " 行目");
		input = notice;
	}
}
