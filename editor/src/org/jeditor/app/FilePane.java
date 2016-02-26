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

package org.jeditor.app;

import org.jeditor.diff.JDiffTextPanel;
import org.jeditor.gui.JEditor;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.jeditor.gui.CodeEditorDefaults;
import org.jeditor.scripts.base.Token;
import org.jeditor.scripts.base.TokenMarker;

/**
 * A Panel showing the contents of a text file.
 * 
 * @version 0.4.1
 */
public class FilePane extends JPanel {

	private static final long serialVersionUID = 1L;
	protected String name;
	protected TextFile fromfile = null;
	protected TextFile tofile = null;
	protected JAppEditor parent;
	protected JEditor ed = null;
	protected JDiffTextPanel textco = null;
	private boolean diffType = false;
	private TokenMarker marker;

	protected static CodeEditorDefaults defaults = new CodeEditorDefaults();
	static {
		defaults.eolMarkers = false;
		defaults.paintInvalid = false;
		defaults.setStyle(Token.KEYWORD1, Color.BLUE, false, true);
		defaults.setStyle(Token.KEYWORD2, new Color(13, 130, 0), false, true);
		defaults.setStyle(Token.KEYWORD3, Color.BLUE, false, true);
		defaults.setStyle(Token.COMMENT1, Color.DARK_GRAY, true, false);
		defaults.setStyle(Token.COMMENT2, Color.DARK_GRAY, true, false);
	}

	public static void setTabSize(int tabSize) {
		defaults.tabSize = tabSize;
	}

	/** default constructor of the class, never to call directly */
	private FilePane(JAppEditor parent) {
		super();
		this.parent = parent;
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}

	public FilePane(JAppEditor parent, File file) {
		this(parent);
		name = file.getName();
		this.fromfile = new TextFile(file);
		// manage syntax for file
		SyntaxManager manager = SyntaxManager.instance();
		marker = manager.getTokenMarkerFromExt(fromfile.getExtension());
		parent.setFileType(manager.getType(fromfile.getExtension()));

		setPanel(fromfile);
	}

	/** base constructor of the class for comparing two handbooks */
	public FilePane(JAppEditor parent, File from, File to) {
		this(parent);
		this.fromfile = new TextFile(from);
		this.tofile = new TextFile(to);
		diffType = true;
		name = from.getName() + " - " + to.getName();
		// manage syntax for file
		SyntaxManager manager = SyntaxManager.instance();
		marker = manager.getTokenMarkerFromExt(fromfile.getExtension());
		parent.setFileType(manager.getType(fromfile.getExtension()));

		setPanel(fromfile, tofile);
	}

	/** base constructor of the class for comparing two handbooks */
	public FilePane(JAppEditor parent, TextFile from, TextFile to) {
		this(parent);
		this.fromfile = from;
		this.tofile = to;
		diffType = true;
		name = from.getName() + " - " + to.getName();
		// manage syntax for file
		SyntaxManager manager = SyntaxManager.instance();
		marker = manager.getTokenMarkerFromExt(fromfile.getExtension());
		parent.setFileType(manager.getType(fromfile.getExtension()));

		setPanel(fromfile, tofile);
	}

	protected void setPanel(TextFile text) {
		ed = new JEditor(this, defaults);
		ed.setTokenMarker(marker);
		ed.setText(text.getText());
		ed.setEdited(false);
		this.add(new JScrollPane(ed));
	}

	protected void setPanel(TextFile fromfile, TextFile tofile) {
		List<String> fromtext = null;
		List<String> totext = null;
		if(fromfile != null)
			fromtext = fromfile.getText();
		else
			fromtext = new ArrayList<>(1);
		if(tofile != null)
			totext = tofile.getText();
		else
			totext = new ArrayList<>(1);
		textco = new JDiffTextPanel(fromtext, totext, defaults);

		textco.setTokenMarker(marker);
		this.add(textco);
	}

	public void setFileType(String filetype) {
		// manage syntax for file
		SyntaxManager manager = SyntaxManager.instance();
		marker = manager.getTokenMarkerFromType(filetype);
		setTokenMarker(marker);
	}

	public void setTokenMarker(TokenMarker marker) {
		this.marker = marker;
		if(!diffType) {
			ed.setTokenMarker(marker);
		} else {
			textco.setTokenMarker(marker);
		}
	}

	public TextFile getFile() {
		return fromfile;
	}

	public TextFile getFromFile() {
		return fromfile;
	}

	public TextFile getToFile() {
		return tofile;
	}

	public void changeEdited(boolean edited) {
		if(getComponentCount() > 0) {
			if(edited) {
				parent.addAsta();
			} else {
				parent.removeAsta();
			}
		}
	}

	@Override
	public String getName() {
		return name;
	}

	public class TextFile {

		public File file;
		public List<String> vfile = new ArrayList<>(10);
		public String ext = "";

		public TextFile(File file) {
			this.file = file;
			ext = getExtension(file);
			String s;
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "SJIS"));) {
				while ((s = reader.readLine()) != null) {
					vfile.add(s);
				}
			} catch (FileNotFoundException e) {
				System.out.println("File not found");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public String getExtension() {
			return ext;
		}

		public String getName() {
			return file.getName();
		}

		public List<String> getText() {
			return vfile;
		}

		private String getExtension(File file) {
			String s = file.getName();
			int idx = s.lastIndexOf('.');
			if(idx != -1) {
				return s.substring(idx + 1, s.length());
			} else
				return "";
		}
	}
}
