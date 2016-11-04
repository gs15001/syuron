/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import javax.swing.JButton;
import org.jeditor.navi.NaviManager;

public class Navi_r1 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_r1(NaviManager mgr) {
		super(mgr, "r1", 2);
		//@formatter:off
		indexLabel.setText("繰り返し文の種類を調査");
		questionLabel.setText("<html>この繰り返し文(while,for)の繰り返し回数は分かりますか。</html>");
		descriptLabel.setText("<html>繰り返し文の種類を確認します。<br>"
				+ "繰り返し文には主に、<br>"
				+ "・一定の回数繰り返すタイプ<br>"
				+ "・一定の条件を満たすまで繰り返すタイプ<br>"
				+ "の2種類があります。種類に応じて、確認すべき部分が異なるため、まず種類を確認します。</html>");
		//@formatter:on
		JButton button = buttons.get(0);
		button.setText("はい");
		button = buttons.get(1);
		button.setText("いいえ");
	}

	@Override
	public void setInput(String notice) {
		super.setInput(notice);
		String[] notices = notice.split("-", 3);

		noticeLabel.setText("着目している繰り返し文　：　" + notices[2] + " 行目");
		inputTmp = notice;
	}

}
