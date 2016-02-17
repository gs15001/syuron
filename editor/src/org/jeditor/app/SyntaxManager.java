/* This file is part of the jEditor library: see http://jeditor.sourceforge.net
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

package org.jeditor.app;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.PropertyResourceBundle;
import java.util.TreeMap;
import java.util.Vector;
import org.jeditor.scripts.BasicTokenMarker;
import org.jeditor.scripts.base.TokenMarker;

/** Manage the possible file types for the application.
 * 
 * @version 0.2
 */
public class SyntaxManager {
    public static final String fileextension = "fileextension.properties";
    public static final String filetype = "filetype.properties";
    public String plain = "PlainText";
    private static boolean inst = false;
    private static SyntaxManager manager;
    public TreeMap maptype = new TreeMap();
    public Vector typelist;
    public Hashtable mapext = new Hashtable(1);

    private SyntaxManager() {
    }

    public static SyntaxManager instance() {
        if (!inst) {
            manager = new SyntaxManager();
            inst = true;
        }
        return manager;
    }

    public void initialize() throws IOException {
        PropertyResourceBundle bext =
                new PropertyResourceBundle(getClass().getResourceAsStream(fileextension));
        PropertyResourceBundle btype =
                new PropertyResourceBundle(getClass().getResourceAsStream(filetype));
        // get hashtable from file extension resources
        Enumeration en = bext.getKeys();
        while (en.hasMoreElements()) {
            String name = (String) en.nextElement();
            String val = (String) bext.getObject(name);
            mapext.put(name, val);
        }
        // get hashtable from file types resources
        en = btype.getKeys();
        while (en.hasMoreElements()) {
            String name = (String) en.nextElement();
            String val = (String) btype.getObject(name);
            maptype.put(name, val);
        }
        typelist = new Vector(maptype.keySet());
    }

    public TreeMap getTypeMap() {
        return maptype;
    }

    public Vector getTypeList() {
        return typelist;
    }

    public String getType(String ext) {
        if (mapext.containsKey(ext)) {
            return (String) mapext.get(ext);
        } else {
            return plain;
        }
    }

    public String getExtClass(String ext) {
        String s = null;
        if (mapext.containsKey(ext)) {
            String type = (String) mapext.get(ext);
            if (maptype.containsKey(type)) {
                s = (String) maptype.get(type);
            }
        }
        return s;
    }

    public String getTypeClass(String type) {
        String s = null;
        if (maptype.containsKey(type)) {
            s = (String) maptype.get(type);
        }
        return s;
    }

    public String getFirstType() {
        return (String) typelist.get(0);
    }

    protected TokenMarker getTokenMarker(String classname) {
        if (classname == null) {
            return new BasicTokenMarker();
        } else {
            TokenMarker marker = null;
            try {
                marker = (TokenMarker) (Class.forName(classname).newInstance());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (ClassCastException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return marker;
        }
    }

    public TokenMarker getTokenMarkerFromExt(String ext) {
        return getTokenMarker(getExtClass(ext));
    }

    public TokenMarker getTokenMarkerFromType(String type) {
        return getTokenMarker(getTypeClass(type));
    }
}
