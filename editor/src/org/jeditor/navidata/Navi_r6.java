/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import javax.swing.JButton;
import org.jeditor.navi.NaviManager;

public class Navi_r6 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	private String called;

	public Navi_r6(NaviManager mgr) {
		super(mgr, "r6", 2);
		//@formatter:off
		indexLabel.setText("条件の条件式の調査");
		questionLabel.setText("<html>この繰り返し文(while,for)の条件式を確認しましょう。<br>"
				+ "複雑な計算を行っている場合は、分解して1つ1つ確認しましょう。<br>"
				+ "確認した結果、条件式は正しいですか。</html>");
		descriptLabel.setText("<html>条件の部分が誤る原因のうち、「使用する変数が誤っている」は先ほど正しいことを確認しました。<br>"
				+ "残る「条件式が誤っている」の確認を行います。</html>");
		//@formatter:on
		JButton button = buttons.get(0);
		button.setText("正しい");
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

		noticeLabel.setText("着目している繰り返し文　：　" + notices[2] + " 行目");
		parent.setNoticeLine(notices[2]);
		inputTmp = notices[1];
	}
}
