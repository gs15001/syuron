/*
 * Copyright (C) 1999 Slava Pestov
 * Copyright (C) 2012 Herve Girod
 *
 * You may use and modify this package for any purpose. Redistribution is
 * permitted, in both source and binary form, provided that this notice
 * remains intact in all source distributions of this package.
 */
package org.jeditor.gui;

import javax.swing.event.DocumentEvent;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.PlainDocument;
import javax.swing.text.Segment;
import javax.swing.undo.UndoManager;
import org.jeditor.scripts.base.TokenMarker;

/**
 * A document implementation that can be tokenized by the syntax highlighting
 * system.
 *
 * @author Slava Pestov
 * @author Herve Girod
 * @version 0.4.2
 */
public class SyntaxDocument extends PlainDocument {
   protected TokenMarker tokenMarker;
   protected UndoManager manager;
   
   public SyntaxDocument() {
      this.manager = new UndoManager();
      this.addUndoableEditListener(new UndoableEditListener() {
         @Override
         public void undoableEditHappened(UndoableEditEvent e) {
            manager.addEdit(e.getEdit());
         }
      });
   }
   
   public UndoManager getUndoManager() {
      return manager;
   }
   
   /**
    * Returns the token marker that is to be used to split lines
    * of this document up into tokens. May return null if this
    * document is not to be colorized.
    */
   public TokenMarker getTokenMarker() {
      return tokenMarker;
   }

   /**
    * Sets the token marker that is to be used to split lines of
    * this document up into tokens. May throw an exception if
    * this is not supported for this type of document.
    *
    * @param tm The new token marker
    */
   public void setTokenMarker(TokenMarker tm) {
      tokenMarker = tm;
      if (tm == null) {
         return;
      }

      tokenMarker.insertLines(0, getDefaultRootElement().getElementCount());
      tokenizeLines();
   }

   /**
    * Reparses the document, by passing all lines to the token
    * marker. This should be called after the document is first
    * loaded.
    */
   public void tokenizeLines() {
      tokenizeLines(0, getDefaultRootElement().getElementCount());
   }

   /**
    * Reparses the document, by passing the specified lines to the
    * token marker. This should be called after a large quantity of
    * text is first inserted.
    *
    * @param start The first line to parse
    * @param len The number of lines, after the first one to parse
    */
   public void tokenizeLines(int start, int len) {
      if (tokenMarker == null || !tokenMarker.supportsMultilineTokens()) {
         return;
      }

      Segment lineSegment = new Segment();
      Element map = getDefaultRootElement();

      len += start;

      try {
         for (int i = start; i < len; i++) {
            Element lineElement = map.getElement(i);
            int lineStart = lineElement.getStartOffset();
            getText(lineStart, lineElement.getEndOffset() - lineStart - 1, lineSegment);
            tokenMarker.markTokens(lineSegment, i);
         }
      } catch (BadLocationException bl) {
         bl.printStackTrace();
      }
   }

   /**
    * We overwrite this method to update the token marker
    * state immediately so that any event listeners get a
    * consistent token marker.
    */
   @Override
   protected void fireInsertUpdate(DocumentEvent evt) {
      if (tokenMarker != null) {
         DocumentEvent.ElementChange ch = evt.getChange(getDefaultRootElement());
         if (ch != null) {
            tokenMarker.insertLines(ch.getIndex() + 1, ch.getChildrenAdded().length - ch.getChildrenRemoved().length);
         }
      }

      super.fireInsertUpdate(evt);
   }

   /**
    * We overwrite this method to update the token marker
    * state immediately so that any event listeners get a
    * consistent token marker.
    */
   @Override
   protected void fireRemoveUpdate(DocumentEvent evt) {
      if (tokenMarker != null) {
         DocumentEvent.ElementChange ch = evt.getChange(getDefaultRootElement());
         if (ch != null) {
            tokenMarker.deleteLines(ch.getIndex() + 1, ch.getChildrenRemoved().length - ch.getChildrenAdded().length);
         }
      }

      super.fireRemoveUpdate(evt);
   }
}
