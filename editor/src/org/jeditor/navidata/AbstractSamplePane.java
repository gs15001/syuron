package org.jeditor.navidata;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.jeditor.app.JAppEditor;
import org.jeditor.navi.NaviManager;

public abstract class AbstractSamplePane extends JPanel {

	private static final long serialVersionUID = 1L;

	private JAppEditor parent;
	private CardLayout layout = null;
	private JPanel main = null;

	public AbstractSamplePane(NaviManager mgr) {
		this.parent = mgr.getParent();

		setLayout(new BorderLayout());

		// 戻る用ボタン
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(null);
		buttonPane.setBackground(new Color(224, 224, 224));
		buttonPane.setPreferredSize(new Dimension(parent.RIGHT_WIDTH, (int) (parent.RIGHT_HEIGHT * 0.07)));
		// ボーダーを使った余白の設定
		int tmpWidth = (int) (parent.RIGHT_WIDTH * 0.03);
		int tmpHeight = 15;
		buttonPane.setBorder(new EmptyBorder(tmpHeight, tmpWidth, tmpHeight, tmpWidth));
		JButton button = new JButton("戻る");
		button.setFont(new Font("メイリオ", Font.PLAIN, 16));
		button.setBounds((int) (parent.RIGHT_WIDTH * 0.75), 5, (int) (parent.RIGHT_WIDTH * 0.15), 30);
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(layout != null) {
					layout.next(main);
				}
			}
		});
		buttonPane.add(button);
		add(buttonPane, BorderLayout.SOUTH);
	}

	public void setLayout(JPanel main, CardLayout layout) {
		this.main = main;
		this.layout = layout;
	}
}
