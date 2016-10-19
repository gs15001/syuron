/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navidata;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import org.jeditor.app.JAppEditor;
import org.jeditor.navi.HistoryData;
import org.jeditor.navi.InputDialog;
import org.jeditor.navi.NaviManager;

public abstract class AbstractNaviPane extends JPanel {

	private static final long serialVersionUID = 1L;

	NaviManager naviManager;

	private String index;
	protected JLabel indexLabel = null;
	protected JLabel questionLabel = null;
	protected JLabel noticeLabel = null;
	protected JLabel descriptLabel = null;
	protected JLabel titleLabel = null;
	private JPanel northPane;
	private JPanel centerPane;
	private JPanel southPane;

	protected InputDialog[] dialog = new InputDialog[4];

	private final int FONT_SIZE_T = 24;
	private final int FONT_SIZE_M = 16;
	private final int FONT_SIZE_B = 18;
	private final int FONT_SIZE_S = 12;

	protected List<JButton> buttons = new ArrayList<>();
	private JCheckBox confi;

	protected String notice = "";
	protected String input = "";
	protected String selected = "";

	public AbstractNaviPane(NaviManager mgr, String index, int buttonNum) {
		super();

		naviManager = mgr;
		JAppEditor parent = mgr.getParent();

		// 自身のパネル
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);

		// 上部のコンテンツ
		northPane = new JPanel();
		northPane.setLayout(new FlowLayout(FlowLayout.LEFT, 30, 0));
		northPane.setBackground(new Color(192, 192, 192));
		northPane.setPreferredSize(new Dimension(parent.RIGHT_WIDTH, (int) (parent.RIGHT_HEIGHT * 0.07)));
		northPane.setBorder(new LineBorder(Color.GRAY));
		this.index = index;
		indexLabel = new JLabel("no text");
		indexLabel.setFont(new Font("メイリオ", Font.PLAIN, FONT_SIZE_T));

		// 戻るボタン
		JButton backButton = new JButton();
		backButton
				.setPreferredSize(new Dimension((int) (parent.RIGHT_WIDTH * 0.06), (int) (parent.RIGHT_HEIGHT * 0.05)));
		// backButton.setText("<");
		// backButton.setFont(new Font("メイリオ", Font.PLAIN, 10));
		// リスナー登録
		backButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				naviManager.backNavi();
			}
		});
		northPane.add(backButton);
		northPane.add(indexLabel);

		// 中部のコンテンツ
		centerPane = new JPanel();
		centerPane.setBackground(new Color(224, 224, 224));
		centerPane.setLayout(new GridLayout(2, 1));;

		// ナビゲーションパネル
		JPanel questionPane = new JPanel();
		questionPane.setBackground(new Color(224, 224, 224));
		questionPane.setPreferredSize(new Dimension(parent.RIGHT_WIDTH, (int) (parent.RIGHT_HEIGHT * 0.45)));
		questionPane.setBorder(new LineBorder(Color.GRAY));

		// 着目しているもの
		noticeLabel = new JLabel("着目している何か");
		noticeLabel.setFont(new Font("メイリオ", Font.PLAIN, FONT_SIZE_M));
		noticeLabel.setHorizontalAlignment(JLabel.RIGHT);
		noticeLabel
				.setPreferredSize(new Dimension((int) (parent.RIGHT_WIDTH * 0.9), (int) (parent.RIGHT_HEIGHT * 0.04)));
		noticeLabel.setBackground(Color.LIGHT_GRAY);
		noticeLabel.setOpaque(true);

		// 本文
		questionLabel = new JLabel("ここに質問が表示される");
		questionLabel.setFont(new Font("メイリオ", Font.PLAIN, FONT_SIZE_M));
		questionLabel.setPreferredSize(new Dimension((int) (parent.RIGHT_WIDTH * 0.9),
				(int) (parent.RIGHT_HEIGHT * 0.39)));
		questionLabel.setVerticalAlignment(JLabel.TOP);
		// ボーダーを使った余白の設定
		int tmpWidth = (int) (parent.RIGHT_WIDTH * 0.03);
		int tmpHeight = 0;
		// questionLabel.setBorder(new EmptyBorder(borderHight, borderWidth, borderHight, borderWidth));
		// questionLabel.setBackground(Color.WHITE);
		// questionLabel.setOpaque(true);

		// ラベル追加
		questionPane.add(noticeLabel);
		questionPane.add(questionLabel);

		// 解説パネル
		JPanel descriptPane = new JPanel();
		descriptPane.setBackground(new Color(224, 224, 224));
		descriptPane.setPreferredSize(new Dimension(parent.RIGHT_WIDTH, (int) (parent.RIGHT_HEIGHT * 0.45)));
		descriptPane.setBorder(new LineBorder(Color.GRAY));

		// タイトル
		titleLabel = new JLabel("解説");
		titleLabel.setFont(new Font("メイリオ", Font.PLAIN, FONT_SIZE_M));
		titleLabel.setHorizontalAlignment(JLabel.LEFT);
		titleLabel
				.setPreferredSize(new Dimension((int) (parent.RIGHT_WIDTH * 0.9), (int) (parent.RIGHT_HEIGHT * 0.04)));
		titleLabel.setBackground(Color.LIGHT_GRAY);
		titleLabel.setOpaque(true);

		// 解説
		descriptLabel = new JLabel("ここに解説が表示される");
		descriptLabel.setFont(new Font("メイリオ", Font.PLAIN, FONT_SIZE_M));
		descriptLabel.setPreferredSize(new Dimension((int) (parent.RIGHT_WIDTH * 0.9),
				(int) (parent.RIGHT_HEIGHT * 0.38)));
		descriptLabel.setVerticalAlignment(JLabel.TOP);
		// ボーダーを使った余白の設定
		// descriptLabel.setBorder(new EmptyBorder(0, borderWidth, 0, borderWidth));
		// descriptLabel.setBackground(Color.WHITE);
		// descriptLabel.setOpaque(true);

		// ラベル追加
		descriptPane.add(titleLabel);
		descriptPane.add(descriptLabel);

		centerPane.add(questionPane);
		centerPane.add(descriptPane);

		// 下部のコンテンツ
		southPane = new JPanel();
		southPane.setPreferredSize(new Dimension(parent.RIGHT_WIDTH, (int) (parent.RIGHT_HEIGHT * 0.08)));
		southPane.setBackground(new Color(192, 192, 192));
		southPane.setLayout(new FlowLayout());
		// ボーダーを使った余白の設定
		tmpWidth = (int) (parent.RIGHT_WIDTH * 0.01);
		tmpHeight = 0;
		southPane.setBorder(new CompoundBorder(new LineBorder(Color.GRAY), new EmptyBorder(tmpHeight, tmpWidth,
				tmpHeight, tmpWidth)));

		JPanel checkPane = new JPanel();
		checkPane
				.setPreferredSize(new Dimension((int) (parent.RIGHT_WIDTH * 0.12), (int) (parent.RIGHT_HEIGHT * 0.06)));
		checkPane.setBackground(new Color(192, 192, 192));

		JPanel buttonPane = new JPanel();
		buttonPane
				.setPreferredSize(new Dimension((int) (parent.RIGHT_WIDTH * 0.78), (int) (parent.RIGHT_HEIGHT * 0.06)));
		buttonPane.setBackground(new Color(192, 192, 192));
		buttonPane.setLayout(new GridLayout(1, buttonNum, tmpWidth, 0));

		// 自信度のチェックボックス
		confi = new JCheckBox("自信なし", false);
		confi.setFont(new Font("メイリオ", Font.PLAIN, FONT_SIZE_S));
		confi.setHorizontalTextPosition(SwingConstants.LEFT);
		confi.setBackground(new Color(192, 192, 192));
		tmpWidth = (int) (parent.RIGHT_WIDTH * 0.12);
		tmpHeight = (int) (parent.RIGHT_HEIGHT * 0.06);
		confi.setPreferredSize(new Dimension(tmpWidth, tmpHeight));
		checkPane.add(confi);

		// ボタン
		for (int i = 0; i < buttonNum; i++) {
			MyButton button = new MyButton(i);
			// リスナー登録
			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					String inputTmp = "";
					if(dialog[button.i] != null) {
						inputTmp = dialog[button.i].showInputDialog(parent);
					}
					if(inputTmp != null || dialog[button.i] == null) {
						selected = button.getText();
						naviManager.changeNavi(getIndex(), button.getText(), input + inputTmp);
					}
				}
			});
			tmpWidth = (int) (parent.RIGHT_WIDTH * 0.8) / buttonNum;
			tmpHeight = (int) (parent.RIGHT_HEIGHT * 0.03);
			button.setPreferredSize(new Dimension(tmpWidth, tmpHeight));
			button.setFont(new Font("メイリオ", Font.PLAIN, FONT_SIZE_B));
			buttons.add(button);
			buttonPane.add(button);
		}

		southPane.add(checkPane);
		southPane.add(buttonPane);

		add(northPane, BorderLayout.NORTH);
		add(centerPane, BorderLayout.CENTER);
		add(southPane, BorderLayout.SOUTH);
	}

	public String getIndex() {
		return index;
	}

	public void setInput(String notice) {
		this.notice = notice;
	}

	public HistoryData createHistoryData() {
		return new HistoryData(index, indexLabel.getText(), notice);
	}

	public void updateHistoryData(HistoryData d) {
		if(confi.isSelected()) {
			d.updateData(selected, input, "なし");
		} else {
			d.updateData(selected, input, "");
		}
		selected = "";
		input = "";
		confi.setSelected(false);
	}

	class MyButton extends JButton {

		private static final long serialVersionUID = 1L;
		int i;

		public MyButton(int i) {
			super();
			this.i = i;
		}
	}

}
