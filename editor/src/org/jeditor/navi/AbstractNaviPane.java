/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public abstract class AbstractNaviPane extends JPanel {

	private static final long serialVersionUID = 1L;

	NaviManager naviManager;

	private String index;
	protected JLabel indexLabel = null;
	protected JLabel questionLabel = null;
	protected JLabel descriptLabel = null;
	private JPanel northPane;
	private JPanel centerPane;
	private JPanel southPane;

	protected List<JButton> buttons = new ArrayList<>();

	public AbstractNaviPane(NaviManager mgr, String index, int buttonNum) {
		super();

		naviManager = mgr;

		// // 自身のパネル
		// GridBagLayout layout = new GridBagLayout();
		// setLayout(layout);
		// setBackground(Color.WHITE);
		// // グリッドマネージャ作成
		// GridBagConstraints gbc = new GridBagConstraints();
		//
		// // 上部のコンテンツ
		// northPane = new JPanel();
		// northPane.setBackground(new Color(192, 192, 192));
		// northPane.setPreferredSize(new Dimension(getWidth(), 50));
		// this.index = index;
		// indexLabel = new JLabel("no text");
		// indexLabel.setFont(new Font("メイリオ", Font.PLAIN, 24));
		// northPane.add(indexLabel);
		// gbc.gridx = 0;
		// gbc.gridy = 0;
		// layout.setConstraints(northPane, gbc);
		//
		// questionPane = new JPanel();
		// questionPane.setBackground(new Color(224, 224, 224));
		// questionPane.setPreferredSize(new Dimension(getWidth(), 300));
		// //questionPane.setBorder(new LineBorder(Color.GRAY));
		// questionLabel = new JLabel("ここに質問が表示される");
		// //questionLabel.setPreferredSize(new Dimension(getWidth(), 300));
		// questionLabel.setFont(new Font("メイリオ", Font.PLAIN, 16));
		// // ボーダーを使った余白の設定
		// //questionLabel.setBorder(new EmptyBorder(10, 20, 10, 20));
		// questionPane.add(questionLabel);
		// gbc.gridx = 0;
		// gbc.gridy = 1;
		// layout.setConstraints(questionPane, gbc);
		//
		// descriptPane = new JPanel();
		// // descriptPane.setBackground(new Color(224, 224, 224));
		// descriptPane.setBorder(new LineBorder(Color.GRAY));
		// descriptLabel = new JLabel("ここに解説が表示される");
		// descriptLabel.setPreferredSize(new Dimension(getWidth(), 300));
		// descriptLabel.setFont(new Font("メイリオ", Font.PLAIN, 16));
		// // ボーダーを使った余白の設定
		// descriptLabel.setBorder(new EmptyBorder(10, 20, 10, 20));
		// descriptPane.add(descriptLabel);
		// gbc.gridx = 0;
		// gbc.gridy = 2;
		// layout.setConstraints(descriptPane, gbc);
		//
		// // 下部のコンテンツ
		// southPane = new JPanel();
		// southPane.setBackground(new Color(192, 192, 192));
		// southPane.setLayout(new GridLayout(1, buttonNum, 30, 0));
		// southPane.setPreferredSize(new Dimension(getWidth(), 100));
		// // ボーダーを使った余白の設定
		// southPane.setBorder(new EmptyBorder(30, 50, 30, 50));
		// for (int i = 0; i < buttonNum; i++) {
		// JButton tmp = new JButton();
		// // リスナー登録
		// tmp.addActionListener(new ActionListener() {
		//
		// @Override
		// public void actionPerformed(ActionEvent e) {
		// naviManager.changeNavi(getIndex(), tmp.getText());
		// }
		// });
		// tmp.setPreferredSize(new Dimension(80, 40));
		// tmp.setFont(new Font("メイリオ", Font.PLAIN, 18));
		// buttons.add(tmp);
		// southPane.add(tmp);
		// }
		// gbc.gridx = 0;
		// gbc.gridy = 3;
		// layout.setConstraints(southPane, gbc);
		//
		// add(northPane);
		// add(questionPane);
		// add(descriptPane);
		// add(southPane);
		// 自身のパネル
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);

		// 上部のコンテンツ
		northPane = new JPanel();
		northPane.setBackground(new Color(192, 192, 192));
		northPane.setPreferredSize(new Dimension(getWidth(), 50));
		northPane.setBorder(new LineBorder(Color.GRAY));
		this.index = index;
		indexLabel = new JLabel("no text");
		indexLabel.setFont(new Font("メイリオ", Font.PLAIN, 24));
		northPane.add(indexLabel);

		// 中部のコンテンツ
		centerPane = new JPanel();
		centerPane.setBackground(new Color(224, 224, 224));
		centerPane.setLayout(new GridLayout(2, 1));;

		JPanel questionPane = new JPanel();
		questionPane.setBackground(new Color(224, 224, 224));
		questionPane.setPreferredSize(new Dimension(getWidth(), 300));
		questionPane.setBorder(new LineBorder(Color.GRAY));
		questionLabel = new JLabel("ここに質問が表示される");
		questionLabel.setFont(new Font("メイリオ", Font.PLAIN, 16));
		// ボーダーを使った余白の設定
		questionLabel.setBorder(new EmptyBorder(10, 20, 10, 20));
		questionPane.add(questionLabel);

		JPanel descriptPane = new JPanel();
		descriptPane.setBackground(new Color(224, 224, 224));
		descriptPane.setPreferredSize(new Dimension(getWidth(), 300));
		descriptPane.setBorder(new LineBorder(Color.GRAY));
		descriptLabel = new JLabel("ここに解説が表示される");
		descriptLabel.setFont(new Font("メイリオ", Font.PLAIN, 16));
		// ボーダーを使った余白の設定
		descriptLabel.setBorder(new EmptyBorder(10, 20, 10, 20));
		descriptPane.add(descriptLabel);

		centerPane.add(questionPane);
		centerPane.add(descriptPane);

		// 下部のコンテンツ
		southPane = new JPanel();
		// ボーダーを使った余白の設定
		southPane.setBorder(new CompoundBorder(new LineBorder(Color.GRAY), new EmptyBorder(30, 50, 30, 50)));
		southPane.setPreferredSize(new Dimension(getWidth(), 100));
		southPane.setBackground(new Color(192, 192, 192));
		southPane.setLayout(new GridLayout(1, buttonNum, 30, 0));

		for (int i = 0; i < buttonNum; i++) {
			JButton tmp = new JButton();
			// リスナー登録
			tmp.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					naviManager.changeNavi(getIndex(), tmp.getText());
				}
			});
			tmp.setPreferredSize(new Dimension(80, 40));
			tmp.setFont(new Font("メイリオ", Font.PLAIN, 18));
			buttons.add(tmp);
			southPane.add(tmp);
		}

		add(northPane, BorderLayout.NORTH);
		add(centerPane, BorderLayout.CENTER);
		add(southPane, BorderLayout.SOUTH);
	}

	public String getIndex() {
		return index;
	}

	// public void paint(Graphics g) {
	// super.paint(g);
	// Graphics2D g2 = (Graphics2D) g;
	// g2.setColor(Color.GRAY);
	// Line2D line = new Line2D.Double(0, 50, getWidth(), 50);
	// g2.draw(line);
	// line = new Line2D.Double(0, 300, getWidth(), 300);
	// g2.draw(line);
	// line = new Line2D.Double(0, getHeight() - 100, getWidth(), getHeight() - 100);
	// g2.draw(line);
	// }
}
