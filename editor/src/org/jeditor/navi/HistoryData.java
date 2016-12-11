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
		Object[] data = new Object[4];
		data[0] = indexLabel;
		data[1] = selected;
		data[2] = input;
		data[3] = confi;

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
			notices[0] = updatePartition2(notices[0], v, startLine, startOffset);
			notices[1] = updatePartition(notices[1], v, startLine, startOffset);
		} else if(index.matches("p[24].*")) {
			for (int i = 0; i < notices.length; i++) {
				notices[i] = updatePartition(notices[i], v, startLine, startOffset);
			}
		} else if(index.equals("p3")) {
			for (int i = 0; i < notices.length; i++) {
				if(i != 2) {
					if(i == 0 && !notices[2].equals("2")) {
						notices[i] = updatePartition2(notices[i], v, startLine, startOffset);
					} else {
						notices[i] = updatePartition(notices[i], v, startLine, startOffset);
					}
				}
			}
		} else if(index.matches("(a[017])|(b[13])|(r[12456])")) {
			for (int i = 1; i < notices.length; i++) {
				if(notices[i].indexOf(",") == -1) {
					notices[i] = updatePartition(notices[i], v, startLine, startOffset);
				} else {
					notices[i] = containComma(notices[i], v, startLine, startOffset);
				}
			}
		} else if(index.matches("(a[234568])|(b[24])|(r[3])")) {
			for (int i = 0; i < notices.length; i++) {
				if(notices[i].indexOf(",") == -1) {
					notices[i] = updatePartition(notices[i], v, startLine, startOffset);
				} else {
					notices[i] = containComma(notices[i], v, startLine, startOffset);
				}
			}
		}

		// 締め処理
		notice = "";
		for (int i = 0; i < notices.length - 1; i++) {
			notice += notices[i] + "-";
		}
		notice += notices[notices.length - 1];

	}

	public String updatePartition(String s, int v, int startLine, int startOffset) {
		try {
			int old = Integer.parseInt(s);
			if(old > startLine + 1) {
				old += v;
			} else if(old == startLine + 1) {
				if(startOffset == 0) {
					old += v;
				}
			}
			return old + "";
		} catch (Exception e) {
			return s;
		}
	}

	public String updatePartition2(String s, int v, int startLine, int startOffset) {
		try {
			int old = Integer.parseInt(s) - 1;
			if(old > startLine + 1) {
				old += v;
			} else if(old == startLine + 1) {
				if(startOffset == 0) {
					old += v;
				}
			}
			return (old + 1) + "";
		} catch (Exception e) {
			return s;
		}
	}

	public String containComma(String s, int v, int startLine, int startOffset) {
		String[] notices2 = s.split(",");
		for (int j = 0; j < notices2.length; j++) {
			notices2[j] = updatePartition(notices2[j], v, startLine, startOffset);
		}
		s = "";
		for (int j = 0; j < notices2.length - 1; j++) {
			s += notices2[j] + ",";
		}
		s += notices2[notices2.length - 1];
		return s;
	}
}
