/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navi;

import java.awt.CardLayout;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.UIManager;
import org.jeditor.app.JAppEditor;
import org.jeditor.navidata.AbstractNaviPane;
import org.jeditor.navidata.Navi_a0;
import org.jeditor.navidata.Navi_a1;
import org.jeditor.navidata.Navi_a2;
import org.jeditor.navidata.Navi_a3;
import org.jeditor.navidata.Navi_a4;
import org.jeditor.navidata.Navi_a5;
import org.jeditor.navidata.Navi_a6;
import org.jeditor.navidata.Navi_a7;
import org.jeditor.navidata.Navi_b1;
import org.jeditor.navidata.Navi_b2;
import org.jeditor.navidata.Navi_b3;
import org.jeditor.navidata.Navi_b4;
import org.jeditor.navidata.Navi_d0;
import org.jeditor.navidata.Navi_d1;
import org.jeditor.navidata.Navi_d2;
import org.jeditor.navidata.Navi_d3;
import org.jeditor.navidata.Navi_d4;
import org.jeditor.navidata.Navi_e1;
import org.jeditor.navidata.Navi_e2;
import org.jeditor.navidata.Navi_e3;
import org.jeditor.navidata.Navi_e4;
import org.jeditor.navidata.Navi_e5;
import org.jeditor.navidata.Navi_e6;
import org.jeditor.navidata.Navi_e7;
import org.jeditor.navidata.Navi_e8;
import org.jeditor.navidata.Navi_i0;
import org.jeditor.navidata.Navi_i1;
import org.jeditor.navidata.Navi_i2;
import org.jeditor.navidata.Navi_i3;
import org.jeditor.navidata.Navi_i4;
import org.jeditor.navidata.Navi_p0;
import org.jeditor.navidata.Navi_p1;
import org.jeditor.navidata.Navi_p2;
import org.jeditor.navidata.Navi_p2_2;
import org.jeditor.navidata.Navi_p3;
import org.jeditor.navidata.Navi_p4;
import org.jeditor.navidata.Navi_r1;
import org.jeditor.navidata.Navi_r2;
import org.jeditor.navidata.Navi_r3;
import org.jeditor.navidata.Navi_r4;
import org.jeditor.navidata.Navi_r5;
import org.jeditor.navidata.Navi_r6;
import org.jeditor.navidata.Navi_t;

public class NaviManager {

	private JAppEditor parent;
	private JPanel viewPane;
	private CardLayout layout;
	private NaviStrategy strategy = new DefaultNaviStrategy();

	private AbstractNaviPane naviPane;

	private HistoryListModel historyModel;
	private Map<String, AbstractNaviPane> naviData;

	public NaviManager(JAppEditor parent) {
		this.parent = parent;

		// ダイアログのfont設定
		UIManager.put("OptionPane.messageFont", new Font("メイリオ", Font.PLAIN, 14));
		UIManager.put("OptionPane.buttonFont", new Font("メイリオ", Font.PLAIN, 14));
		UIManager.put("OptionPane.font", new Font("メイリオ", Font.PLAIN, 14));

		// 表示用パネルの生成
		viewPane = new JPanel();
		layout = new CardLayout();
		viewPane.setLayout(layout);

		// 履歴用リスト生成
		historyModel = new HistoryListModel();

		// ナビゲーションパネル保存用マップ生成
		naviData = new HashMap<>();

		// ナビゲーションパネル生成・viewPaneに追加
		naviPane = new Navi_t(this);
		viewPane.add(naviPane, naviPane.getIndex());
		naviData.put(naviPane.getIndex(), naviPane);
		historyModel.add(naviPane);

		naviPane = new Navi_a0(this);
		viewPane.add(naviPane, naviPane.getIndex());
		naviData.put(naviPane.getIndex(), naviPane);
		naviPane = new Navi_a1(this);
		viewPane.add(naviPane, naviPane.getIndex());
		naviData.put(naviPane.getIndex(), naviPane);
		naviPane = new Navi_a2(this);
		viewPane.add(naviPane, naviPane.getIndex());
		naviData.put(naviPane.getIndex(), naviPane);
		naviPane = new Navi_a3(this);
		viewPane.add(naviPane, naviPane.getIndex());
		naviData.put(naviPane.getIndex(), naviPane);
		naviPane = new Navi_a4(this);
		viewPane.add(naviPane, naviPane.getIndex());
		naviData.put(naviPane.getIndex(), naviPane);
		naviPane = new Navi_a5(this);
		viewPane.add(naviPane, naviPane.getIndex());
		naviData.put(naviPane.getIndex(), naviPane);
		naviPane = new Navi_a6(this);
		viewPane.add(naviPane, naviPane.getIndex());
		naviData.put(naviPane.getIndex(), naviPane);
		naviPane = new Navi_a7(this);
		viewPane.add(naviPane, naviPane.getIndex());
		naviData.put(naviPane.getIndex(), naviPane);

		naviPane = new Navi_b1(this);
		viewPane.add(naviPane, naviPane.getIndex());
		naviData.put(naviPane.getIndex(), naviPane);
		naviPane = new Navi_b2(this);
		viewPane.add(naviPane, naviPane.getIndex());
		naviData.put(naviPane.getIndex(), naviPane);
		naviPane = new Navi_b3(this);
		viewPane.add(naviPane, naviPane.getIndex());
		naviData.put(naviPane.getIndex(), naviPane);
		naviPane = new Navi_b4(this);
		viewPane.add(naviPane, naviPane.getIndex());
		naviData.put(naviPane.getIndex(), naviPane);

		naviPane = new Navi_e1(this);
		viewPane.add(naviPane, naviPane.getIndex());
		naviData.put(naviPane.getIndex(), naviPane);
		naviPane = new Navi_e2(this);
		viewPane.add(naviPane, naviPane.getIndex());
		naviData.put(naviPane.getIndex(), naviPane);
		naviPane = new Navi_e3(this);
		viewPane.add(naviPane, naviPane.getIndex());
		naviData.put(naviPane.getIndex(), naviPane);
		naviPane = new Navi_e4(this);
		viewPane.add(naviPane, naviPane.getIndex());
		naviData.put(naviPane.getIndex(), naviPane);
		naviPane = new Navi_e5(this);
		viewPane.add(naviPane, naviPane.getIndex());
		naviData.put(naviPane.getIndex(), naviPane);
		naviPane = new Navi_e6(this);
		viewPane.add(naviPane, naviPane.getIndex());
		naviData.put(naviPane.getIndex(), naviPane);
		naviPane = new Navi_e7(this);
		viewPane.add(naviPane, naviPane.getIndex());
		naviData.put(naviPane.getIndex(), naviPane);
		naviPane = new Navi_e8(this);
		viewPane.add(naviPane, naviPane.getIndex());
		naviData.put(naviPane.getIndex(), naviPane);

		naviPane = new Navi_r1(this);
		viewPane.add(naviPane, naviPane.getIndex());
		naviData.put(naviPane.getIndex(), naviPane);
		naviPane = new Navi_r2(this);
		viewPane.add(naviPane, naviPane.getIndex());
		naviData.put(naviPane.getIndex(), naviPane);
		naviPane = new Navi_r3(this);
		viewPane.add(naviPane, naviPane.getIndex());
		naviData.put(naviPane.getIndex(), naviPane);
		naviPane = new Navi_r4(this);
		viewPane.add(naviPane, naviPane.getIndex());
		naviData.put(naviPane.getIndex(), naviPane);
		naviPane = new Navi_r5(this);
		viewPane.add(naviPane, naviPane.getIndex());
		naviData.put(naviPane.getIndex(), naviPane);
		naviPane = new Navi_r6(this);
		viewPane.add(naviPane, naviPane.getIndex());
		naviData.put(naviPane.getIndex(), naviPane);

		naviPane = new Navi_d0(this);
		viewPane.add(naviPane, naviPane.getIndex());
		naviData.put(naviPane.getIndex(), naviPane);
		naviPane = new Navi_d1(this);
		viewPane.add(naviPane, naviPane.getIndex());
		naviData.put(naviPane.getIndex(), naviPane);
		naviPane = new Navi_d2(this);
		viewPane.add(naviPane, naviPane.getIndex());
		naviData.put(naviPane.getIndex(), naviPane);
		naviPane = new Navi_d3(this);
		viewPane.add(naviPane, naviPane.getIndex());
		naviData.put(naviPane.getIndex(), naviPane);
		naviPane = new Navi_d4(this);
		viewPane.add(naviPane, naviPane.getIndex());
		naviData.put(naviPane.getIndex(), naviPane);

		naviPane = new Navi_i0(this);
		viewPane.add(naviPane, naviPane.getIndex());
		naviData.put(naviPane.getIndex(), naviPane);
		naviPane = new Navi_i1(this);
		viewPane.add(naviPane, naviPane.getIndex());
		naviData.put(naviPane.getIndex(), naviPane);
		naviPane = new Navi_i2(this);
		viewPane.add(naviPane, naviPane.getIndex());
		naviData.put(naviPane.getIndex(), naviPane);
		naviPane = new Navi_i3(this);
		viewPane.add(naviPane, naviPane.getIndex());
		naviData.put(naviPane.getIndex(), naviPane);
		naviPane = new Navi_i4(this);
		viewPane.add(naviPane, naviPane.getIndex());
		naviData.put(naviPane.getIndex(), naviPane);

		naviPane = new Navi_p0(this);
		viewPane.add(naviPane, naviPane.getIndex());
		naviData.put(naviPane.getIndex(), naviPane);
		naviPane = new Navi_p1(this);
		viewPane.add(naviPane, naviPane.getIndex());
		naviData.put(naviPane.getIndex(), naviPane);
		naviPane = new Navi_p2(this);
		viewPane.add(naviPane, naviPane.getIndex());
		naviData.put(naviPane.getIndex(), naviPane);
		naviPane = new Navi_p2_2(this);
		viewPane.add(naviPane, naviPane.getIndex());
		naviData.put(naviPane.getIndex(), naviPane);
		naviPane = new Navi_p3(this);
		viewPane.add(naviPane, naviPane.getIndex());
		naviData.put(naviPane.getIndex(), naviPane);
		naviPane = new Navi_p4(this);
		viewPane.add(naviPane, naviPane.getIndex());
		naviData.put(naviPane.getIndex(), naviPane);
	}

	private String transformReturn(String nextState, String currentState) {
		if(!nextState.equals("return")) {
			return nextState;
		} else {
			AbstractNaviPane current = naviData.get(currentState);
			if(current instanceof Navi_b1) {
				return ((Navi_b1) current).getCalled();
			} else if(current instanceof Navi_r2) {
				return ((Navi_r2) current).getCalled();
			} else if(current instanceof Navi_r6) {
				return ((Navi_r6) current).getCalled();
			} else {
				return "e2";
			}
		}
	}

	public void changeNavi(String currentState, String buttonLabel, String input) {
		// 既存の履歴を更新
		historyModel.updateCurrentItem(naviData.get(currentState));
		// 次の状態を取得表示
		String nextState = strategy.getNextNavi(currentState, buttonLabel);
		nextState = transformReturn(nextState, currentState);
		AbstractNaviPane nextPane = naviData.get(nextState);
		nextPane.setInput(input);
		layout.show(viewPane, nextState);
		// タイトルに戻る場合は履歴削除
		if(nextState.equals("t")) {
			historyModel.clear();
		}
		// 履歴追加
		historyModel.add(nextPane);
	}

	public void backNavi() {
		HistoryData hd = historyModel.getPre();
		if(hd != null) {
			moveNavi(hd.getIndex(), historyModel.getHistoryIndex() - 2, hd.getNotice());
		}
	}

	public void moveNavi(String nextState, int historyIndex, String input) {
		layout.show(viewPane, nextState);
		AbstractNaviPane nextPane = naviData.get(nextState);
		nextPane.refreshLayout();
		nextPane.setInput(input);
		historyModel.setHistoryIndex(historyIndex + 1);
	}

	public JPanel getViewPane() {
		return viewPane;
	}

	public JAppEditor getParent() {
		return parent;
	}

	public HistoryListModel getHistoryListModel() {
		return historyModel;
	}
}
