/* This file is part of the jEditor library: see http://jeditor.sourceforge.net
 * Copyright (C) 2010 Herve Girod
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE. */

package org.jeditor.gui;

import org.jeditor.scripts.PerlTokenMarker;
import org.jeditor.scripts.base.Token;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 * Test class for the editor.
 * 
 * @version 0.2
 */
public class EditorTest extends JFrame {

	Document doc;
	boolean haschanged = false;
	AbstractAction saveAction;
	AbstractAction exitAction;

	public EditorTest(String title) {
		super(title);
		Container pane = this.getContentPane();
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

		/* specific code to handle JEditTextArea :
		 * - create defaults and setting styles
		 * - create the area
		 * - set the language TokenMarker
		 * - populate textArea
		 * - catch modification events for the enclosing Document */

		// define default colors
		CodeEditorDefaults defaults = new CodeEditorDefaults();
		defaults.setStyle(Token.KEYWORD1, Color.BLUE, false, true);
		defaults.setStyle(Token.KEYWORD2, Color.BLUE, false, true);
		defaults.setStyle(Token.KEYWORD3, Color.BLUE, false, true);
		defaults.setStyle(Token.COMMENT1, Color.DARK_GRAY, false, false);
		defaults.setStyle(Token.COMMENT2, Color.DARK_GRAY, false, false);
		defaults.eolMarkers = false;
		defaults.paintInvalid = false;

		// add highlighter (useful only in diff mode)
		CodeEditorHighlighter highlighter = new CodeEditorHighlighter();
		highlighter.append(1, Color.GREEN); // 1st line in Green
		highlighter.append(3, Color.RED); // 3rd line in Red

		// create JECodeEditor
		JEditor ta = new JEditor(defaults);
		ta.setHighlighter(highlighter); // sets the Highlighter
		ta.setLineNumberOffset(100); // set an offset of 100 to the first line number
		ta.setTokenMarker(new PerlTokenMarker()); // perl syntax (BasicTokenMarker for no syntax)
		// append text lines
		ta.setText("public class Test {\n" + "    public static void main(String[] args) {\n"
				+ "        System.out.println(\"Hello World\");\n" + "    }\n");
		ta.appendTextline("/* this is a comment");
		ta.appendTextline("    end of comment */");

		// creates MenuBar
		saveAction = new AbstractAction("Save") {

			public void actionPerformed(ActionEvent ae) {
				saveDocument();
			}
		};

		exitAction = new AbstractAction("Exit") {

			public void actionPerformed(ActionEvent ae) {
				exitApplication();
			}
		};

		JMenuBar mbar = new JMenuBar();
		JMenu menu = new JMenu("File");
		JMenuItem saveItem = new JMenuItem(saveAction);
		JMenuItem exitItem = new JMenuItem(exitAction);
		menu.add(saveItem);
		menu.add(exitItem);
		mbar.add(menu);

		// add components in panel
		pane.add(ta);
		this.setJMenuBar(mbar);

		// listen to changes in document
		doc = (Document) (ta.getDocument());
		doc.addDocumentListener(new DocumentListener() {

			public void changedUpdate(DocumentEvent e) {
				haschanged = true;
			}

			public void insertUpdate(DocumentEvent e) {
				haschanged = true;
			}

			public void removeUpdate(DocumentEvent e) {
				haschanged = true;
			}
		});
	}

	private void saveDocument() {
		if(!haschanged) {
			System.out.println("Document unchanged");
		} else {
			try {
				String text = doc.getText(0, doc.getLength());
				System.out.println(text);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
	}

	private void exitApplication() {
		System.exit(0);
	}

	public static void main(String[] args) {
		EditorTest test = new EditorTest("test");
		test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		test.show();
		test.pack();
	}
}
