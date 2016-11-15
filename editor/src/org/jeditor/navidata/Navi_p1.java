/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import java.util.HashSet;
import java.util.Set;
import javax.swing.JButton;
import org.jeditor.navi.InputMyDialog;
import org.jeditor.navi.NaviManager;

public class Navi_p1 extends AbstractNaviPane {

	private static final long serialVersionUID = 1L;
	private int[] startEnd = new int[3];

	public Navi_p1(NaviManager mgr) {
		super(mgr, "p1", 2);

		indexLabel.setText("まとまり分割");
		//@formatter:off
		questionLabel.setText("<html>プログラムをサンプルのように2つのまとまりに分割しましょう。<br>"
				+ "境目となる行を入力してください。<br>"
				+ "メソッド呼び出しがある場合は、そこを境目とするので「メソッド」を選択してください。</html>");
		
		descriptLabel.setText("<html>プログラムを大雑把に調べるために、プログラムをいくつかのまとまりに分割します。<br>"
				+ "まとまりに分割し、まとまり毎に動作を確認することで、バグの潜む範囲を大雑把に絞り込んでいきます。</html>");
		//@formatter:on

		JButton button = buttons.get(0);
		button.setText("分割");
		button = buttons.get(1);
		button.setText("メソッド");

		dialog[0] = new InputMyDialog(InputMyDialog.PARTITION);
		dialog[1] = new InputMyDialog(InputMyDialog.METHOD);
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
	}
}
