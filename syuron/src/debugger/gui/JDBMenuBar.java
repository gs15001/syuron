/* Copyright (c) 1998, 2011, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. */

/* This source code is provided to illustrate the usage of a given feature
 * or technique and has been deliberately simplified. Additional steps
 * required for a production-quality application, such as security checks,
 * input validation and proper error handling, might not be present in
 * this sample code. */

package debugger.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;
import debugger.bdi.*;

// ### This is currently just a placeholder!

class JDBMenuBar extends JMenuBar {

	private static final long serialVersionUID = 1L;

	Environment env;

	ExecutionManager runtime;
	ClassManager classManager;
	SourceManager sourceManager;

	CommandInterpreter interpreter;

	JDBMenuBar(Environment env) {
		this.env = env;
		this.runtime = env.getExecutionManager();
		this.classManager = env.getClassManager();
		this.sourceManager = env.getSourceManager();
		this.interpreter = new CommandInterpreter(env, true);
		
		setFont(new Font("メイリオ", Font.PLAIN, 12));

		JMenu fileMenu = new JMenu("File");
		fileMenu.setFont(new Font("メイリオ", Font.PLAIN, 12));

		JMenuItem openItem = new JMenuItem("Open...", 'O');
		openItem.setFont(new Font("メイリオ", Font.PLAIN, 12));
		openItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				openCommand();
			}
		});
		fileMenu.add(openItem);
		addTool(fileMenu, "Exit debugger", "Exit", "exit");

		JMenu cmdMenu = new JMenu("Commands");
		cmdMenu.setFont(new Font("メイリオ", Font.PLAIN, 12));

		addTool(cmdMenu, "実行", "Run", "run");
		addTool(cmdMenu, "停止", "Stop", "quit");
		cmdMenu.addSeparator();
		addTool(cmdMenu, "次の命令を実行", "StepInto", "step");
		addTool(cmdMenu, "次の行を実行", "StepOver", "next");
		addTool(cmdMenu, "メソッドの最後まで実行", "StepReturn", "step up");
		cmdMenu.addSeparator();
		addTool(cmdMenu, "次のBPまで実行", "Continue", "cont");
		cmdMenu.addSeparator();
		addTool(cmdMenu, "最後まで実行", "Last", "last");
		cmdMenu.addSeparator();
		addTool(cmdMenu, "全てのBP削除", "ClearBP", "clear all");

		JMenu optionMenu = new JMenu("Option");
		optionMenu.setFont(new Font("メイリオ", Font.PLAIN, 12));
		JMenu optionSubMenu1 = new JMenu("実行行表示方法");
		optionSubMenu1.setFont(new Font("メイリオ", Font.PLAIN, 12));
		optionSubMenu1.setToolTipText("実行時の今の処理位置の表示方法を選択します");
		ButtonGroup group1 = new ButtonGroup();
		JRadioButtonMenuItem radiomenuitem1 = new JRadioButtonMenuItem("行表示", false);
		radiomenuitem1.setFont(new Font("メイリオ", Font.PLAIN, 12));
		radiomenuitem1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				env.setLineMode(radiomenuitem1.isSelected());
				sourceManager.getSourceTool().getList().repaint();
			}
		});
		JRadioButtonMenuItem radiomenuitem2 = new JRadioButtonMenuItem("線表示", true);
		radiomenuitem2.setFont(new Font("メイリオ", Font.PLAIN, 12));
		radiomenuitem2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				env.setLineMode(radiomenuitem1.isSelected());
				sourceManager.getSourceTool().getList().repaint();
			}
		});
		group1.add(radiomenuitem1);
		group1.add(radiomenuitem2);
		optionSubMenu1.add(radiomenuitem1);
		optionSubMenu1.add(radiomenuitem2);

		JMenu optionSubMenu2 = new JMenu("自動停止モード");
		optionSubMenu2.setToolTipText("ONにするとmainメソッドの最初の命令で停止します");
		optionSubMenu2.setFont(new Font("メイリオ", Font.PLAIN, 12));
		ButtonGroup group2 = new ButtonGroup();
		JRadioButtonMenuItem radiomenuitem3 = new JRadioButtonMenuItem("ON", false);
		radiomenuitem3.setFont(new Font("メイリオ", Font.PLAIN, 12));
		radiomenuitem3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				env.setAutoStopMode(radiomenuitem3.isSelected());
			}
		});
		JRadioButtonMenuItem radiomenuitem4 = new JRadioButtonMenuItem("OFF", true);
		radiomenuitem4.setFont(new Font("メイリオ", Font.PLAIN, 12));
		radiomenuitem4.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				env.setAutoStopMode(radiomenuitem3.isSelected());
			}
		});
		group2.add(radiomenuitem3);
		group2.add(radiomenuitem4);
		optionSubMenu2.add(radiomenuitem3);
		optionSubMenu2.add(radiomenuitem4);

		JMenu optionSubMenu3 = new JMenu("宣言元表示");
		optionSubMenu3.setFont(new Font("メイリオ", Font.PLAIN, 12));
		optionSubMenu3.setToolTipText("宣言元の表示方法を選択肢ます");
		ButtonGroup group3 = new ButtonGroup();
		JRadioButtonMenuItem radiomenuitem5 = new JRadioButtonMenuItem("値", false);
		radiomenuitem5.setFont(new Font("メイリオ", Font.PLAIN, 12));
		radiomenuitem5.setToolTipText("引数の値で表示されます\n配列とオブジェクト型は変数名で表示されます");
		radiomenuitem5.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				env.setPrintDecMode(radiomenuitem5.isSelected());
				env.getVariableTool().update();
			}
		});
		JRadioButtonMenuItem radiomenuitem6 = new JRadioButtonMenuItem("型", true);
		radiomenuitem6.setFont(new Font("メイリオ", Font.PLAIN, 12));
		radiomenuitem6.setToolTipText("引数の型で表示されます");
		radiomenuitem6.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				env.setPrintDecMode(radiomenuitem5.isSelected());
				env.getVariableTool().update();
			}
		});
		group3.add(radiomenuitem5);
		group3.add(radiomenuitem6);
		optionSubMenu3.add(radiomenuitem5);
		optionSubMenu3.add(radiomenuitem6);

		optionMenu.add(optionSubMenu1);
		optionMenu.add(optionSubMenu2);
		optionMenu.add(optionSubMenu3);

		JMenu helpMenu = new JMenu("Help");
		helpMenu.setFont(new Font("メイリオ", Font.PLAIN, 12));
		JMenuItem helpItem = new JMenuItem("Help");
		helpItem.setFont(new Font("メイリオ", Font.PLAIN, 12));
		helpItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				showHelp();
			}
		});
		helpMenu.add(helpItem);

		this.add(fileMenu);
		this.add(cmdMenu);
		this.add(optionMenu);
		this.add(helpMenu);
	}

	private void showHelp() {
		Frame frame = JOptionPane.getRootFrame();
		JDialog dialog = new JDialog(frame, "Help");
		Container contents = dialog.getContentPane();
		JLabel label = new JLabel("Help is no implement");
		contents.setLayout(new FlowLayout());
		contents.add(label);
		dialog.setSize(150, 70);
		dialog.setVisible(true);

	}

	private void openCommand() {
		JFileChooser chooser = new JFileChooser("D:\\TEMP");
		JDBFileFilter filter = new JDBFileFilter("java", "Java source code");
		chooser.setFileFilter(filter);
		int result = chooser.showOpenDialog(this);
		if(result == JFileChooser.APPROVE_OPTION) {
			openFile(chooser.getSelectedFile());
		}
	}

	public void openFile(File f) {
		// 選択された場合、必要情報の書き換え
		env.getClassManager().setClassPath(new SearchPath(f.getParent()));
		env.getSourceManager().setSourcePath(new SearchPath(f.getParent()));

		// MainClassNameの書き換え(引数は基本的になし)
		String clsname = f.getName();
		clsname = clsname.substring(0, clsname.lastIndexOf("."));
		env.getContextManager().setMainClassName(clsname);
		env.getContextManager().setProgramArguments("");

		// 選択したファイルの表示
		env.viewSource(f.getName());
	}

	private void addTool(JMenu menu, String toolTip, String labelText, String command) {
		JMenuItem mi = new JMenuItem(labelText);
		mi.setFont(new Font("メイリオ", Font.PLAIN, 12));
		mi.setToolTipText(toolTip);
		final String cmd = command;
		mi.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				interpreter.executeCommand(cmd);
			}
		});
		menu.add(mi);
	}

}
