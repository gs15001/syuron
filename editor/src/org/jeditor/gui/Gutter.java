/* This file is part of the jEditor library: see http://jeditor.sourceforge.net
 * Copyright (C) 2010, 2012 Herve Girod
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

public class Gutter extends JComponent implements SwingConstants {

	// the JCodeEditor this gutter is attached to
	private JEditor editor;
	private int baseline = 0;
	private int ileft = 0;
	private Dimension gutterSize = new Dimension(0, 0);
	private Dimension collapsedSize = new Dimension(0, 0);
	private Color intervalHighlight;
	private Color caretMark;
	private Color anchorMark;
	private Color selectionMark;
	private FontMetrics fm;
	private int alignment;
	private int interval = 0;
	private boolean lineNumberingEnabled = true;
	private boolean collapsed = false;

	public Gutter(JEditor editor, CodeEditorDefaults defaults) {
		this.editor = editor;

		setBackground(defaults.gutterBgColor);
		setForeground(defaults.gutterFgColor);
		setHighlightedForeground(defaults.gutterHighlightColor);
		setCaretMark(defaults.caretMarkColor);
		setAnchorMark(defaults.anchorMarkColor);
		setSelectionMark(defaults.selectionMarkColor);

		setFont(defaults.gutterFont);
		setBorder(defaults.gutterBorderWidth, defaults.gutterBorderColor);
		setLineNumberAlignment(defaults.gutterNumberAlignment);

		setGutterWidth(defaults.gutterWidth);
		setCollapsed(defaults.gutterCollapsed);

		GutterMouseListener ml = new GutterMouseListener();
		addMouseListener(ml);
		addMouseMotionListener(ml);
	}

	public void paintComponent(Graphics gfx) {
		if(!collapsed) {
			// fill the background
			Rectangle r = gfx.getClipBounds();
			gfx.setColor(getBackground());
			gfx.fillRect(r.x, r.y, r.width, r.height);

			// paint line numbers, if they are enabled
			if(lineNumberingEnabled) {
				paintLineNumbers(gfx);
			}
		}
	}

	protected void paintLineNumbers(Graphics gfx) {
		int numberoffset = editor.getLineNumberOffset();
		FontMetrics pfm = editor.getPainter().getFontMetrics();
		int lineHeight = pfm.getHeight();
		int baseline = (int) Math.round((this.baseline + lineHeight - pfm.getMaxDescent()) / 2.0);

		int firstLine = editor.getFirstLine() + 1;
		int lastLine = firstLine + (getHeight() / lineHeight);

		int firstValidLine = (int) Math.max(1, firstLine);
		int lastValidLine = (int) Math.min(editor.getLineCount(), lastLine);

		gfx.setFont(getFont());
		gfx.setColor(getForeground());

		String number;

		for (int line = firstLine; line <= lastLine; line++, baseline += lineHeight) {
			// only print numbers for valid lines
			if(line < firstValidLine || line > lastValidLine) {
				continue;
			}

			number = Integer.toString(line + numberoffset);
			int offset;

			switch (alignment) {
				case RIGHT:
					offset = gutterSize.width - collapsedSize.width - (fm.stringWidth(number) + 1);
					break;
				case CENTER:
					offset = ((gutterSize.width - collapsedSize.width) - fm.stringWidth(number)) / 2;
					break;
				case LEFT:
				default:
					offset = 1;
			}

			if(interval > 1 && line % interval == 0) {
				gfx.setColor(getHighlightedForeground());
				gfx.drawString(number + numberoffset, ileft + offset, baseline);
				gfx.setColor(getForeground());
			} else {
				gfx.drawString(number + numberoffset, ileft + offset, baseline);
			}

			if(line == editor.getCaretLine() + 1) {
				gfx.setColor(caretMark);
				gfx.drawRect(ileft + offset - 8, baseline - 6, 4, 4);
			}

			int anchor = editor.getAnchorOffset();
			if(anchor != -1 && line == editor.getLineOfOffset(anchor) + 1) {
				gfx.setColor(anchorMark);
				gfx.drawRect(ileft + offset - 8, baseline - 6, 4, 4);
			}

			if(editor.getSelectionStart() == editor.getSelectionEnd()) {
				gfx.setColor(getForeground());
				continue;
			}

			if(line >= editor.getSelectionStartLine() + 1 && line <= editor.getSelectionEndLine() + 1) {
				gfx.setColor(selectionMark);
				gfx.fillRect(ileft + offset - 7, baseline - 5, 3, 3);
			}

			gfx.setColor(getForeground());
		}
	}

	/**
	 * Convenience method for setting a default matte border on the right
	 * with the specified border width and color
	 *
	 * @param width
	 *            The border width (in pixels)
	 * @param color
	 *            The border color
	 */
	public void setBorder(int width, Color color) {
		setBorder(BorderFactory.createMatteBorder(0, 0, 0, width, color));
	}

	/* JComponent.setBorder(Border) is overridden here to cache the left
	 * inset of the border (if any) to avoid having to fetch it during every
	 * repaint. */
	@Override
	public void setBorder(Border border) {
		super.setBorder(border);

		if(border == null) {
			ileft = 0;
			collapsedSize.width = 0;
			collapsedSize.height = 0;
		} else {
			Insets insets = border.getBorderInsets(this);
			ileft = insets.left;
			collapsedSize.width = insets.left + insets.right;
			collapsedSize.height = insets.top + insets.bottom;
		}
	}

	/* JComponent.setFont(Font) is overridden here to cache the baseline for
	 * the font. This avoids having to get the font metrics during every
	 * repaint. */
	@Override
	public void setFont(Font font) {
		super.setFont(font);
		fm = getFontMetrics(font);
		baseline = fm.getHeight() - fm.getMaxDescent();
	}

	/**
	 * Set the foreground color for highlighted line numbers
	 *
	 * @param highlight
	 *            The highlight color
	 */
	public void setHighlightedForeground(Color highlight) {
		intervalHighlight = highlight;
	}

	/**
	 * Get the foreground color for highlighted line numbers
	 *
	 * @return The highlight color
	 */
	public Color getHighlightedForeground() {
		return intervalHighlight;
	}

	public void setCaretMark(Color mark) {
		caretMark = mark;
	}

	public void setAnchorMark(Color mark) {
		anchorMark = mark;
	}

	public void setSelectionMark(Color mark) {
		selectionMark = mark;
	}

	/**
	 * Set the width of the expanded gutter
	 *
	 * @param width
	 *            The gutter width
	 */
	public void setGutterWidth(int width) {
		if(width < collapsedSize.width) {
			width = collapsedSize.width;
		}
		gutterSize.width = width;
		// if the gutter is expanded, ask the text area to revalidate
		// the layout to resize the gutter
		if(!collapsed) {
			editor.revalidate();
		}
	}

	/**
	 * Get the width of the expanded gutter
	 *
	 * @return The gutter width
	 */
	public int getGutterWidth() {
		return gutterSize.width;
	}

	/* Component.getPreferredSize() is overridden here to support the
	 * collapsing behavior. */
	@Override
	public Dimension getPreferredSize() {
		if(collapsed) {
			return collapsedSize;
		} else {
			return gutterSize;
		}
	}

	@Override
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}

	/**
	 * Identifies whether or not the line numbers are drawn in the gutter
	 *
	 * @return true if the line numbers are drawn, false otherwise
	 */
	public boolean isLineNumberingEnabled() {
		return lineNumberingEnabled;
	}

	/**
	 * Turns the line numbering on or off and causes the gutter to be
	 * repainted.
	 *
	 * @param enabled
	 *            true if line numbers are drawn, false otherwise
	 */
	public void setLineNumberingEnabled(boolean enabled) {
		if(lineNumberingEnabled == enabled) {
			return;
		}

		lineNumberingEnabled = enabled;
		repaint();
	}

	/**
	 * Identifies whether the horizontal alignment of the line numbers.
	 *
	 * @return Gutter.RIGHT, Gutter.CENTER, Gutter.LEFT
	 */
	public int getLineNumberAlignment() {
		return alignment;
	}

	/**
	 * Sets the horizontal alignment of the line numbers.
	 *
	 * @param alignment
	 *            Gutter.RIGHT, Gutter.CENTER, Gutter.LEFT
	 */
	public void setLineNumberAlignment(int alignment) {
		if(this.alignment == alignment) {
			return;
		}

		this.alignment = alignment;
		repaint();
	}

	/**
	 * Identifies whether the gutter is collapsed or expanded.
	 *
	 * @return true if the gutter is collapsed, false if it is expanded
	 */
	public boolean isCollapsed() {
		return collapsed;
	}

	/**
	 * Sets whether the gutter is collapsed or expanded and force the text
	 * area to update its layout if there is a change.
	 *
	 * @param collapsed
	 *            true if the gutter is collapsed,
	 *            false if it is expanded
	 */
	public void setCollapsed(boolean collapsed) {
		if(this.collapsed == collapsed) {
			return;
		}

		this.collapsed = collapsed;
		editor.revalidate();
	}

	/**
	 * Toggles whether the gutter is collapsed or expanded.
	 */
	public void toggleCollapsed() {
		setCollapsed(!collapsed);
	}

	class GutterMouseListener extends MouseAdapter implements MouseMotionListener {

		private Point dragStart = null;
		private int startWidth = 0;

		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
			dragStart = e.getPoint();
			startWidth = gutterSize.width;
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if(dragStart == null) {
				return;
			}
			if(isCollapsed()) {
				setCollapsed(false);
			}

			Point p = e.getPoint();
			gutterSize.width = startWidth + p.x - dragStart.x;

			if(gutterSize.width < collapsedSize.width) {
				gutterSize.width = startWidth;
				setCollapsed(true);
			}

			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					editor.revalidate();
				}
			});
		}

		@Override
		public void mouseExited(MouseEvent e) {
			if(dragStart != null && dragStart.x > e.getPoint().x) {
				setCollapsed(true);
				gutterSize.width = startWidth;

				SwingUtilities.invokeLater(new Runnable() {

					public void run() {
						editor.revalidate();
					}
				});
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			dragStart = null;
		}
	}
}
