package debugger.gui;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.tree.*;
import org.jdesktop.swingx.*;
import com.sun.jdi.*;
import debugger.bdi.ExecutionManager;

public class VariableTool extends JPanel {

	private static final long serialVersionUID = 1L;
	private Environment env;
	private ExecutionManager runtime;
	private ContextManager context;

	private MyJXTreeTable treeTable;
	private VariableTreeTableModel tableModel;
	private VariableTreeCellRenderer treeRenderer;
	private VariableTableCellRenderer tableRenderer;

	private String[] columnNames = { "変数名", "値", "型", "宣言元" };

	public VariableTool(Environment env) {
		super(new BorderLayout());
		this.env = env;
		this.runtime = env.getExecutionManager();
		this.context = env.getContextManager();
		env.setVariableTool(this);

		tableModel = new VariableTreeTableModel();
		treeTable = new MyJXTreeTable(tableModel);
		treeRenderer = new VariableTreeCellRenderer();
		tableRenderer = new VariableTableCellRenderer();
		treeTable.setDefaultRenderer(Object.class, tableRenderer);
		treeTable.setTreeCellRenderer(treeRenderer);

		JScrollPane listView = new JScrollPane(treeTable);
		add(listView);
	}

	public void clear() {
		tableModel.clear();
		treeTable.updateUI();
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
			treeTable.setCurrentVariableNum(tableModel.frameToMethodName(currentFrame));
			// 現在の命令の位置を取得(ライン表示に必要）
			env.getSourceManager().getSourceTool().setExcuteLine(currentFrame.location().lineNumber());

			// 既にある変数を一旦削除
			tableModel.clear();

			// 全てのstackframeから見えてるローカル変数を取得
			for (int i = frames.size() - 1; 0 <= i; i--) {
				StackFrame frame = frames.get(i);
				List<LocalVariable> vars = frame.visibleVariables();
				for (LocalVariable var : vars) {
					tableModel.addNode(var, frame);
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

	private class MyJXTreeTable extends JXTreeTable {

		private static final long serialVersionUID = 1L;

		private String currentVariableMethodName;

		public MyJXTreeTable(VariableTreeTableModel tableModel) {
			super(tableModel);
		}

		public void setCurrentVariableNum(String currentVariableMethodName) {
			this.currentVariableMethodName = currentVariableMethodName;
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			int variableNum = getRowCount();
			int currentVariableNum = 0;
			for (int i = 0; i < variableNum; i++) {
				if (((String) getValueAt(i, 3)).equals(currentVariableMethodName)) {
					currentVariableNum++;
				}
				g.setColor(new Color(100, 100, 100, 120));
				Rectangle r = getCellRect(i, 0, true);
				g.drawLine(0, r.y + r.height, getWidth(), r.y + r.height);
			}

			for (int i = 0; i < variableNum - currentVariableNum; i++) {
				g.setColor(new Color(100, 100, 100, 80));
				Rectangle r = getCellRect(i, 0, true);
				g.fillRect(0, r.y, getWidth(), r.height);
			}
		}
	}

	private class VariableTableCellRenderer extends DefaultTableCellRenderer {

		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {

			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

			return this;
		}

	}

	private class VariableTreeCellRenderer extends DefaultTreeCellRenderer {

		private static final long serialVersionUID = 1L;

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
				boolean leaf, int row, boolean hasFocus) {

			super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

			// アイコンを付ける場合
			// setClosedIcon(Icons.closeIcon);
			// setLeafIcon(Icons.openIcon);
			// setOpenIcon(Icons.openIcon);

			// アイコンを付けない場合
			setClosedIcon(null);
			setLeafIcon(null);
			setOpenIcon(null);

			return this;
		}

	}
}
