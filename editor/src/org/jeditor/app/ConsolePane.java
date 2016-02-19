package org.jeditor.app;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import debugger.gui.TypeScript;

public class ConsolePane extends JPanel {

	private static final long serialVersionUID = 1L;

	private TypeScript script;

	private static final String PROMPT = "Input:";

	public ConsolePane() {
		super(new BorderLayout());

		this.script = new TypeScript(PROMPT, false); // No implicit echo.
		this.add(script);

		script.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
	}

	public void outputLine(String text) {
		script.append(text);
		script.newline();
	}
}
