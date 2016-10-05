/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import javax.swing.JButton;
import org.jeditor.navi.NaviManager;

public class Navi_d4 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_d4(NaviManager mgr) {
		super(mgr, "d4", 2);

		indexLabel.setText("最後まで処理されているのか");
		//@formatter:off
		questionLabel.setText("<html</html>");
			
		descriptLabel.setText("<html></html>");
		//@formatter:on

		JButton button = buttons.get(0);
		button.setText("表示される");
		button = buttons.get(1);
		button.setText("表示されない");
	}

	@Override
	public void setInput(String notice) {
		super.setInput(notice);
		noticeLabel.setText("利用なし");
	}
}
