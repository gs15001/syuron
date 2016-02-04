package debugger.gui;

import java.util.*;
import com.sun.jdi.*;
import org.jdesktop.swingx.treetable.*;

public class VariableTreeTableModel extends AbstractTreeTableModel {

	private String[] columnNames = { "変数名", "値", "型", "宣言元" };
	private MyTreeTableNode myroot;
	private MyTreeTableNode preMyroot;
	private Environment env;

	public VariableTreeTableModel(Environment env) {
		super();
		this.env = env;
		myroot = new MyTreeTableNode("", "", "", "");
	}

	public void clear() {
		deleteChangedTag(myroot.getChildren());
		preMyroot = myroot;
		myroot = new MyTreeTableNode("", "", "", "");
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
				children.add(createNode(childName, valList.get(i), childType, varDec));
			}

		} else if(value instanceof ObjectReference) {
			ObjectReference or = (ObjectReference) value;
			ClassType ct = (ClassType) or.type();
			List<Field> fields = ct.fields();
			children = new ArrayList<>();

			for (Field f : fields) {
				Value v = or.getValue(f);
				children.add(createNode(f.name(), v, f.typeName(), varDec));
			}

		}

		// ノードの作成及び追加
		MyTreeTableNode node = new MyTreeTableNode(varName, varValue, varType, varDec);
		// 子ノードのリストがあれば追加
		if(children != null) {
			node.setChildren(children);
		}
		((MyTreeTableNode) myroot).getChildren().add(node);

		addChangedTag(preMyroot.getChildren(), myroot.getChildren());
		// モデルの更新
		modelSupport.fireNewRoot();

	}

	private MyTreeTableNode createNode(String varName, Value value, String varType, String varDec) {

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
				children.add(createNode(childName, vals.get(i), childType, varDec));
			}

		} else if(value instanceof ObjectReference) {
			ObjectReference or = (ObjectReference) value;
			ClassType ct = (ClassType) or.type();
			List<Field> fields = ct.fields();
			children = new ArrayList<>();

			// フィールド1つ1つに対して子ノードを作成
			for (Field f : fields) {
				Value v = or.getValue(f);
				children.add(createNode(f.name(), v, f.typeName(), varDec));
			}
		}

		// ノードの作成及び追加
		MyTreeTableNode node = new MyTreeTableNode(varName, varValue, varType, varDec);
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
		int argNum = 0;
		for (int i = 0; i < f.getArgumentValues().size(); i++) {
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
			if(argNum > 0) {
				buf.append(", ");
			}
		}
		buf.append(")");
		return buf.toString();
	}

	private void addChangedTag(List<MyTreeTableNode> preNodes, List<MyTreeTableNode> nodes) {

		for (MyTreeTableNode preNode : preNodes) {
			for (MyTreeTableNode node : nodes) {
				List<String> preVars = preNode.getVarList();
				List<String> vars = node.getVarList();

				if(vars.get(0).equals(preVars.get(0)) && vars.get(2).equals(preVars.get(2))
						&& vars.get(3).equals(preVars.get(3))) {
					if(isLeaf(node)) {
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

		public MyTreeTableNode(String varName, String varValue, String varType, String varDec) {
			varList.add(varName);
			varList.add(varValue);
			varList.add(varType);
			varList.add(varDec);
		}

		// 以下アクセスメソッド
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
