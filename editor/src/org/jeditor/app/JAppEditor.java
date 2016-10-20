/* This file is part of the jEditor library: see http://jeditor.sourceforge.net
 * Copyright (C) 2010 Herve Girod
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE. */

package org.jeditor.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.TreeMap;
import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import org.jeditor.navi.HistoryList;
import org.jeditor.navi.NaviManager;
import debugger.gui.GUI;
import debugger.gui.JDBFileFilter;

/**
 * A class showing the use of the package. It presents a panel, which adapt to the
 * different possible scripts and allow to modify them.
 * 
 * @version 0.4.1
 */
public class JAppEditor extends JFrame {

	static final String DEFAULTPATH = "D:\\TEMP";

	// settings types
	private static String version = "2.0";
	private static String versiondate = "17/03/08";
	private TreeMap filelist = new TreeMap();
	private JTabbedPane tab = new JTabbedPane();
	private ConsolePane console = new ConsolePane();
	private NaviManager naviManager;
	private HistoryList history;
	private JMenuBar Mbar = new JMenuBar();
	private JMenu fileMenu = new JMenu("File");
	private JMenu toolsMenu = new JMenu("Tools");
	private JMenu debugMenu = new JMenu("Debug");
	private JMenu helpMenu = new JMenu("Help");
	protected JComboBox bTypecb;
	private AbstractAction openfileAction;
	private AbstractAction saveAction;
	private AbstractAction saveAsAction;
	private AbstractAction exitAction;
	private AbstractAction searchAction;
	private AbstractAction settingsAction;
	private AbstractAction compileAction;
	private AbstractAction runAction;
	private AbstractAction killAction;
	private AbstractAction debugAction;
	private AbstractAction debugerAction;
	private AbstractAction aboutAction;
	private SyntaxManager manager;
	public String filetype = null;
	private int tabSize = 4;
	private Process process = null;

	public final int WINDOW_WIDTH;
	public final int WINDOW_HEIGHT;

	public final int LEFT_WIDTH;
	public final int LEFT_HEIGHT;

	public final int RIGHT_WIDTH;
	public final int RIGHT_HEIGHT;

	public final int BOTTOM_WIDTH;
	public final int BOTTOM_HEIGHT;

	/**
	 * constructor of MainWindow class.
	 * Returns the JFrame manager.
	 **/
	public JAppEditor() {
		// Initialise the window
		super("JAppEditor");
		int hight = Toolkit.getDefaultToolkit().getScreenSize().height;
		int width = Toolkit.getDefaultToolkit().getScreenSize().width;

		// 推奨スペック(解像度)1366*768以上
		if(width > 1366) {
			WINDOW_WIDTH = 1366;
			WINDOW_HEIGHT = 768;
		} else {
			WINDOW_WIDTH = width;
			WINDOW_HEIGHT = hight;
		}

		LEFT_WIDTH = (int) (WINDOW_WIDTH * 0.5);
		LEFT_HEIGHT = (int) (WINDOW_HEIGHT * 0.8);
		RIGHT_WIDTH = (int) (WINDOW_WIDTH * 0.5);
		RIGHT_HEIGHT = (int) (WINDOW_HEIGHT * 0.8);
		BOTTOM_WIDTH = WINDOW_WIDTH;
		BOTTOM_HEIGHT = (int) (WINDOW_HEIGHT * 0.2);

		System.out.println("WINDOW_HIGHT : " + WINDOW_HEIGHT);
		System.out.println("WINDOW_WIDTH : " + WINDOW_WIDTH);

		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setBackground(Color.lightGray);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				exitApplication();
			}
		});

		// add Mouse Properties of TabbedPane

		tab.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON3) {
					manageFile(e);
				}
			}
		});

		tab.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				showCurrentFile();
			}
		});
		/* creation du Syntaxmanager */
		manager = SyntaxManager.instance();
		try {
			manager.initialize();
		} catch (IOException e) {
			e.printStackTrace();
		}
		/* creation des composants internes */
		JPanel npane = new JPanel();
		bTypecb = new JComboBox(manager.getTypeList());
		bTypecb.setSelectedItem(manager.getFirstType());

		bTypecb.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				filetype = (String) (e.getItem());
				FilePane p = (FilePane) tab.getSelectedComponent();
				if(p != null) {
					p.setFileType(filetype);
				}
			};
		});
		npane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		npane.add(bTypecb);

		this.setJMenuBar(Mbar);
		loadMenus();

		/* Insertion des composants => Panel de la Frame */
		JPanel mainPane = new JPanel();
		setContentPane(mainPane);
		mainPane.setLayout(new BorderLayout());
		mainPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		// エディター部分
		tab.setPreferredSize(new Dimension(LEFT_WIDTH, LEFT_HEIGHT));
		doOpenFile(new File(DEFAULTPATH, "新規.java"));
		// ナビゲーション部分
		naviManager = new NaviManager(this);
		history = new HistoryList(naviManager);
		JPanel naviPane = naviManager.getViewPane();
		naviPane.setPreferredSize(new Dimension(RIGHT_WIDTH, (int) (RIGHT_HEIGHT * 0.87)));
		history.setPreferredSize(new Dimension(RIGHT_WIDTH, (int) (RIGHT_HEIGHT * 0.1)));
		JSplitPane centerRight = new JSplitPane(JSplitPane.VERTICAL_SPLIT, naviPane, history);
		// コンソール部分
		console.setPreferredSize(new Dimension(BOTTOM_WIDTH, BOTTOM_HEIGHT));

		// パネルの合体
		JSplitPane centerTop = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tab, centerRight);
		centerTop.setDividerLocation(LEFT_WIDTH);
		JSplitPane center = new JSplitPane(JSplitPane.VERTICAL_SPLIT, centerTop, console);
		center.setDividerLocation(LEFT_HEIGHT);
		mainPane.add(center, BorderLayout.CENTER);
	}

	/** loads Menus */
	private void loadMenus() {
		/* Creation des actions pour les menus */
		exitAction = new AbstractAction("Exit") {

			public void actionPerformed(ActionEvent ae) {
				exitApplication();
			}
		};

		openfileAction = new AbstractAction("Open") {

			public void actionPerformed(ActionEvent e) {
				openFile();
			}
		};

		saveAction = new AbstractAction("Save") {

			public void actionPerformed(ActionEvent e) {
				doSave();
			}
		};

		saveAsAction = new AbstractAction("SaveAs") {

			public void actionPerformed(ActionEvent e) {
				doSaveAs();
			}
		};

		searchAction = new AbstractAction("Search") {

			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Not implemented", "Message", JOptionPane.INFORMATION_MESSAGE);
			}
		};

		settingsAction = new AbstractAction("Settings") {

			public void actionPerformed(ActionEvent e) {
				openSettings();
			}
		};

		compileAction = new AbstractAction("compile") {

			public void actionPerformed(ActionEvent e) {
				doCompile();
			}
		};

		runAction = new AbstractAction("run") {

			public void actionPerformed(ActionEvent e) {
				doRun();
			}
		};

		killAction = new AbstractAction("kill") {

			public void actionPerformed(ActionEvent e) {
				doKill();
			}
		};

		debugAction = new AbstractAction("debug support") {

			@Override
			public void actionPerformed(ActionEvent e) {

			}
		};

		debugerAction = new AbstractAction("open debugger") {

			@Override
			public void actionPerformed(ActionEvent e) {
				doOpenDebugger();
			}
		};

		aboutAction = new AbstractAction("About") {

			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "JAppEditor version " + version + "\n building date: "
						+ versiondate, "About", JOptionPane.INFORMATION_MESSAGE);
			}
		};

		/* creation des composants internes */
		JMenuItem openfileItem = new JMenuItem(openfileAction);
		JMenuItem exitItem = new JMenuItem(exitAction);
		JMenuItem aboutItem = new JMenuItem(aboutAction);
		JMenuItem searchItem = new JMenuItem(searchAction);
		JMenuItem settingsItem = new JMenuItem(settingsAction);
		JMenuItem saveAsItem = new JMenuItem(saveAsAction);
		JMenuItem saveItem = new JMenuItem(saveAction);
		JMenuItem compileItem = new JMenuItem(compileAction);
		JMenuItem runItem = new JMenuItem(runAction);
		JMenuItem killItem = new JMenuItem(killAction);
		JMenuItem debugItem = new JMenuItem(debugAction);
		JMenuItem debuggerItem = new JMenuItem(debugerAction);

		/* Insertion des composants => Barre de menu et boutons */
		fileMenu.add(openfileItem);
		fileMenu.add(saveItem);
		fileMenu.add(saveAsItem);
		fileMenu.add(exitItem);
		toolsMenu.add(searchItem);
		toolsMenu.add(settingsItem);
		toolsMenu.add(compileItem);
		toolsMenu.add(runItem);
		toolsMenu.add(killItem);
		debugMenu.add(debugItem);
		debugMenu.add(debugerAction);
		helpMenu.add(aboutItem);

		Mbar.add(fileMenu);
		Mbar.add(toolsMenu);
		Mbar.add(debugMenu);
		Mbar.add(helpMenu);
	}

	private void openSettings() {
		SettingsDialog dialog = new SettingsDialog(this, tabSize);
		int ret = dialog.showDialog();
		if(ret == JFileChooser.APPROVE_OPTION) {
			tabSize = dialog.getTabSizeValue();
			FilePane.setTabSize(tabSize);
		}
	}

	public void setFileType(String type) {
		filetype = type;
		bTypecb.setSelectedItem(type);
	}

	/* choose Destination folder to look for HBK folders */
	private void openFile() {
		JFileChooser chooser = new JFileChooser("D:\\TEMP");
		JDBFileFilter filter = new JDBFileFilter("java", "Java source code");
		chooser.setFileFilter(filter);
		if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			final File file = chooser.getSelectedFile();
			if(!filelist.keySet().contains(file.getName())) {
				chooser.setCurrentDirectory(new File(file.getParent()));
				SwingUtilities.invokeLater(new Runnable() {

					public void run() {
						doOpenFile(file);
					}
				});
			}
		}
	}

	public void doOpenFile(File file) {
		FilePane pane = new FilePane(this, file);
		filelist.put(pane.getName(), pane.getFile());
		tab.addTab(file.getName(), pane);
		tab.setSelectedComponent(pane);
	}

	private void showCurrentFile() {
		if(tab.getSelectedComponent() != null) {
			FilePane.TextFile toFile = ((FilePane) (tab.getSelectedComponent())).getFile();
			setFileType(manager.getType(toFile.getExtension()));
		}
	}

	private void manageFile(MouseEvent e) {
		// catch event position
		int x = e.getX();
		int y = e.getY();

		// create menus
		JPopupMenu menu = new JPopupMenu();
		JMenuItem close = new JMenuItem("Close");
		// compareは使わないので非表示に
		/* JMenu compare = new JMenu("Compare to");
		 * Iterator it = filelist.keySet().iterator();
		 * String curfolder = ((FilePane) (tab.getSelectedComponent())).getName();
		 * while (it.hasNext()) {
		 * String s = (String) (it.next());
		 * if(!s.equals(curfolder)) {
		 * JMenuItem item = new JMenuItem(s);
		 * compare.add(item);
		 * item.addActionListener(new ActionListener() {
		 * 
		 * public void actionPerformed(ActionEvent e) {
		 * FilePane.TextFile toFile = ((FilePane) (tab.getSelectedComponent())).getFile();
		 * String s = ((JMenuItem) (e.getSource())).getText();
		 * FilePane.TextFile fromFile = (FilePane.TextFile) (filelist.get(s));
		 * compareFiles(fromFile, toFile);
		 * }
		 * });
		 * }
		 * } */

		// add listeners to sub-menus
		close.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				FilePane fp = (FilePane) tab.getSelectedComponent();
				doClose(fp);
			}
		});

		menu.add(close);
		// menu.add(compare);
		menu.show(tab, x, y);
	}

	private void compareFiles(FilePane.TextFile fromTextFile, FilePane.TextFile toTextFile) {
		FilePane pane = new FilePane(this, toTextFile, fromTextFile);
		tab.addTab(pane.getName(), pane);
	}

	private void exitApplication() {
		int c = tab.getTabCount();
		boolean exit = true;
		tab.setSelectedIndex(0);
		for (int i = 0; i < c; i++) {
			FilePane fp = (FilePane) tab.getComponentAt(0);
			if(doClose(fp) == 2) {
				exit = false;
				break;
			}
		}
		if(exit) {
			System.exit(0);
		}
	}

	private int doClose(FilePane fp) {
		if(fp != null) {
			if(fp.ed.isEdited()) {
				return showCloseDialog(fp);
			} else {
				fileClose(fp);
			}
		}
		return 0;
	}

	private int showCloseDialog(FilePane fp) {
		String[] buttons = { "保存して閉じる", "保存せず閉じる", "キャンセル" };
		int r = JOptionPane.showOptionDialog(this, fp.fromfile.file.getName() + "は保存されていません。保存しますか", "警告",
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, buttons, "キャンセル");
		if(r == 0) {
			// 保存して閉じる
			doSave();
			fileClose(fp);
		} else if(r == 1) {
			// 保存せず閉じる
			fileClose(fp);
		}
		return r;
	}

	private void fileClose(FilePane fp) {
		if(filelist.containsKey(fp.getName())) {
			filelist.remove(fp.getName());
		}
		tab.removeTabAt(tab.getSelectedIndex());
	}

	private void doCompile() {
		FilePane fp = (FilePane) tab.getSelectedComponent();
		if(fp != null) {
			if(!fp.ed.isEdited()) {
				JavaCompiler javac = ToolProvider.getSystemJavaCompiler();

				File file = ((FilePane) (tab.getSelectedComponent())).getFile().file;
				String fileName = file.getName();
				String fileLoc = file.getParent();
				String[] commands = { "-g", "-cp", fileLoc, fileLoc + "\\" + fileName };
				// コンパイル時の出力を受け取るストリームの生成(これでいいかは不明）
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				ByteArrayOutputStream err = new ByteArrayOutputStream();
				int r = javac.run(null, out, err, commands);
				if(r == 0) {
					console.outputLine("Succeeded compile");
					fp.ed.setCompiled(true);
				}
				console.outputLine(out.toString());
				console.outputLine(err.toString());
			} else {
				JOptionPane.showMessageDialog(this, "ソースが保存されていません。", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void doRun() {
		FilePane fp = (FilePane) tab.getSelectedComponent();
		if(fp != null) {
			if(fp.ed.isCompiled()) {
				ProcessBuilder pb = new ProcessBuilder();

				File file = ((FilePane) (tab.getSelectedComponent())).getFile().file;
				String fileName = file.getName();
				fileName = fileName.substring(0, fileName.lastIndexOf("."));
				String fileLoc = file.getParent();
				pb.command("java", "-cp", fileLoc, fileName);
				try {
					// 前のプロセスが残っているならキル
					if(process != null) {
						process.destroy();
					}
					// 新たなプロセスを生成
					process = pb.start();
					// 入出力に関する設定
					OutputStreamThread it = new OutputStreamThread(process.getInputStream(), "SJIS", console);
					OutputStreamThread et = new OutputStreamThread(process.getErrorStream(), "SJIS", console);
					console.setOutputStream(process.getOutputStream()); // 起動したJavaへの
					it.start();
					et.start();

				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				JOptionPane.showMessageDialog(this, "ソースがcompileされていません", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void doKill() {
		if(process != null) {
			process.destroy();
			process = null;
		}
	}

	public void doSave() {
		FilePane fp = (FilePane) tab.getSelectedComponent();
		if(fp != null) {
			if(fp.ed.isEdited()) {
				File f = fp.fromfile.file;
				try (BufferedWriter br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), "SJIS"))) {
					Document d = fp.ed.getDocument();
					br.write(d.getText(0, d.getLength()), 0, d.getLength());
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (BadLocationException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				console.outputLine("Saved");
				fp.ed.setEdited(false);
			}
		}
	}

	public void doSaveAs() {
		FilePane fp = (FilePane) tab.getSelectedComponent();
		if(fp != null) {
			JFileChooser chooser = new JFileChooser(DEFAULTPATH);
			JDBFileFilter filter = new JDBFileFilter("java", "Java source code");
			chooser.setFileFilter(filter);
			if(chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				File f = chooser.getSelectedFile();
				if(!f.getName().endsWith(".java")) {
					f = new File(f.getParentFile(), f.getName() + ".java");
				}
				try (BufferedWriter br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), "SJIS"))) {
					Document d = fp.ed.getDocument();
					br.write(d.getText(0, d.getLength()), 0, d.getLength());
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (BadLocationException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				console.outputLine("Saved As " + f.getName());
				doOpenFile(f);
			}
		}
	}

	public void doOpenDebugger() {
		FilePane fp = (FilePane) tab.getSelectedComponent();
		if(fp != null) {
			File f = fp.fromfile.file;
			GUI gui = new GUI();
			String[] args = { "-sourcepath", f.getParent(), "-classpath", f.getParent() };
			gui.run(args);
			gui.openFile(f);
		} else {
			GUI gui = new GUI();
			String[] args = { "-sourcepath", DEFAULTPATH, "-classpath", DEFAULTPATH };
			gui.run(args);
		}
	}

	public void addAsta() {
		String title = tab.getTitleAt(tab.getSelectedIndex());
		if(!title.endsWith("*")) {
			title += "*";
			tab.setTitleAt(tab.getSelectedIndex(), title);
		}
	}

	public void removeAsta() {
		String title = tab.getTitleAt(tab.getSelectedIndex());
		if(title.endsWith("*")) {
			title = title.substring(0, title.length() - 1);
			tab.setTitleAt(tab.getSelectedIndex(), title);
		}
	}

	public FilePane getFilePane() {
		return (FilePane) tab.getSelectedComponent();
	}

	public static void main(String args[]) {
		JAppEditor app = new JAppEditor();
		app.setVisible(true);
	}
}