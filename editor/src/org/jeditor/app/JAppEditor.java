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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.TreeMap;
import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/** A class showing the use of the package. It presents a panel, which adapt to the 
 * different possible scripts and allow to modify them.
 * 
 * @version 0.4.1
 */
public class JAppEditor extends JFrame {
        // settings types
        private static String version = "2.0";
        private static String versiondate = "17/03/08";
        private TreeMap filelist = new TreeMap();
        private JTabbedPane tab = new JTabbedPane();
        private JMenuBar Mbar = new JMenuBar();
        private JMenu fileMenu = new JMenu("File");
        private JMenu toolsMenu = new JMenu("Tools");
        private JMenu helpMenu = new JMenu("Help");
        protected JComboBox bTypecb;
        private AbstractAction openfileAction; 
        private AbstractAction exitAction;
        private AbstractAction aboutAction;
        private AbstractAction searchAction;
        private AbstractAction saveAsAction;
        private AbstractAction settingsAction;
        private JFileChooser chooser = new JFileChooser();
        private SyntaxManager manager;
        public String filetype = null;
        private int tabSize = 4;
        
    /**
     * constructor of MainWindow class.
     * Returns the JFrame manager.
     **/
    public JAppEditor() {
        // Initialise the window
        super ("JAppEditor");
        this.setSize(700,700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // add Mouse Properties of TabbedPane
        
        tab.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    manageFile(e);
                }
            }
        });

        tab.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                showCurrentFile();
            }
        });        
        /* creation du Syntaxmanager */
        manager = SyntaxManager.instance();
        try {
            manager.initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /* creation des composants internes */
        JPanel npane = new JPanel();        
        bTypecb = new JComboBox(manager.getTypeList());
        bTypecb.setSelectedItem(manager.getFirstType());
        
        bTypecb.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                filetype = (String)(e.getItem());
                FilePane p = (FilePane)tab.getSelectedComponent();
                if (p != null) {
                    p.setFileType(filetype);
                }
            };
        });
        npane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        npane.add(bTypecb);
        
        this.setJMenuBar(Mbar);
        loadMenus();

        /* Insertion des composants => Panel de la Frame */
        JPanel pane = new JPanel();
        setContentPane(pane);
        pane.setLayout(new BorderLayout());
        pane.add(npane, BorderLayout.NORTH);
        pane.add(tab,BorderLayout.CENTER);
    }

    /** loads Menus */
    private void loadMenus() {
        /* Creation des actions pour les menus */
        exitAction = new AbstractAction("Exit") {
            public void actionPerformed(ActionEvent ae) {
                exitApplication();
            }
        };
        
        openfileAction = new AbstractAction("Open") {
            public void actionPerformed(ActionEvent e) {
                openFile();
            }
        };
        
        aboutAction = new AbstractAction("About") {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "JAppEditor version "+version+"\n building date: "+versiondate,
                        "About",JOptionPane.INFORMATION_MESSAGE);
            }
        };

        // TODO : implement search and saveAs
        saveAsAction = new AbstractAction("SaveAs") {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "Not implemented","Message",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        };
        searchAction = new AbstractAction("Search") {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "Not implemented","Message",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        };

        settingsAction = new AbstractAction("Settings") {
            public void actionPerformed(ActionEvent e) {
                openSettings();
            }
        };
        
        /* creation des composants internes */
        JMenuItem openfileItem = new JMenuItem(openfileAction);
        JMenuItem exitItem = new JMenuItem(exitAction);
        JMenuItem aboutItem = new JMenuItem(aboutAction);
        JMenuItem searchItem = new JMenuItem(searchAction);
        JMenuItem settingsItem = new JMenuItem(settingsAction);
        JMenuItem saveAsItem = new JMenuItem(saveAsAction);
        
        /* Insertion des composants => Barre de menu et boutons */
        fileMenu.add(openfileItem);
        fileMenu.add(saveAsItem);
        fileMenu.add(exitItem);
        toolsMenu.add(searchItem);
        toolsMenu.add(settingsItem);
        helpMenu.add(aboutItem);
        
        Mbar.add(fileMenu);
        Mbar.add(toolsMenu);
        Mbar.add(helpMenu);
    }

    private void openSettings() {
        SettingsDialog dialog = new SettingsDialog(this, tabSize);
        int ret = dialog.showDialog();
        if (ret == JFileChooser.APPROVE_OPTION) {
            tabSize = dialog.getTabSizeValue();
            FilePane.setTabSize(tabSize);
        }
    }
    
    public void setFileType(String type) {
        filetype = type;
        bTypecb.setSelectedItem(type);
    }
        
    /* choose Destination folder to look for HBK folders */
    private void openFile() {
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            final File file = chooser.getSelectedFile();
            if (! filelist.keySet().contains(file.getName())) {
                chooser.setCurrentDirectory(new File(file.getParent()));                
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        doOpenFile(file);
                    }
                });
            }
        }
    }
    
    public void doOpenFile(File file) {
        FilePane pane = new FilePane(this, file);
        filelist.put(pane.getName(), pane.getFile());
        tab.addTab(file.getName(),pane);
        tab.setSelectedComponent(pane);
    }
    
    private void showCurrentFile() {
        if (tab.getSelectedComponent() != null) {
            FilePane.TextFile toFile = ((FilePane)(tab.getSelectedComponent())).getFile();
            setFileType(manager.getType(toFile.getExtension()));
        }
    }
    
    private void manageFile(MouseEvent e) {
        // catch event position
        int x = e.getX();
        int y = e.getY();
        
        // create menus
        JPopupMenu menu = new JPopupMenu();
        JMenuItem close = new JMenuItem("Close");
        JMenu compare = new JMenu("Compare to");
        Iterator it = filelist.keySet().iterator();
        String curfolder = ((FilePane)(tab.getSelectedComponent())).getName();
        while (it.hasNext()) {
            String s = (String)(it.next());
            if (! s.equals(curfolder)) {
                JMenuItem item = new JMenuItem(s);
                compare.add(item);
                item.addActionListener(new ActionListener() {
                    public void actionPerformed (ActionEvent e) {
                        FilePane.TextFile toFile = ((FilePane)(tab.getSelectedComponent())).getFile();
                        String s = ((JMenuItem)(e.getSource())).getText();
                        FilePane.TextFile fromFile = (FilePane.TextFile)(filelist.get(s));
                        compareFiles(fromFile, toFile);
                    }
                });
            }
        }

        // add listeners to sub-menus
        close.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                String name = ((FilePane)(tab.getSelectedComponent())).getName();
                if (filelist.containsKey(name)) {
                    filelist.remove(name);
                }
                tab.removeTabAt(tab.getSelectedIndex());
            }
        });        
        
        menu.add(close);
        menu.add(compare);
        menu.show(tab,x,y);
    }
    
    private void compareFiles(FilePane.TextFile fromTextFile,FilePane.TextFile toTextFile) {
        FilePane pane = new FilePane(this,toTextFile, fromTextFile);
        tab.addTab(pane.getName(), pane);
    }
        
    private void exitApplication() {
        System.exit(0);
    }
    
    public static void main (String args[]) {
        System.out.println("Beginning");
        JAppEditor app = new JAppEditor();
        app.setVisible(true);
    }
}