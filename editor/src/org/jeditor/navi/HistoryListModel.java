package org.jeditor.navi;

import javax.swing.DefaultListModel;

public class HistoryListModel<T> extends DefaultListModel<T> {

	private static final long serialVersionUID = 1L;
	private int historyIndex = 0;

	private HistoryList historyList;

	public HistoryListModel() {
		super();
	}

	public void setHistoryList(HistoryList historyList) {
		this.historyList = historyList;
	}

	public int getHistoryIndex() {
		return historyIndex;
	}

	public void setHistoryIndex(int historyIndex) {
		this.historyIndex = historyIndex;
	}

	@Override
	public void clear() {
		super.clear();
		historyIndex = 0;
	}

	public void add(T e) {
		// 挿入の場合は挿入以下を削除
		if(historyIndex < getSize()) {
			removeRange(historyIndex, getSize() - 1);
		}
		add(historyIndex, e);
		historyIndex++;
	}

	public T getPre() {
		if(historyIndex > 1) {
			T pane = get(historyIndex - 2);
			historyList.selectIndex(historyIndex - 2);
			historyIndex--;
			return pane;
		}
		return null;
	}

}
