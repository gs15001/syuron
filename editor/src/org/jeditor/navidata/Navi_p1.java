/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jeditor.navi.InputMyDialog;
import org.jeditor.navi.NaviManager;

public class Navi_p1 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;
	private int[] startEnd = new int[3];

	public Navi_p1(NaviManager mgr) {
		super(mgr, "p1", 2, true);

		indexLabel.setText("まとまり分割");
		//@formatter:off
		questionLabel.setText("<html>プログラムをサンプルのように2つのまとまりに分割しましょう。<br>"
				+ "境目となる行を入力してください。<br>"
				+ "メソッド呼び出しがある場合は、そこを境目とするので「メソッド」を<br>"
				+ "選択してください。<br>"
				+ "複数ある場合は、上の方ににあるメソッド呼び出しを対象にしてください。</html>");
		
		descriptLabel.setText("<html>プログラムを大雑把に調べるために、処理の役割毎にまとまりを作り、<br>"
				+ "プログラムをいくつかのまとまりに分割します。<br>"
				+ "まとまりに分割し、まとまり毎に動作を確認することで、バグの潜む範囲を大雑把に絞り込んでいきます。<br>"
				+ "メソッドは一連の処理をまとめたものであり、今回作るまとまりと同じ意味に<br>"
				+ "なります。そのため、メソッド呼び出しがある場合は、メソッド呼び出しを<br>"
				+ "1まとまりとし、その前後で分割します。</html>");
		//@formatter:on

		JButton button = buttons.get(0);
		button.setText("分割");
		button = buttons.get(1);
		button.setText("メソッド");
		MyButton button2 = (MyButton) button;
		button2.removeActionListener(button2.getActionListeners()[0]);
		button2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				input = "";
				int tmp = 0;
				if(dialog[button2.i] != null) {
					input = dialog[button2.i].showInputDialog(parent);
					tmp = Integer.parseInt(input);
				}
				parent.clreaAll();
				if(input != null || dialog[button2.i] == null) {
					selected = button2.getText();
					naviManager.changeNavi(getIndex(), button2.getText(), preInput + (tmp - 1) + "-" + tmp);
				}
			}
		});

		dialog[0] = new InputMyDialog(InputMyDialog.PARTITION);
		dialog[1] = new InputMyDialog(InputMyDialog.METHOD);

		setSamplePane(new p1sample(mgr));
	}

	@Override
	public void setInput(String notice) {
		super.setInput(notice);
		noticeLabel.setText("利用なし");
		Set<Integer> partitionLineSet = new HashSet<>();

		String[] notices = notice.split("-");

		for (int i = 0; i < notices.length; i++) {
			try {
				int tmp = Integer.parseInt(notices[i]);
				if(i == 0) {
					tmp--;
				}
				// エディターにライン表示のための値を渡す準備
				partitionLineSet.add(tmp);
				startEnd[i] = tmp;
			} catch (NumberFormatException e) {
				startEnd[i] = -1;
			}
		}
		if(startEnd[0] < startEnd[1]) {
			parent.setPartition(startEnd[0], startEnd[1]);
			parent.setPartitionLine(partitionLineSet);
			preInput = startEnd[0] + "-" + startEnd[1] + "-";
		} else {
			preInput = "0-999-";
		}
		noticeLabel.setText("着目しているまとまり：" + startEnd[0] + "行目から" + startEnd[1] + "行目");
	}
}

class p1sample extends AbstractSamplePane {

	private static final long serialVersionUID = 1L;

	public p1sample(NaviManager mgr) {
		super(mgr);
		JPanel pane = new JPanel();
		pane.setBackground(new Color(224, 224, 224));

		ClassLoader classLoader = this.getClass().getClassLoader();
		URL resUrl = classLoader.getResource("res/p1.png");
		JLabel label = new JLabel(new ImageIcon(resUrl));

		// JLabel label = new JLabel(new ImageIcon("./res/p1.png"));
		((FlowLayout) pane.getLayout()).setVgap(5);
		pane.add(label);
		addMainPane(pane);
	}
}