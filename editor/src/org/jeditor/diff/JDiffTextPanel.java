/*Copyright (c) 2010, Herve Girod.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * 
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 * 
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 * 
 * You should have received a copy of the GNU General Public License version
 * along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * This file is part of the jEditor library: see http://jeditor.sourceforge.net */

package org.jeditor.diff;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import org.jeditor.gui.CodeEditorDefaults;
import org.jeditor.gui.CodeEditorHighlighter;
import org.jeditor.gui.JEditor;
import org.jeditor.scripts.BasicTokenMarker;
import org.jeditor.scripts.base.TokenMarker;

/**
 * A Panel showing the differences between two text files.
 * 
 * @version 0.4
 */
public class JDiffTextPanel extends JComponent {

	protected JEditor rightText;
	protected JEditor leftText;
	protected CodeEditorDefaults defaults;
	protected CodeEditorHighlighter leftHL;
	protected CodeEditorHighlighter rightHL;
	protected List rightV;
	protected List leftV;
	public Color colorUpdated = Color.YELLOW;
	public Color colorAddedAfter = Color.GREEN;
	public Color colorAddedBefore = Color.LIGHT_GRAY;
	public boolean scrolllink = true;

	public abstract class Base {

		protected UnaryPredicate ignore = null;
		protected Object[] file0, file1;
		protected int first0, last0, first1, last1, deletes, inserts;
		protected List changes;

		protected Base(Object[] a, Object[] b) {
			file0 = a;
			file1 = b;
			changes = new ArrayList();
		}

		public List getChanges() {
			return changes;
		}

		/**
		 * Divide SCRIPT into pieces by calling HUNKFUN and
		 * print each piece with PRINTFUN.
		 * Both functions take one arg, an edit script.
		 * 
		 * PRINTFUN takes a subscript which belongs together (with a null
		 * link at the end) and prints it.
		 */
		public void print_script(Diff.change script) {
			Diff.change next = script;

			while (next != null) {
				Diff.change t, end;
				/* Find a set of changes that belong together. */
				t = next;
				end = hunkfun(next);
				/* Disconnect them from the rest of the changes,
				 * making them a hunk, and remember the rest for next iteration. */
				next = end.link;
				end.link = null;
				/* Print this hunk. */
				Change change = print_hunk(t);
				if(change != null)
					changes.add(change);
				/* Reconnect the script so it will all be freed properly. */
				end.link = next;
			}
		}

		/**
		 * Called with the tail of the script
		 * and returns the last link that belongs together with the start
		 * of the tail.
		 */
		protected Diff.change hunkfun(Diff.change hunk) {
			return hunk;
		}

		/**
		 * Look at a hunk of edit script and report the range of lines in each file
		 * that it applies to. HUNK is the start of the hunk, which is a chain
		 * of `struct change'. The first and last line numbers of file 0 are stored
		 * in *FIRST0 and *LAST0, and likewise for file 1 in *FIRST1 and *LAST1.
		 * Note that these are internal line numbers that count from 0.
		 * 
		 * If no lines from file 0 are deleted, then FIRST0 is LAST0+1.
		 * 
		 * Also set *DELETES nonzero if any lines of file 0 are deleted
		 * and set *INSERTS nonzero if any lines of file 1 are inserted.
		 * If only ignorable lines are inserted or deleted, both are
		 * set to 0.
		 */
		protected void analyze_hunk(Diff.change hunk) {
			int f0, l0 = 0, f1, l1 = 0, show_from = 0, show_to = 0;
			int i;
			Diff.change next;
			boolean nontrivial = (ignore == null);

			show_from = show_to = 0;
			f0 = hunk.line0;
			f1 = hunk.line1;

			for (next = hunk; next != null; next = next.link) {
				l0 = next.line0 + next.deleted - 1;
				l1 = next.line1 + next.inserted - 1;
				show_from += next.deleted;
				show_to += next.inserted;
				for (i = next.line0; i <= l0 && !nontrivial; i++)
					if(!ignore.execute(file0[i]))
						nontrivial = true;
				for (i = next.line1; i <= l1 && !nontrivial; i++)
					if(!ignore.execute(file1[i]))
						nontrivial = true;
			}

			first0 = f0;
			last0 = l0;
			first1 = f1;
			last1 = l1;

			/* If all inserted or deleted lines are ignorable,
			 * tell the caller to ignore this hunk. */

			if(!nontrivial)
				show_from = show_to = 0;
			deletes = show_from;
			inserts = show_to;
		}

		protected abstract Change print_hunk(Diff.change hunk);

		protected void print_1_line(String pre, Object linbuf) {
			System.out.println(pre + linbuf.toString());
		}

		/**
		 * Print a pair of line numbers with SEPCHAR, translated for file FILE.
		 * If the two numbers are identical, print just one number.
		 * 
		 * Args A and B are internal line numbers.
		 * We print the translated (real) line numbers.
		 */

		protected void print_number_range(char sepchar, int a, int b) {
			/* Note: we can have B < A in the case of a range of no lines.
			 * In this case, we should print the line number before the range,
			 * which is B. */
			if(++b > ++a) {
				System.out.print("" + a + sepchar + b);
			} else {
				System.out.print(b);
			}
		}

		public char change_letter(int inserts, int deletes) {
			if(inserts == 0)
				return 'd';
			else if(deletes == 0)
				return 'a';
			else
				return 'c';
		}
	}

	public class Change {

		public static final int UNCHANGED = 0;
		public static final int UPDATED = 1;
		public static final int DELETED = 2;
		public static final int ADDED = 3;
		public int first0, last0;
		public int first1, last1;
		public int type;

		public Change(int type) {
			this.type = type;
		}

		public Change(int first0, int last0, int first1, int last1, int type) {
			this.first0 = first0;
			this.last0 = last0;
			this.first1 = first1;
			this.last1 = last1;
			this.type = type;
		}
	}

	/**
	 * Creates two String Vectors + a Boolean Vector
	 * to represent the comparated texts
	 */
	public class GUIPrint extends Base {

		private Vector text0;
		private Vector text1;
		private Vector changeVector = new Vector();

		public GUIPrint(List a, List b) {
			super(a.toArray(), b.toArray());
		}

		public GUIPrint(Object[] a, Object[] b) {
			super(a, b);
		}

		/**
		 * Print a hunk of a normal diff.
		 * This is a contiguous portion of a complete edit script,
		 * describing changes in consecutive lines.
		 */

		protected Change print_hunk(Diff.change hunk) {
			/* Determine range of line numbers involved in each file. */
			analyze_hunk(hunk);
			if(deletes == 0 && inserts == 0) {
				return null;
			}

			Change change;
			if(inserts != 0 && deletes != 0) {
				change = new Change(Change.UPDATED);
			} else if(inserts != 0) {
				change = new Change(Change.ADDED);
			} else { // Change.DELETED
				change = new Change(Change.DELETED);
			}
			change.first0 = first0;
			change.first1 = first1;
			change.last0 = last0;
			change.last1 = last1;

			return change;
		}
	}

	public JDiffTextPanel() {
		super();
	}

	public JDiffTextPanel(List leftV, List rightV) {
		super();
		this.rightV = rightV;
		this.leftV = leftV;
		Diff d = new Diff(leftV.toArray(), rightV.toArray());
		Diff.change script = d.diff_2(false);
		Base p = new GUIPrint(leftV, rightV);
		p.print_script(script);

		defaults = CodeEditorDefaults.getDefaults();
		populatePanel(p.getChanges());
		showPanel();
	}

	public JDiffTextPanel(Object[] leftO, Object[] rightO) {
		super();
		this.rightV = new Vector(Arrays.asList(rightO));
		this.leftV = new Vector(Arrays.asList(leftO));
		Diff d = new Diff(leftO, rightO);
		Diff.change script = d.diff_2(false);
		Base p = new GUIPrint(leftO, rightO);
		p.print_script(script);

		defaults = CodeEditorDefaults.getDefaults();
		populatePanel(p.getChanges());
		showPanel();
	}

	public JDiffTextPanel(List leftV, List rightV, CodeEditorDefaults defaults) {
		this(leftV, rightV);
		this.defaults = defaults;
		setStyles(defaults);
	}

	public JDiffTextPanel(Object[] leftO, Object[] rightO, CodeEditorDefaults defaults) {
		this(leftO, rightO);
		this.defaults = defaults;
		setStyles(defaults);
	}

	public void setCodeEditorDefaults(CodeEditorDefaults defaults) {
		this.defaults = defaults;
	}

	public void setComparables(Vector leftV, Vector rightV) {
		this.rightV = rightV;
		this.leftV = leftV;
	}

	public void setComparables(Object leftO[], Object rightO[]) {
		this.rightV = new Vector(Arrays.asList(rightO));
		this.leftV = new Vector(Arrays.asList(leftO));
	}

	public void createPanel(JComponent leftC, JComponent rightC, int dist) {
		Diff d = new Diff(leftV.toArray(), rightV.toArray());
		Diff.change script = d.diff_2(false);
		Base p = new GUIPrint(leftV, rightV);
		p.print_script(script);
		createEditors();
		populatePanel(p.getChanges());
		createPanelImpl(leftC, rightC, dist);
	}

	protected void createPanelImpl(JComponent leftC, JComponent rightC, int dist) {
		final JScrollBar scrollbar2 = leftText.getVerticalScrollBar();
		final JScrollBar scrollbar1 = rightText.getVerticalScrollBar();
		scrollbar1.setPreferredSize(new Dimension(0, 0));
		scrollbar2.addAdjustmentListener(new AdjustmentListener() {

			public void adjustmentValueChanged(AdjustmentEvent e) {
				scrollbar1.setValue(scrollbar2.getValue());
			}
		});
		if(scrolllink) {
			final JScrollBar scrollbar3 = leftText.getHorizontalScrollBar();
			final JScrollBar scrollbar4 = rightText.getHorizontalScrollBar();
			scrollbar3.addAdjustmentListener(new AdjustmentListener() {

				public void adjustmentValueChanged(AdjustmentEvent e) {
					scrollbar4.setValue(scrollbar3.getValue());
				}
			});
			scrollbar4.addAdjustmentListener(new AdjustmentListener() {

				public void adjustmentValueChanged(AdjustmentEvent e) {
					scrollbar3.setValue(scrollbar4.getValue());
				}
			});
		}
		setLayout(new GridLayout(0, 2));
		if(leftC == null) {
			add(leftText);
		} else {
			JPanel pane = new JPanel();
			pane.setLayout(new BoxLayout(pane, 1));
			pane.add(leftC);
			pane.add(Box.createVerticalStrut(dist));
			pane.add(leftText);
			add(pane);
		}
		if(rightC == null) {
			add(rightText);
		} else {
			JPanel pane = new JPanel();
			pane.setLayout(new BoxLayout(pane, 1));
			pane.add(rightC);
			pane.add(Box.createVerticalStrut(5));
			pane.add(rightText);
			add(pane);
		}
	}

	protected int getnewLine(int first, int last) {
		if(first <= last) {
			return first - 1;
		} else {
			return last;
		}
	}

	public void setStyles(CodeEditorDefaults defaults) {
		this.defaults = defaults;
		leftText.setDefaults(defaults);
		rightText.setDefaults(defaults);
	}

	public void setScrollLink(boolean link) {
		this.scrolllink = link;
	}

	public JEditor getLeftEditor() {
		return leftText;
	}

	public JEditor getRightEditor() {
		return rightText;
	}

	public void setTokenMarker(TokenMarker marker) {
		leftText.setTokenMarker(marker);
		rightText.setTokenMarker(marker);
	}

	public void setDefaultKeymap() {
		leftText.setDefaultKeymap();
		rightText.setDefaultKeymap();
	}

	public void setColors(Color updated, Color addedBefore, Color addedAfter) {
		colorUpdated = updated;
		colorAddedBefore = addedBefore;
		colorAddedAfter = addedAfter;
	}

	private void createEditors() {
		if(defaults == null) {
			defaults = new CodeEditorDefaults();
		}
		leftText = new JEditor(defaults);
		rightText = new JEditor(defaults);
	}

	protected void populatePanel(List changes) {
		// initialize left and right panels
		createEditors();
		leftHL = new CodeEditorHighlighter();
		rightHL = new CodeEditorHighlighter();
		leftText.setHighlighter(leftHL);
		rightText.setHighlighter(rightHL);

		int maxleft = leftV.size();
		int maxright = rightV.size();

		// Iterate through changes
		Change change;
		Iterator it = changes.iterator();
		int first0, last0, first1, last1;
		int lastleft = -1;
		int curleft = -1;
		int lastright = -1;
		int curright = -1;

		while (it.hasNext()) {
			change = (Change) (it.next()); // get next Change

			// add lines before change in left doc
			for (int i = ++lastleft; i <= getnewLine(change.first0, change.last0); i++) {
				String s = (String) (leftV.get(i));
				leftText.appendTextline(s);
				curleft++;
			}
			lastleft = getnewLine(change.first0, change.last0);
			// add lines before change in right doc
			for (int i = ++lastright; i <= getnewLine(change.first1, change.last1); i++) {
				String s = (String) (rightV.get(i));
				rightText.appendTextline(s);
				curright++;
			}
			lastright = getnewLine(change.first1, change.last1);

			if(change.type == Change.UPDATED) {
				int delta0 = change.last0 - change.first0;
				int delta1 = change.last1 - change.first1;

				// add changed lines in left doc
				for (int i = change.first0; i <= change.last0; i++) {
					String s = (String) (leftV.get(i));
					leftText.appendTextline(s);
					curleft++;
					leftHL.append(curleft, colorUpdated);
				}
				// manage case when there is more lines in right part
				if(delta0 < delta1) {
					for (int i = 0; i < delta1 - delta0; i++) {
						leftText.appendTextline("");
						curleft++;
						leftHL.append(curleft, colorUpdated);
					}
				}
				// add changed lines in right doc
				for (int i = change.first1; i <= change.last1; i++) {
					String s = (String) (rightV.get(i));
					rightText.appendTextline(s);
					curright++;
					rightHL.append(curright, colorUpdated);
				}
				// manage case when there is more lines in left part
				if(delta1 < delta0) {
					for (int i = 0; i < delta0 - delta1; i++) {
						rightText.appendTextline("");
						curright++;
						rightHL.append(curright, colorUpdated);
					}
				}
				lastleft = change.last0;
				lastright = change.last1;
			} else if(change.type == Change.ADDED) {
				// add blanck lines in left doc
				for (int i = change.first1; i <= change.last1; i++) {
					leftText.appendTextline("");
					curleft++;
					leftHL.append(curleft, colorAddedBefore);
				}
				// add new lines in right doc
				for (int i = change.first1; i <= change.last1; i++) {
					String s = (String) (rightV.get(i));
					rightText.appendTextline(s);
					curright++;
					rightHL.append(curright, colorAddedAfter);
				}
				lastright = change.last1;
			} else if(change.type == Change.DELETED) {
				// add new lines in left doc
				for (int i = change.first0; i <= change.last0; i++) {
					String s = (String) (leftV.get(i));
					leftText.appendTextline(s);
					curleft++;
					leftHL.append(curleft, colorAddedAfter);
				}
				// add blanck lines in right doc
				for (int i = change.first0; i <= change.last0; i++) {
					rightText.appendTextline("");
					curright++;
					rightHL.append(curright, colorAddedBefore);
				}
				lastleft = change.last0;
			}
		}
		// add lines after all changes in left doc
		for (int i = ++lastleft; i <= leftV.size() - 1; i++) {
			String s = (String) (leftV.get(i));
			leftText.appendTextline(s);
		}
		// add lines after all changes in right doc
		for (int i = ++lastright; i <= rightV.size() - 1; i++) {
			String s = (String) (rightV.get(i));
			rightText.appendTextline(s);
		}
	}

	protected void showPanel() {
		final JScrollBar scrollbar2 = leftText.getVerticalScrollBar();
		final JScrollBar scrollbar1 = rightText.getVerticalScrollBar();
		scrollbar1.setPreferredSize(new Dimension(0, 0));
		scrollbar2.addAdjustmentListener(new AdjustmentListener() {

			public void adjustmentValueChanged(AdjustmentEvent e) {
				scrollbar1.setValue(scrollbar2.getValue());
			};
		});
		if(scrolllink) {
			final JScrollBar scrollbar3 = leftText.getHorizontalScrollBar();
			final JScrollBar scrollbar4 = rightText.getHorizontalScrollBar();
			scrollbar3.addAdjustmentListener(new AdjustmentListener() {

				public void adjustmentValueChanged(AdjustmentEvent e) {
					scrollbar4.setValue(scrollbar3.getValue());
				};
			});
			scrollbar4.addAdjustmentListener(new AdjustmentListener() {

				public void adjustmentValueChanged(AdjustmentEvent e) {
					scrollbar3.setValue(scrollbar4.getValue());
				};
			});
		}
		this.setLayout(new GridLayout(0, 2));
		this.add(leftText);
		this.add(rightText);
	}

	public static void main(String[] argv) throws IOException {
		String[] a = { "aaaTOTOaaa", "aaaTATAaaa", "aaaTATAaaa", "aaaTRRaaa" };
		String[] b = { "aaaTOTaaa", "aaaTATAaaa", "aaaTATA TIaaa", "aaaTATAaaa", "aaaTUTUaaa", "aaaTPPaaa",
				"aaaTRRaaa", "aaaTRIaaa" };

		JFrame frame = new JFrame("JDiffTextPanel");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container pane = frame.getContentPane();
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
		JDiffTextPanel panel = new JDiffTextPanel(a, b);
		panel.setTokenMarker(new BasicTokenMarker());
		panel.setStyles(new CodeEditorDefaults());
		pane.add(panel);
		frame.pack();
		frame.setVisible(true);
	}
}
