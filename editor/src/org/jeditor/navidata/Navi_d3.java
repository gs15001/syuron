/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jeditor.navi.NaviManager;

public class Navi_d3 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_d3(NaviManager mgr) {
		super(mgr, "d3", 2, true);

		indexLabel.setText("実行中のエラーの確認");
		//@formatter:off
		questionLabel.setText("<html>実行中にエラーが発生していないか確認しましょう。</html>");
		
		descriptLabel.setText("<html>プログラムの実行中にもエラーが発生することがあり、<br>"
				+ "このエラーをランタイムエラーと言います。<br>"
				+ "コンパイルエラーと同様に、修正する必要があります。</html>");
		//@formatter:on

		JButton button = buttons.get(0);
		button.setText("発生していない");
		button = buttons.get(1);
		button.setText("発生している");

		setSamplePane(new d3sample(mgr));
	}

	@Override
	public void setInput(String notice) {
		super.setInput(notice);
		noticeLabel.setText("利用なし");
	}
}

class d3sample extends AbstractSamplePane {

	private static final long serialVersionUID = 1L;

	public d3sample(NaviManager mgr) {
		super(mgr);
		JPanel pane = new JPanel();
		pane.setBackground(new Color(224, 224, 224));
		JLabel label = new JLabel(new ImageIcon("./res/d3.png"));
		((FlowLayout) pane.getLayout()).setVgap(50);;
		pane.add(label);
		addMainPane(pane);
	}

}