package debugger.gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.tree.*;
import org.jdesktop.swingx.*;
import com.sun.jdi.*;
import debugger.bdi.ExecutionManager;
import debugger.gui.VariableTreeTableModel.MyTreeTableNode;

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

		tableModel = new VariableTreeTableModel(env);
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
		List<MyTreeTableNode> nodes = treeTable.checkExpand();
		refreshTable();
		treeTable.reflectExpand(nodes);
		treeTable.updateUI();
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
			List<StackFrame> frames = current.frames();
			// stackframeの先頭（現在実行されているメソッドに相当)を取得
			StackFrame currentFrame = frames.get(0);
			treeTable.setCurrentFrame(currentFrame);
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

	public void setPreFrame(ThreadReference pre) throws IncompatibleThreadStateException {
		if(pre != null) {
			tableModel.setPreFrame(pre.frame(0));
		} else {
			tableModel.setPreFrame(null);
		}
	}

	private class MyJXTreeTable extends JXTreeTable {

		private static final long serialVersionUID = 1L;

		private StackFrame currentFrame;

		public MyJXTreeTable(VariableTreeTableModel tableModel) {
			super(tableModel);
		}

		public void setCurrentFrame(StackFrame currentFrame) {
			this.currentFrame = currentFrame;
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			for (int i = 0; i < getRowCount(); i++) {
				// 罫線を引く
				g.setColor(new Color(100, 100, 100, 120));
				Rectangle r = getCellRect(i, 0, true);
				g.drawLine(0, r.y + r.height, getWidth(), r.y + r.height);
			}

			paintYellowRect(g);
			paintGrayRect(g);
		}

		private void paintYellowRect(Graphics g) {
			for (int i = 0; i < getRowCount(); i++) {
				for (int j = 0; j < getColumnCount(); j++) {
					String s = getStringAt(i, j);
					if(s.endsWith("     ")) {
						g.setColor(new Color(255, 255, 0, 100));
						Rectangle r = getCellRect(i, j, true);
						g.fillRect(r.x, r.y + 1, r.width, r.height);
					}
				}
			}
		}

		private void paintGrayRect(Graphics g) {
			int variableNum = getRowCount();
			int currentVariableNum = 0;

			// 現在のスコープの変数の数を数える
			for (int i = 0; i < variableNum; i++) {
				TreePath tp = getPathForRow(i);
				MyTreeTableNode node = (MyTreeTableNode) tp.getPathComponent(tp.getPathCount() - 1);
				if(node.getFrame() == currentFrame) {
					currentVariableNum++;
				}
			}

			// スコープ外の変数を灰色にする
			for (int i = 0; i < variableNum - currentVariableNum; i++) {
				g.setColor(new Color(100, 100, 100, 80));
				Rectangle r = getCellRect(i, 0, true);
				g.fillRect(0, r.y, getWidth(), r.height);
			}
		}

		private List<MyTreeTableNode> checkExpand() {
			List<MyTreeTableNode> nodes = new ArrayList<>();
			for (int i = 0; i < getRowCount(); i++) {
				if(isExpanded(i)) {
					TreePath tp = getPathForRow(i);
					nodes.add((MyTreeTableNode) tp.getPathComponent(tp.getPathCount() - 1));
				}
			}
			return nodes;
		}

		private void reflectExpand(List<MyTreeTableNode> nodes) {
			for (MyTreeTableNode preNode : nodes) {
				for (int i = 0; i < getRowCount(); i++) {
					TreePath tp = getPathForRow(i);
					MyTreeTableNode node = (MyTreeTableNode) tp.getPathComponent(tp.getPathCount() - 1);

					List<String> preVars = preNode.getVarList();
					List<String> vars = node.getVarList();
					if(vars.get(0).equals(preVars.get(0)) && vars.get(2).equals(preVars.get(2))
							&& vars.get(3).equals(preVars.get(3))) {
						expandPath(tp);
					}
				}
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
