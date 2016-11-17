/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import javax.swing.JButton;
import org.jeditor.navi.NaviManager;

public class Navi_i1 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_i1(NaviManager mgr) {
		super(mgr, "i1", 2);

		indexLabel.setText("入力の確認");
		//@formatter:off
		questionLabel.setText("<html>このプログラムには入力はありますか。</html>");
			
		descriptLabel.setText("<html>入力の有無によって、再現性の確認のために行うべきことが異なります。<br>"
				+ "そのため、入力の有無の確認から行います。</html>");
		//@formatter:on

		JButton button = buttons.get(0);
		button.setText("ある");
		button = buttons.get(1);
		button.setText("ない");
	}

	@Override
	public void setInput(String notice) {
		super.setInput(notice);
		noticeLabel.setText("利用なし");
	}
}
