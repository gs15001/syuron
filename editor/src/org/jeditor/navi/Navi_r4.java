/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navi;

import javax.swing.JButton;

public class Navi_r4 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_r4(NaviManager mgr) {
		super(mgr, "r4", 2);
		//@formatter:off
		indexLabel.setText("条件に使用する変数の値の調査");
		questionLabel.setText("<html>この繰り返し文(while,for)の条件に使用する変数の値を確認しましょう。<br>"
				+ "確認した値は正しいですか。</html>");
		descriptLabel.setText("<html>一定の条件を満たすまで繰り返すタイプの繰り返し文では条件の部分に誤りがないかを確認します。<br>"
				+ "条件の部分が誤る原因は、<br>"
				+ "・使用する変数が誤っている<br>"
				+ "・条件式が誤っている<br>"
				+ "のどちらかになります。<br>"
				+ "まず、前者の確認を行います。</html>");
		//@formatter:on
		JButton button = buttons.get(0);
		button.setText("正しい");
		button = buttons.get(1);
		button.setText("誤り");
		
		dialog[1] = new InputVarDialog();
	}

	@Override
	public void setInput(String notice) {
		super.setInput(notice);
		noticeLabel.setText("着目している繰り返し文　：　" + notice + " 行目");
		input = notice;
	}

}
