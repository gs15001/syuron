package org.jeditor.navi;

import java.awt.Component;
import javax.swing.JOptionPane;

public class InputMyDialog extends JOptionPane {

	private static final long serialVersionUID = 1L;

	String text;
	String title;

	public static final int VARIABLE = 0;
	public static final int ROW = 1;
	public static final int PARTITION = 2;
	public static final int METHOD = 3;
	public static final int IF = 4;
	public static final int FOR = 5;

	public InputMyDialog(String text, String title) {
		super();
		this.text = text;
		this.title = title;
	}

	public InputMyDialog(int pattern) {
		switch (pattern) {
			case VARIABLE:
				text = "誤っている変数名を入力してください\n入力なしでスキップ";
				title = "誤っている変数名の入力";
				break;
			case ROW:
				text = "見つけた処理の行番号を入力してください\n入力なしでスキップ";
				title = "見つけた処理の行番号の入力";
				break;
			case PARTITION:
				text = "まとまりの境目となる行番号を入力してください\n入力行と入力行+1行目が境目になります\n入力なしでスキップ";
				title = "まとまりの境目の行番号の入力";
				break;
			case METHOD:
				text = "メソッドを呼び出している行番号を入力してください\n入力なしでスキップ";
				title = "メソッドを呼び出している行番号の入力";
				break;
			case IF:
				text = "調査する条件文の行番号を入力してください\n入力なしでスキップ";
				title = "調査する条件文の行番号の入力";
				break;
			case FOR:
				text = "調査する繰り返し文の行番号を入力してください\n入力なしでスキップ";
				title = "調査する繰り返し文の行番号の入力";
				break;
			default:
				break;
		}
	}

	public String showInputDialog(Component parent) {
		return JOptionPane.showInputDialog(parent, text, title, JOptionPane.QUESTION_MESSAGE);
	}

}
