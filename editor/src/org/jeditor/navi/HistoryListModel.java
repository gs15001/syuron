package org.jeditor.navi;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import org.jeditor.navidata.AbstractNaviPane;

public class HistoryListModel extends DefaultTableModel {

	private static final long serialVersionUID = 1L;
	static String[] columnNames = { "タイトル", "選択肢", "入力", "自信" };

	private List<HistoryData> historyDatas;
	private int historyIndex = 0;

	private HistoryList historyList;

	public HistoryListModel() {
		super(columnNames, 0);
		historyDatas = new ArrayList<>();
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

	public HistoryData getData(int i) {
		return historyDatas.get(i);
	}

	public void clear() {
		historyDatas.clear();
		historyIndex = 0;
		// テーブルデータ全削除
		setRowCount(0);
	}

	private void removeRange(int from, int to) {
		for (int i = from; i < to; i++) {
			historyDatas.remove(from);
			removeRow(from);
		}
	}

	public void add(AbstractNaviPane e) {
		// 挿入の場合は挿入以下を削除
		if(historyIndex < getRowCount()) {
			removeRange(historyIndex, getRowCount());
		}
		HistoryData d = e.createHistoryData();
		historyDatas.add(historyIndex, d);
		historyIndex++;
		addRow(d.getTableData());
	}

	public HistoryData getPre() {
		// historyindex == 1のときはタイトル画面
		if(historyIndex > 1) {
			HistoryData d = historyDatas.get(historyIndex - 2);
			historyList.selectIndex(historyIndex - 2);
			return d;
		}
		return null;
	}

	public void updateCurrentItem(AbstractNaviPane e) {
		HistoryData d = historyDatas.get(historyIndex - 1);
		e.updateHistoryData(d);
		Object[] datas = d.getTableData();
		setValueAt(datas[1], historyIndex - 1, 1);
		setValueAt(datas[2], historyIndex - 1, 2);
		setValueAt(datas[3], historyIndex - 1, 3);
	}

	public void updateNotice(int v, int startLine, int startOffset) {
		for (int i = 0; i < historyDatas.size(); i++) {
			HistoryData d = historyDatas.get(i);
			d.updateNotice(v, startLine, startOffset);
			Object[] datas = d.getTableData();
			setValueAt(datas[1], i, 1);
		}
	}
}
