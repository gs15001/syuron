/*
 * Copyright (C) 1999 Slava Pestov
 * Copyright (C) 2010, 2012, 2014 Herve Girod
 *
 * You may use and modify this package for any purpose. Redistribution is
 * permitted, in both source and binary form, provided that this notice
 * remains intact in all source distributions of this package.
 */
package org.jeditor.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.ToolTipManager;
import javax.swing.text.PlainDocument;
import javax.swing.text.Segment;
import javax.swing.text.StyleContext;
import javax.swing.text.TabExpander;
import javax.swing.text.Utilities;
import org.jeditor.scripts.base.SyntaxStyle;
import org.jeditor.scripts.base.SyntaxUtilities;
import org.jeditor.scripts.base.Token;
import org.jeditor.scripts.base.TokenMarker;

/**
 * The text area repaint manager. It performs double buffering and paints
 * lines of text.
 *
 * @version 0.4.2
 */
public class CodeEditorPainter extends JComponent implements TabExpander {
   // package-private members
   int currentLineIndex;
   Token currentLineTokens;
   Segment currentLine;
   // protected members
   protected JEditor editor;
   protected SyntaxStyle[] styles;
   protected Color caretColor;
   protected Color selectionColor;
   protected Color lineHighlightColor;
   protected Color bracketHighlightColor;
   protected Color eolMarkerColor;
   protected boolean blockCaret;
   protected boolean lineHighlight;
   protected boolean bracketHighlight;
   protected boolean paintInvalid;
   protected boolean eolMarkers;
   protected int cols;
   protected int rows;
   protected int tabSize;
   protected FontMetrics fm;
   protected Highlight highlights;
   protected CodeEditorHighlighter highlighter = null;

   /**
    * Creates a new repaint manager. This should be not be called
    * directly.
    */
   public CodeEditorPainter(JEditor editor, CodeEditorDefaults defaults) {
      this.editor = editor;

      setAutoscrolls(true);
      setDoubleBuffered(true);
      setOpaque(true);

      ToolTipManager.sharedInstance().registerComponent(this);

      currentLine = new Segment();
      currentLineIndex = -1;

      setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));

      setFont(defaults.font);
      setForeground(Color.black);
      setBackground(Color.white);

      blockCaret = defaults.blockCaret;
      styles = defaults.styles;
      cols = defaults.cols;
      rows = defaults.rows;
      caretColor = defaults.caretColor;
      selectionColor = defaults.selectionColor;
      lineHighlightColor = defaults.lineHighlightColor;
      lineHighlight = defaults.lineHighlight;
      bracketHighlightColor = defaults.bracketHighlightColor;
      bracketHighlight = defaults.bracketHighlight;
      paintInvalid = defaults.paintInvalid;
      eolMarkerColor = defaults.eolMarkerColor;
      eolMarkers = defaults.eolMarkers;
   }

   public void setDefaults(CodeEditorDefaults defaults) {
      setFont(defaults.font);

      blockCaret = defaults.blockCaret;
      styles = defaults.styles;
      cols = defaults.cols;
      rows = defaults.rows;
      caretColor = defaults.caretColor;
      selectionColor = defaults.selectionColor;
      lineHighlightColor = defaults.lineHighlightColor;
      lineHighlight = defaults.lineHighlight;
      bracketHighlightColor = defaults.bracketHighlightColor;
      bracketHighlight = defaults.bracketHighlight;
      paintInvalid = defaults.paintInvalid;
      eolMarkerColor = defaults.eolMarkerColor;
      eolMarkers = defaults.eolMarkers;
   }

   public int getTabSize() {
      return tabSize;
   }

   public void setHighlighter(CodeEditorHighlighter highlighter) {
      this.highlighter = highlighter;
   }

   public void removeHighlighter() {
      highlighter = null;
   }

   /**
    * Returns the syntax styles used to paint colorized text. Entry <i>n</i>
    * will be used to paint tokens with id = <i>n</i>.
    */
   public final SyntaxStyle[] getStyles() {
      return styles;
   }

   /**
    * Sets the syntax styles used to paint colorized text. Entry <i>n</i>
    * will be used to paint tokens with id = <i>n</i>.
    *
    * @param styles The syntax styles
    */
   public final void setStyles(SyntaxStyle[] styles) {
      this.styles = styles;
      repaint();
   }

   /**
    * Returns the caret color.
    */
   public final Color getCaretColor() {
      return caretColor;
   }

   /**
    * Sets the caret color.
    *
    * @param caretColor The caret color
    */
   public final void setCaretColor(Color caretColor) {
      this.caretColor = caretColor;
      invalidateSelectedLines();
   }

   /**
    * Returns the selection color.
    */
   public final Color getSelectionColor() {
      return selectionColor;
   }

   /**
    * Sets the selection color.
    *
    * @param selectionColor The selection color
    */
   public final void setSelectionColor(Color selectionColor) {
      this.selectionColor = selectionColor;
      invalidateSelectedLines();
   }

   /**
    * Returns the line highlight color.
    */
   public final Color getLineHighlightColor() {
      return lineHighlightColor;
   }

   /**
    * Sets the line highlight color.
    *
    * @param lineHighlightColor The line highlight color
    */
   public final void setLineHighlightColor(Color lineHighlightColor) {
      this.lineHighlightColor = lineHighlightColor;
      invalidateSelectedLines();
   }

   /**
    * Returns true if line highlight is enabled, false otherwise.
    */
   public final boolean isLineHighlightEnabled() {
      return lineHighlight;
   }

   /**
    * Enables or disables current line highlighting.
    *
    * @param lineHighlight True if current line highlight should be enabled,
    * false otherwise
    */
   public final void setLineHighlightEnabled(boolean lineHighlight) {
      this.lineHighlight = lineHighlight;
      invalidateSelectedLines();
   }

   /**
    * Returns the bracket highlight color.
    */
   public final Color getBracketHighlightColor() {
      return bracketHighlightColor;
   }

   /**
    * Sets the bracket highlight color.
    *
    * @param bracketHighlightColor The bracket highlight color
    */
   public final void setBracketHighlightColor(Color bracketHighlightColor) {
      this.bracketHighlightColor = bracketHighlightColor;
      invalidateLine(editor.getBracketLine());
   }

   /**
    * Returns true if bracket highlighting is enabled, false otherwise.
    * When bracket highlighting is enabled, the bracket matching the
    * one before the caret (if any) is highlighted.
    */
   public final boolean isBracketHighlightEnabled() {
      return bracketHighlight;
   }

   /**
    * Enables or disables bracket highlighting.
    * When bracket highlighting is enabled, the bracket matching the
    * one before the caret (if any) is highlighted.
    *
    * @param bracketHighlight True if bracket highlighting should be
    * enabled, false otherwise
    */
   public final void setBracketHighlightEnabled(boolean bracketHighlight) {
      this.bracketHighlight = bracketHighlight;
      invalidateLine(editor.getBracketLine());
   }

   /**
    * Returns true if the caret should be drawn as a block, false otherwise.
    */
   public final boolean isBlockCaretEnabled() {
      return blockCaret;
   }

   /**
    * Sets if the caret should be drawn as a block, false otherwise.
    *
    * @param blockCaret True if the caret should be drawn as a block,
    * false otherwise.
    */
   public final void setBlockCaretEnabled(boolean blockCaret) {
      this.blockCaret = blockCaret;
      invalidateSelectedLines();
   }

   /**
    * Returns the EOL marker color.
    */
   public final Color getEOLMarkerColor() {
      return eolMarkerColor;
   }

   /**
    * Sets the EOL marker color.
    *
    * @param eolMarkerColor The EOL marker color
    */
   public final void setEOLMarkerColor(Color eolMarkerColor) {
      this.eolMarkerColor = eolMarkerColor;
      repaint();
   }

   /**
    * Returns true if EOL markers are drawn, false otherwise.
    */
   public final boolean getEOLMarkersPainted() {
      return eolMarkers;
   }

   /**
    * Sets if EOL markers are to be drawn.
    *
    * @param eolMarkers True if EOL markers should be drawn, false otherwise
    */
   public final void setEOLMarkersPainted(boolean eolMarkers) {
      this.eolMarkers = eolMarkers;
      repaint();
   }

   /**
    * Returns true if invalid lines are painted as red tildes (~),
    * false otherwise.
    */
   public boolean getInvalidLinesPainted() {
      return paintInvalid;
   }

   /**
    * Sets if invalid lines are to be painted as red tildes.
    *
    * @param paintInvalid True if invalid lines should be drawn, false otherwise
    */
   public void setInvalidLinesPainted(boolean paintInvalid) {
      this.paintInvalid = paintInvalid;
   }

   /**
    * Adds a custom highlight painter.
    *
    * @param highlight The highlight
    */
   public void addCustomHighlight(Highlight highlight) {
      highlight.init(editor, highlights);
      highlights = highlight;
   }

   /**
    * Highlight interface.
    */
   public interface Highlight {
      /**
       * Called after the highlight painter has been added.
       *
       * @param editor The code editor
       * @param next the text to highlight
       */
      void init(JEditor editor, Highlight next);

      /**
       * This should paint the highlight and delgate to the
       * next highlight painter.
       *
       * @param gfx The graphics context
       * @param line The line number
       * @param y The y co-ordinate of the line
       */
      void paintHighlight(Graphics gfx, int line, int y);

      /**
       * Returns the tool tip to display at the specified
       * location. If this highlighter doesn't know what to
       * display, it should delegate to the next highlight
       * painter.
       *
       * @param evt The mouse event
       */
      String getToolTipText(MouseEvent evt);
   }

   /**
    * Returns the tool tip to display at the specified location.
    *
    * @param evt The mouse event
    */
   @Override
   public String getToolTipText(MouseEvent evt) {
      if (highlights != null) {
         return highlights.getToolTipText(evt);
      } else {
         return null;
      }
   }

   /**
    * Returns the font metrics used by this component.
    */
   public FontMetrics getFontMetrics() {
      return fm;
   }

   /**
    * Sets the font for this component. This is overridden to update the
    * cached font metrics and to recalculate which lines are visible.
    *
    * @param font The font
    */
   @Override
   public void setFont(Font font) {
      super.setFont(font);
      // fm = Toolkit.getDefaultToolkit().getFontMetrics(font);
      StyleContext context = new StyleContext();
      fm = context.getFontMetrics(font);
      editor.recalculateVisibleLines();
   }

   /**
    * Repaints the text.
    *
    * @param gfx The graphics context
    */
   @Override
   public void paint(Graphics gfx) {
      SyntaxDocument sdoc = editor.getSyntaxDocument();
      tabSize = fm.charWidth(' ') * ((Integer) sdoc.getProperty(PlainDocument.tabSizeAttribute)).intValue();

      Rectangle clipRect = gfx.getClipBounds();

      gfx.setColor(getBackground());
      gfx.fillRect(clipRect.x, clipRect.y, clipRect.width, clipRect.height);

      // We don't use yToLine() here because that method doesn't
      // return lines past the end of the document
      int height = fm.getHeight();
      int firstLine = editor.getFirstLine();
      int firstInvalid = firstLine + clipRect.y / height;
      int lastInvalid = firstLine + (clipRect.y + clipRect.height - 1) / height;
      int x = editor.getHorizontalOffset();
      int lineCount = editor.getLineCount();

      try {
         TokenMarker tokenMarker = sdoc.getTokenMarker();
         int maxWidth = editor.maxHorizontalScrollWidth;

         boolean updateMaxHorizontalScrollWidth = false;

         for (int line = firstInvalid; line <= lastInvalid; line++) {
            boolean valid = line >= 0 && line < lineCount;

            int width = paintLine(gfx, tokenMarker, line, x) - x + 5 /*
                     * Yay
                     */;
            if (valid) {
               if (tokenMarker != null) {
                  tokenMarker.setLineWidth(line, width);
               }
               if (width > maxWidth) {
                  updateMaxHorizontalScrollWidth = true;
               }
            }
         }

         if (tokenMarker != null && tokenMarker.isNextLineRequested()) {
            int h = clipRect.y + clipRect.height;
            repaint(0, h, getWidth(), getHeight() - h);
         }

         if (updateMaxHorizontalScrollWidth) {
            editor.updateMaxHorizontalScrollWidth();
         }
      } catch (Exception e) {
         System.err.println("Error repainting line" + " range {" + firstInvalid + "," + lastInvalid + "}:");
         e.printStackTrace();
      }
   }

   /**
    * Marks a line as needing a repaint.
    *
    * @param line The line to invalidate
    */
   public final void invalidateLine(int line) {
      repaint(0, editor.lineToY(line) + fm.getMaxDescent() + fm.getLeading(), getWidth(), fm.getHeight());
   }

   /**
    * Marks a range of lines as needing a repaint.
    *
    * @param firstLine The first line to invalidate
    * @param lastLine The last line to invalidate
    */
   public final void invalidateLineRange(int firstLine, int lastLine) {
      repaint(0, editor.lineToY(firstLine) + fm.getMaxDescent() + fm.getLeading(),
              getWidth(), (lastLine - firstLine + 1) * fm.getHeight());
   }

   /**
    * Repaints the lines containing the selection.
    */
   public final void invalidateSelectedLines() {
      invalidateLineRange(editor.getSelectionStartLine(), editor.getSelectionEndLine());
   }

   /**
    * Implementation of TabExpander interface. Returns next tab stop after
    * a specified point.
    *
    * @param x The x co-ordinate
    * @param tabOffset Ignored
    * @return The next tab stop after <i>x</i>
    */
   @Override
   public float nextTabStop(float x, int tabOffset) {
      int offset = editor.getHorizontalOffset();
      int ntabs = ((int) x - offset) / tabSize;
      return (ntabs + 1) * tabSize + offset;
   }

   /**
    * Returns the painter's preferred size.
    */
   @Override
   public Dimension getPreferredSize() {
      Dimension dim = new Dimension();
      dim.width = fm.charWidth('w') * cols;
      dim.height = fm.getHeight() * rows;
      return dim;
   }

   /**
    * Returns the painter's minimum size.
    */
   @Override
   public Dimension getMinimumSize() {
      //return getPreferredSize();
      return new Dimension(50, 50);
   }

   protected int paintLine(Graphics gfx, TokenMarker tokenMarker, int line, int x) {
      Font defaultFont = getFont();
      Color defaultColor = getForeground();

      currentLineIndex = line;
      int y = editor.lineToY(line);

      if (line < 0 || line >= editor.getLineCount()) {
         if (paintInvalid) {
            paintHighlight(gfx, line, y);
            styles[Token.INVALID].setGraphicsFlags(gfx, defaultFont);
            gfx.drawString("~", 0, y + fm.getHeight());
         }
      } else if (tokenMarker == null) {
         x = paintPlainLine(gfx, line, defaultFont, defaultColor, x, y);
      } else {
         x = paintSyntaxLine(gfx, tokenMarker, line, defaultFont,
                 defaultColor, x, y);
      }

      return x;
   }

   protected int paintPlainLine(Graphics gfx, int line, Font defaultFont,
           Color defaultColor, int x, int y) {
      paintHighlight(gfx, line, y);
      editor.getLineText(line, currentLine);

      gfx.setFont(defaultFont);
      gfx.setColor(defaultColor);

      y += fm.getHeight();
      x = Utilities.drawTabbedText(currentLine, x, y, gfx, this, 0);

      if (eolMarkers) {
         gfx.setColor(eolMarkerColor);
         gfx.drawString(".", x, y);
      }

      return x;
   }

   protected int paintSyntaxLine(Graphics gfx, TokenMarker tokenMarker,
           int line, Font defaultFont, Color defaultColor, int x, int y) {
      editor.getLineText(currentLineIndex, currentLine);
      currentLineTokens = tokenMarker.markTokens(currentLine,
              currentLineIndex);

      paintHighlight(gfx, line, y);

      gfx.setFont(defaultFont);
      gfx.setColor(defaultColor);
      y += fm.getHeight();
      x = SyntaxUtilities.paintSyntaxLine(currentLine,
              currentLineTokens, styles, this, gfx, x, y);

      if (eolMarkers) {
         gfx.setColor(eolMarkerColor);
         gfx.drawString(".", x, y);
      }

      return x;
   }

   protected void paintHighlight(Graphics gfx, int line, int y) {
      /*
       * added to be able to force highlighting (for example in
       * diff presentations
       *
       */
      if (highlighter != null && highlighter.getColor(line) != null) {
         paintForceHighlight(gfx, line, y, highlighter.getColor(line));
      }

      if (line >= editor.getSelectionStartLine() && line <= editor.getSelectionEndLine()) {
         paintLineHighlight(gfx, line, y);
      }

      if (highlights != null) {
         highlights.paintHighlight(gfx, line, y);
      }

      if (bracketHighlight && line == editor.getBracketLine()) {
         paintBracketHighlight(gfx, line, y);
      }

      if (line == editor.getCaretLine()) {
         paintCaret(gfx, line, y);
      }
   }

   /*
    * useful to force painting of highlighting (for example in diff presentations)
    *
    */
   protected void paintForceHighlight(Graphics gfx, int line, int y, Color color) {
      int height = fm.getHeight();
      y += fm.getLeading() + fm.getMaxDescent();
      gfx.setColor(color);
      gfx.fillRect(0, y, getWidth(), height);
   }

   protected void paintLineHighlight(Graphics gfx, int line, int y) {
      int height = fm.getHeight();
      y += fm.getLeading() + fm.getMaxDescent();

      int selectionStart = editor.getSelectionStart();
      int selectionEnd = editor.getSelectionEnd();

      if (selectionStart == selectionEnd) {
         if (lineHighlight) {
            gfx.setColor(lineHighlightColor);
            gfx.fillRect(0, y, getWidth(), height);
         }
      } else {
         gfx.setColor(selectionColor);

         int selectionStartLine = editor.getSelectionStartLine();
         int selectionEndLine = editor.getSelectionEndLine();
         int lineStart = editor.getLineStartOffset(line);

         int x1, x2;
         if (editor.isSelectionRectangular()) {
            int lineLen = editor.getLineLength(line);
            x1 = editor._offsetToX(line, Math.min(lineLen,
                    selectionStart - editor.getLineStartOffset(
                    selectionStartLine)));
            x2 = editor._offsetToX(line, Math.min(lineLen,
                    selectionEnd - editor.getLineStartOffset(
                    selectionEndLine)));
            if (x1 == x2) {
               x2++;
            }
         } else if (selectionStartLine == selectionEndLine) {
            x1 = editor._offsetToX(line,
                    selectionStart - lineStart);
            x2 = editor._offsetToX(line,
                    selectionEnd - lineStart);
         } else if (line == selectionStartLine) {
            x1 = editor._offsetToX(line,
                    selectionStart - lineStart);
            x2 = getWidth();
         } else if (line == selectionEndLine) {
            x1 = 0;
            x2 = editor._offsetToX(line,
                    selectionEnd - lineStart);
         } else {
            x1 = 0;
            x2 = getWidth();
         }
         // "inlined" min/max()
         gfx.fillRect(x1 > x2 ? x2 : x1, y, x1 > x2
                 ? (x1 - x2) : (x2 - x1), height);
      }
   }

   protected void paintBracketHighlight(Graphics gfx, int line, int y) {
      int position = editor.getBracketPosition();
      if (position == -1) {
         return;
      }

      y += fm.getLeading() + fm.getMaxDescent();
      int x = editor._offsetToX(line, position);
      gfx.setColor(bracketHighlightColor);
      // Hack!!! Since there is no fast way to get the character
      // from the bracket matching routine, we use ( since all
      // brackets probably have the same width anyway
      gfx.fillRect(x, y, fm.charWidth('(') - 1, fm.getHeight() - 1);
   }

   protected void paintCaret(Graphics gfx, int line, int y) {
      if (editor.isCaretVisible()) {
         int offset = editor.getCaretPosition() - editor.getLineStartOffset(line);
         int caretX = editor._offsetToX(line, offset);
         int caretWidth = ((blockCaret || editor.isOverwriteEnabled()) ? fm.charWidth('w') : 1);
         y += fm.getLeading() + fm.getMaxDescent();
         int height = fm.getHeight();

         gfx.setColor(caretColor);

         if (editor.isOverwriteEnabled()) {
            gfx.fillRect(caretX, y + height - 1, caretWidth, 1);
         } else if (caretWidth > 1) {
            gfx.drawRect(caretX, y, caretWidth - 1, height - 1);
         } else {
            gfx.drawLine(caretX, y, caretX, y + height - 1);
         }
      }
   }
}
