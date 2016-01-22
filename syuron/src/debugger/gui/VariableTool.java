package debugger.gui;

import java.awt.*;
import java.util.List;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.tree.*;

import org.jdesktop.swingx.*;
import org.jdesktop.swingx.decorator.ColorHighlighter;

import com.sun.jdi.*;

import debugger.bdi.ExecutionManager;

public class VariableTool extends JPanel {

	private static final long serialVersionUID = 1L;
	private Environment env;
	private ExecutionManager runtime;
	private ContextManager context;

	private JXTreeTable treeTable;
	private VariableTreeTableModel tableModel;
	private VariableTreeCellRenderer treeRenderer;
	private VariableTableCellRenderer tableRenderer;
	// private CustomRenderer renderer;

	private String[] columnNames = { "変数名", "値", "型", "宣言元" };

	public VariableTool(Environment env) {
		super(new BorderLayout());
		this.env = env;
		this.runtime = env.getExecutionManager();
		this.context = env.getContextManager();
		env.setVariableTool(this);

		tableModel = new VariableTreeTableModel();
		treeTable = new JXTreeTable(tableModel);
		treeRenderer = new VariableTreeCellRenderer();
		tableRenderer = new VariableTableCellRenderer();
		treeTable.setDefaultRenderer(Object.class, tableRenderer);
		treeTable.setTreeCellRenderer(treeRenderer);

		JScrollPane listView = new JScrollPane(treeTable);
		add(listView);
	}

	public void update() {
		refreshTable();
		treeTable.updateUI();
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
			List<StackFrame> frames = current.frames();
			// stackframeの先頭（現在実行されているメソッドに相当)を取得
			StackFrame currentFrame = frames.get(0);
			treeRenderer.setCurrentVariableNum(currentFrame.visibleVariables()
					.size());
			tableRenderer.setCurrentVariableNum(currentFrame.visibleVariables()
					.size());
			// 現在の命令の位置を取得(ライン表示に必要）
			env.getSourceManager().getSourceTool()
					.setExcuteLine(currentFrame.location().lineNumber());

			// 既にある変数を一旦削除
			tableModel.clear();

			// 全てのstackframeから見えてるローカル変数を取得
			int variableNum = 0;
			for (int i = frames.size() - 1; 0 <= i; i--) {
				StackFrame frame = frames.get(i);
				List<LocalVariable> vars = frame.visibleVariables();
				for (LocalVariable var : vars) {
					tableModel.addNode(var, frame);
					variableNum++;
				}
			}
			treeRenderer.setVariableNum(variableNum);
			tableRenderer.setVariableNum(variableNum);

		} catch (IncompatibleThreadStateException e1) {
			e1.printStackTrace();
		} catch (AbsentInformationException e1) {
			e1.printStackTrace();
		} catch (ClassNotLoadedException e) {
			e.printStackTrace();
		}
	}

	private class VariableTableCellRenderer extends DefaultTableCellRenderer {

		private static final long serialVersionUID = 1L;
		private int currentVariableNum;
		private int variableNum;

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {

			super.getTableCellRendererComponent(table, value, isSelected,
					hasFocus, row, column);

			// if (row < variableNum - currentVariableNum) {
			// setBackground(Color.LIGHT_GRAY);
			// }

			return this;
		}

		public void setCurrentVariableNum(int num) {
			currentVariableNum = num;
		}

		public void setVariableNum(int num) {
			variableNum = num;
		}
	}

	private class VariableTreeCellRenderer extends DefaultTreeCellRenderer {

		private static final long serialVersionUID = 1L;
		private int currentVariableNum;
		private int variableNum;

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean sel, boolean expanded, boolean leaf, int row,
				boolean hasFocus) {

			super.getTreeCellRendererComponent(tree, value, sel, expanded,
					leaf, row, hasFocus);
			setClosedIcon(null);
			setLeafIcon(null);
			setOpenIcon(null);
			// if (row < variableNum - currentVariableNum) {
			// // setBackground(Color.LIGHT_GRAY);
			// setBackgroundNonSelectionColor(Color.LIGHT_GRAY);
			// }

			return this;
		}

		public void setCurrentVariableNum(int num) {
			currentVariableNum = num;
		}

		public void setVariableNum(int num) {
			variableNum = num;
		}
	}

	private class CustomRenderer extends JLabel implements TreeCellRenderer,
			TableCellRenderer {

		private static final long serialVersionUID = 1L;
		private int currentVariableNum;
		private int variableNum;

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean selected, boolean expanded, boolean leaf, int row,
				boolean hasFocus) {

			if (row < variableNum - currentVariableNum) {
				setBackground(Color.LIGHT_GRAY);
			}

			return this;
		}

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {

			setText(value != null ? value.toString() : "<null>");
			if (row < variableNum - currentVariableNum) {
				setBackground(Color.LIGHT_GRAY);
			}

			return this;
		}

		public void setCurrentVariableNum(int num) {
			currentVariableNum = num;
		}

		public void setVariableNum(int num) {
			variableNum = num;
		}
	}
}
