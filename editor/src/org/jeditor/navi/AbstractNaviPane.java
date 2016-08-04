package org.jeditor.navi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public abstract class AbstractNaviPane extends JPanel {

	private static final long serialVersionUID = 1L;

	NaviManager naviManager;

	private JLabel indexLabel = null;
	protected JLabel questionLabel = null;
	protected JLabel descriptLabel = null;
	private JPanel northPane;
	private JPanel centerPane;
	private JPanel southPane;
	protected List<JButton> buttons = new ArrayList<>();

	public AbstractNaviPane(NaviManager mgr, String index, int buttonNum) {
		super();

		naviManager = mgr;

		// 自身のパネル
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);

		// 上部のコンテンツ
		northPane = new JPanel();
		northPane.setBackground(Color.YELLOW);
		northPane.setPreferredSize(new Dimension(getWidth(), 50));
		indexLabel = new JLabel(index);
		northPane.add(indexLabel);

		// 中部のコンテンツ
		centerPane = new JPanel();
		centerPane.setBackground(Color.BLUE);
		centerPane.setLayout(new GridLayout(2, 1));;
		questionLabel = new JLabel("ここに質問が表示される");
		questionLabel.setPreferredSize(new Dimension(getWidth(), 300));
		descriptLabel = new JLabel("ここに解説が表示される");
		descriptLabel.setPreferredSize(new Dimension(getWidth(), 300));
		centerPane.add(questionLabel);
		centerPane.add(descriptLabel);

		// 下部のコンテンツ
		southPane = new JPanel();
		southPane.setBackground(Color.RED);
		southPane.setLayout(new GridLayout(1, buttonNum, 30, 0));
		southPane.setPreferredSize(new Dimension(getWidth(), 100));
		// ボーダーを使った余白の設定
		southPane.setBorder(new EmptyBorder(30, 50, 30, 50));
		for (int i = 0; i < buttonNum; i++) {
			JButton tmp = new JButton();
			tmp.setPreferredSize(new Dimension(80, 40));
			buttons.add(tmp);
			southPane.add(tmp);
		}

		add(northPane, BorderLayout.NORTH);
		add(centerPane, BorderLayout.CENTER);
		add(southPane, BorderLayout.SOUTH);
	}

	public String getIndexLabel() {
		return indexLabel.getText();
	}
}
