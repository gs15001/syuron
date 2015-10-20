package editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.*;

public class EditorMenu extends JMenuBar {

	private static final long serialVersionUID = 1L;

	public EditorMenu(JTextArea ta) {
		super();

		FileMenu fm = new FileMenu(ta);
		EditMenu em = new EditMenu(ta);
		ViewMenu vm = new ViewMenu(ta);
		HelpMenu hm = new HelpMenu(ta);

		add(fm);
		add(em);
		add(vm);
		add(hm);
	}
}

abstract class AbstMenu extends JMenu implements ActionListener {

	private static final long serialVersionUID = 1L;
	protected JTextArea ta;
	protected List<JMenuItem> menuItem = new ArrayList<JMenuItem>();

	public AbstMenu(JTextArea ta, String name) {
		super(name);
		this.ta = ta;
	}
}

class FileMenu extends AbstMenu {

	private static final long serialVersionUID = 1L;

	public FileMenu(JTextArea ta) {
		super(ta, "ファイル");

		menuItem.add(new JMenuItem("新規作成"));
		menuItem.add(new JMenuItem("開く"));
		menuItem.add(new JMenuItem("上書き保存"));
		menuItem.add(new JMenuItem("名前をつけて保存"));
		menuItem.add(new JMenuItem("閉じる"));

		for (JMenuItem mi : menuItem) {
			add(mi);
			mi.addActionListener(this);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		EditorFileAcesser fa = new EditorFileAcesser();
		String command = e.getActionCommand();

		if (command == menuItem.get(0).getText()) {
			ta.setText("");
			EditorStatus.FILENAME = "";
		} else if (command == menuItem.get(1).getText()) {
			// テキストエリアを空欄にしてファイル読み込み
			ta.setText("");
			fa.fileOpen(ta);
		} else if (command == menuItem.get(2).getText()) {
			// ファイルを開いているなら上書き、それ以外は名前をつけて保存
			if (EditorStatus.FILENAME == "") {
				fa.fileSave(ta);
			} else {
				fa.overWrite(ta);
			}
		} else if (command == menuItem.get(3).getText()) {
			// 名前をつけて保存
			fa.fileSave(ta);
		} else if (command == menuItem.get(4).getText()) {
			System.exit(0);
		}
	}

}

class EditMenu extends AbstMenu {

	private static final long serialVersionUID = 1L;

	public EditMenu(JTextArea ta) {
		super(ta, "編集");

		menuItem.add(new JMenuItem("切り取り"));
		menuItem.add(new JMenuItem("コピー"));
		menuItem.add(new JMenuItem("貼り付け"));
		menuItem.add(new JMenuItem("すべて選択"));

		for (JMenuItem mi : menuItem) {
			add(mi);
			mi.addActionListener(this);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}

}

class ViewMenu extends AbstMenu {

	private static final long serialVersionUID = 1L;

	public ViewMenu(JTextArea ta) {
		super(ta, "表示");

		menuItem.add(new JMenuItem("拡大"));
		menuItem.add(new JMenuItem("縮小"));
		menuItem.add(new JMenuItem("フォントサイズ"));
		menuItem.add(new JMenuItem("タブサイズ"));

		for (JMenuItem mi : menuItem) {
			add(mi);
			mi.addActionListener(this);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}

}

class HelpMenu extends AbstMenu {

	private static final long serialVersionUID = 1L;

	public HelpMenu(JTextArea ta) {
		super(ta, "ヘルプ");

		menuItem.add(new JMenuItem("ヘルプ"));
		menuItem.add(new JMenuItem("バージョン情報"));

		for (JMenuItem mi : menuItem) {
			add(mi);
			mi.addActionListener(this);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		String command = e.getActionCommand();

		if (command == menuItem.get(0).getText()) {
			JOptionPane.showMessageDialog(null, "未実装", "未実装",
					JOptionPane.ERROR_MESSAGE);
		} else if (command == menuItem.get(1).getText()) {
			JOptionPane.showMessageDialog(null, "バージョン情報", "バージョン情報",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

}