package org.jeditor.navi;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
		list.setFont(new Font("メイリオ", Font.PLAIN, 12));

		listView = new JScrollPane(list);
		add(listView);

		HistorySelectListener listener = new HistorySelectListener();
		list.addListSelectionListener(listener);

		list.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2) {
					int index = list.locationToIndex(e.getPoint());
					list.setSelectedIndex(index);
					AbstractNaviPane pane = list.getSelectedValue();
					naviManager.backNavi(pane.getIndex(), index);
				}
			}
		});
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
			list.clearSelection();
			list.setSelectedIndex(historyModel.getSize() - 1);
			list.updateUI();
		}

		@Override
		public void intervalRemoved(ListDataEvent e) {
			refreshScroll();
			list.clearSelection();
			list.setSelectedIndex(historyModel.getSize() - 1);
			list.updateUI();
		}

		@Override
		public void contentsChanged(ListDataEvent e) {
			refreshScroll();
			list.clearSelection();
			list.setSelectedIndex(historyModel.getSize() - 1);
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
			this.setFont(list.getFont());
			return this;
		}

	}
}
