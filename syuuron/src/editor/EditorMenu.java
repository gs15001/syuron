package editor;

import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.tools.*;

public class EditorMenu extends JMenuBar {

	private static final long serialVersionUID = 1L;

	public EditorMenu(JTextArea ta) {
		super();

		FileMenu fm = new FileMenu(ta);
		EditMenu em = new EditMenu(ta);
		JavaMenu jm = new JavaMenu(ta);
		ViewMenu vm = new ViewMenu(ta);
		HelpMenu hm = new HelpMenu(ta);

		add(fm);
		add(em);
		add(jm);
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
		super(ta, "�t�@�C��");

		menuItem.add(new JMenuItem("�V�K�쐬"));
		menuItem.add(new JMenuItem("�J��"));
		menuItem.add(new JMenuItem("�㏑���ۑ�"));
		menuItem.add(new JMenuItem("���O�����ĕۑ�"));
		menuItem.add(new JMenuItem("����"));

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
			// �e�L�X�g�G���A���󗓂ɂ��ăt�@�C���ǂݍ���
			ta.setText("");
			fa.fileOpen(ta);
		} else if (command == menuItem.get(2).getText()) {
			// �t�@�C�����J���Ă���Ȃ�㏑���A����ȊO�͖��O�����ĕۑ�
			if (EditorStatus.FILENAME == "") {
				fa.fileSave(ta);
			} else {
				fa.overWrite(ta);
			}
		} else if (command == menuItem.get(3).getText()) {
			// ���O�����ĕۑ�
			fa.fileSave(ta);
		} else if (command == menuItem.get(4).getText()) {
			System.exit(0);
		}
	}

}

class EditMenu extends AbstMenu {

	private static final long serialVersionUID = 1L;

	public EditMenu(JTextArea ta) {
		super(ta, "�ҏW");

		menuItem.add(new JMenuItem(""));
		menuItem.add(new JMenuItem(""));
		menuItem.add(new JMenuItem(""));
		menuItem.add(new JMenuItem(""));

		for (JMenuItem mi : menuItem) {
			add(mi);
			mi.addActionListener(this);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}

}

class JavaMenu extends AbstMenu {

	private static final long serialVersionUID = 1L;

	public JavaMenu(JTextArea ta) {
		super(ta, "Java");

		menuItem.add(new JMenuItem("�R���p�C��"));
		menuItem.add(new JMenuItem("���s"));

		for (JMenuItem mi : menuItem) {
			add(mi);
			mi.addActionListener(this);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();

		if (command == menuItem.get(0).getText()) {
			System.out.println("�R���p�C���X�^�[�g");
			System.out.println(EditorStatus.FILENAME);
			JavaCompiler c = ToolProvider.getSystemJavaCompiler();
			int r = c.run(null, System.out, System.out, EditorStatus.FILENAME);
			System.out.println("�߂�l�F" + r);
			System.out.println("�R���p�C���X�g�b�v");
		}
	}
}

class ViewMenu extends AbstMenu {

	private static final long serialVersionUID = 1L;

	public ViewMenu(JTextArea ta) {
		super(ta, "");

		menuItem.add(new JMenuItem("�g��"));
		menuItem.add(new JMenuItem("�k��"));
		menuItem.add(new JMenuItem("�t�H���g�T�C�Y"));
		menuItem.add(new JMenuItem("�^�u�T�C�Y"));

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
		super(ta, "�w���v");

		menuItem.add(new JMenuItem("�w���v"));
		menuItem.add(new JMenuItem("�o�[�W�������"));

		for (JMenuItem mi : menuItem) {
			add(mi);
			mi.addActionListener(this);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		String command = e.getActionCommand();

		if (command == menuItem.get(0).getText()) {
			JOptionPane.showMessageDialog(null, "������", "������",
					JOptionPane.ERROR_MESSAGE);
		} else if (command == menuItem.get(1).getText()) {
			JOptionPane.showMessageDialog(null, "�o�[�W�������", "�o�[�W�������",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

}