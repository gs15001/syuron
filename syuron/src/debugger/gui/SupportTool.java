package debugger.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class SupportTool extends JPanel {

	private static final long serialVersionUID = 1L;
	private CardLayout layout;
	private GUI parent;
	private JPanel main;

	public SupportTool(GUI parent) {
		this.parent = parent;

		// 自身のパネル
		setLayout(new BorderLayout());

		// 表示用パネル
		main = new JPanel();
		layout = new CardLayout();
		main.setLayout(layout);
		main.add(createPage("./res/debugger1.png"));
		main.add(createPage("./res/debugger2.png"));
		main.add(createPage("./res/debugger3.png"));
		main.add(createPage("./res/debugger4.png"));
		main.add(createPage("./res/debugger5.png"));
		main.add(createPage("./res/debugger6.png"));
		main.add(createPage("./res/debugger7.png"));

		// 戻る用ボタン
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(null);
		buttonPane.setBackground(Color.LIGHT_GRAY);
		buttonPane.setPreferredSize(new Dimension((int) (parent.WINDOW_WIDTH * 0.25),
				(int) (parent.WINDOW_WIDTH * 0.35 * 0.1)));
		// ボーダーを使った余白の設定
		int tmpWidth = (int) (parent.WINDOW_WIDTH * 0.01);
		int tmpHeight = 10;
		buttonPane.setBorder(new EmptyBorder(tmpHeight, tmpWidth, tmpHeight, tmpWidth));
		// ボタン
		JButton button = new JButton("next");
		button.setFont(new Font("メイリオ", Font.PLAIN, 14));
		button.setBounds((int) (parent.WINDOW_WIDTH * 0.35 * 0.82), 10, 80, 30);
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(layout != null) {
					layout.next(main);
				}
			}
		});
		buttonPane.add(button);
		// ボタン
		button = new JButton("prev");
		button.setFont(new Font("メイリオ", Font.PLAIN, 14));
		button.setBounds((int) (parent.WINDOW_WIDTH * 0.35 * 0.05), 10, 80, 30);
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(layout != null) {
					layout.previous(main);
				}
			}
		});
		buttonPane.add(button);
		add(buttonPane, BorderLayout.SOUTH);
		add(main, BorderLayout.CENTER);
	}

	private JPanel createPage(String path) {
		JPanel pane = new JPanel();
		pane.setBackground(new Color(224, 224, 224));
		JLabel label = new JLabel(new ImageIcon(path));
		pane.add(label);
		return pane;
	}
}
