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
package org.jeditor;

import java.io.IOException;
import java.net.URL;
import java.util.PropertyResourceBundle;

/**
 * Main class, only used to give some informations about the version of
 * the library on the command line.
 * 
 * @since 0.2
 */
public class Main_GPL {

	public static void main(String[] args) {
		URL url = Thread.currentThread().getContextClassLoader()
				.getResource("org/jeditor/resources/jeditor.properties");
		try {
			PropertyResourceBundle prb = new PropertyResourceBundle(url.openStream());
			String version = prb.getString("version");
			String date = prb.getString("date");
			System.out.println("jEditor version " + version + " build on " + date);
			System.out.println("Distributed under GPL license");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
