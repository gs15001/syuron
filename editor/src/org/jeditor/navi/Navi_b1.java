/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navi;

import javax.swing.JButton;

public class Navi_b1 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_b1(NaviManager mgr) {
		super(mgr, "b1", 2);
		//@formatter:off
		indexLabel.setText("分岐先の調査");
		questionLabel.setText("<html>この条件文(if)によってどの分岐先が実行されているか確認しましょう。<br>"
				+ "確認した分岐先は正しいですか。</html>");
		descriptLabel.setText("<html>条件文に誤りがないかを確認します。<br>"
				+ "条件文に誤りがある場合、正解とは異なる分岐先を実行している場合が多いです。<br>"
				+ "そのため、まずどの分岐先が実行されているか確認します。</html>");
		//@formatter:on
		JButton button = buttons.get(0);
		button.setText("正しい");
		button = buttons.get(1);
		button.setText("誤り");
	}

}
