package debugger.gui;

import java.util.*;
import java.util.List;
import java.awt.*;

import javax.swing.*;
import javax.swing.table.*;

import com.sun.jdi.*;

import debugger.bdi.ExecutionManager;

public class VariableTool extends JPanel {

	private static final long serialVersionUID = 1L;
	private Environment env;
	private ExecutionManager runtime;
	private ContextManager context;

	private JTable table;
	private DefaultTableModel tableModel;

	private String[] columnNames = { "変数名", "値", "型", "宣言元" };

	public VariableTool(Environment env) {
		super(new BorderLayout());
		this.env = env;
		this.runtime = env.getExecutionManager();
		this.context = env.getContextManager();
		env.setVariableTool(this);

		tableModel = new DefaultTableModel(columnNames, 0);
		table = new JTable(tableModel);
		table.setDefaultRenderer(String.class, new VariableCellRenderer());

		JScrollPane listView = new JScrollPane(table);
		add(listView);
	}

	public void update() {
		refreshTable();
	}

	private void refreshTable() {
		try {
			// 現在のスレッドを取得 基本mainスレッド
			ThreadReference current = context.getCurrentThread();
			if (current == null) {
				env.failure("No thread");
				return;
			}
			if (current.frameCount() <= 0) {
				env.failure("Threads have not yet created any stack frames.");
				return;
			}
			// 現在のスレッドのstackframeのリストを取得
			LinkedList<StackFrame> frames = new LinkedList<StackFrame>(
					current.frames());
			// stackframeの先頭（現在実行されているメソッドに相当)を取得
			StackFrame Currentframe = frames.getFirst();
			// 現在の命令の位置を取得(ライン表示に必要）
			env.getSourceManager().getSourceTool()
					.setExcuteLine(Currentframe.location().lineNumber());
			// 全てのstackframeから見えてるローカル変数を取得
			for (StackFrame frame : frames) {
				List<LocalVariable> vars = frame.visibleVariables();
				for (LocalVariable var : vars) {
					Value v = frame.getValue(var);
					if (v instanceof IntegerValue) {
						IntegerValue iv = (IntegerValue) v;
						Object data[] = { var.name(), iv.value(),
								var.type().name(), frame.toString() };
						tableModel.addRow(data);
					}

				}
			}

		} catch (IncompatibleThreadStateException e1) {
			e1.printStackTrace();
		} catch (AbsentInformationException e1) {
			e1.printStackTrace();
		} catch (ClassNotLoadedException e) {
			e.printStackTrace();
		}
	}

	private class VariableCellRenderer extends DefaultTableCellRenderer {

		private static final long serialVersionUID = 1L;

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			super.getTableCellRendererComponent(table, value, isSelected,
					hasFocus, row, column);

			return this;
		}

	}
}
