package org.jeditor.app;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A generic dialog component, with Yes / No buttons.
 *
 * @version 0.46.3
 */
public class SettingsDialog extends JComponent {

	protected JDialog dialog;
	protected JFrame frame;
	private String yesLabel = "Yes";
	private String noLabel = "Cancel";
	private boolean resizable = false;
	protected AbstractAction yesaction;
	protected AbstractAction cancelaction;
	protected int returnValue;
	private JSpinner tabSizeSpinner;
	private SpinnerNumberModel tabSizeModel = new SpinnerNumberModel(4, 1, 15, 1);
	private int tabSizeValue = 4;

	public SettingsDialog(Component parent, int tabSizeValue) {
		super();
		this.tabSizeValue = tabSizeValue;
		this.createDialog(parent);
	}

	public int getTabSizeValue() {
		return tabSizeValue;
	}

	public JDialog getDialog() {
		return dialog;
	}

	public JFrame getFrame() {
		return frame;
	}

	protected void disposeDialog() {
		doCancel();
	}

	protected JFrame createFrame(Component parent) {
		frame = new JFrame("Settings");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setResizable(resizable);

		createPanel();

		frame.pack();
		frame.setLocationRelativeTo(parent);

		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				disposeDialog();
				returnValue = JFileChooser.CANCEL_OPTION;
			}
		});
		returnValue = JFileChooser.ERROR_OPTION;

		return frame;
	}

	protected JDialog createDialog(Component parent) {
		Frame _frame = parent instanceof Frame ? (Frame) parent : (Frame) SwingUtilities.getAncestorOfClass(
				Frame.class, parent);

		dialog = new JDialog(_frame, true);
		dialog.setTitle("Settings");
		dialog.setResizable(resizable);

		createPanel();

		dialog.pack();
		dialog.setLocationRelativeTo(parent);

		dialog.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				disposeDialog();
				returnValue = JFileChooser.CANCEL_OPTION;
			}
		});
		returnValue = JFileChooser.ERROR_OPTION;

		return dialog;
	}

	protected JPanel createYesNoPanel() {
		JPanel yesnopanel = new JPanel();
		yesnopanel.setLayout(new FlowLayout());

		yesaction = new AbstractAction(yesLabel) {

			@Override
			public void actionPerformed(ActionEvent ae) {
				doYes();
			}
		};

		cancelaction = new AbstractAction(noLabel) {

			@Override
			public void actionPerformed(ActionEvent ae) {
				doCancel();
			}
		};

		yesnopanel.add(new JButton(yesaction), BorderLayout.EAST);
		yesnopanel.add(new JButton(cancelaction), BorderLayout.WEST);

		return yesnopanel;
	}

	public AbstractAction getYesAction() {
		return yesaction;
	}

	public AbstractAction getCancelAction() {
		return cancelaction;
	}

	protected void createPanel() {
		// queue size spinner
		tabSizeSpinner = new JSpinner(tabSizeModel);
		tabSizeSpinner.setEditor(new JSpinner.NumberEditor(tabSizeSpinner, "##"));
		tabSizeSpinner.setMaximumSize(tabSizeSpinner.getPreferredSize());

		// queue size spinner modification
		tabSizeSpinner.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				try {
					// get the value of the spacing JSpinner
					tabSizeValue = ((Integer) (((JSpinner) (e.getSource())).getValue())).intValue();
					if(tabSizeValue < 1) {
						tabSizeValue = 1;
					} else if(tabSizeValue > 15) {
						tabSizeValue = 15;
					}
				} catch (ArithmeticException ex) {
				}
			}
		});

		JPanel tabSizePanel = new JPanel();
		tabSizePanel.setLayout(new BoxLayout(tabSizePanel, BoxLayout.X_AXIS));
		tabSizePanel.add(new JLabel("Tab Size"));
		tabSizePanel.add(Box.createHorizontalStrut(30));
		tabSizePanel.add(tabSizeSpinner);
		tabSizePanel.add(Box.createHorizontalStrut(30));
		tabSizePanel.add(Box.createHorizontalGlue());

		// yes/no panel
		JPanel yesnopanel = this.createYesNoPanel();

		Container pane = dialog.getContentPane();
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
		pane.add(tabSizePanel);
		pane.add(Box.createRigidArea(new Dimension(50, 20)));
		pane.add(yesnopanel);
	}

	/**
	 * show the dialog.
	 * 
	 * @return the JFileChooser return value.
	 */
	public int showDialog() {
		if(dialog != null) {
			dialog.setVisible(true);
			dialog.dispose();
		} else {
			frame.setVisible(true);
		}

		return returnValue;
	}

	protected void doYes() {
		returnValue = JFileChooser.APPROVE_OPTION;
		if(dialog != null) {
			dialog.setVisible(false);
		} else if(frame != null) {
			frame.dispose();
		}
	}

	protected void doCancel() {
		returnValue = JFileChooser.CANCEL_OPTION;
		if(dialog != null) {
			dialog.dispose();
		} else if(frame != null) {
			frame.dispose();
		}
	}
}
