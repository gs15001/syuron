/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import javax.swing.JButton;
import org.jeditor.navi.NaviManager;

public class Navi_d6 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_d6(NaviManager mgr) {
		super(mgr, "d6", 1);

		indexLabel.setText("バグはありません");
		//@formatter:off
		questionLabel.setText("<html</html>");
					
		descriptLabel.setText("<html></html>");
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
