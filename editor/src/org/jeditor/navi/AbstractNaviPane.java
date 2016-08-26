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
import org.jeditor.app.JAppEditor;

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

	private final int FONT_SIZE_T;
	private final int FONT_SIZE_M;
	private final int FONT_SIZE_B;

	protected List<JButton> buttons = new ArrayList<>();

	public AbstractNaviPane(NaviManager mgr, String index, int buttonNum) {
		super();

		naviManager = mgr;
		JAppEditor parent = mgr.getParent();

		if(parent.WINDOW_WIDTH >= 1280) {
			FONT_SIZE_T = 24;
			FONT_SIZE_M = 16;
			FONT_SIZE_B = 18;
		} else {
			FONT_SIZE_T = 20;
			FONT_SIZE_M = 12;
			FONT_SIZE_B = 14;
		}

		// 自身のパネル
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);

		// 上部のコンテンツ
		northPane = new JPanel();
		northPane.setBackground(new Color(192, 192, 192));
		northPane.setPreferredSize(new Dimension(parent.RIGHT_WIDTH, (int) (parent.RIGHT_HIGHT * 0.08)));
		northPane.setBorder(new LineBorder(Color.GRAY));
		this.index = index;
		indexLabel = new JLabel("no text");
		indexLabel.setFont(new Font("メイリオ", Font.PLAIN, FONT_SIZE_T));
		northPane.add(indexLabel);

		// 中部のコンテンツ
		centerPane = new JPanel();
		centerPane.setBackground(new Color(224, 224, 224));
		centerPane.setLayout(new GridLayout(2, 1));;

		JPanel questionPane = new JPanel();
		questionPane.setBackground(new Color(224, 224, 224));
		questionPane.setPreferredSize(new Dimension(parent.RIGHT_WIDTH, (int) (parent.RIGHT_HIGHT * 0.4)));
		questionPane.setBorder(new LineBorder(Color.GRAY));
		questionLabel = new JLabel("ここに質問が表示される");
		questionLabel.setFont(new Font("メイリオ", Font.PLAIN, FONT_SIZE_M));
		// ボーダーを使った余白の設定
		int borderWidth = (int) (parent.RIGHT_WIDTH * 0.05);
		int borderHight = (int) (parent.RIGHT_HIGHT * 0.03);
		questionLabel.setBorder(new EmptyBorder(borderHight, borderWidth, borderHight, borderWidth));
		questionPane.add(questionLabel);

		JPanel descriptPane = new JPanel();
		descriptPane.setBackground(new Color(224, 224, 224));
		descriptPane.setPreferredSize(new Dimension(parent.RIGHT_WIDTH, (int) (parent.RIGHT_HIGHT * 0.4)));
		descriptPane.setBorder(new LineBorder(Color.GRAY));
		descriptLabel = new JLabel("ここに解説が表示される");
		descriptLabel.setFont(new Font("メイリオ", Font.PLAIN, FONT_SIZE_M));
		// ボーダーを使った余白の設定
		descriptLabel.setBorder(new EmptyBorder(borderHight, borderWidth, borderHight, borderWidth));
		descriptPane.add(descriptLabel);

		centerPane.add(questionPane);
		centerPane.add(descriptPane);

		// 下部のコンテンツ
		southPane = new JPanel();
		// ボーダーを使った余白の設定
		borderWidth = (int) (parent.RIGHT_WIDTH * 0.05);
		borderHight = (int) (parent.RIGHT_HIGHT * 0.03);
		southPane.setBorder(new CompoundBorder(new LineBorder(Color.GRAY), new EmptyBorder(borderHight, borderWidth,
				borderHight, borderWidth)));
		
		southPane.setPreferredSize(new Dimension(parent.RIGHT_WIDTH, (int) (parent.RIGHT_HIGHT * 0.12)));
		southPane.setBackground(new Color(192, 192, 192));
		southPane.setLayout(new GridLayout(1, buttonNum, borderWidth, 0));

		for (int i = 0; i < buttonNum; i++) {
			JButton tmp = new JButton();
			// リスナー登録
			tmp.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					naviManager.changeNavi(getIndex(), tmp.getText());
				}
			});
			int buttonWidth = (int) (parent.RIGHT_WIDTH * 0.2);
			int buttonHIGHT = (int) (parent.RIGHT_HIGHT * 0.06);
			tmp.setPreferredSize(new Dimension(buttonWidth, buttonHIGHT));
			tmp.setFont(new Font("メイリオ", Font.PLAIN, FONT_SIZE_B));
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
