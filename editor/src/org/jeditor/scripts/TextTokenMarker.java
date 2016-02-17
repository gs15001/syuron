/* This file is part of the javaXUL library: see http://jeditor.sourceforge.net
   Copyright (C) 2010 Herve Girod

   Permission is hereby granted, free of charge, to any person obtaining a copy
   of this software and associated documentation files (the "Software"), to deal
   in the Software without restriction, including without limitation the rights
   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
   copies of the Software, and to permit persons to whom the Software is
   furnished to do so, subject to the following conditions:

   The above copyright notice and this permission notice shall be included in
   all copies or substantial portions of the Software.

   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
   THE SOFTWARE.
 */

package org.jeditor.scripts;

import javax.swing.text.Segment;
import org.jeditor.scripts.base.Token;
import org.jeditor.scripts.base.TokenMarker;

/**
 * Text token marker.
 */
public class TextTokenMarker extends TokenMarker {

    public TextTokenMarker() {
    }

    public byte markTokensImpl(byte token, Segment line, int lineIndex) {
        char[] array = line.array;
        int offset = line.offset;
        int length = line.count + offset;

        int textLength = 0;
        boolean dot = true;

        for (int i = offset; i < length; i++) {
            char c = array[i];

            switch (c) {
                case '.':
                case '!':
                case '?':
                    if (textLength != 0) {
                        addToken(textLength, Token.NULL);
                        textLength = 0;
                    }
                    addToken(1, Token.KEYWORD3);
                    dot = true;
                    break;
                case ':':
                case ';':
                case ',':
                    if (textLength != 0) {
                        addToken(textLength, Token.NULL);
                        textLength = 0;
                    }
                    addToken(1, Token.KEYWORD1);
                    dot = false;
                    break;
                case '\'':
                case '\"':
                case '(':
                case ')':
                case '{':
                case '}':
                case '[':
                case ']':
                    if (textLength != 0) {
                        addToken(textLength, Token.NULL);
                        textLength = 0;
                    }
                    addToken(1, Token.LITERAL1);
                    dot = false;
                    break;
                case '/':
                case '\\':
                case '+':
                case '=':
                case '-':
                case '*':
                case '%':
                case '^':
                    if (textLength != 0) {
                        addToken(textLength, Token.NULL);
                        textLength = 0;
                    }
                    addToken(1, Token.OPERATOR);
                    dot = false;
                    break;
                default:
                    if (Character.isLetter(c) && Character.isUpperCase(c) && dot) {
                        if (textLength != 0) {
                            addToken(textLength, Token.NULL);
                            textLength = 0;
                        }
                        addToken(1, Token.COMMENT1);
                    } else if (Character.isDigit(c)) {
                        if (textLength != 0) {
                            addToken(textLength, Token.NULL);
                            textLength = 0;
                        }
                        addToken(1, Token.LABEL);
                    } else {
                        textLength++;
                    }
                    if (!Character.isWhitespace(c)) {
                        dot = false;
                    }
            }
        }

        if (textLength != 0) {
            addToken(textLength, Token.NULL);
        }
        return token;
    }
}

// End of TextTokenMarker.java

