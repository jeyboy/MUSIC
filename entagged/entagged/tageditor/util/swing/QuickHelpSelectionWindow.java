/*
 *  ********************************************************************   **
 *  Copyright notice                                                       **
 *  **																	   **
 *  (c) 2003 Entagged Developpement Team				                   **
 *  http://www.sourceforge.net/projects/entagged                           **
 *  **																	   **
 *  All rights reserved                                                    **
 *  **																	   **
 *  This script is part of the Entagged project. The Entagged 			   **
 *  project is free software; you can redistribute it and/or modify        **
 *  it under the terms of the GNU General Public License as published by   **
 *  the Free Software Foundation; either version 2 of the License, or      **
 *  (at your option) any later version.                                    **
 *  **																	   **
 *  The GNU General Public License can be found at                         **
 *  http://www.gnu.org/copyleft/gpl.html.                                  **
 *  **																	   **
 *  This copyright notice MUST APPEAR in all copies of the file!           **
 *  ********************************************************************
 */
package entagged.tageditor.util.swing;

import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;

/**
 * This window is for displaying possible completion values given by an instance
 * of {@link entagged.tageditor.util.swing.QuickHelpFieldExtender}.<br>
 * 
 * @author Christian Laireiter
 */
public class QuickHelpSelectionWindow extends Window {

    protected QuickHelpFieldExtender helper;

    private javax.swing.JPanel jContentPane = null;

    private JScrollPane jScrollPane = null;

    private ListModel listModel;

    protected JList valueList = null;

    /**
     * This is the default constructor
     * 
     * @param fieldExtender
     *                  The controlling instance.
     * @param lmodel
     *                  The model which contains the values.
     *  
     */
    public QuickHelpSelectionWindow(QuickHelpFieldExtender fieldExtender,
            ListModel lmodel) {
        super((Window) SwingUtilities.getRoot(fieldExtender.getTextField()));
        this.listModel = lmodel;
        this.helper = fieldExtender;
        initialize();
    }

    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    private javax.swing.JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new javax.swing.JPanel();
            jContentPane.setLayout(new java.awt.BorderLayout());
            jContentPane.add(getJScrollPane(), java.awt.BorderLayout.CENTER);
        }
        return jContentPane;
    }

    /**
     * This method initializes jScrollPane
     * 
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            jScrollPane.setViewportView(getValueList());
        }
        return jScrollPane;
    }

    /**
     * This method initializes jList
     * 
     * @return javax.swing.JList
     */
    public JList getValueList() {
        if (valueList == null) {
            valueList = new JList(this.listModel);
            valueList
                    .setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
            valueList.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() >= 2) {
                        Point point = e.getPoint();
                        int index = valueList.locationToIndex(point);
                        if (index >= 0
                                && index < valueList.getModel().getSize()) {
                            helper.select(index);
                        }
                    }
                }
            });
            valueList.addKeyListener(helper);
            this.addKeyListener(helper);
        }
        return valueList;
    }

    /**
     * This method initializes this
     */
    private void initialize() {
        this.setSize(300, 200);
        this.add(getJContentPane());
    }

    /**
     * (overridden)
     * 
     * @see java.awt.Component#setVisible(boolean)
     */
    public void setVisible(boolean b) {
        if (getValueList().getModel().getSize() > 0 && b) {
            getValueList().setSelectedIndex(0);
        }
        super.setVisible(b);
    }
}