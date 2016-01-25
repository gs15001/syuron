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
import debugger.bdi.*;

class JDBToolBar extends JToolBar {

	Environment env;

	ExecutionManager runtime;
	ClassManager classManager;
	SourceManager sourceManager;

	CommandInterpreter interpreter;

	JDBToolBar(Environment env) {

		this.env = env;
		this.runtime = env.getExecutionManager();
		this.classManager = env.getClassManager();
		this.sourceManager = env.getSourceManager();
		this.interpreter = new CommandInterpreter(env, true);

		// ===== Configure toolbar here =====

		addTool("Run", "実行", "run");
		addSeparator();

		addTool("Step Into", "次の命令", "step");
		addTool("Step Over", "次の行", "next");
		addTool("Step Return", "メソッドの最後", "step up");
		addSeparator();
		addTool("Continue", "次のBP", "cont");
		addSeparator();
		addTool("Last", "最後まで", "last");
		addSeparator();
		addTool("Clear", "全てのBP削除", "clear all");

	}

	private void addTool(String toolTip, String labelText, String command) {
		JButton button = new JButton(labelText);
		button.setToolTipText(toolTip);
		final String cmd = command;
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				interpreter.executeCommand(cmd);
			}
		});
		this.add(button);
	}

}
