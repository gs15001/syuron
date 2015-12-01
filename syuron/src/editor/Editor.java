package editor;

import java.awt.*;
import javax.swing.*;

public class Editor {
		public static void main(String args[]){
			JTextArea ta = new JTextArea();
			ta.setFont(new Font("Dialog",Font.PLAIN,12));
			ta.setTabSize(4);
			EditorMenu em = new EditorMenu(ta);
			EditorControler ef = new EditorControler(ta,em);
		}
}
