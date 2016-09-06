package org.jeditor.navi;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class HistoryList extends JPanel {

	private static final long serialVersionUID = 1L;

	private NaviManager naviManager;
	private JList<AbstractNaviPane> list;
	private JScrollPane listView;
	private ListModel<AbstractNaviPane> historyModel;

	public HistoryList(NaviManager naviManager) {
		super(new BorderLayout());

		this.naviManager = naviManager;

		historyModel = naviManager.getHistoryListModel();
		historyModel.addListDataListener(new HistoryChangeListener());
		list = new JList<>(historyModel);
		list.setCellRenderer(new HistoryRenderer());

		listView = new JScrollPane(list);
		add(listView);

		HistorySelectListener listener = new HistorySelectListener();
		list.addListSelectionListener(listener);
	}
	
	private void refreshScroll() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				JScrollBar bar = listView.getVerticalScrollBar();
				bar.setValue(bar.getMaximum());

			}
		});
	}

	private class HistorySelectListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			int index = list.getSelectedIndex();
			if(index != -1) {
			}
		}
	}

	private class HistoryChangeListener implements ListDataListener {

		@Override
		public void intervalAdded(ListDataEvent e) {
			refreshScroll();
			list.updateUI();
		}

		@Override
		public void intervalRemoved(ListDataEvent e) {
			refreshScroll();
			list.updateUI();
		}

		@Override
		public void contentsChanged(ListDataEvent e) {
			refreshScroll();
			list.updateUI();
		}

	}

	private class HistoryRenderer extends DefaultListCellRenderer {

		private static final long serialVersionUID = 1L;

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

			AbstractNaviPane pane = (AbstractNaviPane) value;
			this.setText(pane.getIndex());

			return this;
		}

	}
}
