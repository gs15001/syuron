package org.jeditor.navi;

import java.awt.Component;
import javax.swing.JOptionPane;

public class InputRowDialog extends JOptionPane implements InputDialog {

	private static final long serialVersionUID = 1L;

	public InputRowDialog() {
		super();
	}

	@Override
	public String showInputDialog(Component parent) {
		return JOptionPane.showInputDialog(parent, "見つけた処理の行番号を入力してください\n入力なしでスキップ", "見つけた処理の行番号の入力",
				JOptionPane.QUESTION_MESSAGE);
	}

}
