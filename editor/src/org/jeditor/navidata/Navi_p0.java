/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import java.awt.Color;
import java.awt.FlowLayout;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jeditor.navi.InputMyDialog;
import org.jeditor.navi.NaviManager;

public class Navi_p0 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;

	public Navi_p0(NaviManager mgr) {
		super(mgr, "p0", 1, true);

		indexLabel.setText("ステップ3 まとまり分割による絞り込み");
		//@formatter:off
		questionLabel.setText("<html>ステップ2では、再現性の確認をし、誤った結果を確実に出力できるように<br>しました。<br>"
				+ "ステップ3から、実際にバグを探していきます。まず、バグを効率良く探すために<br>"
				+ "<b>プログラムを大雑把に調べ、バグの潜む範囲を絞り込んでいきます。</b><br>"
				+ "このステップは、小さなプログラムでは恩恵が少なく、無駄に思えますが、<br>"
				+ "大きなプログラムになるほど重要になってきます。</html>");
		
		descriptLabel.setText("<html>「次へ」を押すと、着目する範囲を可視化するために<br>"
				+ "mainメソッドの開始行と終了行の入力を促されます。<br>"
				+ "あらかじめ確認しておきましょう。<br>"
				+ "サンプルに開始行と終了行の例があります。</html>");
		//@formatter:on

		JButton button = buttons.get(0);
		button.setText("次へ");

		dialog[0] = new InputMyDialog("mainメソッドが定義されている行番号を入力してください。\n「開始行-終了行」の形式で入力", "mainメソッドの定義されている行番号の入力", 6);

		setSamplePane(new p0sample(mgr));
	}

	@Override
	public void setInput(String notice) {
		super.setInput(notice);
		noticeLabel.setText("全体");
	}
}

class p0sample extends AbstractSamplePane {

	private static final long serialVersionUID = 1L;

	public p0sample(NaviManager mgr) {
		super(mgr);
		JPanel pane = new JPanel();
		pane.setBackground(new Color(224, 224, 224));

		ClassLoader classLoader = this.getClass().getClassLoader();
		URL resUrl = classLoader.getResource("res/p0.png");
		JLabel label = new JLabel(new ImageIcon(resUrl));

		// JLabel label = new JLabel(new ImageIcon("./res/p0.png"));
		((FlowLayout) pane.getLayout()).setVgap(5);
		pane.add(label);
		addMainPane(pane);
	}
}
