package org.jeditor.navi;

import java.awt.Component;
import javax.swing.JOptionPane;

public class InputCtrlRowDialog extends JOptionPane implements InputDialog {

	private static final long serialVersionUID = 1L;

	public InputCtrlRowDialog() {
		super();
	}

	@Override
	public String showInputDialog(Component parent) {
		return JOptionPane.showInputDialog(parent, "調査する条件文・繰り返し文の行番号を入力してください\n入力なしでスキップ", "調査する条件文・繰り返し文の行番号の入力",
				JOptionPane.QUESTION_MESSAGE);
	}

}
