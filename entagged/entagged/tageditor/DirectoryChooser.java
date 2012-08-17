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
package entagged.tageditor;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import entagged.tageditor.listeners.NavigatorListener;

/**
 * This class is meant for displaying the current location of entagged. <br>
 * Further it is possible to insert the path by the usere. <br>
 */
public class DirectoryChooser extends JComboBox implements NavigatorListener {

    /**
     * This class will listen to the keys "tab" for taking the suggestion.
     */
    private final class KeyListener extends KeyAdapter {
        /**
         * (overridden)
         * 
         * @see java.awt.event.KeyAdapter#keyTyped(java.awt.event.KeyEvent)
         */
        public void keyTyped(KeyEvent e) {
            char pressed = e.getKeyChar();
            setPopupVisible(false);
            if (pressed == '/' || pressed == '\\') {
                pathEditListener.enabled = true;
            }
            if (e.getModifiers() == 0) {
                if (pressed == '\t') {
                    JTextComponent textComp = (JTextComponent) getEditor()
                            .getEditorComponent();
                    textComp.setCaretPosition(textComp.getText().length());
                }
                if (pressed == '\n') {
                    System.out.println("Enter");
                    File tmp = new File(getText());
                    if (tmp.exists() && tmp.isDirectory() && tmp.canRead()) {
                        ((JTextComponent) getEditor().getEditorComponent())
                                .setCaretPosition(getText().length());
                        setAndSelectDirectory(tmp);
                    }
                }
                if (pressed == 27) {
                    if (stateModify) {
                        setText(getUnselectedText());
                        stateModify = false;
                    } else {
                        setText(control.getNavigator().getCurrentFolder()
                                .getAbsolutePath());
                    }
                    pathEditListener.enabled = false;
                }
            }
        }
    }

    /**
     * This class is intended to fill the combobox values upon the current
     * state. <br>
     * If the user is changing the textfield of the combobox, all completion
     * possibilites are shown. Else the directories up to the root will be
     * displayed.
     */
    private final class Model extends DefaultComboBoxModel {

        /**
         * Contains all Values that should be displayed
         */
        private String[] values = new String[0];

        /**
         * (overridden)
         * 
         * @see javax.swing.DefaultComboBoxModel#getElementAt(int)
         */
        public Object getElementAt(int index) {
            return values[index];
        }

        /**
         * (overridden)
         * 
         * @see javax.swing.DefaultComboBoxModel#getSize()
         */
        public int getSize() {
            return values.length;
        }

        /**
         * Looks at the current state and alters the list.
         */
        public void update() {
            if (isPopupVisible()) {
                return;
            }
            if (stateModify) {
                String[] sugg = getSuggestions();
                if (sugg != null) {
                    values = new String[sugg.length];
                    if (sugg != null) {
                        for (int i = 0; i < sugg.length; i++) {
                            values[i] = getPath()
                                    + System.getProperty("file.separator")
                                    + sugg[i];
                        }
                    }
                } else {
                    values = new String[] { getText() };
                }
            } else {
                values = getParentHierarchy();
            }
        }
    }

    /**
     * This class is used to listen to the users insertions, and provide the
     * next possible sollution. <br>
     */
    private final class PathEditListener implements DocumentListener {

        boolean enabled = true;

        /**
         * (overridden)
         * 
         * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
         */
        public void changedUpdate(DocumentEvent e) {
            // of no interest
        }

        /**
         * (overridden)
         * 
         * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
         */
        public void insertUpdate(DocumentEvent e) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    process();
                }
            });
        }

        /**
         * This method will perform the suggestions. <br>
         */
        protected void process() {
            if (!enabled || isPopupVisible()) {
                validateAndNotify();
                return;
            }
            stateModify = true;
            JTextComponent comp = getTextComponent();
            String input = getText().replaceAll("\\\\", "/");
            if (oldValue.equals(input)) {
                return;
            }
            if (comp.getCaretPosition() == input.trim().length()) {
                int pos = input.lastIndexOf('/');
                String[] sorted = getSuggestions();
                if (sorted != null && sorted.length != 0) {
                    int caretPos = comp.getCaretPosition();
                    String tillComplete = input.substring(pos + 1);
                    tillComplete = sorted[0].substring(tillComplete.length());
                    comp.setText(input + tillComplete);
                    comp.setCaretPosition(comp.getText().length());
                    if (tillComplete.length() > 0) {
                        comp.moveCaretPosition(caretPos);
                    } else {
                        comp.setCaretPosition(comp.getText().length());
                    }
                    comboModel.update();
                }
            }
            validateAndNotify();
        }

        /**
         * (overridden)
         * 
         * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
         */
        public void removeUpdate(DocumentEvent e) {
            stateModify = true;
            validateAndNotify();
        }

    }

    /**
     * The model for the combobox.
     */
    protected Model comboModel = null;

    /**
     * The table model which will recieve changes.
     */
    protected TagEditorFrame control;

    /**
     * On each value change of the textfield, its value is stored here. <br>
     * It is used to ignore multiple fired events on the same typing and for
     * retrieving the path if a completion was selected.
     */
    protected String oldValue = "";

    /**
     * This field provides possible completions.
     */
    protected PathEditListener pathEditListener;

    /**
     * This field shows whether the user is entering another directory.
     */
    protected boolean stateModify = false;

    /**
     * Creates an instance.
     * 
     * @param ctrl
     *                  The model which will be notified if a directory change occurs.
     */
    public DirectoryChooser(TagEditorFrame ctrl) {
        this.control = ctrl;
        this.setEditable(true);
        ((JTextComponent) getEditor().getEditorComponent()).getDocument()
                .addDocumentListener(pathEditListener = new PathEditListener());
        this.getEditor().getEditorComponent().addKeyListener(new KeyListener());
        this.setModel(this.comboModel = new Model());
        this.control.getNavigator().addNavigatorListener(this);
    }

    /**
     * (overridden)
     * 
     * @see entagged.tageditor.listeners.NavigatorListener#directoryChanged(java.io.File,
     *           java.io.File[], int)
     */
    public void directoryChanged(File newDirectory, File[] contents, int how) {
        this.setDirectory(newDirectory);
    }

    /**
     * This method will interpret the value of the textfield as a existing
     * direcotry and return the full paths of all parents. <br>
     * 
     * @return the parents paths.
     */
    protected String[] getParentHierarchy() {
        ArrayList parents = new ArrayList();
        File file = new File(getPath());
        while (file != null && file.exists()) {
            parents.add(0, file.getAbsolutePath());
            file = file.getParentFile();
        }
        return (String[]) parents.toArray(new String[parents.size()]);
    }

    /**
     * This method will extract a valid path of the content of the textfield.
     * <br>
     * 
     * @return The valid path out of the textfield. If none could be extracted
     *              <code>null</code> is returned.
     */
    public String getPath() {
        String result = getUnselectedText();
        if (isPopupVisible()) {
            result = oldValue;
        }
        if (new File(result).isDirectory()) {
            return result;
        }
        int pos = result.replaceAll("\\\\", "/").lastIndexOf('/');
        if (pos >= 0 && pos < getText().length()) {
            result = result.substring(0, pos);
            File file = null;
            if (result.length() == 0) {
                file = File.listRoots()[0];
            } else {
                file = new File(result);
            }
            if (file.exists() && file.isDirectory()) {
                return result;
            }
        }
        return null;
    }

    /**
     * This method will look at the users input and return all compeltion
     * possibilities. <br>
     * 
     * @return Sorted list of completions.
     */
    protected String[] getSuggestions() {
        String[] sorted = null;
        String path = getPath() + System.getProperty("file.separator");
        if (path != null && path.length() < getText().length()) {
            File curr = new File(path);
            if (curr.exists() && curr.isDirectory()) {
                File[] possibleDirs = curr.listFiles(new FileFilter() {
                    public boolean accept(File pathname) {
                        return pathname.isDirectory();
                    }
                });
                ArrayList strings = new ArrayList();
                String prefix = getUnselectedText().substring(path.length());
                for (int i = 0; i < possibleDirs.length; i++) {
                    if (prefix.length() == 0) {
                        strings.add(possibleDirs[i]);
                    } else {
                        String name = possibleDirs[i].getName();
                        if (name.startsWith(prefix)) {
                            strings.add(possibleDirs[i].getName());
                        }
                    }
                }
                sorted = (String[]) strings.toArray(new String[strings.size()]);
                Arrays.sort(sorted);
            }
        }
        return sorted;
    }

    /**
     * This method will return the currently entered text. <br>
     * 
     * @return Text.
     */
    protected String getText() {
        return getTextComponent().getText();
    }

    /**
     * This method will return the editor field of the current combobox if it's
     * an instance of {@link JTextComponent}.<br>
     * 
     * @return <code>null</code> only if a programmer did modify this class
     *              that the editorfield is no {@link JTextComponent}any more.
     */
    protected JTextComponent getTextComponent() {
        Component comp = getEditor().getEditorComponent();
        if (comp instanceof JTextComponent) {
            return (JTextComponent) comp;
        }
        return null;
    }

    /**
     * This method will only return the beginning of the textfield until the
     * start of the selection. If no selection is active the whole field will be
     * returned.
     * 
     * @return the unselected text of the combobox's textfield.
     */
    protected String getUnselectedText() {
        JTextComponent comp = getTextComponent();
        String selected = comp.getSelectedText();
        if (selected != null)
            return comp.getText().substring(0,
                    comp.getText().length() - comp.getSelectedText().length());
        return comp.getText();
    }

    /**
     * This method will set the given file as selected for the current component
     * and the table model.
     * 
     * @param f
     *                  Directory which should be displayed.
     */
    protected void setAndSelectDirectory(File f) {
        if (f == null || !f.isDirectory())
            return;
        control.getNavigator().setDirectory(f);
    }

    /**
     * Sets the text of this component to the path of the given file.
     * 
     * @param f
     *                  Directory, where entagged has browsed to.
     */
    protected void setDirectory(File f) {
        if (f == null || !f.isDirectory())
            return;
        setText(f.getAbsolutePath());
        oldValue = getText();
        stateModify = false;
        pathEditListener.enabled = false;
        comboModel.update();
        pathEditListener.enabled = true;
        validateAndNotify();
    }

    /**
     * This method will set the given text.
     * 
     * @param text
     *                  new Value of the component.
     */
    protected void setText(String text) {
        getTextComponent().setText(text);
    }

    /**
     * This method checks the currently entered text and checks whether it
     * represents an existing directory. <br>
     * If not the background color of the Combobox is changed to yellow.
     */
    protected void validateAndNotify() {
        final File candidate = new File(getText());
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (!candidate.exists() || !candidate.isDirectory()) {
                    getEditor().getEditorComponent()
                            .setBackground(Color.RED);
                } else {
                    getEditor().getEditorComponent().setBackground(Color.WHITE);
                }
            }
        });
    }
}