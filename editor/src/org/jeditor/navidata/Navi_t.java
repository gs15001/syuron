/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jeditor.navi.InputMyDialog;
import org.jeditor.navi.NaviManager;

public class Navi_t extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_t(NaviManager mgr) {
		super(mgr, "t", 4, true);

		indexLabel.setText("デバッグプロセスナビゲーション");
		//@formatter:off
		questionLabel.setText("<html>ここに表示されるナビゲーションに従ってデバッグを進めていきましょう。<br>"
				+ "下のボタンをクリックして、ナビゲーションに応答してください。<br>"
				+ "まずは、「ステップ1」をクリックしてください。<br><br>"
				+ "「サンプル」ボタンを押すと、具体例が表示されます。ナビゲーションが<br>"
				+ "よく分からない場合は見てみましょう。今回は、変数の値を確認するための<br>"
				+ "print文の例が表示されます。確認しておきましょう。</html>");
		descriptLabel.setText("<html>ここにはナビゲーションの解説が表示されます。<br>"
				+ "「なぜそのナビゲーションなのか」といった理由などが表示されます。<br><br>"
				+ "下のボタンの左横にあるチェックボックスは応答に自信が無い場合にチェックを付けます。<br>"
				+ "チェックをして置くことで、やり直しをする際に目安になります。</html>");
		//@formatter:on
		JButton button = buttons.get(0);
		button.setText("ステップ1");
		button = buttons.get(1);
		button.setText("ステップ2");
		button = buttons.get(2);
		button.setText("ステップ3");
		button = buttons.get(3);
		button.setText("ステップ4");

		dialog[3] = new InputMyDialog(InputMyDialog.VARIABLE);
		setSamplePane(new sample(mgr));
	}

	@Override
	public void setInput(String notice) {
		super.setInput(notice);
		noticeLabel.setText("着目する場所が表示されます");
	}
}

class sample extends AbstractSamplePane {

	private static final long serialVersionUID = 1L;

	public sample(NaviManager mgr) {
		super(mgr);
		JPanel pane = new JPanel();
		pane.setBackground(new Color(224, 224, 224));
		JLabel label = new JLabel(new ImageIcon("./res/t.png"));
		((FlowLayout) pane.getLayout()).setVgap(50);
		pane.add(label);
		addMainPane(pane);
	}

}
