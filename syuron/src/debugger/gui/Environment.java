/* Copyright (c) 1998, 2011, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. */

/* This source code is provided to illustrate the usage of a given feature
 * or technique and has been deliberately simplified. Additional steps
 * required for a production-quality application, such as security checks,
 * input validation and proper error handling, might not be present in
 * this sample code. */

package debugger.gui;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import com.sun.jdi.*;
import debugger.bdi.*;

public class Environment {

	private SourceManager sourceManager;
	private ClassManager classManager;
	private ContextManager contextManager;
	private MonitorListModel monitorListModel;
	private ExecutionManager runtime;

	private PrintWriter typeScript;
	private VariableTool variableTool;
	private JDBToolBar toolBar;

	private TypeScript appScript;
	private List<String> waitCommand = new ArrayList<>();
	private final CommandInterpreter interpreter;

	private boolean verbose;
	private boolean autoStopMode = false;
	private boolean lineMode = false;
	private boolean printDecMode = false;

	public Environment() {
		this.classManager = new ClassManager(this);
		// ### Order of the next three lines is important! (FIX THIS)
		this.runtime = new ExecutionManager();
		this.sourceManager = new SourceManager(this);
		this.contextManager = new ContextManager(this);
		this.monitorListModel = new MonitorListModel(this);
		interpreter = new CommandInterpreter(this);
	}

	// Services used by debugging tools.

	public SourceManager getSourceManager() {
		return sourceManager;
	}

	public ClassManager getClassManager() {
		return classManager;
	}

	public ContextManager getContextManager() {
		return contextManager;
	}

	public MonitorListModel getMonitorListModel() {
		return monitorListModel;
	}

	public ExecutionManager getExecutionManager() {
		return runtime;
	}

	public void setVariableTool(VariableTool variableTool) {
		this.variableTool = variableTool;
	}

	public VariableTool getVariableTool() {
		return variableTool;
	}

	public void setScript(TypeScript script) {
		appScript = script;
	}

	public void setToolBar(JDBToolBar toolBar) {
		this.toolBar = toolBar;
	}

	public JDBToolBar getToolBar() {
		return toolBar;
	}

	public boolean isAutoStopMode() {
		return autoStopMode;
	}

	public void setAutoStopMode(boolean autoStopMode) {
		this.autoStopMode = autoStopMode;
	}

	public boolean isLineMode() {
		return lineMode;
	}

	public void setLineMode(boolean lineMode) {
		this.lineMode = lineMode;
	}

	public boolean isPrintDecMode() {
		return printDecMode;
	}

	public void setPrintDecMode(boolean printDecMode) {
		this.printDecMode = printDecMode;
	}

	public void executeWaitCommand() {
		// 実行待ちコマンドを実行
		for (String s : waitCommand) {
			interpreter.executeCommand(s);
			if(s.startsWith("clear")) {
				String classname = s.substring(s.indexOf(" ") + 1, s.indexOf(":"));
				String line = s.substring(s.indexOf(":") + 1, s.length());
				appScript.append("この行はBPを設定することができない行のため、BPを削除しました ： ");
				appScript.append(classname + "の" + line + "行目");
				appScript.newline();
			}
		}
		sourceManager.getSourceTool().repaint();
		waitCommand.clear();
	}

	public void addWaitCommand(String command) {
		waitCommand.add(command);
	}

	// ### TODO:
	// ### Tools should attach/detach from environment
	// ### via a property, which should call an 'addTool'
	// ### method when set to maintain a registry of
	// ### tools for exit-time cleanup, etc. Tool
	// ### class constructors should be argument-free, so
	// ### that they may be instantiated by bean builders.
	// ### Will also need 'removeTool' in case property
	// ### value is changed.
	//
	// public void addTool(Tool t);
	// public void removeTool(Tool t);

	public void endProcess() {
		// 実行待ちコマンドを実行
		executeWaitCommand();
		// VMとの接続を切断したらソースを初期値に戻す
		sourceManager.getSourceTool().showSourceFile(sourceManager.getFirstSourceModel());
		// 実行行を初期値に戻す
		sourceManager.getSourceTool().setExcuteLine(-1);
		// BPや実行可能行情報を削除
		sourceManager.clearmarkClassLines();
		// 既存のBPの状態をunresolvedに
		List<EventRequestSpec> specs = runtime.eventRequestSpecs();
		for (EventRequestSpec spec : specs) {
			spec.setStateUnResolved();
		}
		// ボタンの押せる状態を変更
		for (JButton button : toolBar.buttonList) {
			if(button.getText().equals("Run") || button.getText().equals("ClearBP")) {
				button.setEnabled(true);
			} else {
				button.setEnabled(false);
			}
		}
		// リペイントのタイミングがわからないためとりあえずここに
		sourceManager.getSourceTool().getList().repaint();
		variableTool.clear();
	}

	public void terminate() {
		System.exit(0);
	}

	// public void refresh(); // notify all tools to refresh their views

	// public void addStatusListener(StatusListener l);
	// public void removeStatusListener(StatusListener l);

	// public void addOutputListener(OutputListener l);
	// public void removeOutputListener(OutputListener l);

	public void setTypeScript(PrintWriter writer) {
		typeScript = writer;
	}

	public void error(String message) {
		if(typeScript != null) {
			typeScript.println(message);
		} else {
			System.out.println(message);
		}
	}

	public void failure(String message) {
		if(typeScript != null) {
			typeScript.println(message);
		} else {
			System.out.println(message);
		}
	}

	public void notice(String message) {
		if(typeScript != null) {
			typeScript.println(message);
		} else {
			System.out.println(message);
		}
	}

	public OutputSink getOutputSink() {
		return new OutputSink(typeScript);
	}

	public void viewSource(String fileName) {
		// ### HACK ###
		// ### Should use listener here.
		debugger.gui.GUI.srcTool.showSourceFile(fileName);
	}

	public void viewLocation(Location locn) {
		// ### HACK ###
		// ### Should use listener here.
		// ### Should we use sourceForLocation here?
		debugger.gui.GUI.srcTool.showSourceForLocation(locn);
	}

	// ### Also in 'ContextManager'. Do we need both?

	public boolean getVerboseFlag() {
		return verbose;
	}

	public void setVerboseFlag(boolean verbose) {
		this.verbose = verbose;
	}

}
