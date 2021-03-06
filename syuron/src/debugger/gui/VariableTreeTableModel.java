package debugger.gui;

import java.util.*;
import com.sun.jdi.*;
import org.jdesktop.swingx.treetable.*;

public class VariableTreeTableModel extends AbstractTreeTableModel {

	private String[] columnNames = { "変数名", "値", "型", "宣言元" };
	private MyTreeTableNode myroot;
	private MyTreeTableNode preMyroot;
	private Environment env;
	private StackFrame currentFrame;

	public VariableTreeTableModel(Environment env) {
		super();
		this.env = env;
		myroot = new MyTreeTableNode("", "", "", "", null);
	}

	public void clear() {
		deleteChangedTag(myroot.getChildren());
		preMyroot = myroot;
		myroot = new MyTreeTableNode("", "", "", "", null);
	}

	public void addNode(LocalVariable var, StackFrame frame) throws ClassNotLoadedException {

		String varName;
		String varValue = "";
		String varType;
		String varDec;
		List<MyTreeTableNode> children = null;
		Value value = frame.getValue(var);

		// 変数名
		varName = var.name();
		// 型
		varType = var.type().name();
		if(varType.startsWith("java.lang.String")) {
			varType = var.type().name().substring(varType.lastIndexOf(".") + 1);
		}
		// 宣言元
		varDec = frameToMethodName(frame);
		// 値
		if(value instanceof StringReference || value instanceof PrimitiveValue) {
			varValue = value.toString();
		} else if(value instanceof ArrayReference) {
			ArrayReference ar = (ArrayReference) value;
			// 配列なら変数名を変更
			varName += varType.substring(varType.indexOf("["), varType.lastIndexOf("]") + 1);

			// 子ノードのリストを作成
			children = new ArrayList<>();
			List<Value> valList = ar.getValues();
			for (int i = 0; i < valList.size(); i++) {
				// 子ノードの変数名生成
				StringBuilder sb = new StringBuilder(varName);
				String childName = sb.insert(sb.indexOf("[]") + 1, i).toString();
				// 子ノードの型生成
				sb = new StringBuilder(varType);
				String childType = sb.delete(sb.length() - 2, sb.length()).toString();
				children.add(createNode(childName, valList.get(i), childType, varDec, frame));
			}

		} else if(value instanceof ObjectReference) {
			ObjectReference or = (ObjectReference) value;
			ClassType ct = (ClassType) or.type();
			List<Field> fields = ct.fields();
			children = new ArrayList<>();

			for (Field f : fields) {
				Value v = or.getValue(f);
				children.add(createNode(f.name(), v, f.typeName(), varDec, frame));
			}

		}

		// ノードの作成及び追加
		MyTreeTableNode node = new MyTreeTableNode(varName, varValue, varType, varDec, frame);
		// 子ノードのリストがあれば追加
		if(children != null) {
			node.setChildren(children);
		}
		((MyTreeTableNode) myroot).getChildren().add(node);

		// モデルの更新
		modelSupport.fireNewRoot();

	}

	private MyTreeTableNode createNode(String varName, Value value, String varType, String varDec, StackFrame frame) {

		String varValue = "";
		List<MyTreeTableNode> children = null;

		if(value instanceof StringReference || value instanceof PrimitiveValue) {
			varValue = value.toString();
		} else if(value instanceof ArrayReference) {
			ArrayReference ar = (ArrayReference) value;
			children = new ArrayList<>();
			List<Value> vals = ar.getValues();
			for (int i = 0; i < vals.size(); i++) {
				// 子ノードの変数名生成
				StringBuilder sb = new StringBuilder(varName);
				String childName = sb.insert(sb.indexOf("[]") + 1, i).toString();
				// 子ノードの型生成
				sb = new StringBuilder(varType);
				String childType = sb.delete(sb.length() - 2, sb.length()).toString();
				children.add(createNode(childName, vals.get(i), childType, varDec, frame));
			}

		} else if(value instanceof ObjectReference) {
			ObjectReference or = (ObjectReference) value;
			ClassType ct = (ClassType) or.type();
			List<Field> fields = ct.fields();
			children = new ArrayList<>();

			// フィールド1つ1つに対して子ノードを作成
			for (Field f : fields) {
				Value v = or.getValue(f);
				children.add(createNode(f.name(), v, f.typeName(), varDec, frame));
			}
		}

		// ノードの作成及び追加
		MyTreeTableNode node = new MyTreeTableNode(varName, varValue, varType, varDec, frame);
		// 子ノードのリストがあれば追加
		if(children != null) {
			node.setChildren(children);
		}
		return node;
	}

	public String frameToMethodName(StackFrame f) {
		Method method = f.location().method();
		StringBuilder buf = new StringBuilder();
		buf.append(method.name());
		buf.append("(");
		int argNum = f.getArgumentValues().size();
		for (int i = 0; i < argNum; i++) {
			Value value = f.getArgumentValues().get(i);
			if(env.isPrintDecMode()) {
				if(value instanceof StringReference || value instanceof PrimitiveValue) {
					buf.append(value.toString());
				} else {
					try {
						buf.append(f.visibleVariables().get(i).name());
					} catch (AbsentInformationException e) {
						e.printStackTrace();
					}
				}
			} else {
				String s = value.type().name();
				s = s.substring(s.lastIndexOf(".") + 1);
				buf.append(s);
			}
			if(i + 1 < argNum) {
				buf.append(", ");
			}
		}
		buf.append(")");
		return buf.toString();
	}

	public void addChangedTag() {
		addChangedTag(preMyroot.getChildren(), myroot.getChildren());
	}

	private void addChangedTag(List<MyTreeTableNode> preNodes, List<MyTreeTableNode> nodes) {

		for (MyTreeTableNode node : nodes) {
			List<String> vars = node.getVarList();
			if(currentFrame == node.getFrame()) {
				for (MyTreeTableNode preNode : preNodes) {
					List<String> preVars = preNode.getVarList();

					// メソッドの呼び出し階層が同じかつ変数名・型が同じか
					if(node.frameC == preNode.frameC && vars.get(0).equals(preVars.get(0))
							&& vars.get(2).equals(preVars.get(2))) {
						if(isLeaf(node)) {
							// 既にある変数の値が変わっていたらタグ付け
							if(!preVars.get(1).equals(vars.get(1))) {
								String s = vars.get(1);
								vars.remove(1);
								s += "     ";
								vars.add(1, s);
							}
						} else {
							addChangedTag(preNode.getChildren(), node.getChildren());
						}
					}
				}
			}
		}
	}

	private void deleteChangedTag(List<MyTreeTableNode> nodes) {

		for (MyTreeTableNode node : nodes) {
			List<String> vars = node.getVarList();

			if(isLeaf(node)) {
				if(vars.get(1).endsWith("     ")) {
					String s = vars.get(1);
					vars.remove(1);
					s = s.substring(0, s.lastIndexOf("     "));
					vars.add(1, s);
				}
			} else {
				deleteChangedTag(node.getChildren());
			}
		}
	}

	public void setCurrentFrame(StackFrame currentFrame) {
		this.currentFrame = currentFrame;
	}

	@Override
	public String getColumnName(int column) {
		if(0 <= column && column < columnNames.length) {
			return columnNames[column];
		}
		return "unknown";
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(Object node, int column) {
		MyTreeTableNode treeNode = (MyTreeTableNode) node;
		return treeNode.getVarList().get(column);
	}

	@Override
	public Object getChild(Object parent, int index) {
		MyTreeTableNode treeNode = (MyTreeTableNode) parent;
		return treeNode.getChildren().get(index);
	}

	@Override
	public int getChildCount(Object parent) {
		MyTreeTableNode treeNode = (MyTreeTableNode) parent;
		return treeNode.getChildren().size();
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		MyTreeTableNode treeNode = (MyTreeTableNode) parent;
		for (int i = 0; i > treeNode.getChildren().size(); i++) {
			if(treeNode.getChildren().get(i) == child) {
				return i;
			}
		}
		return 0;
	}

	@Override
	public boolean isLeaf(Object node) {
		MyTreeTableNode treeNode = (MyTreeTableNode) node;
		if(treeNode.getChildren().size() > 0) {
			return false;
		}
		return true;
	}

	@Override
	public Object getRoot() {
		return myroot;
	}

	class MyTreeTableNode {

		private List<String> varList = new ArrayList<>();
		private List<MyTreeTableNode> children = new ArrayList<>();
		private StackFrame frame;
		private int frameC;

		public MyTreeTableNode(String varName, String varValue, String varType, String varDec, StackFrame frame) {
			varList.add(varName);
			varList.add(varValue);
			varList.add(varType);
			varList.add(varDec);
			this.frame = frame;
			// 現在のフレームの位置（呼び出し階層）を算出　メソッドの識別に使用
			try {
				frameC = -1;
				if(frame != null) {
					ThreadReference tr = frame.thread();
					for (int i = 0; i < tr.frameCount(); i++) {
						if(tr.frame(i) == frame) {
							frameC = tr.frameCount() - i;
						}
					}
				}
			} catch (IncompatibleThreadStateException e) {
				e.printStackTrace();
			}
		}

		// 以下アクセスメソッド
		public StackFrame getFrame() {
			return frame;
		}

		public List<String> getVarList() {
			return varList;
		}

		public List<MyTreeTableNode> getChildren() {
			return this.children;
		}

		public void setChildren(List<MyTreeTableNode> children) {
			this.children = children;
		}

		@Override
		public String toString() {
			return varList.get(0);
		}
	}
}
