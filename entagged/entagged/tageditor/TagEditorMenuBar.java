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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import miage.ihm.JPanel_AjoutDossier;

import entagged.listing.gui.ListingWizard;
import entagged.listing.gui.statistic.StatisticsControl;
import entagged.tageditor.optionpanels.FreedbOptionPanel;
import entagged.tageditor.optionpanels.GeneralOptionPanel;
import entagged.tageditor.optionpanels.OptionDialog;
import entagged.tageditor.optionpanels.OptionPanelInterface;
import entagged.tageditor.resources.Initialization;
import entagged.tageditor.resources.LangageManager;
import entagged.tageditor.util.MultipleFieldsMergingTable;

/**
 * Creates the Parent Frame Menu Bar $Id: TagEditorMenuBar.java,v 1.10
 * 2005/01/05 12:28:54 liree Exp $
 * 
 * @author Raphael Slinckx (KiKiDonK) ; Nicolas Velin
 * @version v0.05
 */
public class TagEditorMenuBar extends JMenuBar {

	protected TagEditorFrame f;

	private JMenu jMenu_file = null;
	private JMenu jMenu_help = null;
	private JMenu jMenu_tools = null;
	private JMenuItem jMenuItem_Exit = null;
	private JMenuItem jMenuItem_Help = null;
	private JMenuItem jMenuItem_About = null;
	private JMenuItem jMenuItem_Listing = null;
	private JMenuItem jMenuItem_Options = null;
	private JMenuItem jMenuItem_Statistics = null;
	private JMenuItem jMenuItem_Index = null;
	private JMenuItem jMenuItem_WrongInput = null;
	private JMenuItem jMenuItem_Double = null;

	public TagEditorMenuBar(TagEditorFrame f) {
		super();
		this.f = f;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.add(getJMenu_file());
		this.add(getJMenu_tools());
		this.add(getJMenu_help());
	}

	/**
	 * This method initializes jMenu_file	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getJMenu_file() {
		if (jMenu_file == null) {
			jMenu_file = new JMenu(LangageManager.getProperty("entaggedmenubar.filemenu"));
			jMenu_file.add(getJMenuItem_Exit());
		}
		return jMenu_file;
	}

	/**
	 * This method initializes jMenuItem_Exit	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItem_Exit() {
		if (jMenuItem_Exit == null) {
			jMenuItem_Exit = new JMenuItem(LangageManager.getProperty("entaggedmenubar.exititem"));
			jMenuItem_Exit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					f.saveGUIPreferences();
					Initialization.exit();
				}
			});
		}
		return jMenuItem_Exit;
	}
	
	/**
	 * This method initializes jMenu_help	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getJMenu_help() {
		if (jMenu_help == null) {
			jMenu_help = new JMenu(LangageManager.getProperty("entaggedmenubar.helpmenu"));
			jMenu_help.add(getJMenuItem_Help());
			jMenu_help.add(getJMenuItem_About());
		}
		return jMenu_help;
	}
	
	/**
	 * This method initializes jMenuItem_Help	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItem_Help() {
		if (jMenuItem_Help == null) {
			jMenuItem_Help = new JMenuItem(LangageManager.getProperty("entaggedmenubar.helpmenu"));
			jMenuItem_Help.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JOptionPane.showMessageDialog(f, LangageManager.getProperty("entaggedmenubar.helpmessage"), LangageManager.getProperty("entaggedmenubar.helpmenu"), JOptionPane.INFORMATION_MESSAGE);
				}
			});
		}
		return jMenuItem_Help;
	}

	/**
	 * This method initializes jMenuItem_About	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItem_About() {
		if (jMenuItem_About == null) {
			jMenuItem_About = new JMenuItem(LangageManager.getProperty("entaggedmenubar.aboutitem"));
			jMenuItem_About.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new AboutDialog(f);
				}
			});
		}
		return jMenuItem_About;
	}
	
	/**
	 * This method initializes jMenu_tools	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getJMenu_tools() {
		if (jMenu_tools == null) {
			jMenu_tools = new JMenu(LangageManager.getProperty("entaggedmenubar.toolsmenu"));
			jMenu_tools.add(getJMenuItem_Listing());
			jMenu_tools.add(getJMenuItem_Options());
			jMenu_tools.add(getJMenuItem_Statistics());
			jMenu_tools.add(getJMenuItem_Index());
			jMenu_tools.add(getJMenuItem_WrongInput());
			jMenu_tools.add(getJMenuItem_Double());
		}
		return jMenu_tools;
	}
	
	/**
	 * This method initializes jMenuItem_Listing	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItem_Listing() {
		if (jMenuItem_Listing == null) {
			jMenuItem_Listing = new JMenuItem(LangageManager.getProperty("entaggedmenubar.listingitem"));
			jMenuItem_Listing.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							try {
								ListingWizard wizard = new ListingWizard();
								MultipleFieldsMergingTable audioFiles = f.getControlPanel().getAudioFiles();
								List fileList = audioFiles.getAudioFiles();
								if (fileList != null && fileList.size() > 0) {
									Object obj = fileList.get(0);
									if (obj instanceof java.io.File)
										wizard.setSource((java.io.File) obj);
								}

								wizard.launch();
							}
							catch (Exception ex) {
								ex.printStackTrace();
							}
						}
					});
				}
			});
		}
		return jMenuItem_Listing;
	}

	/**
	 * This method initializes jMenuItem_Options	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItem_Options() {
		if (jMenuItem_Options == null) {
			jMenuItem_Options = new JMenuItem(LangageManager.getProperty("entaggedmenubar.optionsitem"));
			jMenuItem_Options.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					OptionPanelInterface general = new GeneralOptionPanel(f);
					OptionPanelInterface freedb = new FreedbOptionPanel();
					OptionPanelInterface[] opts = new OptionPanelInterface[] {general, freedb};
					OptionDialog eod = new OptionDialog(f, opts);
					eod.setVisible(true);
				}
			});
		}
		return jMenuItem_Options;
	}

	/**
	 * This method initializes jMenuItem_Statistics	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItem_Statistics() {
		if (jMenuItem_Statistics == null) {
			jMenuItem_Statistics = new JMenuItem(LangageManager.getProperty("entaggedmenubar.statisticitem"));
			jMenuItem_Statistics.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					new StatisticsControl((File[]) f.getControlPanel().getAudioFiles().getAudioFiles().toArray(new File[0]), f).start();
				}
			});
		}
		return jMenuItem_Statistics;
	}

	/**
	 * This method initializes jMenuItem_Index	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItem_Index() {
		if(jMenuItem_Index == null) {
			jMenuItem_Index = new JMenuItem(LangageManager.getProperty("entaggedmenubar.index"));
			jMenuItem_Index.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					new JPanel_AjoutDossier(f);
				}
			});
		}
		return jMenuItem_Index;
	}
	
	/**
	 * This method initializes jMenuItem_Index	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItem_WrongInput() {
		if(jMenuItem_WrongInput == null) {
			jMenuItem_WrongInput = new JMenuItem(LangageManager.getProperty("entaggedmenubar.wronginput"));
			jMenuItem_WrongInput.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					f.openWrongInput();
				}
			});
		}
		return jMenuItem_WrongInput;
	}
	
	/**
	 * This method initializes jMenuItem_Index	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItem_Double() {
		if(jMenuItem_Double == null) {
			jMenuItem_Double = new JMenuItem(LangageManager.getProperty("entaggedmenubar.double"));
			jMenuItem_Double.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					f.openDouble();
				}
			});
		}
		return jMenuItem_Double;
	}
}
