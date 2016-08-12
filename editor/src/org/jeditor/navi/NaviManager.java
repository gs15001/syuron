/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navi;

import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import org.jeditor.app.JAppEditor;

public class NaviManager {

	private JAppEditor parent;
	private JPanel viewPane;
	private CardLayout layout;
	private NaviStrategy strategy = new DefaultNaviStrategy();

	private AbstractNaviPane naviPane;

	private List<AbstractNaviPane> history;
	private Map<String, AbstractNaviPane> naviData;

	public NaviManager(JAppEditor parent) {
		this.parent = parent;

		// 表示用パネルの生成
		viewPane = new JPanel();
		layout = new CardLayout();
		viewPane.setLayout(layout);

		// 履歴用リスト生成
		history = new ArrayList<>();

		// ナビゲーションパネル保存用マップ生成
		naviData = new HashMap<>();

		// ナビゲーションパネル生成・viewPaneに追加
		naviPane = new Navi_t(this);
		viewPane.add(naviPane, naviPane.getIndex());
		history.add(naviPane);
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
	}

	public void changeNavi(String currentState, String buttonLabel) {
		String nextState = strategy.getNextNavi(currentState, buttonLabel);
		layout.show(viewPane, nextState);
		history.add(naviData.get(nextState));
	}

	public JPanel getViewPane() {
		return viewPane;
	}
}
