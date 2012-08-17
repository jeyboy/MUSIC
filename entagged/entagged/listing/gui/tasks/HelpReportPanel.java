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
package entagged.listing.gui.tasks;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.AbstractButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;

import entagged.tageditor.resources.PreferencesManager;

/**
 * This panel is meant to be used for displaying wizard task contents. <br>
 * In addition to the interface this class implements the display of an help
 * text.
 * 
 * @author Christian Laireiter
 */
public class HelpReportPanel extends JPanel implements HyperlinkListener {

    /**
     * Stores the help field.
     */
    private JEditorPane helpPanel;

    /**
     * This panel contains all the buttons and so on.
     */
    private JPanel interfacePanel;

    /**
     * Creates an instance.
     *  
     */
    protected HelpReportPanel() {
        this(null);
    }

    /**
     * Creates an instance
     * 
     * @param help
     *                  Here should be the resource path to the help file which should
     *                  be displayed. The language shortage and file extension ".html"
     *                  will be appended by this class.
     */
    protected HelpReportPanel(String help) {
        initialize(help);
    }

    /**
     * Returns the panel where all components should be inserted. <br>
     * 
     * @return the content panel
     */
    protected JPanel getContentPane() {
        return this.interfacePanel;
    }

    /**
     * This method will try to load the given resource by prior appending the
     * the language and then ".html". If for Example German language is
     * selected, "_de.html" will be appended to <code>help</code>.<br>
     * If such a resource can't be found, just ".html" will be appended.
     * 
     * @param help
     *                  The resource path. (Must reside within the classpath and
     *                  readable by currents Class classloader).
     * @return InputStream if resource was found.
     */
    private InputStream getHelp(String help) {
        InputStream result = null;
        try {
            String language = PreferencesManager.get("entagged.langage");
            String extend = "";
            if (language.equals("german.lang")) {
                extend = "_de";
            } else if (language.equals("french.lang")) {
                extend = "_fr";
            } else if (language.equals("spanish.lang")) {
                extend = "_es";
            }
            result = HelpReportPanel.class.getClassLoader()
                    .getResourceAsStream(help + extend + ".html");
            if (result == null) {
                result = HelpReportPanel.class.getClassLoader()
                        .getResourceAsStream(help + ".html");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * This method will check if <code>current</code> is a subtype of
     * {@link AbstractButton}and perform a {@link AbstractButton#doClick()}.
     * <br>
     * 
     * @param current
     *                  the component whose name matched <code>cmd[1]</code>.
     */
    private void handleButton(Component current) {
        if (current instanceof AbstractButton) {
            ((AbstractButton) current).doClick();
        }
    }

    /**
     * This method checks if current is a <code>instanceof</code>
     * {@link JComboBox}and performs the cmd operation.
     * 
     * @param cmd
     *                  commands.
     * @param current
     *                  component.
     */
    private void handleSelectable(String[] cmd, Component current) {
        if (current instanceof JComboBox) {
            if (cmd[2].equals("open")) {
                ((JComboBox) current).setPopupVisible(true);
            } else if (cmd[2].equals("select")) {
                int index = Integer.parseInt(cmd[3]);
                ((JComboBox) current).setSelectedIndex(index);
                ((JComboBox) current).setPopupVisible(true);
            }
        }
    }

    /**
     * Similiar to {@link #handleButton(Component)}this method checks if
     * <code>current</code> is a <b>instanceof </b>
     * {@link javax.swing.JToggleButton}and sets its selection to the
     * <b>boolean </b> representation of <code>value</code>.
     * 
     * @param value
     *                  <code>true</code> or <code>false</code>. if
     *                  <code>null</code> <code>false</code> is assumed.
     * @param current
     *                  The component which should be changed.
     */
    private void handleToggleButton(String value, Component current) {
        if (current instanceof JToggleButton) {
            if (value == null) {
                value = "false";
            }
            ((JToggleButton) current).setSelected(Boolean.valueOf(value)
                    .booleanValue());
        }
    }

    /**
     * (overridden)
     * 
     * @see javax.swing.event.HyperlinkListener#hyperlinkUpdate(javax.swing.event.HyperlinkEvent)
     */
    public void hyperlinkUpdate(HyperlinkEvent e) {
        // Note: The hyperlink handling is very specialized to current needs.
        // I didn't follow any standards. So if you want to modify or implement
        // other functionality, you'll have to start reading this code.
        // In addition to that the html files displayed are somehow coding too.
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED
                && e.getDescription() != null) {
            /**
             * First is the type: <br>
             * Currently implemented: <br>
             * btn: AbstractButton; will only perform doClick() <br>
             * chk: JToggleButton; will setSelected(boolean) to
             * Boolean.valueOf(cmd[2]).booleanValue <br>
             * lst: JComboBox; command comes with cmd[2] <br>
             * <br>
             * Second the name of the component (Component.setName(String)) to
             * find it within interfacePanel. <br>
             * <br>
             * Third holds the value for e.g. "true" when using "chk"
             */
            String[] cmd = e.getDescription().split(":");
            // First, find component by name
            for (int i = 0; i < interfacePanel.getComponentCount(); i++) {
                Component current = interfacePanel.getComponent(i);
                if (cmd[1].trim().equals(current.getName())) {
                    if (cmd[0].equals("btn")) {
                        handleButton(current);
                    } else if (cmd[0].equals("chk")) {
                        handleToggleButton(cmd[2], current);
                    } else if (cmd[0].equals("lst")) {
                        handleSelectable(cmd, current);
                    }
                }
            }
        } else if (e.getEventType() == HyperlinkEvent.EventType.ENTERED) {
            helpPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            helpPanel.validate();
            helpPanel.repaint();
        } else if (e.getEventType() == HyperlinkEvent.EventType.EXITED) {
            helpPanel.setCursor(Cursor
                    .getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    /**
     * Initializes the panel.
     * 
     * @param help
     */
    private void initialize(String help) {
        this.setLayout(new GridBagLayout());
        interfacePanel = new JPanel(new GridBagLayout());
        if (help != null) {
            HTMLEditorKit hek = new HTMLEditorKit();
            helpPanel = new JEditorPane();
            helpPanel.setEditable(false);
            helpPanel.setEditorKit(hek);
            try {
                InputStream helpStream = getHelp(help);
                if (helpStream != null) {
                    hek.read(new InputStreamReader(helpStream), helpPanel
                            .getDocument(), 0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            helpPanel.addHyperlinkListener(this);
            JScrollPane sp = new JScrollPane(helpPanel);
            super.add(sp, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                    new Insets(5, 5, 5, 5), 100, 0));
            // Not nice but my only sollution
            helpPanel.setSelectionStart(0);
            helpPanel.setSelectionEnd(1);
        }
        super.add(interfacePanel, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
                        0, 0, 0, 0), 0, 0));
    }
}