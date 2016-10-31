/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import javax.swing.JButton;
import org.jeditor.navi.NaviManager;

public class Navi_p0 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_p0(NaviManager mgr) {
		super(mgr, "p0", 1);

		indexLabel.setText("ステップ3 まとまり分割による絞り込み");
		//@formatter:off
		questionLabel.setText("<html>ステップ2では、誤った結果を再現できるようにしました。<br>"
				+ "ステップ3では、バグを効率良く探せるようにプログラムを大雑把に調べ、バグの潜む範囲を絞り込んでいきます。<br>"
				+ "このステップは、小さなプログラムでは恩恵が少なく、無駄に思えますが、大きなプログラムになるほど重要になってきます。</html>");
		
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
