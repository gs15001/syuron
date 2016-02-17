/* Copyright (C) 1999 Slava Pestov
 * Copyright (C) 2012 Herve Girod
 * 
 * You may use and modify this package for any purpose. Redistribution is
 * permitted, in both source and binary form, provided that this notice
 * remains intact in all source distributions of this package. */
package org.jeditor.scripts;

import javax.swing.text.Segment;
import org.jeditor.scripts.base.Token;
import org.jeditor.scripts.base.TokenMarker;

/**
 * Basic token marker (with no Tokens)
 */
public class BasicTokenMarker extends TokenMarker {

	public BasicTokenMarker() {
	}

	@Override
	public byte markTokensImpl(byte token, Segment line, int lineIndex) {
		char[] array = line.array;
		int offset = line.offset;
		int length = line.count + offset;

		int textLength = 0;
		boolean dot = false;

		for (int i = offset; i < length; i++) {
			char c = array[i];
			textLength++;
		}

		if(textLength != 0) {
			addToken(textLength, Token.NULL);
		}
		return token;
	}
}
// End of TextTokenMarker.java

