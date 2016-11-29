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

	public void updateNotice(int v, int startLine, int startOffset) {
		if(index.matches("[^abpr].*")) {
			return;
		}
		String[] notices = notice.split("-");
		if(notices.length < 2) {
			return;
		}

		if(index.equals("p1")) {
			notices[0] = updatePartition(Integer.parseInt(notices[0]) - 1, v, startLine) + 1 + "";
			notices[1] = updatePartition(Integer.parseInt(notices[1]), v, startLine) + "";
		} else if(index.matches("p[24].*")) {
			for (int i = 0; i < notices.length; i++) {
				notices[i] = updatePartition(Integer.parseInt(notices[i]), v, startLine) + "";
			}
		} else if(index.equals("p3")) {
			for (int i = 0; i < notices.length; i++) {
				if(i != 2) {
					if(i == 0 && !notices[2].equals("2")) {
						notices[i] = updatePartition(Integer.parseInt(notices[i]) - 1, v, startLine) + 1 + "";
					} else {
						notices[i] = updatePartition(Integer.parseInt(notices[i]), v, startLine) + "";
					}
				}
			}
		} else if(index.matches("(a[017])|(b[13])|(r[12456])")) {
			for (int i = 1; i < notices.length; i++) {
				notices[i] = updatePartition(Integer.parseInt(notices[i]), v, startLine) + "";
			}
		} else if(index.matches("(a[23456])|(b[24])|(r[3])")) {
			for (int i = 0; i < notices.length; i++) {
				notices[i] = updatePartition(Integer.parseInt(notices[i]), v, startLine) + "";
			}
		}

		// 締め処理
		notice = "";
		for (int i = 0; i < notices.length - 1; i++) {
			notice += notices[i] + "-";
		}
		notice += notices[notices.length - 1];

	}

	public int updatePartition(int old, int v, int startLine) {
		if(old > startLine) {
			return old + v;
		}
		return old;
	}
}
