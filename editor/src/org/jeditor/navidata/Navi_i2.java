/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import javax.swing.JButton;
import org.jeditor.navi.NaviManager;

public class Navi_i2 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_i2(NaviManager mgr) {
		super(mgr, "i2", 2);

		indexLabel.setText("入出力の関係の確認");
		//@formatter:off
		questionLabel.setText("<html</html>");
			
		descriptLabel.setText("<html></html>");
		//@formatter:on

		JButton button = buttons.get(0);
		button.setText("全て誤り");
		button = buttons.get(1);
		button.setText("一部正しい");
	}

	@Override
	public void setInput(String notice) {
		super.setInput(notice);
		noticeLabel.setText("利用なし");
	}
}
