/*
 * Copyright (c) 1998, 2011, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

/*
 * This source code is provided to illustrate the usage of a given feature
 * or technique and has been deliberately simplified. Additional steps
 * required for a production-quality application, such as security checks,
 * input validation and proper error handling, might not be present in
 * this sample code.
 */

package debugger.gui;

import java.io.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import com.sun.jdi.*;
import com.sun.jdi.request.*;

import debugger.bdi.*;
import debugger.gui.SourceModel.Line;

public class SourceTool extends JPanel {

	private static final long serialVersionUID = -5461299294186395257L;

	private Environment env;

	private ExecutionManager runtime;
	private ContextManager context;
	private SourceManager sourceManager;

	private JList list;
	private ListModel sourceModel;

	// Information on source file that is on display, or failed to be
	// displayed due to inaccessible source. Used to update display
	// when sourcepath is changed.

	private String sourceName; // relative path name, if showSourceFile
	private Location sourceLocn; // location, if showSourceForLocation
	private CommandInterpreter interpreter;
	private int excuteLine = -1;

	public SourceTool(Environment env) {

		super(new BorderLayout());

		this.env = env;

		runtime = env.getExecutionManager();
		sourceManager = env.getSourceManager();
		sourceManager.setSourceTool(this);
		this.context = env.getContextManager();
		this.interpreter = new CommandInterpreter(env, true);

		sourceModel = new DefaultListModel(); // empty

		list = new JList(sourceModel);
		list.setCellRenderer(new SourceLineRenderer());

		list.setPrototypeCellValue(SourceModel.prototypeCellValue);

		SourceToolListener listener = new SourceToolListener();
		context.addContextListener(listener);
		runtime.addSpecListener(listener);
		sourceManager.addSourceListener(listener);

		MouseListener squeek = new STMouseListener();
		list.addMouseListener(squeek);

		add(new JScrollPane(list));
	}

	public void setTextFont(Font f) {
		list.setFont(f);
		list.setPrototypeCellValue(SourceModel.prototypeCellValue);
	}

	public SourceModel getSourceModel() {
		if (sourceModel instanceof DefaultListModel) {
			return null;
		}
		return (SourceModel) sourceModel;
	}

	public void setExcuteLine(int excuteLine) {
		this.excuteLine = excuteLine;
	}

	public JList getList() {
		return list;
	}
	private class SourceToolListener implements ContextListener,
			SourceListener, SpecListener {

		// ContextListener

		@Override
		public void currentFrameChanged(CurrentFrameChangedEvent e) {
			showSourceContext(e.getThread(), e.getIndex());
		}

		// Clear source view.
		// sourceModel = new DefaultListModel(); // empty

		// SourceListener

		@Override
		public void sourcepathChanged(SourcepathChangedEvent e) {
			// Reload source view if its contents depend
			// on the source path.
			if (sourceName != null) {
				showSourceFile(sourceName);
			} else if (sourceLocn != null) {
				showSourceForLocation(sourceLocn);
			}
		}

		// SpecListener

		@Override
		public void breakpointSet(SpecEvent e) {
			breakpointResolved(e);
		}

		@Override
		public void breakpointDeferred(SpecEvent e) {
		}

		@Override
		public void breakpointDeleted(SpecEvent e) {
			BreakpointRequest req = (BreakpointRequest) e.getEventRequest();
			if (req == null) {
				return;
			}
			Location loc = req.location();
			if (loc != null) {
				try {
					SourceModel sm = sourceManager.sourceForLocation(loc);
					sm.showBreakpoint(loc.lineNumber(), false);
					showSourceForLocation(loc);
				} catch (Exception exc) {
				}
			}
		}

		@Override
		public void breakpointResolved(SpecEvent e) {
			BreakpointRequest req = (BreakpointRequest) e.getEventRequest();
			Location loc = req.location();
			try {
				SourceModel sm = sourceManager.sourceForLocation(loc);
				sm.showBreakpoint(loc.lineNumber(), true);
				showSourceForLocation(loc);
			} catch (Exception exc) {
			}
		}

		@Override
		public void breakpointError(SpecErrorEvent e) {
			breakpointDeleted(e);
		}

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
	}

	private void showSourceContext(ThreadReference thread, int index) {
		// ### Should use ThreadInfo here.
		StackFrame frame = null;
		if (thread != null) {
			try {
				frame = thread.frame(index);
			} catch (IncompatibleThreadStateException e) {
			}
		}
		if (frame == null) {
			return;
		}
		Location locn = frame.location();
		/*****
		 * if (!showSourceForLocation(locn)) {
		 * env.notice("Could not display source for " +
		 * Utils.locationString(locn)); }
		 *****/
		showSourceForLocation(locn);
	}

	public boolean showSourceForLocation(Location locn) {
		sourceName = null;
		sourceLocn = locn;
		int lineNo = locn.lineNumber();
		if (lineNo != -1) {
			SourceModel source = sourceManager.sourceForLocation(locn);
			if (source != null) {
				showSourceAtLine(source, lineNo - 1);
				return true;
			}
		}
		// Here if we could not display source.
		showSourceUnavailable();
		return false;
	}

	public boolean showSourceFile(String fileName) {
		sourceLocn = null;
		File file;
		if (!fileName.startsWith(File.separator)) {
			sourceName = fileName;
			SearchPath sourcePath = sourceManager.getSourcePath();
			file = sourcePath.resolve(fileName);
			if (file == null) {
				// env.failure("Source not found on current source path.");
				showSourceUnavailable();
				return false;
			}
		} else {
			sourceName = null; // Absolute pathname does not depend on
								// sourcepath.
			file = new File(fileName);
		}
		SourceModel source = sourceManager.sourceForFile(file);
		if (source != null) {
			showSource(source);
			return true;
		}
		showSourceUnavailable();
		return false;
	}

	private void showSource(SourceModel model) {
		setViewModel(model);
	}

	private void showSourceAtLine(SourceModel model, int lineNo) {
		setViewModel(model);
		if (model.isActuallySource && (lineNo < model.getSize())) {
			list.setSelectedIndex(lineNo);
			if (lineNo + 4 < model.getSize()) {
				list.ensureIndexIsVisible(lineNo + 4); // give some context
				list.clearSelection();
			}
			list.ensureIndexIsVisible(lineNo);
			list.clearSelection();
		}
	}

	private void showSourceUnavailable() {
		SourceModel model = new SourceModel("[Source code is not available]");
		setViewModel(model);
	}

	private void setViewModel(SourceModel model) {
		if (model != sourceModel) {
			// install new model
			list.setModel(model);
			sourceModel = model;
		}
	}

	private class SourceLineRenderer extends DefaultListCellRenderer {

		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {

			super.getListCellRendererComponent(list, value, index, isSelected,
					cellHasFocus);

			SourceModel.Line line = (SourceModel.Line) value;
			String text = line.text;

			MyJTextPane pane = new MyJTextPane();
			boolean isExecute = index + 1 == excuteLine; // 現在のラインが実行される行か
			if (isSelected) {
				pane.setForeground(list.getSelectionForeground());
				pane.setBackground(list.getSelectionBackground());
			}
			pane.setExecution(isExecute);
			pane.setBreakpoint(line.hasBreakpoint);
			pane.setFont(list.getFont());
			pane.setText(text);
			pane.setMargin(new Insets(0, 5, 0, 0));
			return pane;

			// setText(line.text);
			// if (line.hasBreakpoint) {
			// setIcon(Icons.stopSignIcon);
			// } else if (line.isExecutable()) {
			// setIcon(Icons.execIcon);
			// } else {
			// setIcon(Icons.blankIcon);
			// }
			// return this;
		}

		@Override
		public Dimension getPreferredSize() {
			Dimension dim = super.getPreferredSize();
			return new Dimension(dim.width, dim.height - 5);
		}

		protected void paintComponent(Graphics g) {
			super.paintComponents(g);
			if (this.getComponent(0) instanceof MyJTextPane) {
				MyJTextPane mjtp = (MyJTextPane) this.getComponent(0);
				mjtp.paintComponent(g);
			}
		}

		protected class MyJTextPane extends JTextPane {
			private static final long serialVersionUID = 1L;

			private boolean isExecution;
			private boolean hasBreakpoint;

			public MyJTextPane() {
				super();
			}

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				int red = hasBreakpoint || !(hasBreakpoint || isExecution) ? 255
						: 0;
				int green = !(hasBreakpoint || isExecution) ? 255 : 0;
				int blue = isExecution || !(hasBreakpoint || isExecution) ? 255
						: 0;
				g.setColor(new Color(red, green, blue));
				g.drawLine(0, 0, env.getSourceManager().getSourceTool()
						.getPreferredSize().width, 0);
			}

			public void setExecution(boolean b) {
				isExecution = b;
			}

			public void setBreakpoint(boolean b) {
				hasBreakpoint = b;
			}

		}

	}

	private class STMouseListener extends MouseAdapter implements MouseListener {
		@Override
		public void mousePressed(MouseEvent e) {
			if (e.isPopupTrigger()) {
				showPopupMenu((Component) e.getSource(), e.getX(), e.getY());
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (e.isPopupTrigger()) {
				showPopupMenu((Component) e.getSource(), e.getX(), e.getY());
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() >= 2) {
				setOrClearBreakPoint((Component) e.getSource());
			}
		}

		private void setOrClearBreakPoint(Component invoker) {
			JList list = (JList) invoker;
			int ln = list.getSelectedIndex() + 1;
			SourceModel.Line line = (SourceModel.Line) list.getSelectedValue();
			if (line != null) {
				if (line.isExecutable()) {
					// 実行中の場合
					String className = line.refType.name();
					if (line.hasBreakpoint()) {
						interpreter.executeCommand("clear " + className + ":"
								+ ln);
					} else {
						interpreter.executeCommand("stop at " + className + ":"
								+ ln);
					}
				}
			}
		}

		private void showPopupMenu(Component invoker, int x, int y) {
			JList list = (JList) invoker;
			int ln = list.getSelectedIndex() + 1;
			SourceModel.Line line = (SourceModel.Line) list.getSelectedValue();
			JPopupMenu popup = new JPopupMenu();

			if (line == null) {
				popup.add(new JMenuItem("please select a line"));
			} else if (line.isExecutable()) {
				String className = line.refType.name();
				if (line.hasBreakpoint()) {
					popup.add(commandItem("Clear Breakpoint", "clear "
							+ className + ":" + ln));
				} else {
					popup.add(commandItem("Set Breakpoint", "stop at "
							+ className + ":" + ln));
				}
			} else {
				popup.add(new JMenuItem("not an executable line"));
			}

			popup.show(invoker, x + popup.getWidth() / 2, y + popup.getHeight()
					/ 2);
		}

		private JMenuItem commandItem(String label, final String cmd) {
			JMenuItem item = new JMenuItem(label);
			item.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					interpreter.executeCommand(cmd);
				}
			});
			return item;
		}
	}
}