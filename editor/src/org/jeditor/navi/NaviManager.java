package org.jeditor.navi;

import java.awt.CardLayout;
import javax.swing.JPanel;
import org.jeditor.app.JAppEditor;

public class NaviManager {

	private JAppEditor parent;
	private JPanel viewPane;
	private CardLayout layout;
	private NaviStrategy strategy = new DefaultNaviStrategy();

	private AbstractNaviPane topPane;
	private AbstractNaviPane secondPane;

	public NaviManager(JAppEditor parent) {
		this.parent = parent;

		// 表示用パネルの生成
		viewPane = new JPanel();
		layout = new CardLayout();
		viewPane.setLayout(layout);

		// ナビゲーションパネル生成
		topPane = new NaviTop(this);
		secondPane = new NaviSecond(this);

		// ナビゲーションパネル追加
		viewPane.add(topPane, topPane.getIndexLabel());
		viewPane.add(secondPane, secondPane.getIndexLabel());
	}

	public void changeNavi(String currentState, String buttonLabel) {
		layout.show(viewPane, strategy.getNextNavi(currentState, buttonLabel));
	}

	public JPanel getViewPane() {
		return viewPane;
	}
}
