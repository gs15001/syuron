/* Copyright (c) 1998, 2011, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. */

/* This source code is provided to illustrate the usage of a given feature
 * or technique and has been deliberately simplified. Additional steps
 * required for a production-quality application, such as security checks,
 * input validation and proper error handling, might not be present in
 * this sample code. */

package debugger.gui;

import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import debugger.bdi.*;

class JDBToolBar extends JToolBar {

	private static final long serialVersionUID = 1L;

	Environment env;

	ExecutionManager runtime;
	ClassManager classManager;
	SourceManager sourceManager;

	CommandInterpreter interpreter;
	List<JButton> buttonList = new ArrayList<>();

	JDBToolBar(Environment env) {

		this.env = env;
		this.runtime = env.getExecutionManager();
		this.classManager = env.getClassManager();
		this.sourceManager = env.getSourceManager();
		this.interpreter = new CommandInterpreter(env, true);

		env.setToolBar(this);
		// ===== Configure toolbar here =====

		addTool("実行", "Run", "run", true);
		addSeparator();
		addTool("停止", "Stop", "quit", false);
		addSeparator();
		addTool("次の命令を実行", "StepInto", "step", false);
		addSeparator();
		addTool("次の行を実行", "StepOver", "next", false);
		addSeparator();
		addTool("メソッドの最後まで実行", "StepReturn", "step up", false);
		addSeparator();
		addTool("次のBPまで実行", "Continue", "cont", false);
		addSeparator();
		addTool("最後まで実行", "Last", "last", false);
		addSeparator();
		addTool("全てのBP削除", "ClearBP", "clear all", true);

	}

	private void addTool(String toolTip, String labelText, String command, boolean state) {
		JButton button = new JButton(labelText);
		button.setEnabled(state);
		buttonList.add(button);
		button.setToolTipText(toolTip);
		final String cmd = command;
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				interpreter.executeCommand(cmd);
				env.executeWaitCommand();
			}
		});
		this.add(button);
	}

}
