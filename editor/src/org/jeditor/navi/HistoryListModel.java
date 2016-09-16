package org.jeditor.navi;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class HistoryListModel extends DefaultTableModel {

	private static final long serialVersionUID = 1L;
	private static String[] columnNames = { "index", "title", "selected", "data3" };

	private List<AbstractNaviPane> historyData;
	private int historyIndex = 0;

	private HistoryList historyList;

	public HistoryListModel() {
		super(columnNames, 0);
		historyData = new ArrayList<>();
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

	public AbstractNaviPane getData(int i) {
		return historyData.get(i);
	}

	public void clear() {
		historyData.clear();
		historyIndex = 0;
		// テーブルデータ全削除
		setRowCount(0);
	}

	private void removeRange(int from, int to) {
		for (int i = from; i < to; i++) {
			historyData.remove(from);
			removeRow(from);
		}
	}

	private void dataToTableData(AbstractNaviPane e) {
		Object[] data = new Object[4];
		data[0] = e.getIndex();
		data[1] = e.getIndexLabel();
		data[2] = null;
		data[3] = null;

		addRow(data);
	}

	public void add(AbstractNaviPane e) {
		// 挿入の場合は挿入以下を削除
		if(historyIndex < getRowCount()) {
			removeRange(historyIndex, getRowCount());
		}
		historyData.add(historyIndex, e);
		historyIndex++;
		dataToTableData(e);
	}

	public AbstractNaviPane getPre() {
		// historyindex == 1のときはタイトル画面
		if(historyIndex > 1) {
			AbstractNaviPane pane = historyData.get(historyIndex - 2);
			historyList.selectIndex(historyIndex - 2);
			historyIndex--;
			return pane;
		}
		return null;
	}

}
