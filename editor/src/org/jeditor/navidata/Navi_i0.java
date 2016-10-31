/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import javax.swing.JButton;
import org.jeditor.navi.NaviManager;

public class Navi_i0 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_i0(NaviManager mgr) {
		super(mgr, "i0", 1);

		indexLabel.setText("ステップ2 再現性の確認");
		//@formatter:off
		questionLabel.setText("<html>ステップ1では、バグが存在しているかどうかを確認しました。<br>"
				+ "ステップ2では、どんな時に誤った結果になるかを確認し、誤った結果を再現できるようにします。<br>"
				+ "プログラムによっては、ある条件の時だけ誤った結果になることがあるため、その条件を調べていきます。</html>");
			
		descriptLabel.setText("<html></html>");
		//@formatter:on

		JButton button = buttons.get(0);
		button.setText("次へ");
	}

	@Override
	public void setInput(String notice) {
		super.setInput(notice);
		noticeLabel.setText("利用なし");
	}
}
