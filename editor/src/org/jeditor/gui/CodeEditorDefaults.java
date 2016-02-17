/*
 * Copyright (C) 1999 Slava Pestov
 * Copyright (C) 2010, 2012 Herve Girod
 *
 * You may use and modify this package for any purpose. Redistribution is
 * permitted, in both source and binary form, provided that this notice
 * remains intact in all source distributions of this package.
 */
package org.jeditor.gui;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import org.jeditor.scripts.base.SyntaxStyle;
import org.jeditor.scripts.base.SyntaxUtilities;

/**
 * Encapsulates default settings for a text area. This can be passed
 * to the constructor once the necessary fields have been filled out.
 * The advantage of doing this over calling lots of set() methods after
 * creating the text area is that this method is faster.
 *
 * @version 0.4.2
 */
public class CodeEditorDefaults {
   private static CodeEditorDefaults DEFAULTS = null;
   public InputHandler inputHandler;
   public SyntaxDocument document;
   public boolean editable;
   public boolean caretVisible;
   public boolean caretBlinks;
   public boolean blockCaret;
   public int electricScroll;
   public boolean gutterCollapsed;
   public int gutterWidth;
   public Color gutterBgColor;
   public Color gutterFgColor;
   public Color gutterHighlightColor;
   public Color gutterBorderColor;
   public int gutterBorderWidth;
   public int gutterNumberAlignment;
   public Font gutterFont;
   public Color caretMarkColor;
   public Color anchorMarkColor;
   public Color selectionMarkColor;
   public int cols;
   public int rows;
   public SyntaxStyle[] styles;
   public Font font;
   public Color caretColor;
   public Color selectionColor;
   public Color lineHighlightColor;
   public boolean lineHighlight;
   public Color bracketHighlightColor;
   public boolean bracketHighlight;
   public Color eolMarkerColor;
   public boolean eolMarkers;
   public boolean paintInvalid;
   public int tabSize = 4;
   public JPopupMenu popup;

   /**
    * Returns a new TextAreaDefaults object with the default values filled
    * in.
    */
   public static CodeEditorDefaults getDefaults() {
      if (DEFAULTS == null) {
         DEFAULTS = new CodeEditorDefaults();

         DEFAULTS.inputHandler = new DefaultInputHandler();
         DEFAULTS.inputHandler.addDefaultKeyBindings();
         DEFAULTS.document = new SyntaxDocument();
         DEFAULTS.editable = true;

         DEFAULTS.caretVisible = true;
         DEFAULTS.caretBlinks = true;
         DEFAULTS.electricScroll = 3;

         DEFAULTS.tabSize = 4;

         DEFAULTS.gutterCollapsed = true;
         DEFAULTS.gutterWidth = 40;
         DEFAULTS.gutterBgColor = Color.white;
         DEFAULTS.gutterFgColor = Color.black;
         DEFAULTS.gutterHighlightColor = new Color(0x8080c0);
         DEFAULTS.gutterBorderColor = Color.gray;
         DEFAULTS.caretMarkColor = Color.green;
         DEFAULTS.anchorMarkColor = Color.red;
         DEFAULTS.selectionMarkColor = Color.blue;
         DEFAULTS.gutterBorderWidth = 4;
         DEFAULTS.gutterNumberAlignment = SwingConstants.RIGHT;
         DEFAULTS.gutterFont = new Font("monospaced", Font.PLAIN, 10);

         DEFAULTS.cols = 80;
         DEFAULTS.rows = 25;
         DEFAULTS.font = new Font("Monospaced", Font.PLAIN, 12);
         DEFAULTS.styles = SyntaxUtilities.getDefaultSyntaxStyles();
         DEFAULTS.caretColor = Color.black;
         DEFAULTS.selectionColor = new Color(0xccccff);
         DEFAULTS.lineHighlightColor = new Color(0xe0e0e0);
         DEFAULTS.lineHighlight = true;
         DEFAULTS.bracketHighlightColor = Color.magenta;
         DEFAULTS.bracketHighlight = true;
         DEFAULTS.eolMarkerColor = new Color(0x009999);
         DEFAULTS.eolMarkers = true;
         DEFAULTS.paintInvalid = true;
      }

      return DEFAULTS;
   }

   public CodeEditorDefaults() {
      inputHandler = new DefaultInputHandler();
      inputHandler.addDefaultKeyBindings();
      document = new SyntaxDocument();
      editable = true;

      caretVisible = true;
      caretBlinks = true;
      electricScroll = 3;

      gutterCollapsed = true;
      gutterWidth = 40;
      gutterBgColor = Color.white;
      gutterFgColor = Color.black;
      gutterHighlightColor = new Color(0x8080c0);
      gutterBorderColor = Color.gray;
      caretMarkColor = Color.green;
      anchorMarkColor = Color.red;
      selectionMarkColor = Color.blue;
      gutterBorderWidth = 4;
      gutterNumberAlignment = SwingConstants.RIGHT;
      gutterFont = new Font("monospaced", Font.PLAIN, 10);

      cols = 80;
      rows = 25;
      font = new Font("Monospaced", Font.PLAIN, 12);
      styles = SyntaxUtilities.getDefaultSyntaxStyles();
      caretColor = Color.black;
      selectionColor = new Color(0xccccff);
      lineHighlightColor = new Color(0xe0e0e0);
      lineHighlight = true;
      bracketHighlightColor = Color.magenta;
      bracketHighlight = true;
      eolMarkerColor = new Color(0x009999);
      eolMarkers = true;
      paintInvalid = true;
   }

   public CodeEditorDefaults(CodeEditorDefaults defaults) {
      caretVisible = defaults.caretVisible;
      caretBlinks = defaults.caretBlinks;
      electricScroll = defaults.electricScroll;

      gutterCollapsed = defaults.gutterCollapsed;
      gutterWidth = defaults.gutterWidth;
      gutterBgColor = defaults.gutterBgColor;
      gutterFgColor = defaults.gutterFgColor;
      gutterHighlightColor = defaults.gutterHighlightColor;
      gutterBorderColor = defaults.gutterBorderColor;
      caretMarkColor = defaults.caretMarkColor;
      anchorMarkColor = defaults.anchorMarkColor;
      selectionMarkColor = defaults.selectionMarkColor;
      gutterBorderWidth = defaults.gutterBorderWidth;
      gutterNumberAlignment = defaults.gutterNumberAlignment;
      gutterFont = defaults.gutterFont;


      cols = defaults.cols;
      rows = defaults.rows;
      styles = defaults.styles;
      font = defaults.font;
      caretColor = defaults.caretColor;
      selectionColor = defaults.selectionColor;
      lineHighlightColor = defaults.lineHighlightColor;
      lineHighlight = defaults.lineHighlight;
      bracketHighlightColor = defaults.bracketHighlightColor;
      bracketHighlight = defaults.bracketHighlight;
      eolMarkerColor = defaults.eolMarkerColor;
      eolMarkers = defaults.eolMarkers;
      paintInvalid = defaults.paintInvalid;
   }

   public void setStyle(byte keyword, Color color, boolean italic, boolean bold) {
      SyntaxStyle style = new SyntaxStyle(color, italic, bold);
      styles[keyword] = style;
   }

   public static Color copyColor(Color color) {
      int red = color.getRed();
      int green = color.getGreen();
      int blue = color.getBlue();
      return new Color(red, green, blue);
   }
}
