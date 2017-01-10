package org.jeditor.navi;

import java.awt.Component;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

public class InputMyDialog extends JOptionPane {

	private static final long serialVersionUID = 1L;

	private String text;
	private String title;
	private int pattern = -1;

	public static final int VARIABLE = 0;
	public static final int ROW = 1;
	public static final int PARTITION = 2;
	public static final int METHOD = 3;
	public static final int IF = 4;
	public static final int FOR = 5;

	public InputMyDialog(String text, String title, int pattern) {
		super();
		this.text = text;
		this.title = title;
		this.pattern = pattern;
	}

	public InputMyDialog(int pattern) {
		this.pattern = pattern;
		switch (pattern) {
			case VARIABLE:
				text = "誤っている変数名を入力してください\n" + "配列の場合は「[]」は入力しないでください";
				title = "誤っている変数名の入力";
				break;
			case ROW:
				text = "代入している処理の行番号を入力してください";
				title = "代入している処理の行番号の入力";
				break;
			case PARTITION:
				text = "まとまりの境目となる行番号を入力してください\n入力行と入力行+1行目が境目になります";
				title = "まとまりの境目の行番号の入力";
				break;
			case METHOD:
				text = "メソッドを呼び出している行番号を入力してください";
				title = "メソッドを呼び出している行番号の入力";
				break;
			case IF:
				text = "調査する条件文の行番号を入力してください";
				title = "調査する条件文の行番号の入力";
				break;
			case FOR:
				text = "調査する繰り返し文の行番号を入力してください";
				title = "調査する繰り返し文の行番号の入力";
				break;
			default:
				break;
		}
	}

	public String showInputDialog(Component parent) {
		// return JOptionPane.showInputDialog(parent, text, title, JOptionPane.QUESTION_MESSAGE);
		String input = "";
		do {
			input = JOptionPane.showInputDialog(parent, text, title, JOptionPane.QUESTION_MESSAGE);
			if(input == null) {
				return null;
			}
		} while (!judgedError(input));
		return input;
	}

	public boolean judgedError(String input) {
		// 入力なしの判定
		if(input.equals("")) {
			return false;
		}
		// ハイフン付き
		if(pattern == 6) {
			Pattern p = Pattern.compile("^[0-9]*\\-[0-9]*$");
			Matcher m = p.matcher(input);
			if(m.matches()) {
				return true;
			} else {
				JOptionPane.showMessageDialog(this, "「開始行-終了行」の形式で入力してください", "エラー", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		// 数字を入力しているかの判定
		if(pattern > 0) {
			Pattern p = Pattern.compile("^[0-9]*$");
			Matcher m = p.matcher(input);
			if(m.matches()) {
				return true;
			} else {
				JOptionPane.showMessageDialog(this, "数値で入力してください", "エラー", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		return true;
	}
}
