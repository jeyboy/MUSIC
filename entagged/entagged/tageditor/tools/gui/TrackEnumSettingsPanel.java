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
package entagged.tageditor.tools.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTextField;

import entagged.tageditor.AutoCompleteComboBox;
import entagged.tageditor.resources.LangageManager;
import entagged.tageditor.resources.PreferencesManager;
import entagged.tageditor.tools.TrackEnumeratorSettings;

/**
 * This panel allows the manipulation of
 * {@link entagged.tageditor.tools.TrackEnumeratorSettings}<br>
 * 
 * @author Christian Laireiter
 */
public class TrackEnumSettingsPanel extends JPanel {

    /**
     * This constant gives the name of the preference which stores all used
     * patterns.
     */
    public final static String PREF_PATTERNS = TrackEnumeratorSettings.class
            .getName()
            + "_PATTERNS";

    /**
     * This method will use the
     * {@link entagged.tageditor.resources.PreferencesManager}to load all used
     * patterns for the of functionality.
     * 
     * @return All stored patterns.
     */
    public static String[] getPatterns() {
        String[] result = new String[0];
        String string = PreferencesManager.get(PREF_PATTERNS);
        if (string != null) {
            result = string.split("::");
            for (int i = 0; i < result.length; i++) {
                result[i] = result[i].replaceAll("\\:", ":");
            }
        }
        return result;
    }

    /**
     * Displays the value for the number of digits.
     */
    private JTextField digitCountField = null;

    /**
     * Used to change the amount of digits to use.
     */
    protected JScrollBar digitCountScroller = null;

    private JCheckBox directoryRestartBox = null;

    private JCheckBox dynamicExtendBox = null;

    private JCheckBox filesFirstBox = null;

    private JPanel jPanel = null;

    private JPanel jPanel1 = null;

    private JPanel jPanel2 = null;

    private JCheckBox numberExtensionBox = null;

    private AutoCompleteComboBox ofPatternBox = null;

    private JCheckBox saveRunBox = null;

    private JCheckBox useOfFunctionBox = null;

    /**
     * This is the default constructor
     * 
     * @param settings
     *                  initial values. may be <code>null</code>
     */
    public TrackEnumSettingsPanel(TrackEnumeratorSettings settings) {
        super();
        initialize();
        if (settings != null)
            applySettings(settings);
    }

    /**
     * This method will apply all given settings to the panel.
     * 
     * @param settings
     *                  Settings to display.
     */
    public void applySettings(TrackEnumeratorSettings settings) {
        this.digitCountScroller.setValue(settings.getDigitCount());
        this.digitCountField.setText(String.valueOf(settings.getDigitCount()));
        this.dynamicExtendBox.setSelected(settings.isDynamicDigitExtend());
        this.useOfFunctionBox.setSelected(settings.getOfString() != null);
        this.filesFirstBox.setSelected(settings.isFilesFirst());
        this.directoryRestartBox.setSelected(settings.isDirectoryRestart());
        this.numberExtensionBox.setSelected(settings.isNumberExtension());
        this.saveRunBox.setSelected(settings.isSaveRun());
    }

    /**
     * This method initializes jTextField
     * 
     * @return javax.swing.JTextField
     */
    protected JTextField getDigitCountField() {
        if (digitCountField == null) {
            digitCountField = new JTextField();
            digitCountField.setText("2");
            digitCountField.setEditable(false);
        }
        return digitCountField;
    }

    /**
     * This method initializes jScrollBar
     * 
     * @return javax.swing.JScrollBar
     */
    protected JScrollBar getDigitCountScroller() {
        if (digitCountScroller == null) {
            digitCountScroller = new JScrollBar();
            digitCountScroller.setBlockIncrement(1);
            digitCountScroller.setMinimumSize(new java.awt.Dimension(17, 0));
            digitCountScroller.setPreferredSize(new java.awt.Dimension(17, 5));
            digitCountScroller
                    .addAdjustmentListener(new java.awt.event.AdjustmentListener() {
                        public void adjustmentValueChanged(
                                java.awt.event.AdjustmentEvent e) {
                            getDigitCountField().setText(
                                    String.valueOf(digitCountScroller
                                            .getValue()));
                        }
                    });
        }
        return digitCountScroller;
    }

    /**
     * This method initializes jCheckBox
     * 
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getDirectoryRestartBox() {
        if (directoryRestartBox == null) {
            directoryRestartBox = new JCheckBox();
            directoryRestartBox.setText(LangageManager
                    .getProperty("trackenum.directoryrestart"));
            directoryRestartBox.setName("restartDirectoryField");
        }
        return directoryRestartBox;
    }

    /**
     * This method initializes jCheckBox2
     * 
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getDynamicExtendBox() {
        if (dynamicExtendBox == null) {
            dynamicExtendBox = new JCheckBox();
            dynamicExtendBox.setText(LangageManager
                    .getProperty("trackenum.digitextension"));
            dynamicExtendBox.setName("dynamicExtendBox");
        }
        return dynamicExtendBox;
    }

    /**
     * This method initializes jCheckBox1
     * 
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getFilesFirstBox() {
        if (filesFirstBox == null) {
            filesFirstBox = new JCheckBox();
            filesFirstBox.setText(LangageManager
                    .getProperty("trackenum.filesfirst"));
            filesFirstBox.setName("filesFirstBox");
        }
        return filesFirstBox;
    }

    /**
     * This method initializes jPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel() {
        if (jPanel == null) {
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            jPanel = new JPanel();
            jPanel.setLayout(new GridBagLayout());
            jPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
                    LangageManager.getProperty("trackenum.offunction"),
                    javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                    javax.swing.border.TitledBorder.DEFAULT_POSITION, null,
                    null));
            gridBagConstraints2.gridx = 0;
            gridBagConstraints2.gridy = 0;
            gridBagConstraints2.anchor = GridBagConstraints.WEST;
            jPanel.add(getUseOfFunctionBox(), gridBagConstraints2);
            gridBagConstraints2.gridx = 1;
            gridBagConstraints2.fill = GridBagConstraints.REMAINDER;
            jPanel.add(getOfPatternBox(), gridBagConstraints2);
        }
        return jPanel;
    }

    /**
     * This method initializes jPanel1
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel1() {
        if (jPanel1 == null) {
            jPanel1 = new JPanel();
            jPanel1.setLayout(new BorderLayout());
            jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(
                    null, LangageManager.getProperty("trackenum.numofdigits"),
                    javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                    javax.swing.border.TitledBorder.DEFAULT_POSITION, null,
                    null));
            jPanel1.add(getDigitCountField(), java.awt.BorderLayout.CENTER);
            jPanel1.add(getDigitCountScroller(), java.awt.BorderLayout.EAST);
            jPanel1.add(getDynamicExtendBox(), java.awt.BorderLayout.SOUTH);
        }
        return jPanel1;
    }

    /**
     * This method initializes jPanel2
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel2() {
        if (jPanel2 == null) {
            GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
            GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            jPanel2 = new JPanel();
            jPanel2.setLayout(new GridBagLayout());
            jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(
                    null, LangageManager.getProperty("trackenum.various"),
                    javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                    javax.swing.border.TitledBorder.DEFAULT_POSITION, null,
                    null));
            gridBagConstraints5.gridx = 0;
            gridBagConstraints5.gridy = 0;
            gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints6.gridx = 0;
            gridBagConstraints6.gridy = 1;
            gridBagConstraints6.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints9.gridx = 0;
            gridBagConstraints9.gridy = 2;
            gridBagConstraints9.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints10.gridx = 0;
            gridBagConstraints10.gridy = 3;
            gridBagConstraints10.anchor = java.awt.GridBagConstraints.WEST;
            jPanel2.add(getDirectoryRestartBox(), gridBagConstraints5);
            jPanel2.add(getFilesFirstBox(), gridBagConstraints6);
            jPanel2.add(getNumberExtensionBox(), gridBagConstraints9);
            jPanel2.add(getSaveRunBox(), gridBagConstraints10);
        }
        return jPanel2;
    }

    /**
     * This method initializes jCheckBox
     * 
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getNumberExtensionBox() {
        if (numberExtensionBox == null) {
            numberExtensionBox = new JCheckBox();
            numberExtensionBox.setName("numberExtensionBox");
            numberExtensionBox.setText(LangageManager
                    .getProperty("trackenum.extendnumbers"));
        }
        return numberExtensionBox;
    }

    /**
     * @return Returns the ofPatternBox.
     */
    private AutoCompleteComboBox getOfPatternBox() {
        if (ofPatternBox == null) {
            ofPatternBox = new AutoCompleteComboBox(getPatterns());
        }
        return ofPatternBox;
    }

    /**
     * This method initializes jCheckBox
     * 
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getSaveRunBox() {
        if (saveRunBox == null) {
            saveRunBox = new JCheckBox();
            saveRunBox.setName("saveRunBox");
            saveRunBox.setText(LangageManager.getProperty("trackenum.saverun"));
        }
        return saveRunBox;
    }

    /**
     * This method initializes jCheckBox
     * 
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getUseOfFunctionBox() {
        if (useOfFunctionBox == null) {
            useOfFunctionBox = new JCheckBox();
            useOfFunctionBox.setActionCommand("useOfFunction");
            useOfFunctionBox.setText(LangageManager
                    .getProperty("trackenum.useof"));
            useOfFunctionBox.setName("useOfFunctionBox");
        }
        return useOfFunctionBox;
    }

    /**
     * This method initializes this
     */
    private void initialize() {
        GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        this.setLayout(new GridBagLayout());
        this.setSize(425, 202);
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.weightx = 1.0D;
        gridBagConstraints1.weighty = 1.0D;
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 1;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.weightx = 1.0D;
        gridBagConstraints3.weighty = 1.0D;
        gridBagConstraints4.gridx = 0;
        gridBagConstraints4.gridy = 2;
        gridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
        this.add(getJPanel(), gridBagConstraints1);
        this.add(getJPanel1(), gridBagConstraints3);
        this.add(getJPanel2(), gridBagConstraints4);
    }

    /**
     * This method will store all used Patterns and if needed the one just
     * entered within the {@link #ofPatternBox}.
     */
    public void storePatterns() {
        String[] patterns = getPatterns();
        String current = getOfPatternBox().getTextField().getText();
        boolean insert = true;
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < patterns.length && insert; i++) {
            if (patterns[i].equals(current))
                insert = false;
            if (i > 0 && patterns[i].trim().length() > 0) {
                result.append("::::");
            }
            if (patterns[i].trim().length() > 0)
                result.append(patterns[i].replaceAll(":", "\\:"));
        }
        if (insert) {
            if (result.length() > 0) {
                result.append("::::");
            }
            result.append(current.replaceAll(":", "\\:"));
        }
        PreferencesManager.put(PREF_PATTERNS, result.toString());
    }

    /**
     * This method will store all values of the panel into the given
     * <code>settings</code>.
     * 
     * @param settings
     *                  Settings which will recieve adjusted values.
     */
    public void storeSettings(TrackEnumeratorSettings settings) {
        settings.setDigitCount(getDigitCountScroller().getValue());
        settings.setDynamicDigitExtend(getDynamicExtendBox().isSelected());
        settings.setFilesFirst(getFilesFirstBox().isSelected());
        settings.setDirectoryRestart(getDirectoryRestartBox().isSelected());
        settings.setNumberExtension(getNumberExtensionBox().isSelected());
        settings.setSaveRun(getSaveRunBox().isSelected());
        if (getUseOfFunctionBox().isSelected()) {
            settings.setOfString(getOfPatternBox().getTextField().getText());
        } else {
            settings.setOfString(null);
        }
    }

} //  @jve:decl-index=0:visual-constraint="10,10"
