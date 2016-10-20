package org.jeditor.navi;

public class HistoryData {

	private String index;
	private String indexLabel;
	private String notice;
	private String selected;
	private String input;
	private String confi;

	public HistoryData(String index, String indexLabel, String notice) {
		this.index = index;
		this.indexLabel = indexLabel;
		this.notice = notice;
		this.selected = "";
		this.input = "";
		this.confi = "";
	}

	public Object[] getTableData() {
		Object[] data = new Object[5];
		data[0] = indexLabel;
		data[1] = notice;
		data[2] = selected;
		data[3] = input;
		data[4] = confi;

		return data;
	}

	public void updateData(String selected, String input, String confi) {
		this.selected = selected;
		this.input = input;
		this.confi = confi;
	}

	public String getIndex() {
		return index;
	}

	public String getNotice() {
		return notice;
	}
}
