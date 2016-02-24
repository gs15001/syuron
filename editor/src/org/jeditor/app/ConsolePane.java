package org.jeditor.app;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import javax.swing.JPanel;
import debugger.gui.TypeScript;

public class ConsolePane extends JPanel {

	private static final long serialVersionUID = 1L;

	private TypeScript script;

	private static final String PROMPT = "Input:";

	private BufferedWriter output;

	public ConsolePane() {
		super(new BorderLayout());

		this.script = new TypeScript(PROMPT, false); // No implicit echo.
		this.add(script);

		script.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String s = script.readln();
				if(output != null) {
					try {
						output.write(s, 0, s.length());
						output.newLine();
						output.flush();
					} catch (IOException e1) {
						output = null;
						// e1.printStackTrace();
					}
				}
			}
		});
	}

	public void outputLine(String text) {
		script.append(text);
		script.newline();
	}

	public void setOutputStream(OutputStream output) {
		this.output = new BufferedWriter(new OutputStreamWriter(output));
	}
}
