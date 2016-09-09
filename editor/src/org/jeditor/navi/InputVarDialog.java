package org.jeditor.navi;

import java.awt.Component;
import javax.swing.JOptionPane;

public class InputVarDialog extends JOptionPane implements InputDialog {

	private static final long serialVersionUID = 1L;

	public InputVarDialog() {
		super();
	}

	@Override
	public String showInputDialog(Component parent) {
		return JOptionPane.showInputDialog(parent, "誤っている変数名を入力してください\n入力なしでスキップ", "誤っている変数名の入力",
				JOptionPane.QUESTION_MESSAGE);
	}

}
