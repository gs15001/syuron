package debugger.gui;

import java.util.*;
import java.util.List;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import com.sun.jdi.*;
import debugger.bdi.ExecutionManager;

public class VariableTool2 extends JPanel {

	private static final long serialVersionUID = 1L;
	private Environment env;
	private ExecutionManager runtime;
	private ContextManager context;

	private JTable table;
	private DefaultTableModel tableModel;

	private List<Object[]> beforeVariables = new ArrayList<Object[]>();
	private List<Object[]> currentVariables = new ArrayList<Object[]>();

	private String[] columnNames = { "変数名", "値", "型", "宣言元" };

	public VariableTool2(Environment env) {
		super(new BorderLayout());
		this.env = env;
		this.runtime = env.getExecutionManager();
		this.context = env.getContextManager();
		// env.setVariableTool(this);

		tableModel = new DefaultTableModel(columnNames, 0);
		table = new JTable(tableModel);
		table.setDefaultRenderer(String.class, new VariableCellRenderer());

		JScrollPane listView = new JScrollPane(table);
		add(listView);
	}

	public void update() {
		tableModel.setRowCount(0);
		beforeVariables = currentVariables;
		currentVariables = new ArrayList<Object[]>();
		refreshTable();
	}

	private void refreshTable() {
		try {
			// 現在のスレッドを取得 基本mainスレッド
			ThreadReference current = context.getCurrentThread();
			if(current == null) {
				env.failure("No thread");
				return;
			}
			if(current.frameCount() <= 0) {
				env.failure("Threads have not yet created any stack frames.");
				return;
			}
			// 現在のスレッドのstackframeのリストを取得
			LinkedList<StackFrame> frames = new LinkedList<StackFrame>(current.frames());
			// stackframeの先頭（現在実行されているメソッドに相当)を取得
			StackFrame Currentframe = frames.getFirst();
			// 現在の命令の位置を取得(ライン表示に必要）
			env.getSourceManager().getSourceTool().setExcuteLine(Currentframe.location().lineNumber());

			// 全てのstackframeから見えてるローカル変数を取得
			for (StackFrame frame : frames) {
				List<LocalVariable> vars = frame.visibleVariables();
				for (LocalVariable var : vars) {
					Object[] data = valueToTableData(var, frame);
					tableModel.addRow(data);
					currentVariables.add(data);
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

	private Object[] valueToTableData(LocalVariable var, StackFrame frame) throws ClassNotLoadedException {
		Value v = frame.getValue(var);
		Object[] data = new Object[4];
		if(v instanceof StringReference || v instanceof PrimitiveValue) {
			data[0] = var.name();
			data[1] = v.toString();
			data[2] = var.type().name();
			if(data[2].equals("java.lang.String")) {
				data[2] = "String";
			}
			data[3] = frameToMethodName(frame);
		} else if(v instanceof ArrayReference) {
			ArrayReference ar = (ArrayReference) v;
		} else if(v instanceof ObjectReference) {
			ObjectReference or = (ObjectReference) v;
		}
		return data;
	}

	private String frameToMethodName(StackFrame f) {
		Method method = f.location().method();
		StringBuffer buf = new StringBuffer();
		buf.append(method.name());
		buf.append("(");
		int argNum = 0;
		for (Value value : f.getArgumentValues()) {
			String s = value.type().name();
			s = s.substring(s.lastIndexOf(".") + 1);
			buf.append(s);
			if(argNum > 0) {
				buf.append(", ");
			}
		}
		buf.append(")");
		return buf.toString();
	}

	private class VariableCellRenderer extends DefaultTableCellRenderer {

		private static final long serialVersionUID = 1L;

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

			return this;
		}

	}
}
