package editor;

import java.io.*;
import javax.swing.*;

public class EditorFileAcesser {

	JFileChooser fc;

	public EditorFileAcesser() {
		fc = new JFileChooser("C:/Users/lab/Desktop");
	}

	public void fileOpen(JTextArea ta) {

		int selected = fc.showOpenDialog(null);
		if (selected == JFileChooser.CANCEL_OPTION
				|| selected == JFileChooser.ERROR_OPTION) {
			return;
		}
		
		File f = fc.getSelectedFile();
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String s;

			while ((s = br.readLine()) != null) {
				ta.append(s + '\n');
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return;
		}
		EditorStatus.FILENAME = f.getPath();
	}

	public void fileSave(JTextArea ta) {

		int selected = fc.showOpenDialog(null);
		if (selected == JFileChooser.CANCEL_OPTION) {
			return;
		} else if (selected == JFileChooser.ERROR_OPTION) {
			return;
		}
		File f = fc.getSelectedFile();

		try {
			PrintWriter pw = new PrintWriter(new FileWriter(f, false));

			String s = ta.getText();
			String st[] = s.split("\n");
			for (int i = 0; i < st.length; i++) {
				pw.println(st[i]);
			}

			pw.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return;
		}

		EditorStatus.FILENAME = f.getPath();
	}

	public void overWrite(JTextArea ta) {
		File f = new File(EditorStatus.FILENAME);

		try {
			PrintWriter pw = new PrintWriter(new FileWriter(f, false));

			String s = ta.getText();
			String st[] = s.split("\n");
			for (int i = 0; i < st.length; i++) {
				pw.println(st[i]);
			}

			pw.close();
		} catch (IOException e) {
			return;
		}
	}
}

class EditorStatus {
	static String FILENAME = "";
}
