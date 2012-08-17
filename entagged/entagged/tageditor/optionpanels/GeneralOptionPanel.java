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
package entagged.tageditor.optionpanels;

import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import entagged.audioformats.mp3.Id3v2Tag;
import entagged.tageditor.models.Navigator;
import entagged.tageditor.optionpanels.OptionPanelInterface;
import entagged.tageditor.resources.LAFManager;
import entagged.tageditor.resources.LangageManager;
import entagged.tageditor.resources.PreferencesManager;
import entagged.tageditor.resources.ResourcesRepository;
import entagged.tageditor.tools.FileTimePreserver;

import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;

import java.awt.Component;
import java.awt.Insets;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JTextField;

public class GeneralOptionPanel extends JPanel implements OptionPanelInterface, ItemListener {

	private static final long serialVersionUID = 1L;

	private final ImageIcon IMAGE = ResourcesRepository.getImageIcon("general-optionpanel-button.png");

	/**  The corresponding langage Names in english */
	private static Atome[] langage = Atome.init();
	
	private Vector availableLAFs;
	private int indexOfCurrent;
	
	private JFrame frame;

	private JLabel jLabel_Encoding = null;
	private JComboBox jComboBox_Encoding = null;
	private JCheckBox jCheckBox_preserveFileTime = null;
	private JCheckBox jCheckBox_automaticUpdate = null;
	private JLabel jLabel_Language = null;
	private JComboBox jComboBox_Language = null;
	private JLabel jLabel_LAF = null;
	private JComboBox jComboBox_LAF = null;

	private JLabel jLabel_MediaPlayer = null;

	private JTextField jTextField_MediaPlayer = null;

	private JButton jButton_MediaPlayer = null;

	/**
	 * This is the default constructor
	 * @param frame 
	 */
	public GeneralOptionPanel(JFrame frame) {
		super();

		this.frame = frame;
		
		availableLAFs = new Vector();
		availableLAFs.add(new LookAndFeelAdapter("Java/Metal Look And Feel (builtin)","javax.swing.plaf.metal.MetalLookAndFeel"));
		availableLAFs.add(new LookAndFeelAdapter("Motif/CDE Look And Feel (builtin)","com.sun.java.swing.plaf.motif.MotifLookAndFeel"));
		availableLAFs.add(new LookAndFeelAdapter("Windows Look And Feel (builtin & needs windows)","com.sun.java.swing.plaf.windows.WindowsLookAndFeel"));
		availableLAFs.add(new LookAndFeelAdapter("GTK Look And Feel (builtin & needs gtk)","com.sun.java.swing.plaf.gtk.GTKLookAndFeel"));
		availableLAFs.add(new LookAndFeelAdapter("Squareness Look And Feel","net.beeger.squareness.SquarenessLookAndFeel"));

		// No seth the installed flag 
		indexOfCurrent = determineAvailability(availableLAFs);

		Id3v2Tag.DEFAULT_ENCODING = PreferencesManager.get("entagged.tag.encoding");

		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
		gridBagConstraints31.gridx = 2;
		gridBagConstraints31.fill = GridBagConstraints.BOTH;
		gridBagConstraints31.insets = new Insets(5, 10, 10, 10);
		gridBagConstraints31.gridy = 3;
		GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
		gridBagConstraints23.fill = GridBagConstraints.BOTH;
		gridBagConstraints23.gridy = 3;
		gridBagConstraints23.weightx = 1.0;
		gridBagConstraints23.insets = new Insets(5, 10, 10, 10);
		gridBagConstraints23.gridx = 1;
		GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
		gridBagConstraints13.gridx = 0;
		gridBagConstraints13.fill = GridBagConstraints.BOTH;
		gridBagConstraints13.insets = new Insets(5, 10, 10, 10);
		gridBagConstraints13.gridy = 3;
		jLabel_MediaPlayer = new JLabel("Media Player :");
		jLabel_MediaPlayer.setText(LangageManager.getProperty("mediaplayeroptionpanel.mediaplayer"));
		Border border = BorderFactory.createEtchedBorder();
		TitledBorder titledBorder = BorderFactory.createTitledBorder(border,
				LangageManager.getProperty("generaloptionpanel.title"), TitledBorder.LEFT, TitledBorder.TOP);

		this.setSize(322, 220);
		this.setBorder(titledBorder);
		this.setLayout(new GridBagLayout());

		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.insets = new Insets(10, 10, 5, 10);
		gridBagConstraints.gridy = 0;
		jLabel_Encoding = new JLabel("Encoding : ");
		jLabel_Encoding.setText(LangageManager.getProperty("generaloptionpanel.encoding"));
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.fill = GridBagConstraints.BOTH;
		gridBagConstraints1.gridy = 0;
		gridBagConstraints1.weightx = 1.0;
		gridBagConstraints1.insets = new Insets(10, 10, 5, 10);
		gridBagConstraints1.gridwidth = 2;
		gridBagConstraints1.gridx = 1;
		this.add(jLabel_Encoding, gridBagConstraints);
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.gridwidth = 2;
		gridBagConstraints2.fill = GridBagConstraints.BOTH;
		gridBagConstraints2.insets = new Insets(10, 10, 2, 10);
		gridBagConstraints2.gridy = 4;

		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.gridx = 0;
		gridBagConstraints3.gridwidth = 2;
		gridBagConstraints3.fill = GridBagConstraints.BOTH;
		gridBagConstraints3.insets = new Insets(2, 10, 5, 10);
		gridBagConstraints3.gridy = 5;
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.gridx = 0;
		gridBagConstraints11.fill = GridBagConstraints.BOTH;
		gridBagConstraints11.insets = new Insets(5, 10, 5, 10);
		gridBagConstraints11.gridy = 1;
		jLabel_Language = new JLabel("Language : ");
		jLabel_Language.setText(LangageManager.getProperty("langageoptionpanel.langage"));
		GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
		gridBagConstraints21.fill = GridBagConstraints.BOTH;
		gridBagConstraints21.gridy = 1;
		gridBagConstraints21.weightx = 1.0;
		gridBagConstraints21.insets = new Insets(5, 10, 5, 10);
		gridBagConstraints21.gridwidth = 2;
		gridBagConstraints21.gridx = 1;
		GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
		gridBagConstraints12.gridx = 0;
		gridBagConstraints12.fill = GridBagConstraints.BOTH;
		gridBagConstraints12.insets = new Insets(5, 10, 5, 10);
		gridBagConstraints12.gridy = 2;
		jLabel_LAF = new JLabel("LAF : ");
		jLabel_LAF.setText(LangageManager.getProperty("lafoptionpanel.laf"));
		this.add(getJComboBox_Encoding(), gridBagConstraints1);
		this.add(getJCheckBox_preserveFileTime(), gridBagConstraints2);
		this.add(getJCheckBox_automaticUpdate(), gridBagConstraints3);
		this.add(jLabel_Language, gridBagConstraints11);
		GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
		gridBagConstraints22.fill = GridBagConstraints.BOTH;
		gridBagConstraints22.gridy = 2;
		gridBagConstraints22.weightx = 1.0;
		gridBagConstraints22.insets = new Insets(5, 10, 5, 10);
		gridBagConstraints22.gridx = 1;
		gridBagConstraints22.gridwidth = 2;
		this.add(getJComboBox_Language(), gridBagConstraints21);
		this.add(jLabel_LAF, gridBagConstraints12);
		this.add(getJComboBox_LAF(), gridBagConstraints22);
		this.add(jLabel_MediaPlayer, gridBagConstraints13);
		this.add(getJTextField_MediaPlayer(), gridBagConstraints23);
		this.add(getJButton_MediaPlayer(), gridBagConstraints31);
	}

	/**
	 * This method initializes jComboBox_Encoding	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getJComboBox_Encoding() {
		if(jComboBox_Encoding == null) {
			jComboBox_Encoding = new JComboBox(new String[] { "UTF-16", "ISO-8859-1", "UTF-16BE", "UTF-8" });
			jComboBox_Encoding.setSelectedItem(PreferencesManager.get("entagged.tag.encoding"));
		}
		return jComboBox_Encoding;
	}

	/**
	 * This method initializes jComboBox_Language	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getJComboBox_Language() {
		if(jComboBox_Language == null) {
			jComboBox_Language = new JComboBox();

			String currentLangage = PreferencesManager.get("entagged.langage");
			for(int i = 0 ; i < langage.length ; i++) {
				jComboBox_Language.addItem(langage[i]);
				if(langage[i].getValue().equals(currentLangage))
					jComboBox_Language.setSelectedIndex(i);
			}
		}
		return jComboBox_Language;
	}

	/**
	 * This method initializes jCheckBox_preserveFileTime
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBox_preserveFileTime() {
		if(jCheckBox_preserveFileTime == null) {
			jCheckBox_preserveFileTime = new JCheckBox("Preserve File Time");
			jCheckBox_preserveFileTime.setText(LangageManager.getProperty("generaloptionpanel.preseverfiletime"));
			jCheckBox_preserveFileTime.setSelected(FileTimePreserver.isOptionActive());
			jCheckBox_preserveFileTime.addItemListener(this);
		}
		return jCheckBox_preserveFileTime;
	}

	/**
	 * This method initializes jCheckBox_automaticUpdate
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBox_automaticUpdate() {
		if(jCheckBox_automaticUpdate == null) {
			jCheckBox_automaticUpdate = new JCheckBox("Automatic Update");
			jCheckBox_automaticUpdate.setText(LangageManager.getProperty("generaloptionpanel.automaticupdate"));
			jCheckBox_automaticUpdate.setSelected(PreferencesManager.getInt("tageditor.navigator.updateinterval") > 0);
			jCheckBox_automaticUpdate.addItemListener(this);
		}
		return jCheckBox_automaticUpdate;
	}
	
	/**
	 * This method initializes jComboBox_LAF	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getJComboBox_LAF() {
		if(jComboBox_LAF == null) {
			jComboBox_LAF = new JComboBox(availableLAFs);
			jComboBox_LAF.setRenderer(new LaFRenderer());
			jComboBox_LAF.addItemListener(new LaFSelectionMgr(indexOfCurrent));
			jComboBox_LAF.setSelectedIndex(indexOfCurrent);

			for( int i =  0; i<availableLAFs.size(); i++) {
				if(PreferencesManager.get( "entagged.lookandfeel").equals(((LookAndFeelAdapter)availableLAFs.elementAt(i)).getClassName()))
					jComboBox_LAF.setSelectedIndex(i);
			}
		}
		return jComboBox_LAF;
	}
	
	/**
	 * This method initializes jTextField_MediaPlayer	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextField_MediaPlayer() {
		if(jTextField_MediaPlayer == null) {
			jTextField_MediaPlayer = new JTextField();
			jTextField_MediaPlayer.setEditable(false);
			
			String mediaPlayer = PreferencesManager.get("entagged.mediaplayer");
			if(mediaPlayer != null)
				jTextField_MediaPlayer.setText(mediaPlayer);
		}
		return jTextField_MediaPlayer;
	}

	/**
	 * This method initializes jButton_MediaPlayer	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton_MediaPlayer() {
		if(jButton_MediaPlayer == null) {
			jButton_MediaPlayer = new JButton();
			jButton_MediaPlayer.setText(LangageManager.getProperty("common.dialog.explore"));
			jButton_MediaPlayer.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					onPacourir();
				}
			});
		}
		return jButton_MediaPlayer;
	}
	
	/**
	 * Affiche un sélecteur de fichers
	 */
	private void onPacourir() {
		JFileChooser chooser = new JFileChooser(); //création dun nouveau filechooser
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setApproveButtonText(LangageManager.getProperty("common.dialog.select")); //intitulé du bouton
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
			getJTextField_MediaPlayer().setText(chooser.getSelectedFile().getAbsolutePath());
	}

	public JButton getButton() {
		return new JButton(LangageManager.getProperty("generaloptionpanel.button"), IMAGE);
	}

	public String getOptionName() {
		return "general";
	}

	public JPanel getPanel() {
		return this;
	}
	
	public boolean saveOptions() {
		boolean forceReboot = false;
		
		Id3v2Tag.DEFAULT_ENCODING = getJComboBox_Encoding().getSelectedItem().toString();
		PreferencesManager.put("entagged.tag.encoding", getJComboBox_Encoding().getSelectedItem().toString());
		FileTimePreserver.setOptionActive(getJCheckBox_preserveFileTime().isSelected());
		PreferencesManager.putInt("tageditor.navigator.updateinterval",
			getJCheckBox_automaticUpdate().isSelected() ? Navigator.UpdateWatcher.DEFAULT_UPDATE_INTERVAL : -1);
		
		// LOOK AND FEEL
		LookAndFeelAdapter selected = (LookAndFeelAdapter)getJComboBox_LAF().getSelectedItem();
		if(!PreferencesManager.get("entagged.lookandfeel").equals(selected.getClassName())) {
			PreferencesManager.put("entagged.lookandfeel", selected.getClassName());
			try{
				LAFManager.updateLookAndFeel(this.frame, selected.getClassName());
			}
			catch(Exception ex) {
				JOptionPane.showMessageDialog(this, LangageManager.getProperty("lafoptionpanel.error"), LangageManager.getProperty( "lafoptionpanel.title" ), JOptionPane.INFORMATION_MESSAGE );				
			}
		}
		
		// LANGUAGE
		String lang = ((Atome)getJComboBox_Language().getSelectedItem()).getValue();
		if(!PreferencesManager.get("entagged.langage").equals(lang)) {
			PreferencesManager.put("entagged.langage",lang);
			forceReboot = true;
		}
		
		String mediaPlayer = getJTextField_MediaPlayer().getText();
		if(mediaPlayer.length() > 0)
			PreferencesManager.put("entagged.mediaplayer",mediaPlayer);
		
		return forceReboot;
	}

	public void itemStateChanged(ItemEvent e) {
		// For now, just preserveFiletime and automaticUpdate are registered,
		// so the source won't be verified.
		if (getJCheckBox_automaticUpdate().isSelected() && getJCheckBox_preserveFileTime().isSelected()) {
			JOptionPane.showMessageDialog(this,
				LangageManager.getProperty("generaloptionpanel.warnfiletimeupdateconflict"),
				LangageManager.getProperty("common.dialog.warning"),
				JOptionPane.WARNING_MESSAGE);
		}
	}

	/**
	 * This Method determines if the given look-and-feels are really installed
	 * on the current system.<br>
	 * The field {@link LookAndFeelAdapter#installed} will be modified accordingly.
	 * @param availableLAFs A collection of {@link LookAndFeelAdapter} objects.
	 * @return the index of the LookAndFeelAdapter which represents 
	 * 	the currently used one.
	 */
	private int determineAvailability(Collection availableLAFs) {
		int result = -1;
		String currClsName = UIManager.getLookAndFeel().getClass().getName();
		Iterator it = availableLAFs.iterator();
		int indexCounter = 0;
		while (it.hasNext()) {
			LookAndFeelAdapter current = (LookAndFeelAdapter)it.next();
			boolean gotIt = false;
			try {
				LookAndFeel lf = (LookAndFeel) Class.forName(current.getClassName()).newInstance();
				gotIt = lf.isSupportedLookAndFeel();
			}
			catch (Exception e) {}
			current.installed = gotIt;
			if (currClsName.equalsIgnoreCase(current.getClassName()))
				result =  indexCounter;
			indexCounter++;
		}
		return result;
	}

	private class LookAndFeelAdapter {
		private String name,className;
		/**
		 * If <code>true</code> the currently represented Look-And-Feel is
		 * installed on the current system.<br>
		 */
		boolean installed = false; 

		public LookAndFeelAdapter(String name, String className) {
			this.name = name;
			this.className = className;
		}

		public String getClassName() {
			return className;
		}

		public String getName() {
			return name;
		}

		public String toString() {
			return name;
		}
	}

	/**
	 * Renders the {@link LookAndFeelAdapter} objects in the combobox.
	 * 
	 * @author Christian Laireiter
	 */
	private final class LaFRenderer extends DefaultListCellRenderer {
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			LookAndFeelAdapter adapter = null;
			if (value instanceof LookAndFeelAdapter)
				adapter = (LookAndFeelAdapter)value;
			if (adapter != null && !adapter.installed)
				isSelected = false;
			Component result = super.getListCellRendererComponent(list, value,index, isSelected, cellHasFocus);
			if (adapter != null && !adapter.installed)
				this.setEnabled(false);
			return result;
		}
	}

	/**
	 * This listener won't accept the selection of {@link LookAndFeelAdapter} objects
	 * whithin {@link #laf}.<br> 
	 *
	 * @author Christian Laireiter
	 */	
	private final class LaFSelectionMgr implements ItemListener {
		/**
		 * If <code>true</code>, the {@link #itemStateChanged(ItemEvent)} method
		 * won't do anything.
		 */
		boolean adjusting = false;

		/**
		 * The last deselected index is stored here.<br>
		 * With this it is possible to restore the old selection, when
		 * an invalid adapter is selected. 
		 */
		int lastSelectedIndex = -1;

		/**
		 * Creates an instance.
		 * @param selectedIndex The initial fallback index if the selected 
		 * 	L&F is invalid.
		 */
		public LaFSelectionMgr (int selectedIndex) {
			this.lastSelectedIndex = selectedIndex;
		}

		/** 
		 * (overridden)
		 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
		 */
		public void itemStateChanged(final ItemEvent e) {
			if (lastSelectedIndex == -1)
				lastSelectedIndex = ((JComboBox)e.getSource()).getSelectedIndex();
			if (!adjusting) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					if (e.getItem() instanceof LookAndFeelAdapter) {
						LookAndFeelAdapter curr = (LookAndFeelAdapter)e.getItem();
						if (!curr.installed) {
							adjusting = true;
							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									((JComboBox)e.getSource())
									.setSelectedIndex(lastSelectedIndex);
									adjusting = false;
								}
							});
						}
						else
							this.lastSelectedIndex = ((JComboBox)e.getSource()).getSelectedIndex();
					}
				}
			}
		}	
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"

class Atome {

	private String name;
	private String value;

	public Atome(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public String toString() {
		return name;
	}

	public static Atome [] init() {
		Atome [] grp = new Atome[4];
		grp[0] = new Atome("English","english.lang");
		grp[1] = new Atome("French","french.lang");
		grp[2] = new Atome("Spanish","spanish.lang");
		grp[3] = new Atome("German","german.lang");
		return grp;
	}
}