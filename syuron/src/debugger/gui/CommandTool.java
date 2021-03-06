/* Copyright (c) 1998, 2011, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. */

/* This source code is provided to illustrate the usage of a given feature
 * or technique and has been deliberately simplified. Additional steps
 * required for a production-quality application, such as security checks,
 * input validation and proper error handling, might not be present in
 * this sample code. */

package debugger.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.util.EventObject;
import javax.swing.JPanel;
import com.sun.jdi.Location;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.event.BreakpointEvent;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventIterator;
import com.sun.jdi.event.MethodEntryEvent;
import com.sun.jdi.event.MethodExitEvent;
import com.sun.jdi.event.StepEvent;
import debugger.bdi.EventRequestSpec;
import debugger.bdi.ExecutionManager;
import debugger.bdi.LineBreakpointSpec;
import debugger.bdi.OutputListener;
import debugger.bdi.PatternReferenceTypeSpec;
import debugger.bdi.SessionListener;
import debugger.bdi.SpecErrorEvent;
import debugger.bdi.SpecEvent;
import debugger.bdi.SpecListener;
import debugger.bdi.Utils;
import debugger.event.AccessWatchpointEventSet;
import debugger.event.ClassPrepareEventSet;
import debugger.event.ClassUnloadEventSet;
import debugger.event.ExceptionEventSet;
import debugger.event.JDIListener;
import debugger.event.LocatableEventSet;
import debugger.event.LocationTriggerEventSet;
import debugger.event.ModificationWatchpointEventSet;
import debugger.event.ThreadDeathEventSet;
import debugger.event.ThreadStartEventSet;
import debugger.event.VMDeathEventSet;
import debugger.event.VMDisconnectEventSet;
import debugger.event.VMStartEventSet;

public class CommandTool extends JPanel {

	private static final long serialVersionUID = 8613516856378346415L;

	private Environment env;

	private ContextManager context;
	private ExecutionManager runtime;
	private SourceManager sourceManager;

	private TypeScript script;

	private static final String DEFAULT_CMD_PROMPT = "Command:";

	final CommandInterpreter interpreter;

	private int preExcuteLine;
	private String preExcuteClass;

	public CommandTool(Environment env) {

		super(new BorderLayout());

		this.env = env;
		this.context = env.getContextManager();
		this.runtime = env.getExecutionManager();
		this.sourceManager = env.getSourceManager();

		script = new TypeScript(DEFAULT_CMD_PROMPT, false); // no echo
		this.add(script);

		interpreter = new CommandInterpreter(env);

		// Establish handler for incoming commands.

		script.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				interpreter.executeCommand(script.readln());
			}
		});

		// Establish ourselves as the listener for VM diagnostics.

		OutputListener diagnosticsListener = new TypeScriptOutputListener(script, true);
		runtime.addDiagnosticsListener(diagnosticsListener);

		// Establish ourselves as the shared debugger typescript.

		env.setTypeScript(new PrintWriter(new TypeScriptWriter(script)));

		// Handle VM events.

		TTYDebugListener listener = new TTYDebugListener(diagnosticsListener);

		runtime.addJDIListener(listener);
		runtime.addSessionListener(listener);
		runtime.addSpecListener(listener);
		context.addContextListener(listener);

		// ### remove listeners on exit!

	}

	private class TTYDebugListener implements JDIListener, SessionListener, SpecListener, ContextListener {

		private OutputListener diagnostics;

		TTYDebugListener(OutputListener diagnostics) {
			this.diagnostics = diagnostics;
		}

		// JDIListener

		@Override
		public void accessWatchpoint(AccessWatchpointEventSet e) {
			setThread(e);
			for (EventIterator it = e.eventIterator(); it.hasNext();) {
				it.nextEvent();
				diagnostics.putString("Watchpoint hit: " + locationString(e));
			}
		}

		@Override
		public void classPrepare(ClassPrepareEventSet e) {
			if(context.getVerboseFlag()) {
				String name = e.getReferenceType().name();
				diagnostics.putString("Class " + name + " loaded");
			}
		}

		@Override
		public void classUnload(ClassUnloadEventSet e) {
			if(context.getVerboseFlag()) {
				diagnostics.putString("Class " + e.getClassName() + " unloaded.");
			}
		}

		@Override
		public void exception(ExceptionEventSet e) {
			setThread(e);
			String name = e.getException().referenceType().name();
			diagnostics.putString("Exception: " + name);
		}

		@Override
		public void locationTrigger(LocationTriggerEventSet e) {
			String locString = locationString(e);
			// System.out.println("locString : " + locString);
			setThread(e);

			env.executeWaitCommand();
			for (EventIterator it = e.eventIterator(); it.hasNext();) {
				Event evt = it.nextEvent();
				if(evt instanceof BreakpointEvent) {
					diagnostics.putString("Breakpoint hit: " + locString);
					env.getVariableTool().update();
				} else if(evt instanceof StepEvent) {
					diagnostics.putString("Step completed: " + locString);

					int excuteLine = Integer.parseInt(locString.substring(locString.lastIndexOf("=") + 1, locString
							.length()));
					String excuteClass = locString.substring(locString.indexOf(" ") + 1, locString.lastIndexOf("."));

					// locstringからパッケージ名を取得
					String pacName = locString.substring(locString.indexOf(" ") + 1, locString.indexOf(".") + 1);

					if(pacName.startsWith("java.")) {
						// パッケージ名がjavaから始まる処理はスキップ
						interpreter.executeCommand("next");
					} else if(excuteClass.equals(preExcuteClass) && excuteLine == preExcuteLine) {
						// 前回実行行と同じならステップ
						interpreter.executeCommand("step");
					} else {
						// ステップイベント完了後の処理
						preExcuteLine = excuteLine;
						preExcuteClass = excuteClass;
						env.getVariableTool().update();
					}
				} else if(evt instanceof MethodEntryEvent) {
					diagnostics.putString("Method entered: " + locString);
				} else if(evt instanceof MethodExitEvent) {
					diagnostics.putString("Method exited: " + locString);
				} else {
					diagnostics.putString("UNKNOWN event: " + e);
				}
			}
			// リペイントのタイミングがわからないためとりあえずここに
			sourceManager.getSourceTool().getList().repaint();
		}

		@Override
		public void modificationWatchpoint(ModificationWatchpointEventSet e) {
			setThread(e);
			for (EventIterator it = e.eventIterator(); it.hasNext();) {
				it.nextEvent();
				diagnostics.putString("Watchpoint hit: " + locationString(e));
			}
		}

		@Override
		public void threadDeath(ThreadDeathEventSet e) {
			if(context.getVerboseFlag()) {
				diagnostics.putString("Thread " + e.getThread() + " ended.");
			}
		}

		@Override
		public void threadStart(ThreadStartEventSet e) {
			if(context.getVerboseFlag()) {
				diagnostics.putString("Thread " + e.getThread() + " started.");
			}
		}

		@Override
		public void vmDeath(VMDeathEventSet e) {
			script.setPrompt(DEFAULT_CMD_PROMPT);
			diagnostics.putString("VM exited");
		}

		@Override
		public void vmDisconnect(VMDisconnectEventSet e) {
			script.setPrompt(DEFAULT_CMD_PROMPT);
			diagnostics.putString("Disconnected from VM");
			env.endProcess();
		}

		@Override
		public void vmStart(VMStartEventSet e) {
			script.setPrompt(DEFAULT_CMD_PROMPT);
			diagnostics.putString("VM started");
		}

		// SessionListener

		@Override
		public void sessionStart(EventObject e) {
		}

		@Override
		public void sessionInterrupt(EventObject e) {
			Thread.yield(); // fetch output
			diagnostics.putString("VM interrupted by user.");
			script.setPrompt(DEFAULT_CMD_PROMPT);
		}

		@Override
		public void sessionContinue(EventObject e) {
			diagnostics.putString("Execution resumed.");
			script.setPrompt(DEFAULT_CMD_PROMPT);
		}

		// SpecListener

		@Override
		public void breakpointSet(SpecEvent e) {
			EventRequestSpec spec = e.getEventRequestSpec();
			diagnostics.putString("Breakpoint set at " + spec + ".");
		}

		@Override
		public void breakpointDeferred(SpecEvent e) {
			EventRequestSpec spec = e.getEventRequestSpec();
			diagnostics.putString("Breakpoint will be set at " + spec + " when its class is loaded.");
		}

		@Override
		public void breakpointDeleted(SpecEvent e) {
			EventRequestSpec spec = e.getEventRequestSpec();
			diagnostics.putString("Breakpoint at " + spec.toString() + " deleted.");
		}

		@Override
		public void breakpointResolved(SpecEvent e) {
			EventRequestSpec spec = e.getEventRequestSpec();
			diagnostics.putString("Breakpoint resolved to " + spec.toString() + ".");
		}

		@Override
		public void breakpointError(SpecErrorEvent e) {
			EventRequestSpec spec = e.getEventRequestSpec();
			if(spec instanceof LineBreakpointSpec) {
				LineBreakpointSpec bp = (LineBreakpointSpec) spec;
				PatternReferenceTypeSpec prSpec = (PatternReferenceTypeSpec) bp.getRefSpec();
				env.addWaitCommand(("clear " + prSpec.toString() + ":" + bp.lineNumber()));
			}
			diagnostics.putString("Deferred breakpoint at " + spec + " could not be resolved:" + e.getReason());
		}

		// ### Add info for watchpoints and exceptions

		@Override
		public void watchpointSet(SpecEvent e) {
		}

		@Override
		public void watchpointDeferred(SpecEvent e) {
		}

		@Override
		public void watchpointDeleted(SpecEvent e) {
		}

		@Override
		public void watchpointResolved(SpecEvent e) {
		}

		@Override
		public void watchpointError(SpecErrorEvent e) {
		}

		@Override
		public void exceptionInterceptSet(SpecEvent e) {
		}

		@Override
		public void exceptionInterceptDeferred(SpecEvent e) {
		}

		@Override
		public void exceptionInterceptDeleted(SpecEvent e) {
		}

		@Override
		public void exceptionInterceptResolved(SpecEvent e) {
		}

		@Override
		public void exceptionInterceptError(SpecErrorEvent e) {
		}

		// ContextListener.

		// If the user selects a new current thread or frame, update prompt.

		@Override
		public void currentFrameChanged(CurrentFrameChangedEvent e) {
			// Update prompt only if affect thread is current.
			ThreadReference thread = e.getThread();
			if(thread == context.getCurrentThread()) {
				script.setPrompt(promptString(thread, e.getIndex()));
			}
		}

	}

	private String locationString(LocatableEventSet e) {
		Location loc = e.getLocation();
		return "thread=\"" + e.getThread().name() + "\", " + Utils.locationString(loc);
	}

	private void setThread(LocatableEventSet e) {
		if(!e.suspendedNone()) {
			Thread.yield(); // fetch output
			script.setPrompt(promptString(e.getThread(), 0));
			// ### Current thread should be set elsewhere, e.g.,
			// ### in ContextManager
			// ### context.setCurrentThread(thread);
		}
	}

	private String promptString(ThreadReference thread, int frameIndex) {
		if(thread == null) {
			return DEFAULT_CMD_PROMPT;
		} else {
			// Frame indices are presented to user as indexed from 1.
			return (thread.name() + "[" + (frameIndex + 1) + "]:");
		}
	}
}
