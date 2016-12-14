/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import java.awt.Color;
import java.awt.FlowLayout;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jeditor.navi.NaviManager;

public class Navi_d4 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_d4(NaviManager mgr) {
		super(mgr, "d4", 2, true);

		indexLabel.setText("最後まで処理されているのか");
		//@formatter:off
		questionLabel.setText("<html>プログラムの最後にprint文を挿入し、そのprint文が出力されるか確認しましょう。</html>");
			
		descriptLabel.setText("<html>エラーは発生していないことが確認できたので、次にプログラムが最後まで<br>"
				+ "処理されているかを確認します。<br>"
				+ "print文を挿入し、そのprint文が出力されていることを確認することで、その<br>"
				+ "print文までは確実に処理されていることを確認できます。</html>");
		//@formatter:on

		JButton button = buttons.get(0);
		button.setText("表示される");
		button = buttons.get(1);
		button.setText("表示されない");

		setSamplePane(new d4sample(mgr));
	}

	@Override
	public void setInput(String notice) {
		super.setInput(notice);
		noticeLabel.setText("全体");
	}
}

class d4sample extends AbstractSamplePane {

	private static final long serialVersionUID = 1L;

	public d4sample(NaviManager mgr) {
		super(mgr);
		JPanel pane = new JPanel();
		pane.setBackground(new Color(224, 224, 224));

		ClassLoader classLoader = this.getClass().getClassLoader();
		URL resUrl = classLoader.getResource("res/d4.png");
		JLabel label = new JLabel(new ImageIcon(resUrl));

		// JLabel label = new JLabel(new ImageIcon("./res/d4.png"));
		((FlowLayout) pane.getLayout()).setVgap(50);
		pane.add(label);
		addMainPane(pane);
	}

}