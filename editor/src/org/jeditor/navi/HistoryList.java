package org.jeditor.navi;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.metal.MetalBorders;
import javax.swing.table.DefaultTableCellRenderer;
import org.jeditor.app.JAppEditor;

public class HistoryList extends JPanel {

	private static final long serialVersionUID = 1L;

	private NaviManager naviManager;
	private JTable table;
	private JScrollPane listView;
	private HistoryListModel historyModel;

	public HistoryList(NaviManager naviManager) {
		super(new BorderLayout());

		this.naviManager = naviManager;
		JAppEditor parent = naviManager.getParent();

		historyModel = naviManager.getHistoryListModel();
		historyModel.setHistoryList(this);
		historyModel.addTableModelListener(new HistoryChangeListener());
		table = new JTable(historyModel);
		table.setDefaultRenderer(Object.class, new HistoryRenderer());
		table.setFont(new Font("メイリオ", Font.PLAIN, 12));
		table.setDefaultEditor(Object.class, null);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// 列幅調整
		table.getColumn(HistoryListModel.columnNames[0]).setPreferredWidth((int) (parent.RIGHT_WIDTH * 0.5));
		table.getColumn(HistoryListModel.columnNames[1]).setPreferredWidth((int) (parent.RIGHT_WIDTH * 0.15));
		table.getColumn(HistoryListModel.columnNames[2]).setPreferredWidth((int) (parent.RIGHT_WIDTH * 0.15));
		table.getColumn(HistoryListModel.columnNames[3]).setPreferredWidth((int) (parent.RIGHT_WIDTH * 0.15));
		table.getColumn(HistoryListModel.columnNames[4]).setPreferredWidth((int) (parent.RIGHT_WIDTH * 0.05));

		// テーブルヘッダーの余白調整のおまじない
		UIManager.put("TableHeader.cellBorder", new MetalBorders.TableHeaderBorder() {

			private static final long serialVersionUID = 1L;

			@Override
			public Insets getBorderInsets(Component c, Insets insets) {
				insets.left = insets.top = insets.right = insets.bottom = 0;
				return insets;
			}
		});
		SwingUtilities.updateComponentTreeUI(this);

		listView = new JScrollPane(table);
		add(listView);

		HistorySelectListener listener = new HistorySelectListener();
		table.getSelectionModel().addListSelectionListener(listener);

		table.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2) {
					Point pt = e.getPoint();
					int row = table.rowAtPoint(pt);
					naviManager.moveNavi(historyModel.getData(row).getIndex(), row);
				}
			}
		});
	}

	public void selectIndex(int index) {
		table.setRowSelectionInterval(index, index);
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
			int index = table.getSelectedRow();
			if(index != -1) {
			}
		}
	}

	private class HistoryChangeListener implements TableModelListener {

		@Override
		public void tableChanged(TableModelEvent e) {
			if(historyModel.getHistoryIndex() > 0) {
				refreshScroll();
				table.setRowSelectionInterval(historyModel.getHistoryIndex() - 1, historyModel.getHistoryIndex() - 1);
				table.updateUI();
			}
		}

	}

	private class HistoryRenderer extends DefaultTableCellRenderer {

		private static final long serialVersionUID = 1L;

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

			if(value != null) {
				this.setText((String) value);
			}
			this.setFont(table.getFont());
			return this;
		}
	}
}
