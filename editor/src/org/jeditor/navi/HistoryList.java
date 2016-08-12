package org.jeditor.navi;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class HistoryList extends JPanel {

	private static final long serialVersionUID = 1L;

	private JList<AbstractNaviPane> list;

	public HistoryList() {
		super();

		list = new JList<>();
		JScrollPane listView = new JScrollPane(list);
		add(listView);
	}
}
