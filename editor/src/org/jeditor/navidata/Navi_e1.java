/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import javax.swing.JButton;
import org.jeditor.navi.NaviManager;

public class Navi_e1 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_e1(NaviManager mgr) {
		super(mgr, "e1", 1);
		//@formatter:off
		indexLabel.setText("現在着目している部分に誤りがあります");
		questionLabel.setText("<html>見つけた誤りを修正しましょう。<br>"
				+ "修正の方法が分からなければ、友達やTAに聞いてみましょう。");
		descriptLabel.setText("");
		//@formatter:on
		
		JButton button = buttons.get(0);
		button.setText("終了");
	}

	@Override
	public void setInput(String notice) {
		super.setInput(notice);
		noticeLabel.setText("利用なし");
	}
}
