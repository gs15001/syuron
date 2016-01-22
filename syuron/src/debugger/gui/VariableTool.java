package debugger.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.jdesktop.swingx.*;
import org.jdesktop.swingx.treetable.*;

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.ClassNotLoadedException;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.LocalVariable;
import com.sun.jdi.StackFrame;
import com.sun.jdi.ThreadReference;

import debugger.bdi.ExecutionManager;

public class VariableTool extends JPanel {

	private static final long serialVersionUID = 1L;
	private Environment env;
	private ExecutionManager runtime;
	private ContextManager context;

	private JXTreeTable treeTable;
	private VariableTreeTableModel tableModel;

	private String[] columnNames = { "変数名", "値", "型", "宣言元" };

	public VariableTool(Environment env) {
		super(new BorderLayout());
		this.env = env;
		this.runtime = env.getExecutionManager();
		this.context = env.getContextManager();
		env.setVariableTool(this);

		tableModel = new VariableTreeTableModel();
		treeTable = new JXTreeTable(tableModel);
		// treeTable.setTreeCellRenderer(new VariableTreeCellRenderer());

		JScrollPane listView = new JScrollPane(treeTable);
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
			List<StackFrame> frames = current.frames();
			// stackframeの先頭（現在実行されているメソッドに相当)を取得
			StackFrame Currentframe = frames.get(0);
			// 現在の命令の位置を取得(ライン表示に必要）
			env.getSourceManager().getSourceTool()
					.setExcuteLine(Currentframe.location().lineNumber());

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

	private class VariableTreeCellRenderer extends DefaultTreeCellRenderer {

		private static final long serialVersionUID = 1L;

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean sel, boolean expanded, boolean leaf, int row,
				boolean hasFocus) {

			super.getTreeCellRendererComponent(tree, value, sel, expanded,
					leaf, row, hasFocus);
			return this;
		}
	}
}
