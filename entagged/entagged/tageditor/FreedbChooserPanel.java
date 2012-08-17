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
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import entagged.audioformats.AudioFile;
import entagged.audioformats.AudioFileIO;
import entagged.freedb.FreedbReadResult;
import entagged.tageditor.listeners.DialogWindowListener;
import entagged.tageditor.resources.LangageManager;
import entagged.tageditor.resources.PreferencesManager;
import entagged.tageditor.resources.ResourcesRepository;

/**
 * This class is used to display the selected files and hold a freedb query
 * result against it. <br>
 * The user will be able to modify the query result in order to match the
 * selected audiofiles.
 * 
 * @author Kikidonk
 */
public class FreedbChooserPanel extends JDialog {

    /**
     * This class simply will take a look at the selection of
     * {@link FreedbChooserPanel#freedb}and mark it in its list. <br>
     */
    private final class AudioRenderer extends DefaultListCellRenderer {
        /**
         * The border used on rows whose number equals the selection in
         * {@link FreedbChooserPanel#freedb}.<br>
         */
        private final Border border = new LineBorder(Color.BLACK);

        /**
         * (overridden)
         * 
         * @see javax.swing.DefaultListCellRenderer#getListCellRendererComponent(javax.swing.JList,
         *           java.lang.Object, int, boolean, boolean)
         */
        public Component getListCellRendererComponent(JList list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected,
                    cellHasFocus);
            if (index == freedb.getSelectedIndex()) {
                setBorder(border);
            }
            return this;
        }
    }

    /**
     * This class will trigger the dialog to close and set
     * {@link FreedbChooserPanel#aborted}to the given value.
     */
    private final class ControlAction extends AbstractAction {

        private boolean closeValue;

        /**
         * Creates an instance.
         * 
         * @param value
         *                  The value {@link FreedbChooserPanel#aborted}will get.
         */
        public ControlAction(boolean value) {
            this.closeValue = value;
        }

        /**
         * (overridden)
         * 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            aborted = closeValue;
            dispose();
        }
    }

    /**
     * Is used to notify user about unmatching track lengths if available from
     * freedb. <br>
     * 
     * @author Christian Laireiter
     */
    private final class FreedbRenderer extends DefaultListCellRenderer {

        /**
         * (overridden)
         * 
         * @see javax.swing.DefaultListCellRenderer#getListCellRendererComponent(javax.swing.JList,
         *           java.lang.Object, int, boolean, boolean)
         */
        public Component getListCellRendererComponent(JList list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected,
                    cellHasFocus);
            this.setBackground(isSelected ? Color.YELLOW : Color.WHITE);
            int trackLengthFreeDb = result.getTrackDuration(index);
            if (index < audioFiles.size()) {
                int trackLengthFile = ((AudioFile) audioFiles.get(index))
                        .getLength();

                if (trackLengthFreeDb != -1) {
                    int diff = trackLengthFile - trackLengthFreeDb;
                    if (diff < 0)
                        diff *= -1;
                    // More than 2 seconds difference
                    if (diff > 2) {
                        if (isSelected) {
                            this.setBackground(Color.ORANGE);
                        } else {
                            this.setBackground(Color.RED);
                        }
                    }
                }

            }

            if (trackLengthFreeDb != -1)
                this.setText(this.getText() + " "
                        + getLength(trackLengthFreeDb));

            return this;
        }

    }

    /**
     * This action will recieve the direction and call
     * {@link FreedbChooserPanel#moveTrack(int, int)}whenever it is triggered.
     */
    private final class MoveAction extends AbstractAction {
        /**
         * Stores the direction this action will move to.
         */
        private final int direction;

        /**
         * Creates an instance.
         * 
         * @param dir
         *                  The direction which should be triggered.
         */
        public MoveAction(int dir) {
            this.direction = dir;
        }

        /**
         * (overridden)
         * 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            moveTrack(freedb.getSelectedIndex(), direction);
        }

    }

    /**
     * Keystroke for ctrl+down arrow
     */
    private final static KeyStroke CTRL_DOWN = KeyStroke.getKeyStroke(
            KeyEvent.VK_DOWN, InputEvent.CTRL_MASK, true);

    /**
     * Keystroke for ctrl+up arrow
     */
    private final static KeyStroke CTRL_UP = KeyStroke.getKeyStroke(
            KeyEvent.VK_UP, InputEvent.CTRL_MASK, true);

    /**
     * This constant is used to move one freedb result down using
     * {@link #moveTrack(int, int)}.
     */
    public final static int DOWN = 1;

    /**
     * Keystroke for enter arrow
     */
    private final static KeyStroke ENTER = KeyStroke.getKeyStroke(
            KeyEvent.VK_ENTER, 0, true);

    /**
     * Keystroke for escape arrow
     */
    private final static KeyStroke ESCAPE = KeyStroke.getKeyStroke(
            KeyEvent.VK_ESCAPE, 0, true);

    /**
     * This constant is used to move one freedb result up using
     * {@link #moveTrack(int, int)}.
     */
    public static final int UP = 0;

    public static void main(String[] args) {
        try {
            PreferencesManager.initPreferences();
            LangageManager.initLangage();
            File toRead = new File("/tresor/tmp/antx/Freedbinfo.txt");
            int len = (int) toRead.length();
            byte[] content = new byte[len];
            new FileInputStream(toRead).read(content);
            String result = new String(content);
            FreedbReadResult qr = new FreedbReadResult(result, true);
            JFrame parent = new JFrame();

            File dir = new File("/tresor/tmp/antx/ogg");
            File[] files = dir.listFiles();
            AudioFile[] afiles = new AudioFile[files.length];
            for (int i = 0; i < files.length; i++) {
                afiles[i] = AudioFileIO.read(files[i]);
            }
            new FreedbChooserPanel(parent, Arrays.asList(afiles), qr);
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Action for aborting.
     */
    private final ControlAction abortAction = new ControlAction(true);

    /**
     * This field will be set to false if the ok button was pressed.
     */
    protected boolean aborted = true;

    /**
     * This field holds all audiofiles on which the result should be applied.
     * <br>
     * Content is {@link AudioFile}objects.
     */
    protected List audioFiles;

    /**
     * Stores the list for the audiofiles.
     */
    private JScrollPane audioPane;

    /**
     * This panel stores the Lists and move buttons.
     */
    private JPanel content;

    /**
     * This panel stores the control buttons OK and ABORT.
     */
    private JPanel controlButtonPanel;

    /**
     * The list for the freedb results.
     */
    protected JList freedb;

    /**
     * stores {@link #freedb}
     */
    private JScrollPane freeDbPane;

    /**
     * Action which will move down the selected freedb result.
     */
    private final MoveAction moveDownAction = new MoveAction(DOWN);

    /**
     * Action which will move up the selected freedb result.
     */
    private final MoveAction moveUpAction = new MoveAction(UP);

    /**
     * Actin for confirming.
     */
    private final ControlAction okAction = new ControlAction(false);

    /**
     * The results which should be adjusted.
     */
    protected FreedbReadResult result;

    /**
     * Creates and isntance and will display immediately (modal).
     * 
     * @param parent
     *                  The parent frame of the dialog.
     * @param files
     *                  The audiofiles, in that order they will be processed.
     * @param freeDbResult
     *                  The results which should be adjusted (regarding position)
     */
    public FreedbChooserPanel(Frame parent, List files,
            FreedbReadResult freeDbResult) {
        /*
         * configure dialog
         */
        super(parent, LangageManager.getProperty("freedbpanel.matching"), true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.addWindowListener(new DialogWindowListener());
        this.setSize(600, 600);
        this.setLocation(
                PreferencesManager.getInt("entagged.screen.width") / 2 - 300,
                PreferencesManager.getInt("entagged.screen.height") / 2 - 300);
        this.getContentPane().setLayout(new GridBagLayout());

        /*
         * store parameters.
         */
        this.result = freeDbResult;
        this.audioFiles = files;

        /*
         * add components
         */
        this.getContentPane().add(
                getContent(),
                new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
        this.getContentPane().add(
                getControlButtonPanel(),
                new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0), 0, 0));

        registerActions();

        this.setVisible(true);
    }

    protected void doAutoArrangement() {
        for (int i = 0; i < audioFiles.size(); i++) {
            int fileLength = ((AudioFile) audioFiles.get(i)).getLength();
            for (int j = i; j < result.getTracksNumber(); j++) {
                if (fileLength == result.getTrackDuration(j)) {
                    for (int k = 0; k < j - i; k++) {
                        moveTrack(j - k, UP);
                    }
                    break;
                }
            }
        }
        getContent().repaint();
    }

    /**
     * This method creates and returns the audiofile list.
     * 
     * @return Jlist containing the audiofiles.
     */
    protected JScrollPane getAudioList() {
        if (this.audioPane == null) {
            this.audioPane = new JScrollPane();
            DefaultListModel audioModel = new DefaultListModel();
            Iterator it = this.audioFiles.iterator();
            while (it.hasNext()) {
                AudioFile current = (AudioFile) it.next();
                audioModel.addElement(current.getName() + " "
                        + getLength(current.getLength()));
            }
            JList list = new JList(audioModel);
            list.setEnabled(false);
            list.setCellRenderer(new AudioRenderer());
            audioPane.setViewportView(list);
        }
        return this.audioPane;
    }

    /**
     * Creates and returns the panel which contains the lists.
     * 
     * @return
     */
    private JPanel getContent() {
        if (this.content == null) {
            content = new JPanel();

            JButton up = new JButton(moveUpAction);
            up.setToolTipText(LangageManager.getProperty("common.dialog.up"));
            up.setIcon(ResourcesRepository.getImageIcon("uparrow.png"));

            JButton down = new JButton(moveDownAction);
            down.setToolTipText(LangageManager
                    .getProperty("common.dialog.down"));
            down.setIcon(ResourcesRepository.getImageIcon("downarrow.png"));

            JButton autoArrange = new JButton("Auto");
            autoArrange.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    doAutoArrangement();
                }
            });

            GridBagLayout gbl = new GridBagLayout();
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(2, 5, 2, 5);
            content.setLayout(gbl);

            gbc.fill = GridBagConstraints.BOTH;
            gbc.anchor = GridBagConstraints.WEST;
            gbc.weighty = 1;
            gbc.weightx = 1;

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridheight = 3;
            content.add(getAudioList(), gbc);

            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.gridheight = 1;
            gbc.gridheight = 3;
            content.add(getFreeDbList(), gbc);

            gbc.weightx = 0.0;
            gbc.weighty = 0;
            gbc.fill = GridBagConstraints.NONE;
            gbc.anchor = GridBagConstraints.NORTH;
            gbc.gridwidth = 1;
            gbc.gridheight = 1;

            gbc.gridx = 2;
            gbc.gridy = 0;
            gbl.setConstraints(up, gbc);
            content.add(up);

            gbc.gridx = 2;
            gbc.gridy = 1;
            gbl.setConstraints(down, gbc);
            content.add(down);

            gbc.gridx = 2;
            gbc.gridy = 2;
            gbl.setConstraints(autoArrange, gbc);
            content.add(autoArrange);
        }
        return this.content;
    }

    /**
     * Returns the buttonpanel containing OK and ABORT.
     * 
     * @return Control buttons in a separate panel
     */
    private JPanel getControlButtonPanel() {
        if (this.controlButtonPanel == null) {
            this.controlButtonPanel = new JPanel(new GridBagLayout());

            JButton ok = new JButton(okAction);
            ok.setText(LangageManager.getProperty("common.dialog.ok"));

            JButton abort = new JButton(abortAction);
            abort.setText(LangageManager.getProperty("common.dialog.abort"));

            this.controlButtonPanel.add(ok, new GridBagConstraints(0, 0, 1, 1,
                    0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
                    new Insets(2, 5, 2, 5), 0, 0));
            this.controlButtonPanel.add(abort, new GridBagConstraints(1, 0, 1,
                    1, 0.0, 0.0, GridBagConstraints.EAST,
                    GridBagConstraints.NONE, new Insets(2, 5, 2, 5), 0, 0));
        }
        return this.controlButtonPanel;
    }

    /**
     * 
     * @return
     */
    private JScrollPane getFreeDbList() {
        if (freeDbPane == null) {
            DefaultListModel freedbModel = new DefaultListModel();
            for (int i = 0; i < result.getTracksNumber(); i++)
                freedbModel.addElement(result.getTrackTitle(i));
            this.freedb = new JList(freedbModel);
            this.freedb.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            this.freedb.setCellRenderer(new FreedbRenderer());
            freeDbPane = new JScrollPane(freedb);
            freedb.getSelectionModel().addListSelectionListener(
                    new ListSelectionListener() {
                        public void valueChanged(ListSelectionEvent e) {
                            getAudioList().repaint();
                        }
                    });
        }
        return freeDbPane;
    }

    /**
     * This method computes mm:ss out of the given value.
     * 
     * @param length
     *                  seconds which should be transformed.
     * @return the seconds as mm:ss.
     */
    protected String getLength(int length) {
        if (length != -1) {
            int minutes = length / 60;
            int sec = (length - minutes * 60);
            DecimalFormat fmt = new DecimalFormat("00");
            return fmt.format(minutes) + ":" + fmt.format(sec);
        }
        return "??";
    }

    /**
     * This method returns <code>true</code>, if the dialog was closed by
     * abort.
     * 
     * @return <code>true</code> if aborted.
     */
    public boolean isAborted() {
        return this.aborted;
    }

    /**
     * This method will move the freedb result at given index into given
     * direction.
     * 
     * @param index
     *                  Index of the freedb result to move.
     * @param direction
     *                  direction to move it. <br>
     * @see #UP
     * @see #DOWN
     */
    protected void moveTrack(int index, int direction) {
        if (index == -1)
            return;

        if (index == 0 && direction == UP)
            return;

        if (index == result.getTracksNumber() - 1 && direction == DOWN)
            return;

        result.swapTracks(index, (direction == UP) ? index - 1 : index + 1);

        Object o = ((DefaultListModel) freedb.getModel()).elementAt(index);
        ((DefaultListModel) freedb.getModel()).set(index,
                ((DefaultListModel) freedb.getModel())
                        .elementAt((direction == UP) ? index - 1 : index + 1));
        ((DefaultListModel) freedb.getModel()).set(
                (direction == UP) ? index - 1 : index + 1, o);
        freedb.setSelectedIndex((direction == UP) ? index - 1 : index + 1);
    }

    /**
     * Creates aciton and input -Map and assigns it to each component.
     */
    private void registerActions() {
        JPanel thePanel = (JPanel) this.getContentPane();
        InputMap inputMap = thePanel
                .getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(CTRL_UP, moveUpAction);
        inputMap.put(CTRL_DOWN, moveDownAction);
        inputMap.put(ESCAPE, abortAction);
        inputMap.put(ENTER, okAction);
        ActionMap actionMap = thePanel.getActionMap();
        actionMap.put(moveUpAction, moveUpAction);
        actionMap.put(moveDownAction, moveDownAction);
        actionMap.put(okAction, okAction);
        actionMap.put(abortAction, abortAction);
    }
}