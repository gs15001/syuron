/* Copyright (C) 1999 Slava Pestov
 * Copyright (C) 2010, 2012, 2014 Herve Girod
 *
 * You may use and modify this package for any purpose. Redistribution is
 * permitted, in both source and binary form, provided that this notice
 * remains intact in all source distributions of this package. */
package org.jeditor.gui;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.EventListenerList;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Keymap;
import javax.swing.text.PlainDocument;
import javax.swing.text.Position;
import javax.swing.text.Segment;
import javax.swing.text.TabExpander;
import javax.swing.text.Utilities;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;
import org.jeditor.app.FilePane;
import org.jeditor.scripts.TextTokenMarker;
import org.jeditor.scripts.base.SyntaxStyle;
import org.jeditor.scripts.base.Token;
import org.jeditor.scripts.base.TokenMarker;

/**
 * jEdit's text area component. It is more suited for editing program
 * source code than JEditorPane, because it drops the unnecessary features
 * (images, variable-width lines, and so on) and adds a whole bunch of
 * useful goodies such as:
 * <ul>
 * <li>More flexible key binding scheme
 * <li>Supports macro recorders
 * <li>Rectangular selection
 * <li>Bracket highlighting
 * <li>Syntax highlighting
 * <li>Command repetition
 * <li>Block caret can be enabled
 * </ul>
 * It is also faster and doesn't have as many problems. It can be used
 * in other applications; the only other part of jEdit it depends on is
 * the syntax package.
 * <p>
 *
 * To use it in your app, treat it like any other component, for example:
 *
 * <pre>
 * JEditTextArea ta = new JEditTextArea();
 * ta.setTokenMarker(new JavaTokenMarker());
 * ta.setText(&quot;public class Test {\n&quot; + &quot;    public static void main(String[] args) {\n&quot;
 * 		+ &quot;        System.out.println(\&quot;Hello World\&quot;);\n&quot; + &quot;    }\n&quot; + &quot;}&quot;);
 * </pre>
 *
 * @author Slava Pestov
 * @author Herve Girod
 *
 * @version 0.4.4
 */
public class JEditor extends JComponent {

	// **********************************************
	// KEYMAPS ATTRRIBUTES
	// are copied from JTextComponent
	// will no longer be useful when JEditTextArea will extend JTextComponent
	// **********************************************
	/**
	 * The default keymap that will be shared by all <code>JEditTextArea</code> instances unless they
	 * have had a different keymap set.
	 */
	public static final String DEFAULT_KEYMAP = "default";
	private static Map keymapTable = null;
	private transient Keymap keymap;

	static {
		try {
			keymapTable = new HashMap(17);
			Keymap binding = addKeymap(DEFAULT_KEYMAP, null);
			binding.setDefaultAction(new DefaultEditorKit.DefaultKeyTypedAction());
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	// protected members
	protected static String CENTER = "center";
	protected static String LEFT = "left";
	protected static String RIGHT = "right";
	protected static String BOTTOM = "bottom";
	protected JEditor focusedComponent;
	protected Timer caretTimer;
	protected CodeEditorPainter painter;
	protected Gutter gutter;
	protected JPopupMenu popup;
	protected EventListenerList _listenerList;
	protected MutableCaretEvent caretEvent;
	protected boolean caretBlinks;
	protected boolean caretVisible;
	protected boolean blink;
	protected boolean editable;
	protected int firstLine;
	protected int visibleLines;
	protected int electricScroll;
	protected int horizontalOffset;
	protected JScrollBar vertical;
	protected JScrollBar horizontal;
	protected boolean scrollBarsInitialized;
	protected InputHandler inputHandler;
	protected SyntaxDocument document;
	protected DocumentHandler documentHandler;
	protected boolean documentHandlerInstalled;
	protected Segment lineSegment;
	protected int selectionStart;
	protected int selectionStartLine;
	protected int selectionEnd;
	protected int selectionEndLine;
	protected boolean biasLeft;
	protected int maxHorizontalScrollWidth;
	protected int bracketPosition;
	protected int bracketLine;
	protected int magicCaret;
	protected boolean overwrite;
	protected boolean rectSelect;
	private Position anchor;
	protected int lineNumberOffset = 0;
	private static final String uiClassID = "CodeEditorUI";

	private FilePane parent;
	private boolean edited;
	private boolean compiled = false;
	/**
	 * Adding components with this name to the text area will place
	 * them left of the horizontal scroll bar. In jEdit, the status
	 * bar is added this way.
	 */
	public static String LEFT_OF_SCROLLBAR = "los";

	/**
	 * Creates a new JEditTextArea with the default settings.
	 */
	public JEditor() {
		this(CodeEditorDefaults.getDefaults());
		// need to do this to enabled backspace in component for the focus
		this.setFocusTraversalKeysEnabled(false);
	}

	/**
	 * Creates a new JEditTextArea with the specified settings.
	 *
	 * @param defaults
	 *            The default settings
	 */
	public JEditor(CodeEditorDefaults defaults) {
		super();
		// need to do this to enabled backspace in component for the focus
		this.setFocusTraversalKeysEnabled(false);

		caretTimer = new Timer(500, new CaretBlinker());
		caretTimer.setInitialDelay(500);
		caretTimer.start();

		// Enable the necessary events
		enableEvents(AWTEvent.KEY_EVENT_MASK);

		// Initialize some misc. stuff
		painter = new CodeEditorPainter(this, defaults);
		gutter = new Gutter(this, defaults);
		setDefaultKeymap();
		documentHandler = new DocumentHandler();
		_listenerList = new EventListenerList();
		caretEvent = new MutableCaretEvent();
		lineSegment = new Segment();
		bracketLine = bracketPosition = -1;
		blink = true;

		// Initialize the GUI
		setLayout(new ScrollLayout());
		add(LEFT, gutter);
		add(CENTER, painter);
		add(RIGHT, vertical = new JScrollBar(JScrollBar.VERTICAL));
		add(BOTTOM, horizontal = new JScrollBar(JScrollBar.HORIZONTAL));
		// setPreferredSize(new Dimension(450, 600));

		// Add some event listeners
		vertical.addAdjustmentListener(new AdjustHandler());
		MyMouseAdapter tmp = new MyMouseAdapter();
		vertical.addMouseListener(tmp);
		vertical.addMouseMotionListener(tmp);
		horizontal.addAdjustmentListener(new AdjustHandler());
		painter.addComponentListener(new ComponentHandler());
		painter.addMouseListener(new MouseHandler());
		painter.addMouseWheelListener(new MouseWheelHandler());
		painter.addMouseMotionListener(new DragHandler());
		addFocusListener(new FocusHandler());

		// Load the defaults
		setInputHandler(defaults.inputHandler);
		SyntaxDocument doc = new SyntaxDocument();
		doc.putProperty(PlainDocument.tabSizeAttribute, defaults.tabSize);
		setDocument(doc);
		editable = defaults.editable;
		caretVisible = defaults.caretVisible;
		caretBlinks = defaults.caretBlinks;
		electricScroll = defaults.electricScroll;

		popup = defaults.popup;

		// We don't seem to get the initial focus event?
		focusedComponent = this;
	}

	class MyMouseAdapter extends MouseAdapter {

		int x;
		int y;

		@Override
		public void mousePressed(MouseEvent e) {
			super.mousePressed(e);
			x = e.getX();
			y = e.getY();
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			super.mouseDragged(e);
			int move = e.getY() - y;
			int moveLine = vertical.getMaximum() / 20;
			if(moveLine < 1) {
				moveLine = 1;
			}
			if(move >= 15) {
				offsetVerticalScroll(moveLine);
				y = e.getY();
			} else if(move <= -15) {
				offsetVerticalScroll(-1 * moveLine);
				y = e.getY();
			}
		}
	}

	public JEditor(FilePane parent, CodeEditorDefaults defaults) {
		this(defaults);
		this.parent = parent;
	}

	public boolean isEdited() {
		return edited;
	}

	public void setEdited(boolean edited) {
		parent.changeEdited(edited);
		this.edited = edited;
	}

	public void setCompiled(boolean compiled) {
		this.compiled = compiled;
	}

	public boolean isCompiled() {
		return compiled;
	}

	// 分割ライン用データ
	private Set<Integer> partitionLines = new HashSet<>();

	public void setPartitionLine(Set<Integer> set) {
		partitionLines = set;
		painter.repaint();
		gutter.repaint();
	}

	public Set<Integer> getPartitionLine() {
		return partitionLines;
	}

	public void clearPartitionLine() {
		partitionLines.clear();
		painter.repaint();
		gutter.repaint();
	}

	// まとまり用データ
	private int[] partition = { -1, -1 };

	public void setPartition(int from, int to) {
		partition[0] = from;
		partition[1] = to;
		painter.repaint();
		gutter.repaint();
	}

	public int[] getPartition() {
		return partition;
	}

	// 1行ハイライト用データ
	private int noticeLine = -1;

	public void setNoticeLine(int i) {
		noticeLine = i;
		painter.repaint();
		gutter.repaint();
	}

	public int getNoticeLine() {
		return noticeLine;
	}

	// リターン用データ
	private int returnLine = -1;

	public void setReturnLine(int i) {
		returnLine = i;
	}

	public int getReturnLine() {
		return returnLine;
	}

	// 変数ハイライト用データ
	private String variableName = null;

	public void setVariableName(String name) {
		variableName = name;
		painter.repaint();
		gutter.repaint();
	}

	public String getVariableName() {
		return variableName;
	}

	// 更新用
	public void updateData(int v, int startLine, int startOffset) {
		// 選択行を改行する場合は、先頭での改行のみ変更対象とする
		if(noticeLine > startLine) {
			noticeLine += v;
		} else if(noticeLine == startLine) {
			if(startOffset == 0) {
				noticeLine += v;
			}
		}

		if(returnLine > startLine) {
			returnLine += v;
		} else if(returnLine == startLine) {
			if(startOffset == 0) {
				returnLine += v;
			}
		}

		for (int i = 0; i < partition.length; i++) {
			if(partition[i] > startLine) {
				partition[i] += v;
			}
		}

		if(partitionLines.size() > 0) {
			Set<Integer> tmp = new HashSet<>();
			for (Integer i : partitionLines) {
				if(i.intValue() > startLine) {
					tmp.add(new Integer(i.intValue() + v));
				} else if(i.intValue() == startLine && v < 0) {
					tmp.add(new Integer(i.intValue() + v));
				} else {
					tmp.add(i);
				}
			}
			partitionLines = tmp;
		}
		parent.getParent().updateData();
	}

	// 以下初期から
	public int getTabSize() {
		return painter.getTabSize();
	}

	/**
	 * Gets the class ID for the UI.
	 * <P>
	 * HG : not useful for the moment. will be useful when this JEditTextArea will extend JTexComponent instead of
	 * JComponent.
	 * <p>
	 * This is not now the case as a new UI manager class (like BasicTextUI) must be created, and also a new
	 * EditorKit...
	 *
	 * @return the string "EditorPaneUI"
	 * @see JComponent#getUIClassID
	 */
	@Override
	public String getUIClassID() {
		return uiClassID;
	}

	// **********************************************
	// KEYMAPS METHODS
	// are copied from JTextComponent
	// will no longer be useful when JEditTExtArea will extend JTextComponent
	// **********************************************
	/**
	 * Updates the <code>InputMap</code>s in response to a <code>Keymap</code> change.
	 *
	 * @param oldKm
	 *            the old <code>Keymap</code>
	 * @param newKm
	 *            the new <code>Keymap</code>
	 */
	void updateInputMap(Keymap oldKm, Keymap newKm) {
		// Locate the current KeymapWrapper.
		InputMap km = getInputMap(JComponent.WHEN_FOCUSED);
		InputMap last = km;
		while (km != null && !(km instanceof KeymapWrapper)) {
			last = km;
			km = km.getParent();
		}
		if(km != null) {
			// Found it, tweak the InputMap that points to it, as well
			// as anything it points to.
			if(newKm == null) {
				if(last != km) {
					last.setParent(km.getParent());
				} else {
					last.setParent(null);
				}
			} else {
				InputMap newKM = new KeymapWrapper(newKm);
				last.setParent(newKM);
				if(last != km) {
					newKM.setParent(km.getParent());
				}
			}
		} else if(newKm != null) {
			km = getInputMap(JComponent.WHEN_FOCUSED);
			if(km != null) {
				// Couldn't find it.
				// Set the parent of WHEN_FOCUSED InputMap to be the new one.
				InputMap newKM = new KeymapWrapper(newKm);
				newKM.setParent(km.getParent());
				km.setParent(newKM);
			}
		}

		// Do the same thing with the ActionMap
		ActionMap am = getActionMap();
		ActionMap lastAM = am;
		while (am != null && !(am instanceof KeymapActionMap)) {
			lastAM = am;
			am = am.getParent();
		}
		if(am != null) {
			// Found it, tweak the Actionap that points to it, as well
			// as anything it points to.
			if(newKm == null) {
				if(lastAM != am) {
					lastAM.setParent(am.getParent());
				} else {
					lastAM.setParent(null);
				}
			} else {
				ActionMap newAM = new KeymapActionMap(newKm);
				lastAM.setParent(newAM);
				if(lastAM != am) {
					newAM.setParent(am.getParent());
				}
			}
		} else if(newKm != null) {
			am = getActionMap();
			if(am != null) {
				// Couldn't find it.
				// Set the parent of ActionMap to be the new one.
				ActionMap newAM = new KeymapActionMap(newKm);
				newAM.setParent(am.getParent());
				am.setParent(newAM);
			}
		}
	}

	/**
	 * Fetches the keymap currently active in this text
	 * component.
	 *
	 * @return the keymap
	 */
	public Keymap getKeymap() {
		return keymap;
	}

	/**
	 * Adds a new keymap into the keymap hierarchy. Keymap bindings
	 * resolve from bottom up so an attribute specified in a child
	 * will override an attribute specified in the parent.
	 *
	 * @param nm
	 *            the name of the keymap (must be unique within the
	 *            collection of named keymaps in the document); the name may
	 *            be <code>null</code> if the keymap is unnamed,
	 *            but the caller is responsible for managing the reference
	 *            returned as an unnamed keymap can't
	 *            be fetched by name
	 * @param parent
	 *            the parent keymap; this may be <code>null</code> if
	 *            unspecified bindings need not be resolved in some other keymap
	 * @return the keymap
	 */
	public static Keymap addKeymap(String nm, Keymap parent) {
		Keymap map = new DefaultKeymap(nm, parent);
		if(nm != null) {
			// add a named keymap, a class of bindings
			keymapTable.put(nm, map);
		}
		return map;
	}

	/**
	 * Removes a named keymap previously added to the document. Keymaps
	 * with <code>null</code> names may not be removed in this way.
	 *
	 * @param nm
	 *            the name of the keymap to remove
	 * @return the keymap that was removed
	 */
	public static Keymap removeKeymap(String nm) {
		return (Keymap) keymapTable.remove(nm);
	}

	/**
	 * Fetches a named keymap previously added to the document.
	 * This does not work with <code>null</code>-named keymaps.
	 *
	 * @param nm
	 *            the name of the keymap
	 * @return the keymap
	 */
	public static Keymap getKeymap(String nm) {
		return (Keymap) keymapTable.get(nm);
	}

	/**
	 * Sets the keymap to use for binding events to
	 * actions. Setting to <code>null</code> effectively disables
	 * keyboard input.
	 * A PropertyChange event ("keymap") is fired when a new keymap
	 * is installed.
	 *
	 * @param map
	 *            the keymap
	 * @see #getKeymap
	 */
	public void setKeymap(Keymap map) {
		Keymap old = keymap;
		keymap = map;
		firePropertyChange("keymap", old, keymap);
		updateInputMap(old, map);
	}

	/**
	 * Binding record for creating key bindings.
	 * <p>
	 * <strong>Warning:</strong> Serialized objects of this class will not be compatible with future Swing releases. The
	 * current serialization support is appropriate for short term storage or RMI between applications running the same
	 * version of Swing. As of 1.4, support for long term storage of all JavaBeans<sup><font size="-2">TM</font></sup>
	 * has been added to the <code>java.beans</code> package.
	 */
	public static class KeyBinding {

		/**
		 * The key.
		 */
		public KeyStroke key;
		/**
		 * The name of the action for the key.
		 */
		public String actionName;

		/**
		 * Creates a new key binding.
		 *
		 * @param key
		 *            the key
		 * @param actionName
		 *            the name of the action for the key
		 */
		public KeyBinding(KeyStroke key, String actionName) {
			this.key = key;
			this.actionName = actionName;
		}
	}

	/**
	 * <p>
	 * Loads a keymap with a bunch of bindings. This can be used to take a static table of definitions and load them
	 * into some keymap. The following example illustrates an example of binding some keys to the cut, copy, and paste
	 * actions associated with a JTextComponent. A code fragment to accomplish this might look as follows:
	 *
	 * <pre>
	 * <code>
	 *
	 *   static final JTextComponent.KeyBinding[] defaultBindings = {
	 *     new JTextComponent.KeyBinding(
	 *       KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK),
	 *       DefaultEditorKit.copyAction),
	 *     new JTextComponent.KeyBinding(
	 *       KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK),
	 *       DefaultEditorKit.pasteAction),
	 *     new JTextComponent.KeyBinding(
	 *       KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK),
	 *       DefaultEditorKit.cutAction),
	 *   };
	 *
	 *   JTextComponent c = new JTextPane();
	 *   Keymap k = c.getKeymap();
	 *   JTextComponent.loadKeymap(k, defaultBindings, c.getActions());
	 *
	 * </code>
	 * </pre>
	 *
	 * The sets of bindings and actions may be empty but must be non- <code>null</code>.
	 *
	 * @param map
	 *            the keymap
	 * @param bindings
	 *            the bindings
	 * @param actions
	 *            the set of actions
	 */
	public static void loadKeymap(Keymap map, KeyBinding[] bindings, Action[] actions) {
		Map h = new HashMap();
		for (int i = 0; i < actions.length; i++) {
			Action a = actions[i];
			String value = (String) a.getValue(Action.NAME);
			h.put((value != null ? value : ""), a);
		}
		for (int i = 0; i < bindings.length; i++) {
			Action a = (Action) h.get(bindings[i].actionName);
			if(a != null) {
				map.addActionForKeyStroke(bindings[i].key, a);
			}
		}
	}

	// END OF KEYMAP METHODS
	public String setDefaultKeymap() {
		String key = "defaultKeyMap";

		keymap = addKeymap(key, this.getKeymap());

		/* Creation des actions */
		AbstractAction copyaction = new AbstractAction("Copy") {

			@Override
			public void actionPerformed(ActionEvent ae) {
				copy();
			}
		};
		AbstractAction cutaction = new AbstractAction("Cut") {

			@Override
			public void actionPerformed(ActionEvent ae) {
				cut();
			}
		};
		AbstractAction pasteaction = new AbstractAction("Paste") {

			@Override
			public void actionPerformed(ActionEvent ae) {
				paste();
			}
		};
		KeyStroke keycopy = KeyStroke.getKeyStroke(KeyEvent.VK_C, Event.CTRL_MASK);
		keymap.addActionForKeyStroke(keycopy, copyaction);
		KeyStroke keycut = KeyStroke.getKeyStroke(KeyEvent.VK_X, Event.CTRL_MASK);
		keymap.addActionForKeyStroke(keycut, cutaction);
		KeyStroke keypaste = KeyStroke.getKeyStroke(KeyEvent.VK_V, Event.CTRL_MASK);
		keymap.addActionForKeyStroke(keypaste, pasteaction);

		// 追加分
		AbstractAction saveaction = new AbstractAction("Save") {

			@Override
			public void actionPerformed(ActionEvent ae) {
				parent.getParent().doSave();
			}
		};
		AbstractAction compileaction = new AbstractAction("Compile") {

			@Override
			public void actionPerformed(ActionEvent ae) {
				parent.getParent().doCompile();
			}
		};
		AbstractAction runaction = new AbstractAction("run") {

			@Override
			public void actionPerformed(ActionEvent ae) {
				parent.getParent().doRun();
			}
		};
		KeyStroke keysave = KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK);
		keymap.addActionForKeyStroke(keysave, saveaction);
		KeyStroke keycompile = KeyStroke.getKeyStroke(KeyEvent.VK_E, Event.CTRL_MASK);
		KeyStroke keycompile2 = KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0);
		keymap.addActionForKeyStroke(keycompile, compileaction);
		keymap.addActionForKeyStroke(keycompile2, compileaction);
		KeyStroke keyrun = KeyStroke.getKeyStroke(KeyEvent.VK_R, Event.CTRL_MASK);
		KeyStroke keyrun2 = KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0);
		keymap.addActionForKeyStroke(keyrun, runaction);
		keymap.addActionForKeyStroke(keyrun2, runaction);

		return key;
	}

	public void manageEventforKey(KeyEvent event) {
		KeyStroke key = KeyStroke.getKeyStrokeForEvent(event);
		if(keymap.isLocallyDefined(key)) {
			Action action = keymap.getAction(key);
			if(action != null) {
				ActionEvent evt = new ActionEvent(event.getSource(), ActionEvent.ACTION_PERFORMED, String.valueOf(event
						.getKeyChar()));
				action.actionPerformed(evt);
			}
		}
	}

	public void setDefaults(CodeEditorDefaults defaults) {
		painter.setDefaults(defaults);
	}

	public void setHighlighter(CodeEditorHighlighter highlighter) {
		painter.setHighlighter(highlighter);
	}

	public void removeHighlighter() {
		painter.removeHighlighter();
	}

	public void setLineNumberOffset(int offset) {
		this.lineNumberOffset = offset;
	}

	public int getLineNumberOffset() {
		return lineNumberOffset;
	}

	public JScrollBar getHorizontalScrollBar() {
		return horizontal;
	}

	public JScrollBar getVerticalScrollBar() {
		return vertical;
	}

	/**
	 * Returns if this component can be traversed by pressing
	 * the Tab key. This returns false.
	 * HG: deleted because it is deprecated
	 * public final boolean isManagingFocus() {
	 * return true;
	 * }
	 */
	/**
	 * Returns the object responsible for painting this text area.
	 */
	public final CodeEditorPainter getPainter() {
		return painter;
	}

	/**
	 * Returns the gutter to the left of the text area or null if the gutter
	 * is disabled
	 */
	public final Gutter getGutter() {
		return gutter;
	}

	/**
	 * Returns the input handler.
	 */
	public final InputHandler getInputHandler() {
		return inputHandler;
	}

	/**
	 * Go to anchor position
	 */
	public void gotoAnchor() {
		if(anchor == null) {
			getToolkit().beep();
		} else {
			setCaretPosition(anchor.getOffset());
		}
	}

	/**
	 * Set the anchor postion.
	 */
	public void setAnchor() {
		try {
			anchor = document.createPosition(getCaretPosition());
		} catch (BadLocationException ble) {
		}
	}

	public int getAnchorOffset() {
		if(anchor == null) {
			return -1;
		} else {
			return anchor.getOffset();
		}
	}

	/**
	 * Sets the input handler.
	 *
	 * @param inputHandler
	 *            The new input handler
	 */
	public void setInputHandler(InputHandler inputHandler) {
		this.inputHandler = inputHandler;
	}

	/**
	 * Returns true if the caret is blinking, false otherwise.
	 */
	public final boolean isCaretBlinkEnabled() {
		return caretBlinks;
	}

	/**
	 * Toggles caret blinking.
	 *
	 * @param caretBlinks
	 *            True if the caret should blink, false otherwise
	 */
	public void setCaretBlinkEnabled(boolean caretBlinks) {
		this.caretBlinks = caretBlinks;
		if(!caretBlinks) {
			blink = false;
		}

		painter.invalidateSelectedLines();
	}

	/**
	 * Returns true if the caret is visible, false otherwise.
	 */
	public final boolean isCaretVisible() {
		return (!caretBlinks || blink) && caretVisible;
	}

	/**
	 * Sets if the caret should be visible.
	 *
	 * @param caretVisible
	 *            True if the caret should be visible, false
	 *            otherwise
	 */
	public void setCaretVisible(boolean caretVisible) {
		this.caretVisible = caretVisible;
		blink = true;

		painter.invalidateSelectedLines();
	}

	/**
	 * Blinks the caret.
	 */
	public final void blinkCaret() {
		if(caretBlinks) {
			blink = !blink;
			painter.invalidateSelectedLines();
		} else {
			blink = true;
		}
	}

	/**
	 * Returns the number of lines from the top and button of the
	 * text area that are always visible.
	 */
	public final int getElectricScroll() {
		return electricScroll;
	}

	/**
	 * Sets the number of lines from the top and bottom of the text
	 * area that are always visible
	 *
	 * @param electricScroll
	 *            The number of lines always visible from
	 *            the top or bottom
	 */
	public final void setElectricScroll(int electricScroll) {
		this.electricScroll = electricScroll;
	}

	/**
	 * Updates the state of the scroll bars. This should be called
	 * if the number of lines in the document changes, or when the
	 * size of the text are changes.
	 */
	public void updateScrollBars() {
		if(vertical != null && visibleLines != 0) {
			int lineCount = getLineCount();
			if(firstLine < 0) {
				setFirstLine(0);
				return;
			} else if(lineCount < firstLine + visibleLines) {
				int newFirstLine = Math.max(0, lineCount - visibleLines);
				if(newFirstLine != firstLine) {
					setFirstLine(newFirstLine);
					return;
				}
			}
			vertical.setValues(firstLine, visibleLines, 0, getLineCount());
			vertical.setUnitIncrement(2);
			vertical.setBlockIncrement(visibleLines);
			vertical.setBlockIncrement(0);
		}

		int width = painter.getWidth();
		if(horizontal != null && width != 0) {
			maxHorizontalScrollWidth = 0;
			painter.repaint();

			horizontal.setUnitIncrement(painter.getFontMetrics().charWidth('w'));
			horizontal.setBlockIncrement(width / 2);
		}
	}

	/**
	 * Returns the line displayed at the text area's origin.
	 */
	public final int getFirstLine() {
		return firstLine;
	}

	/**
	 * Sets the line displayed at the text area's origin without
	 * updating the scroll bars.
	 */
	public void setFirstLine(int firstLine) {
		if(firstLine == this.firstLine) {
			return;
		}
		int oldFirstLine = this.firstLine;
		this.firstLine = firstLine;
		maxHorizontalScrollWidth = 0;
		if(firstLine != vertical.getValue()) {
			updateScrollBars();
		}
		painter.repaint();
		gutter.repaint();
	}

	/**
	 * Sets the line displayed at the text area's origin without
	 * updating the scroll bars.
	 */
	public void offsetVerticalScroll(int verticaloffset) {
		// System.out.println(verticaloffset);
		int firstLine = this.firstLine + verticaloffset;
		if(firstLine == this.firstLine) {
			return;
		}
		int oldFirstLine = this.firstLine;
		this.firstLine = firstLine;
		maxHorizontalScrollWidth = 0;
		if(firstLine != vertical.getValue()) {
			updateScrollBars();
		}
		painter.repaint();
		gutter.repaint();
	}

	/**
	 * Returns the number of lines visible in this text area.
	 */
	public final int getVisibleLines() {
		return visibleLines;
	}

	/**
	 * Recalculates the number of visible lines. This should not
	 * be called directly.
	 */
	public final void recalculateVisibleLines() {
		if(painter == null) {
			return;
		}

		int height = painter.getHeight();
		int lineHeight = painter.getFontMetrics().getHeight();
		int oldVisibleLines = visibleLines;
		visibleLines = height / lineHeight;
		updateScrollBars();
	}

	void updateMaxHorizontalScrollWidth() {
		int _maxHorizontalScrollWidth = getTokenMarker().getMaxLineWidth(firstLine, visibleLines);
		if(_maxHorizontalScrollWidth != maxHorizontalScrollWidth) {
			maxHorizontalScrollWidth = _maxHorizontalScrollWidth;
			horizontal.setValues(-horizontalOffset, painter.getWidth(), 0, maxHorizontalScrollWidth
					+ painter.getFontMetrics().charWidth('w'));
		}
	}

	/**
	 * Returns the horizontal offset of drawn lines.
	 */
	public final int getHorizontalOffset() {
		return horizontalOffset;
	}

	/**
	 * Sets the horizontal offset of drawn lines. This can be used to
	 * implement horizontal scrolling.
	 *
	 * @param horizontalOffset
	 *            offset The new horizontal offset
	 */
	public void setHorizontalOffset(int horizontalOffset) {
		if(horizontalOffset == this.horizontalOffset) {
			return;
		}

		this.horizontalOffset = horizontalOffset;

		if(horizontalOffset != horizontal.getValue()) {
			updateScrollBars();
		}

		painter.repaint();
	}

	/**
	 * A fast way of changing both the first line and horizontal
	 * offset.
	 *
	 * @param firstLine
	 *            The new first line
	 * @param horizontalOffset
	 *            The new horizontal offset
	 * @return True if any of the values were changed, false otherwise
	 */
	public boolean setOrigin(int firstLine, int horizontalOffset) {
		boolean changed = false;
		int oldFirstLine = this.firstLine;

		if(horizontalOffset != this.horizontalOffset) {
			this.horizontalOffset = horizontalOffset;
			changed = true;
		}

		if(firstLine != this.firstLine) {
			this.firstLine = firstLine;
			changed = true;
		}

		if(changed) {
			updateScrollBars();
			painter.repaint();
			gutter.repaint();
		}

		return changed;
	}

	/**
	 * Ensures that the caret is visible by scrolling the text area if
	 * necessary.
	 *
	 * @return True if scrolling was actually performed, false if the
	 *         caret was already visible
	 */
	public boolean scrollToCaret() {
		int line = getCaretLine();
		int lineStart = getLineStartOffset(line);
		int offset = Math.max(0, Math.min(getLineLength(line) - 1, getCaretPosition() - lineStart));

		return scrollTo(line, offset);
	}

	/**
	 * Ensures that the specified line and offset is visible by scrolling
	 * the text area if necessary.
	 *
	 * @param line
	 *            The line to scroll to
	 * @param offset
	 *            The offset in the line to scroll to
	 * @return True if scrolling was actually performed, false if the
	 *         line and offset was already visible
	 */
	public boolean scrollTo(int line, int offset) {
		// visibleLines == 0 before the component is realized
		// we can't do any proper scrolling then, so we have
		// this hack...
		if(visibleLines == 0) {
			/* HG: changed to begin at the begining of the text
			 * setFirstLine(Math.max(0,line - electricScroll)); */
			return true;
		}

		int newFirstLine = firstLine;
		int newHorizontalOffset = horizontalOffset;

		if(line < firstLine + electricScroll) {
			newFirstLine = Math.max(0, line - electricScroll);
		} else if(line + electricScroll >= firstLine + visibleLines) {
			newFirstLine = (line - visibleLines) + electricScroll + 1;
			if(newFirstLine + visibleLines >= getLineCount()) {
				newFirstLine = getLineCount() - visibleLines;
			}
			if(newFirstLine < 0) {
				newFirstLine = 0;
			}
		}

		int x = _offsetToX(line, offset);
		int width = painter.getFontMetrics().charWidth('w');

		if(x < 0) {
			newHorizontalOffset = Math.min(0, horizontalOffset - x + width + 5);
		} else if(x + width >= painter.getWidth()) {
			newHorizontalOffset = horizontalOffset + (painter.getWidth() - x) - width - 5;
		}
		return setOrigin(newFirstLine, newHorizontalOffset);
	}

	/**
	 * Converts a line index to a y co-ordinate.
	 *
	 * @param line
	 *            The line
	 */
	public int lineToY(int line) {
		FontMetrics fm = painter.getFontMetrics();
		return (line - firstLine) * fm.getHeight() - (fm.getLeading() + fm.getMaxDescent());
	}

	/**
	 * Converts a y co-ordinate to a line index.
	 *
	 * @param y
	 *            The y co-ordinate
	 */
	public int yToLine(int y) {
		FontMetrics fm = painter.getFontMetrics();
		int height = fm.getHeight();
		// 座標の調整　値は適当
		y -= 60;
		return Math.max(0, Math.min(getLineCount() - 1, y / height + firstLine));
	}

	/**
	 * Converts an offset in a line into an x co-ordinate. This is a
	 * slow version that can be used any time.
	 *
	 * @param line
	 *            The line
	 * @param offset
	 *            The offset, from the start of the line
	 */
	public final int offsetToX(int line, int offset) {
		// don't use cached tokens
		painter.currentLineTokens = null;
		return _offsetToX(line, offset);
	}

	public int getTabbedTextWidth2(Segment s, FontMetrics metrics, int x, TabExpander e, int startOffset) {
		// 日本語表示のための処理
		FontMetrics fm;
		String str = s.toString();
		int i = 0;
		int j = 0;
		while (j < str.length()) {
			i = j;
			fm = metrics;
			for (; j < str.length(); j++) {
				if(str.charAt(j) > '\u00ff') {
					break;
				}
			}
			String str2 = str.substring(i, j);
			Segment seg = new Segment(str2.toCharArray(), 0, j - i);
			x += Utilities.getTabbedTextWidth(seg, fm, x, painter, 0);
			i = j;
			fm = getFontMetrics(CodeEditorDefaults.getDefaults().jfont);
			for (; j < str.length(); j++) {
				if(str.charAt(j) <= '\u00ff') {
					break;
				}
			}
			str2 = str.substring(i, j);
			seg = new Segment(str2.toCharArray(), 0, j - i);
			x += Utilities.getTabbedTextWidth(seg, fm, x, painter, 0);
		}
		return x;
	}

	/**
	 * Converts an offset in a line into an x co-ordinate. This is a
	 * fast version that should only be used if no changes were made
	 * to the text since the last repaint.
	 *
	 * @param line
	 *            The line
	 * @param offset
	 *            The offset, from the start of the line
	 */
	public int _offsetToX(int line, int offset) {
		TokenMarker tokenMarker = getTokenMarker();

		/* Use painter's cached info for speed */
		FontMetrics fm = painter.getFontMetrics();

		getLineText(line, lineSegment);

		int segmentOffset = lineSegment.offset;
		int x = horizontalOffset;

		// System.out.println(lineSegment.toString() + "   " + lineSegment.length());

		/* If syntax coloring is disabled, do simple translation */
		if(tokenMarker == null) {
			lineSegment.count = offset;
			return x + getTabbedTextWidth2(lineSegment, fm, x, painter, 0);
			// return x + Utilities.getTabbedTextWidth(lineSegment, fm, x, painter, 0);
		} else {
			/* If syntax coloring is enabled, we have to do this because
			 * tokens can vary in width */
			Token tokens;
			if(painter.currentLineIndex == line && painter.currentLineTokens != null) {
				tokens = painter.currentLineTokens;
			} else {
				painter.currentLineIndex = line;
				tokens = painter.currentLineTokens = tokenMarker.markTokens(lineSegment, line);
			}

			Toolkit toolkit = painter.getToolkit();
			Font defaultFont = painter.getFont();
			SyntaxStyle[] styles = painter.getStyles();

			for (;;) {
				byte id = tokens.id;
				if(id == Token.END) {
					return x;
				}

				if(id == Token.NULL) {
					fm = painter.getFontMetrics();
				} else {
					fm = styles[id].getFontMetrics(defaultFont);
				}

				int length = tokens.length;

				if(offset + segmentOffset < lineSegment.offset + length) {
					lineSegment.count = offset - (lineSegment.offset - segmentOffset);
					// return x + Utilities.getTabbedTextWidth(lineSegment, fm, x, painter, 0);
					return getTabbedTextWidth2(lineSegment, fm, x, painter, 0);
				} else {
					lineSegment.count = length;
					// x += Utilities.getTabbedTextWidth(lineSegment, fm, x, painter, 0);
					x = getTabbedTextWidth2(lineSegment, fm, x, painter, 0);
					lineSegment.offset += length;
				}
				tokens = tokens.next;
			}
		}
	}

	/**
	 * Converts an x co-ordinate to an offset within a line.
	 *
	 * @param line
	 *            The line
	 * @param x
	 *            The x co-ordinate
	 */
	public int xToOffset(int line, int x) {
		TokenMarker tokenMarker = getTokenMarker();

		/* Use painter's cached info for speed */
		FontMetrics fm = painter.getFontMetrics();
		FontMetrics jfm = getFontMetrics(CodeEditorDefaults.getDefaults().jfont);

		getLineText(line, lineSegment);

		char[] segmentArray = lineSegment.array;
		int segmentOffset = lineSegment.offset;
		int segmentCount = lineSegment.count;

		int width = horizontalOffset;

		if(tokenMarker == null) {
			for (int i = 0; i < segmentCount; i++) {
				char c = segmentArray[i + segmentOffset];
				int charWidth;
				if(c == '\t') {
					charWidth = (int) painter.nextTabStop(width, i) - width;
				} else if(c > '\u00ff') {
					// 日本語なら
					charWidth = jfm.charWidth(c);
				} else {
					charWidth = fm.charWidth(c);
				}

				if(painter.isBlockCaretEnabled()) {
					if(x - charWidth <= width) {
						return i;
					}
				} else {
					if(x - charWidth / 2 <= width) {
						return i;
					}
				}
				width += charWidth;
			}
			return segmentCount;
		} else {
			Token tokens;
			if(painter.currentLineIndex == line && painter.currentLineTokens != null) {
				tokens = painter.currentLineTokens;
			} else {
				painter.currentLineIndex = line;
				tokens = painter.currentLineTokens = tokenMarker.markTokens(lineSegment, line);
			}

			int offset = 0;
			Toolkit toolkit = painter.getToolkit();
			Font defaultFont = painter.getFont();
			SyntaxStyle[] styles = painter.getStyles();

			for (;;) {
				byte id = tokens.id;
				if(id == Token.END) {
					return offset;
				}

				if(id == Token.NULL) {
					fm = painter.getFontMetrics();
				} else {
					fm = styles[id].getFontMetrics(defaultFont);
				}

				int length = tokens.length;

				for (int i = 0; i < length; i++) {
					char c = segmentArray[segmentOffset + offset + i];
					int charWidth;

					if(c == '\t') {
						charWidth = (int) painter.nextTabStop(width, offset + i) - width;
					} else if(c > '\u00ff') {
						// 日本語なら
						charWidth = jfm.charWidth(c);
					} else {
						charWidth = fm.charWidth(c);
					}

					if(painter.isBlockCaretEnabled()) {
						if(x - charWidth <= width) {
							return offset + i;
						}
					} else {
						if(x - charWidth / 2 <= width) {
							return offset + i;
						}
					}
					width += charWidth;
				}

				offset += length;
				tokens = tokens.next;
			}
		}
	}

	/**
	 * Converts a point to an offset, from the start of the text.
	 *
	 * @param x
	 *            The x co-ordinate of the point
	 * @param y
	 *            The y co-ordinate of the point
	 */
	public int xyToOffset(int x, int y) {
		int line = yToLine(y);
		int start = getLineStartOffset(line);
		return start + xToOffset(line, x);
	}

	/**
	 * Returns the document this text area is editing.
	 */
	public Document getDocument() {
		return document;
	}

	/**
	 * COnvenience method (returns same Document as getDocument()).
	 */
	public SyntaxDocument getSyntaxDocument() {
		return document;
	}

	/**
	 * Sets the document this text area is editing.
	 *
	 * @param document
	 *            The document
	 */
	public void setDocument(SyntaxDocument document) {
		if(this.document == document) {
			return;
		}
		if(this.document != null) {
			this.document.removeDocumentListener(documentHandler);
		}

		this.document = document;

		document.addDocumentListener(documentHandler);
		documentHandlerInstalled = true;

		maxHorizontalScrollWidth = 0;
		select(0, 0);
		updateScrollBars();
		painter.repaint();
		gutter.repaint();
	}

	/**
	 * Returns the document's token marker. Equivalent to calling <code>getDocument().getTokenMarker()</code>.
	 */
	public final TokenMarker getTokenMarker() {
		if(document.getTokenMarker() == null) {
			document.setTokenMarker(new TextTokenMarker());
		}
		return document.getTokenMarker();
	}

	/**
	 * Sets the document's token marker. Equivalent to caling <code>getDocument().setTokenMarker()</code>.
	 *
	 * @param tokenMarker
	 *            The token marker
	 */
	public final void setTokenMarker(TokenMarker tokenMarker) {
		document.setTokenMarker(tokenMarker);
		painter.repaint();
	}

	/**
	 * Returns the length of the document. Equivalent to calling <code>getDocument().getLength()</code>.
	 */
	public final int getDocumentLength() {
		return document.getLength();
	}

	/**
	 * Returns the number of lines in the document.
	 */
	public final int getLineCount() {
		return document.getDefaultRootElement().getElementCount();
	}

	/**
	 * Returns the line containing the specified offset.
	 *
	 * @param offset
	 *            The offset
	 */
	public final int getLineOfOffset(int offset) {
		return document.getDefaultRootElement().getElementIndex(offset);
	}

	/**
	 * Returns the start offset of the specified line.
	 *
	 * @param line
	 *            The line
	 * @return The start offset of the specified line, or -1 if the line is
	 *         invalid
	 */
	public int getLineStartOffset(int line) {
		Element lineElement = document.getDefaultRootElement().getElement(line);
		if(lineElement == null) {
			return -1;
		} else {
			return lineElement.getStartOffset();
		}
	}

	/**
	 * Returns the end offset of the specified line.
	 *
	 * @param line
	 *            The line
	 * @return The end offset of the specified line, or -1 if the line is
	 *         invalid.
	 */
	public int getLineEndOffset(int line) {
		Element lineElement = document.getDefaultRootElement().getElement(line);
		if(lineElement == null) {
			return -1;
		} else {
			return lineElement.getEndOffset();
		}
	}

	/**
	 * Returns the length of the specified line.
	 *
	 * @param line
	 *            The line
	 */
	public int getLineLength(int line) {
		Element lineElement = document.getDefaultRootElement().getElement(line);
		if(lineElement == null) {
			return -1;
		} else {
			return lineElement.getEndOffset() - lineElement.getStartOffset() - 1;
		}
	}

	/**
	 * Returns the entire text of this text area.
	 */
	public String getText() {
		try {
			return document.getText(0, document.getLength());
		} catch (BadLocationException bl) {
			bl.printStackTrace();
			return null;
		}
	}

	/**
	 * Sets the entire text of this text area.
	 */
	public void setText(String text) {
		try {
			document.remove(0, document.getLength());
			document.insertString(0, text, null);
			// this is to make sure that this setText can not be undon by the undo manager
			document.getUndoManager().discardAllEdits();
		} catch (BadLocationException bl) {
			bl.printStackTrace();
		}
	}

	/**
	 * Sets the entire text of this text area.
	 * <p>
	 * the Vector must contain the (String) lines of the text
	 * </p>
	 */
	public void setText(List<String> vtext) {
		try {
			document.remove(0, document.getLength());
			Iterator it = vtext.iterator();
			while (it.hasNext()) {
				String text = (String) (it.next()) + "\n";
				document.insertString(document.getLength(), text, null);
			}
			// this is to make sure that this setText can not be undon by the undo manager
			document.getUndoManager().discardAllEdits();
		} catch (ClassCastException e) {
			e.printStackTrace();
		} catch (BadLocationException bl) {
			bl.printStackTrace();
		}
	}

	/**
	 * append text at the end of this text area.
	 */
	public void appendText(String text) {
		try {
			document.insertString(document.getLength(), text, null);
		} catch (BadLocationException bl) {
			bl.printStackTrace();
		}
	}

	/**
	 * append a line of text at the end of this text area.
	 */
	public void appendTextline(String text) {
		appendText(text + "\n");
	}

	/**
	 * Returns the specified substring of the document.
	 *
	 * @param start
	 *            The start offset
	 * @param len
	 *            The length of the substring
	 * @return The substring, or null if the offsets are invalid
	 */
	public final String getText(int start, int len) {
		try {
			return document.getText(start, len);
		} catch (BadLocationException bl) {
			bl.printStackTrace();
			return null;
		}
	}

	/**
	 * Copies the specified substring of the document into a segment.
	 * If the offsets are invalid, the segment will contain a null string.
	 *
	 * @param start
	 *            The start offset
	 * @param len
	 *            The length of the substring
	 * @param segment
	 *            The segment
	 */
	public final void getText(int start, int len, Segment segment) {
		try {
			document.getText(start, len, segment);
		} catch (BadLocationException bl) {
			bl.printStackTrace();
			segment.offset = segment.count = 0;
		}
	}

	/**
	 * Returns the text on the specified line.
	 *
	 * @param lineIndex
	 *            The line
	 * @return The text, or null if the line is invalid
	 */
	public final String getLineText(int lineIndex) {
		int start = getLineStartOffset(lineIndex);
		return getText(start, getLineEndOffset(lineIndex) - start - 1);
	}

	/**
	 * Copies the text on the specified line into a segment. If the line
	 * is invalid, the segment will contain a null string.
	 *
	 * @param lineIndex
	 *            The line
	 */
	public final void getLineText(int lineIndex, Segment segment) {
		int start = getLineStartOffset(lineIndex);
		getText(start, getLineEndOffset(lineIndex) - start - 1, segment);
	}

	/**
	 * Returns the selection start offset.
	 */
	public final int getSelectionStart() {
		return selectionStart;
	}

	/**
	 * Returns the offset where the selection starts on the specified
	 * line.
	 */
	public int getSelectionStart(int line) {
		if(line == selectionStartLine) {
			return selectionStart;
		} else if(rectSelect) {
			Element map = document.getDefaultRootElement();
			int start = selectionStart - map.getElement(selectionStartLine).getStartOffset();

			Element lineElement = map.getElement(line);
			int lineStart = lineElement.getStartOffset();
			int lineEnd = lineElement.getEndOffset() - 1;
			return Math.min(lineEnd, lineStart + start);
		} else {
			return getLineStartOffset(line);
		}
	}

	/**
	 * Returns the selection start line.
	 */
	public final int getSelectionStartLine() {
		return selectionStartLine;
	}

	/**
	 * Sets the selection start. The new selection will be the new
	 * selection start and the old selection end.
	 *
	 * @param selectionStart
	 *            The selection start
	 * @see #select(int,int)
	 */
	public final void setSelectionStart(int selectionStart) {
		select(selectionStart, selectionEnd);
	}

	/**
	 * Returns the selection end offset.
	 */
	public final int getSelectionEnd() {
		return selectionEnd;
	}

	/**
	 * Returns the offset where the selection ends on the specified
	 * line.
	 */
	public int getSelectionEnd(int line) {
		if(line == selectionEndLine) {
			return selectionEnd;
		} else if(rectSelect) {
			Element map = document.getDefaultRootElement();
			int end = selectionEnd - map.getElement(selectionEndLine).getStartOffset();

			Element lineElement = map.getElement(line);
			int lineStart = lineElement.getStartOffset();
			int lineEnd = lineElement.getEndOffset() - 1;
			return Math.min(lineEnd, lineStart + end);
		} else {
			return getLineEndOffset(line) - 1;
		}
	}

	/**
	 * Returns the selection end line.
	 */
	public final int getSelectionEndLine() {
		return selectionEndLine;
	}

	/**
	 * Sets the selection end. The new selection will be the old
	 * selection start and the bew selection end.
	 *
	 * @param selectionEnd
	 *            The selection end
	 * @see #select(int,int)
	 */
	public final void setSelectionEnd(int selectionEnd) {
		select(selectionStart, selectionEnd);
	}

	/**
	 * Returns the caret position. This will either be the selection
	 * start or the selection end, depending on which direction the
	 * selection was made in.
	 */
	public final int getCaretPosition() {
		return (biasLeft ? selectionStart : selectionEnd);
	}

	/**
	 * Returns the caret line.
	 */
	public final int getCaretLine() {
		return (biasLeft ? selectionStartLine : selectionEndLine);
	}

	/**
	 * Returns the mark position. This will be the opposite selection
	 * bound to the caret position.
	 *
	 * @see #getCaretPosition()
	 */
	public final int getMarkPosition() {
		return (biasLeft ? selectionEnd : selectionStart);
	}

	/**
	 * Returns the mark line.
	 */
	public final int getMarkLine() {
		return (biasLeft ? selectionEndLine : selectionStartLine);
	}

	/**
	 * Sets the caret position. The new selection will consist of the
	 * caret position only (hence no text will be selected)
	 *
	 * @param caret
	 *            The caret position
	 * @see #select(int,int)
	 */
	public final void setCaretPosition(int caret) {
		select(caret, caret);
	}

	/**
	 * Selects all text in the document.
	 */
	public final void selectAll() {
		select(0, getDocumentLength());
	}

	/**
	 * Moves the mark to the caret position.
	 */
	public final void selectNone() {
		select(getCaretPosition(), getCaretPosition());
	}

	/**
	 * Selects from the start offset to the end offset. This is the
	 * general selection method used by all other selecting methods.
	 * The caret position will be start if start &lt; end, and end
	 * if end &gt; start.
	 *
	 * @param start
	 *            The start offset
	 * @param end
	 *            The end offset
	 */
	public void select(int start, int end) {
		int newStart, newEnd;
		boolean newBias;
		if(start <= end) {
			newStart = start;
			newEnd = end;
			newBias = false;
		} else {
			newStart = end;
			newEnd = start;
			newBias = true;
		}

		if(newStart < 0 || newEnd > getDocumentLength()) {
			throw new IllegalArgumentException("Bounds out of" + " range: " + newStart + "," + newEnd);
		}

		// If the new position is the same as the old, we don't
		// do all this crap, however we still do the stuff at
		// the end (clearing magic position, scrolling)
		if(newStart != selectionStart || newEnd != selectionEnd || newBias != biasLeft) {
			updateBracketHighlight(end);

			int newStartLine = getLineOfOffset(newStart);
			int newEndLine = getLineOfOffset(newEnd);

			painter.invalidateLineRange(selectionStartLine, selectionEndLine);
			selectionStart = newStart;
			selectionEnd = newEnd;
			selectionStartLine = newStartLine;
			selectionEndLine = newEndLine;
			painter.invalidateLineRange(selectionStartLine, selectionEndLine);
			painter.repaint();
			biasLeft = newBias;
			gutter.repaint();

			fireCaretEvent();
		}
		// When the user is typing, etc, we don't want the caret
		// to blink
		blink = true;
		caretTimer.restart();

		// Disable rectangle select if selection start = selection end
		if(selectionStart == selectionEnd) {
			rectSelect = false;
		}

		// Clear the `magic' caret position used by up/down
		magicCaret = -1;

		scrollToCaret();
	}

	/**
	 * Returns the selected text, or null if no selection is active.
	 */
	public final String getSelectedText() {
		if(selectionStart == selectionEnd) {
			return null;
		}

		if(rectSelect) {
			// Return each row of the selection on a new line

			Element map = document.getDefaultRootElement();

			int start = selectionStart - map.getElement(selectionStartLine).getStartOffset();
			int end = selectionEnd - map.getElement(selectionEndLine).getStartOffset();

			// Certain rectangles satisfy this condition...
			if(end < start) {
				int tmp = end;
				end = start;
				start = tmp;
			}

			StringBuilder buf = new StringBuilder();
			Segment seg = new Segment();

			for (int i = selectionStartLine; i <= selectionEndLine; i++) {
				Element lineElement = map.getElement(i);
				int lineStart = lineElement.getStartOffset();
				int lineEnd = lineElement.getEndOffset() - 1;
				int lineLen = lineEnd - lineStart;

				lineStart = Math.min(lineStart + start, lineEnd);
				lineLen = Math.min(end - start, lineEnd - lineStart);

				getText(lineStart, lineLen, seg);
				buf.append(seg.array, seg.offset, seg.count);

				if(i != selectionEndLine) {
					buf.append('\n');
				}
			}

			return buf.toString();
		} else {
			return getText(selectionStart, selectionEnd - selectionStart);
		}
	}

	/**
	 * Replaces the selection with the specified text.
	 *
	 * @param selectedText
	 *            The replacement text for the selection
	 */
	public void setSelectedText(String selectedText) {
		if(!editable) {
			throw new InternalError("Text component" + " read only");
		}

		try {
			if(rectSelect) {
				Element map = document.getDefaultRootElement();

				int start = selectionStart - map.getElement(selectionStartLine).getStartOffset();
				int end = selectionEnd - map.getElement(selectionEndLine).getStartOffset();

				// Certain rectangles satisfy this condition...
				if(end < start) {
					int tmp = end;
					end = start;
					start = tmp;
				}

				int lastNewline = 0;
				int currNewline = 0;

				for (int i = selectionStartLine; i <= selectionEndLine; i++) {
					Element lineElement = map.getElement(i);
					int lineStart = lineElement.getStartOffset();
					int lineEnd = lineElement.getEndOffset() - 1;
					int rectStart = Math.min(lineEnd, lineStart + start);

					document.remove(rectStart, Math.min(lineEnd - rectStart, end - start));

					if(selectedText == null) {
						continue;
					}

					currNewline = selectedText.indexOf('\n', lastNewline);
					if(currNewline == -1) {
						currNewline = selectedText.length();
					}

					document.insertString(rectStart, selectedText.substring(lastNewline, currNewline), null);

					lastNewline = Math.min(selectedText.length(), currNewline + 1);
				}

				if(selectedText != null && currNewline != selectedText.length()) {
					int offset = map.getElement(selectionEndLine).getEndOffset() - 1;
					document.insertString(offset, "\n", null);
					document.insertString(offset + 1, selectedText.substring(currNewline + 1), null);
				}
			} else {
				document.remove(selectionStart, selectionEnd - selectionStart);
				if(selectedText != null) {
					document.insertString(selectionStart, selectedText, null);
				}
			}
		} catch (BadLocationException bl) {
			bl.printStackTrace();
			throw new InternalError("Cannot replace" + " selection");
		}

		setCaretPosition(selectionEnd);
	}

	/**
	 * Returns true if this text area is editable, false otherwise.
	 */
	public final boolean isEditable() {
		return editable;
	}

	/**
	 * Sets if this component is editable.
	 *
	 * @param editable
	 *            True if this text area should be editable,
	 *            false otherwise
	 */
	public final void setEditable(boolean editable) {
		this.editable = editable;
	}

	/**
	 * Returns the right click popup menu.
	 */
	public final JPopupMenu getRightClickPopup() {
		return popup;
	}

	/**
	 * Sets the right click popup menu.
	 *
	 * @param popup
	 *            The popup
	 */
	public final void setRightClickPopup(JPopupMenu popup) {
		this.popup = popup;
	}

	/**
	 * Returns the `magic' caret position. This can be used to preserve
	 * the column position when moving up and down lines.
	 */
	public final int getMagicCaretPosition() {
		return magicCaret;
	}

	/**
	 * Sets the `magic' caret position. This can be used to preserve
	 * the column position when moving up and down lines.
	 *
	 * @param magicCaret
	 *            The magic caret position
	 */
	public final void setMagicCaretPosition(int magicCaret) {
		this.magicCaret = magicCaret;
	}

	/**
	 * Similar to <code>setSelectedText()</code>, but overstrikes the
	 * appropriate number of characters if overwrite mode is enabled.
	 *
	 * @param str
	 *            The string
	 * @see #setSelectedText(String)
	 * @see #isOverwriteEnabled()
	 */
	public void overwriteSetSelectedText(String str) {
		// Don't overstrike if there is a selection
		if(!overwrite || selectionStart != selectionEnd) {
			setSelectedText(str);
			return;
		}

		// Don't overstrike if we're on the end of
		// the line
		int caret = getCaretPosition();
		int caretLineEnd = getLineEndOffset(getCaretLine());
		if(caretLineEnd - caret <= str.length()) {
			setSelectedText(str);
			return;
		}

		try {
			document.remove(caret, str.length());
			document.insertString(caret, str, null);
		} catch (BadLocationException bl) {
			bl.printStackTrace();
		}
	}

	/**
	 * Returns true if overwrite mode is enabled, false otherwise.
	 */
	public final boolean isOverwriteEnabled() {
		return overwrite;
	}

	/**
	 * Sets if overwrite mode should be enabled.
	 *
	 * @param overwrite
	 *            True if overwrite mode should be enabled,
	 *            false otherwise.
	 */
	public final void setOverwriteEnabled(boolean overwrite) {
		this.overwrite = overwrite;
		painter.invalidateSelectedLines();
	}

	/**
	 * Returns true if the selection is rectangular, false otherwise.
	 */
	public final boolean isSelectionRectangular() {
		return rectSelect;
	}

	/**
	 * Sets if the selection should be rectangular.
	 *
	 * @param rectSelect
	 *            True if the selection should be rectangular,
	 *            false otherwise.
	 */
	public final void setSelectionRectangular(boolean rectSelect) {
		this.rectSelect = rectSelect;
		painter.invalidateSelectedLines();
	}

	/**
	 * Returns the position of the highlighted bracket (the bracket
	 * matching the one before the caret)
	 */
	public final int getBracketPosition() {
		return bracketPosition;
	}

	/**
	 * Returns the line of the highlighted bracket (the bracket
	 * matching the one before the caret)
	 */
	public final int getBracketLine() {
		return bracketLine;
	}

	/**
	 * Adds a caret change listener to this text area.
	 *
	 * @param listener
	 *            The listener
	 */
	public final void addCaretListener(CaretListener listener) {
		_listenerList.add(CaretListener.class, listener);
	}

	/**
	 * Removes a caret change listener from this text area.
	 *
	 * @param listener
	 *            The listener
	 */
	public final void removeCaretListener(CaretListener listener) {
		_listenerList.remove(CaretListener.class, listener);
	}

	/**
	 * Deletes the selected text from the text area and places it
	 * into the clipboard.
	 */
	public void cut() {
		if(editable) {
			copy();

			if(selectionStart == selectionEnd) {
				int line = getCaretLine();
				int start = getLineStartOffset(line);
				int end = getLineEndOffset(line);

				if(end == document.getLength() + 1) {
					end--;
				}

				try {
					document.remove(start, end - start);
				} catch (BadLocationException ble) {
				}
			} else {
				setSelectedText("");
			}
		}
	}

	/**
	 * Places the selected text into the clipboard.
	 */
	public void copy() {
		String selection;

		if(selectionStart == selectionEnd) {
			int line = getCaretLine();
			int start = getLineStartOffset(line);
			int end = getLineEndOffset(line);

			selection = getText(start, end - start);
			setSelectionStart(start);
			setSelectionEnd(start);
		} else {
			selection = getSelectedText();
		}

		Clipboard clipboard = getToolkit().getSystemClipboard();
		clipboard.setContents(new StringSelection(selection), null);
	}

	/**
	 * Inserts the clipboard contents into the text.
	 */
	public void paste() {
		if(editable) {
			Clipboard clipboard = getToolkit().getSystemClipboard();
			try {
				// The MacOS MRJ doesn't convert \r to \n,
				// so do it here
				String selection = ((String) clipboard.getContents(this).getTransferData(DataFlavor.stringFlavor))
						.replace('\r', '\n');
				setSelectedText(selection);
			} catch (Exception e) {
				getToolkit().beep();
			}
		}
	}

	/**
	 * Called by the AWT when this component is added to a parent.
	 * Adds document listener.
	 */
	@Override
	public void addNotify() {
		super.addNotify();

		if(!documentHandlerInstalled) {
			documentHandlerInstalled = true;
			document.addDocumentListener(documentHandler);
		}
	}

	/**
	 * Called by the AWT when this component is removed from it's parent.
	 * This clears the pointer to the currently focused component.
	 * Also removes document listener.
	 */
	@Override
	public void removeNotify() {
		super.removeNotify();
		if(focusedComponent == this) {
			focusedComponent = null;
		}

		if(documentHandlerInstalled) {
			document.removeDocumentListener(documentHandler);
			documentHandlerInstalled = false;
		}
	}

	/**
	 * Forwards key events directly to the input handler.
	 * This is slightly faster than using a KeyListener
	 * because some Swing overhead is avoided.
	 */
	@Override
	public void processKeyEvent(KeyEvent evt) {
		if(inputHandler == null) {
			return;
		}
		switch (evt.getID()) {
			case KeyEvent.KEY_TYPED:
				inputHandler.keyTyped(evt);
				break;
			case KeyEvent.KEY_PRESSED:
				int oldLineC = getLineCount();
				int oldLine = selectionStartLine;
				int offset = getCaretPosition() - getLineStartOffset(selectionStartLine);
				inputHandler.keyPressed(evt);
				manageEventforKey(evt);
				int newLineC = getLineCount();
				updateData(newLineC - oldLineC, oldLine, offset);
				break;
			case KeyEvent.KEY_RELEASED:
				inputHandler.keyReleased(evt);
				break;
		}
	}

	protected void fireCaretEvent() {
		Object[] listeners = _listenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i--) {
			if(listeners[i] == CaretListener.class) {
				((CaretListener) listeners[i + 1]).caretUpdate(caretEvent);
			}
		}
	}

	protected void updateBracketHighlight(int newCaretPosition) {
		if(newCaretPosition == 0) {
			bracketPosition = bracketLine = -1;
			return;
		}

		try {
			int offset = TextUtilities.findMatchingBracket(document, newCaretPosition - 1);
			if(offset != -1) {
				bracketLine = getLineOfOffset(offset);
				bracketPosition = offset - getLineStartOffset(bracketLine);
				return;
			}
		} catch (BadLocationException bl) {
			bl.printStackTrace();
		}
		bracketLine = bracketPosition = -1;
	}

	protected void documentChanged(DocumentEvent evt) {
		DocumentEvent.ElementChange ch = evt.getChange(document.getDefaultRootElement());
		int count;
		if(ch == null) {
			count = 0;
		} else {
			count = ch.getChildrenAdded().length - ch.getChildrenRemoved().length;
		}

		int line = getLineOfOffset(evt.getOffset());
		if(count == 0) {
			painter.invalidateLine(line);
		} // do magic stuff
		else if(line < firstLine) {
			setFirstLine(firstLine + count);
		} else { // end of magic stuff
			painter.invalidateLineRange(line, firstLine + visibleLines);
			gutter.repaint();
			updateScrollBars();
		}
		setEdited(true);
		setCompiled(false);
	}

	class ScrollLayout implements LayoutManager {

		// private members
		private Component center;
		private Component right;
		private Component bottom;
		private Component left;
		private Component leftOfScrollBar;

		@Override
		public void addLayoutComponent(String name, Component comp) {
			if(name.equals(CENTER)) {
				center = comp;
			} else if(name.equals(RIGHT)) {
				right = comp;
			} else if(name.equals(LEFT)) {
				left = comp;
			} else if(name.equals(BOTTOM)) {
				bottom = comp;
			} else if(name.equals(LEFT_OF_SCROLLBAR)) {
				leftOfScrollBar = comp;
			}
		}

		@Override
		public void removeLayoutComponent(Component comp) {
			if(center == comp) {
				center = null;
			}
			if(right == comp) {
				right = null;
			}
			if(left == comp) {
				left = null;
			}
			if(bottom == comp) {
				bottom = null;
			} else {
				leftOfScrollBar = null;
			}
		}

		@Override
		public Dimension preferredLayoutSize(Container parent) {
			Dimension dim = new Dimension();
			Insets insets = getInsets();
			dim.width = insets.left + insets.right;
			dim.height = insets.top + insets.bottom;

			Dimension leftPref = left.getPreferredSize();
			dim.width += leftPref.width;
			Dimension centerPref = center.getPreferredSize();
			dim.width += centerPref.width;
			dim.height += centerPref.height;
			Dimension rightPref = right.getPreferredSize();
			dim.width += rightPref.width;
			Dimension bottomPref = bottom.getPreferredSize();
			dim.height += bottomPref.height;

			return dim;
		}

		public Dimension minimumLayoutSize(Container parent) {
			Dimension dim = new Dimension();
			Insets insets = getInsets();
			dim.width = insets.left + insets.right;
			dim.height = insets.top + insets.bottom;

			Dimension leftPref = left.getMinimumSize();
			dim.width += leftPref.width;
			Dimension centerPref = center.getMinimumSize();
			dim.width += centerPref.width;
			dim.height += centerPref.height;
			Dimension rightPref = right.getMinimumSize();
			dim.width += rightPref.width;
			Dimension bottomPref = bottom.getMinimumSize();
			dim.height += bottomPref.height;

			return dim;
		}

		@Override
		public void layoutContainer(Container parent) {
			Dimension size = parent.getSize();
			Insets insets = parent.getInsets();
			int itop = insets.top;
			int ileft = insets.left;
			int ibottom = insets.bottom;
			int iright = insets.right;

			int rightWidth = right.getPreferredSize().width;
			int leftWidth = left.getPreferredSize().width;
			int bottomHeight = bottom.getPreferredSize().height;
			int centerWidth = size.width - leftWidth - rightWidth - ileft - iright;
			int centerHeight = size.height - bottomHeight - itop - ibottom;

			left.setBounds(ileft, itop, leftWidth, centerHeight);
			center.setBounds(ileft + leftWidth, itop, centerWidth, centerHeight);
			right.setBounds(ileft + leftWidth + centerWidth, itop, rightWidth, centerHeight);

			if(leftOfScrollBar != null) {
				Dimension dim = leftOfScrollBar.getPreferredSize();
				leftOfScrollBar.setBounds(ileft, itop + centerHeight, dim.width, bottomHeight);
				ileft += dim.width;
			}

			bottom.setBounds(ileft, itop + centerHeight, size.width - rightWidth - ileft - iright, bottomHeight);
		}
	}

	protected class CaretBlinker implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent evt) {
			if(focusedComponent != null && focusedComponent.hasFocus()) {
				focusedComponent.blinkCaret();
			}
		}
	}

	protected class MutableCaretEvent extends CaretEvent {

		protected MutableCaretEvent() {
			super(JEditor.this);
		}

		@Override
		public int getDot() {
			return getCaretPosition();
		}

		@Override
		public int getMark() {
			return getMarkPosition();
		}
	}

	class AdjustHandler implements AdjustmentListener {

		@Override
		public void adjustmentValueChanged(final AdjustmentEvent evt) {
			if(!scrollBarsInitialized) {
				return;
			}

			// If this is not done, mousePressed events accumilate
			// and the result is that scrolling doesn't stop after
			// the mouse is released
			SwingUtilities.invokeLater(new Runnable() {

				public void run() {
					if(evt.getAdjustable() == vertical) {
						setFirstLine(vertical.getValue());
					} else {
						setHorizontalOffset(-horizontal.getValue());
					}
				}
			});
		}
	}

	class MouseWheelHandler implements MouseWheelListener {

		@Override
		public void mouseWheelMoved(final MouseWheelEvent evt) {
			SwingUtilities.invokeLater(new Runnable() {

				public void run() {
					// offsetVerticalScroll(evt.getScrollAmount() * evt.getUnitsToScroll());
					if(evt.getUnitsToScroll() > 0) {
						offsetVerticalScroll(3);
					}
					if(evt.getUnitsToScroll() < 0) {
						offsetVerticalScroll(-3);
					}
				}
			});
		}
	}

	class ComponentHandler extends ComponentAdapter {

		@Override
		public void componentResized(ComponentEvent evt) {
			recalculateVisibleLines();
			scrollBarsInitialized = true;
		}
	}

	protected class DocumentHandler implements DocumentListener {

		@Override
		public void insertUpdate(DocumentEvent evt) {
			documentChanged(evt);

			int offset = evt.getOffset();
			int length = evt.getLength();

			int newStart;
			int newEnd;

			if(selectionStart > offset || (selectionStart == selectionEnd && selectionStart == offset)) {
				newStart = selectionStart + length;
			} else {
				newStart = selectionStart;
			}

			if(selectionEnd >= offset) {
				newEnd = selectionEnd + length;
			} else {
				newEnd = selectionEnd;
			}

			select(newStart, newEnd);
		}

		@Override
		public void removeUpdate(DocumentEvent evt) {
			documentChanged(evt);

			int offset = evt.getOffset();
			int length = evt.getLength();

			int newStart;
			int newEnd;

			if(selectionStart > offset) {
				if(selectionStart > offset + length) {
					newStart = selectionStart - length;
				} else {
					newStart = offset;
				}
			} else {
				newStart = selectionStart;
			}

			if(selectionEnd > offset) {
				if(selectionEnd > offset + length) {
					newEnd = selectionEnd - length;
				} else {
					newEnd = offset;
				}
			} else {
				newEnd = selectionEnd;
			}

			select(newStart, newEnd);
		}

		@Override
		public void changedUpdate(DocumentEvent evt) {
		}
	}

	class DragHandler implements MouseMotionListener {

		@Override
		public void mouseDragged(MouseEvent evt) {
			if(popup != null && popup.isVisible()) {
				return;
			}

			setSelectionRectangular((evt.getModifiers() & InputEvent.CTRL_MASK) != 0);
			select(getMarkPosition(), xyToOffset(evt.getX(), evt.getY()));
		}

		@Override
		public void mouseMoved(MouseEvent evt) {
		}
	}

	class FocusHandler implements FocusListener {

		@Override
		public void focusGained(FocusEvent evt) {
			setCaretVisible(true);
			focusedComponent = JEditor.this;
		}

		@Override
		public void focusLost(FocusEvent evt) {
			setCaretVisible(false);
			focusedComponent = null;
		}
	}

	class MouseHandler extends MouseAdapter {

		@Override
		public void mousePressed(MouseEvent evt) {
			requestFocusInWindow(); // HG: correct requestFocus method

			// Focus events not fired sometimes?
			setCaretVisible(true);
			focusedComponent = JEditor.this;

			if((evt.getModifiers() & InputEvent.BUTTON3_MASK) != 0 && popup != null) {
				popup.show(painter, evt.getX(), evt.getY());
				return;
			}

			int line = yToLine(evt.getY());
			int offset = xToOffset(line, evt.getX() - 15);
			int dot = getLineStartOffset(line) + offset;

			switch (evt.getClickCount()) {
				case 1:
					doSingleClick(evt, line, offset, dot);
					break;
				case 2:
					// It uses the bracket matching stuff, so
					// it can throw a BLE
					try {
						doDoubleClick(evt, line, offset, dot);
					} catch (BadLocationException bl) {
						bl.printStackTrace();
					}
					break;
				case 3:
					doTripleClick(evt, line, offset, dot);
					break;
			}
		}

		private void doSingleClick(MouseEvent evt, int line, int offset, int dot) {
			if((evt.getModifiers() & InputEvent.SHIFT_MASK) != 0) {
				rectSelect = (evt.getModifiers() & InputEvent.CTRL_MASK) != 0;
				select(getMarkPosition(), dot);
			} else {
				setCaretPosition(dot);
			}
		}

		private void doDoubleClick(MouseEvent evt, int line, int offset, int dot) throws BadLocationException {
			// Ignore empty lines
			if(getLineLength(line) == 0) {
				return;
			}

			try {
				int bracket = TextUtilities.findMatchingBracket(document, Math.max(0, dot - 1));
				if(bracket != -1) {
					int mark = getMarkPosition();
					// Hack
					if(bracket > mark) {
						bracket++;
						mark--;
					}
					select(mark, bracket);
					return;
				}
			} catch (BadLocationException bl) {
				bl.printStackTrace();
			}

			// Ok, it's not a bracket... select the word
			String lineText = getLineText(line);
			char ch = lineText.charAt(Math.max(0, offset - 1));

			String noWordSep = (String) document.getProperty("noWordSep");
			if(noWordSep == null) {
				noWordSep = "";
			}

			// If the user clicked on a non-letter char,
			// we select the surrounding non-letters
			boolean selectNoLetter = (!Character.isLetterOrDigit(ch) && noWordSep.indexOf(ch) == -1);

			int wordStart = 0;

			for (int i = offset - 1; i >= 0; i--) {
				ch = lineText.charAt(i);
				if(selectNoLetter ^ (!Character.isLetterOrDigit(ch) && noWordSep.indexOf(ch) == -1)) {
					wordStart = i + 1;
					break;
				}
			}

			int wordEnd = lineText.length();
			for (int i = offset; i < lineText.length(); i++) {
				ch = lineText.charAt(i);
				if(selectNoLetter ^ (!Character.isLetterOrDigit(ch) && noWordSep.indexOf(ch) == -1)) {
					wordEnd = i;
					break;
				}
			}

			int lineStart = getLineStartOffset(line);
			select(lineStart + wordStart, lineStart + wordEnd);
		}

		private void doTripleClick(MouseEvent evt, int line, int offset, int dot) {
			select(getLineStartOffset(line), getLineEndOffset(line) - 1);
		}
	}

	class CaretUndo extends AbstractUndoableEdit {

		private int start;
		private int end;
		private int newStart;
		private int newEnd;

		CaretUndo(int start, int end, int newStart, int newEnd) {
			this.start = start;
			this.end = end;

			this.newStart = newStart;
			this.newEnd = newEnd;
		}

		@Override
		public boolean isSignificant() {
			return false;
		}

		@Override
		public String getPresentationName() {
			return "caret move";
		}

		@Override
		public void undo() throws CannotUndoException {
			super.undo();
			select(start, end);
		}

		@Override
		public boolean addEdit(UndoableEdit edit) {
			if(edit instanceof CaretUndo) {
				CaretUndo cedit = (CaretUndo) edit;
				cedit.die();
				return true;
			} else {
				return false;
			}
		}

		@Override
		public String toString() {
			return getPresentationName() + "[start=" + start + ",end=" + end + "]";
		}
	}

	static class DefaultKeymap implements Keymap {

		DefaultKeymap(String nm, Keymap parent) {
			this.nm = nm;
			this.parent = parent;
			bindings = new Hashtable();
		}

		/**
		 * Fetch the default action to fire if a
		 * key is typed (ie a KEY_TYPED KeyEvent is received)
		 * and there is no binding for it. Typically this
		 * would be some action that inserts text so that
		 * the keymap doesn't require an action for each
		 * possible key.
		 */
		@Override
		public Action getDefaultAction() {
			if(defaultAction != null) {
				return defaultAction;
			}
			return (parent != null) ? parent.getDefaultAction() : null;
		}

		/**
		 * Set the default action to fire if a key is typed.
		 */
		@Override
		public void setDefaultAction(Action a) {
			defaultAction = a;
		}

		@Override
		public String getName() {
			return nm;
		}

		@Override
		public Action getAction(KeyStroke key) {
			Action a = (Action) bindings.get(key);
			if((a == null) && (parent != null)) {
				a = parent.getAction(key);
			}
			return a;
		}

		@Override
		public KeyStroke[] getBoundKeyStrokes() {
			KeyStroke[] keys = new KeyStroke[bindings.size()];
			int i = 0;
			for (Enumeration e = bindings.keys(); e.hasMoreElements();) {
				keys[i++] = (KeyStroke) e.nextElement();
			}
			return keys;
		}

		@Override
		public Action[] getBoundActions() {
			Action[] actions = new Action[bindings.size()];
			int i = 0;
			for (Enumeration e = bindings.elements(); e.hasMoreElements();) {
				actions[i++] = (Action) e.nextElement();
			}
			return actions;
		}

		public KeyStroke[] getKeyStrokesForAction(Action a) {
			if(a == null) {
				return null;
			}
			KeyStroke[] retValue = null;
			// Determine local bindings first.
			Vector keyStrokes = null;
			for (Enumeration _enum = bindings.keys(); _enum.hasMoreElements();) {
				Object key = _enum.nextElement();
				if(bindings.get(key) == a) {
					if(keyStrokes == null) {
						keyStrokes = new Vector();
					}
					keyStrokes.addElement(key);
				}
			}
			// See if the parent has any.
			if(parent != null) {
				KeyStroke[] pStrokes = parent.getKeyStrokesForAction(a);
				if(pStrokes != null) {
					// Remove any bindings defined in the parent that
					// are locally defined.
					int rCount = 0;
					for (int counter = pStrokes.length - 1; counter >= 0; counter--) {
						if(isLocallyDefined(pStrokes[counter])) {
							pStrokes[counter] = null;
							rCount++;
						}
					}
					if(rCount > 0 && rCount < pStrokes.length) {
						if(keyStrokes == null) {
							keyStrokes = new Vector();
						}
						for (int counter = pStrokes.length - 1; counter >= 0; counter--) {
							if(pStrokes[counter] != null) {
								keyStrokes.addElement(pStrokes[counter]);
							}
						}
					} else if(rCount == 0) {
						if(keyStrokes == null) {
							retValue = pStrokes;
						} else {
							retValue = new KeyStroke[keyStrokes.size() + pStrokes.length];
							keyStrokes.copyInto(retValue);
							System.arraycopy(pStrokes, 0, retValue, keyStrokes.size(), pStrokes.length);
							keyStrokes = null;
						}
					}
				}
			}
			if(keyStrokes != null) {
				retValue = new KeyStroke[keyStrokes.size()];
				keyStrokes.copyInto(retValue);
			}
			return retValue;
		}

		@Override
		public boolean isLocallyDefined(KeyStroke key) {
			return bindings.containsKey(key);
		}

		@Override
		public void addActionForKeyStroke(KeyStroke key, Action a) {
			bindings.put(key, a);
		}

		@Override
		public void removeKeyStrokeBinding(KeyStroke key) {
			bindings.remove(key);
		}

		@Override
		public void removeBindings() {
			bindings.clear();
		}

		@Override
		public Keymap getResolveParent() {
			return parent;
		}

		@Override
		public void setResolveParent(Keymap parent) {
			this.parent = parent;
		}

		/**
		 * String representation of the keymap... potentially
		 * a very long string.
		 */
		@Override
		public String toString() {
			return "Keymap[" + nm + "]" + bindings;
		}

		String nm;
		Keymap parent;
		Hashtable bindings;
		Action defaultAction;
	}

	/**
	 * KeymapWrapper wraps a Keymap inside an InputMap. For KeymapWrapper
	 * to be useful it must be used with a KeymapActionMap.
	 * KeymapWrapper for the most part, is an InputMap with two parents.
	 * The first parent visited is ALWAYS the Keymap, with the second
	 * parent being the parent inherited from InputMap. If <code>keymap.getAction</code> returns null, implying the
	 * Keymap
	 * does not have a binding for the KeyStroke,
	 * the parent is then visited. If the Keymap has a binding, the
	 * Action is returned, if not and the KeyStroke represents a
	 * KeyTyped event and the Keymap has a defaultAction, <code>DefaultActionKey</code> is returned.
	 * <p>
	 * KeymapActionMap is then able to transate the object passed in to either message the Keymap, or message its
	 * default implementation.
	 */
	static class KeymapWrapper extends InputMap {

		static final Object DefaultActionKey = new Object();
		private Keymap keymap;

		KeymapWrapper(Keymap keymap) {
			this.keymap = keymap;
		}

		@Override
		public KeyStroke[] keys() {
			KeyStroke[] sKeys = super.keys();
			KeyStroke[] keymapKeys = keymap.getBoundKeyStrokes();
			int sCount = (sKeys == null) ? 0 : sKeys.length;
			int keymapCount = (keymapKeys == null) ? 0 : keymapKeys.length;
			if(sCount == 0) {
				return keymapKeys;
			}
			if(keymapCount == 0) {
				return sKeys;
			}
			KeyStroke[] retValue = new KeyStroke[sCount + keymapCount];
			// There may be some duplication here...
			System.arraycopy(sKeys, 0, retValue, 0, sCount);
			System.arraycopy(keymapKeys, 0, retValue, sCount, keymapCount);
			return retValue;
		}

		@Override
		public int size() {
			// There may be some duplication here...
			KeyStroke[] keymapStrokes = keymap.getBoundKeyStrokes();
			int keymapCount = (keymapStrokes == null) ? 0 : keymapStrokes.length;
			return super.size() + keymapCount;
		}

		@Override
		public Object get(KeyStroke keyStroke) {
			Object retValue = keymap.getAction(keyStroke);
			if(retValue == null) {
				retValue = super.get(keyStroke);
				if(retValue == null && keyStroke.getKeyChar() != KeyEvent.CHAR_UNDEFINED
						&& keymap.getDefaultAction() != null) {
					// Implies this is a KeyTyped event, use the default
					// action.
					retValue = DefaultActionKey;
				}
			}
			return retValue;
		}
	}

	/**
	 * Wraps a Keymap inside an ActionMap. This is used with
	 * a KeymapWrapper. If <code>get</code> is passed in <code>KeymapWrapper.DefaultActionKey</code>, the default action
	 * is
	 * returned, otherwise if the key is an Action, it is returned.
	 */
	static class KeymapActionMap extends ActionMap {

		private Keymap keymap;

		KeymapActionMap(Keymap keymap) {
			this.keymap = keymap;
		}

		@Override
		public Object[] keys() {
			Object[] sKeys = super.keys();
			Object[] keymapKeys = keymap.getBoundActions();
			int sCount = (sKeys == null) ? 0 : sKeys.length;
			int keymapCount = (keymapKeys == null) ? 0 : keymapKeys.length;
			boolean hasDefault = (keymap.getDefaultAction() != null);
			if(hasDefault) {
				keymapCount++;
			}
			if(sCount == 0) {
				if(hasDefault) {
					Object[] retValue = new Object[keymapCount];
					if(keymapCount > 1) {
						System.arraycopy(keymapKeys, 0, retValue, 0, keymapCount - 1);
					}
					retValue[keymapCount - 1] = KeymapWrapper.DefaultActionKey;
					return retValue;
				}
				return keymapKeys;
			}
			if(keymapCount == 0) {
				return sKeys;
			}
			Object[] retValue = new Object[sCount + keymapCount];
			// There may be some duplication here...
			System.arraycopy(sKeys, 0, retValue, 0, sCount);
			if(hasDefault) {
				if(keymapCount > 1) {
					System.arraycopy(keymapKeys, 0, retValue, sCount, keymapCount - 1);
				}
				retValue[sCount + keymapCount - 1] = KeymapWrapper.DefaultActionKey;
			} else {
				System.arraycopy(keymapKeys, 0, retValue, sCount, keymapCount);
			}
			return retValue;
		}

		@Override
		public int size() {
			// There may be some duplication here...
			Object[] actions = keymap.getBoundActions();
			int keymapCount = (actions == null) ? 0 : actions.length;
			if(keymap.getDefaultAction() != null) {
				keymapCount++;
			}
			return super.size() + keymapCount;
		}

		public Action get(Object key) {
			Action retValue = super.get(key);
			if(retValue == null) {
				// Try the Keymap.
				if(key == KeymapWrapper.DefaultActionKey) {
					retValue = keymap.getDefaultAction();
				} else if(key instanceof Action) {
					// This is a little iffy, technically an Action is
					// a valid Key. We're assuming the Action came from
					// the InputMap though.
					retValue = (Action) key;
				}
			}
			return retValue;
		}
	}
}
