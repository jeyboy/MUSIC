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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import miage.Atome;
import miage.ListeFichiers;
import miage.ihm.JButton_Playlist;
import miage.ihm.JDialog_Doublons;
import miage.ihm.JDialog_MauvaisesFrappes;
import miage.ihm.JTextField_Recherche;
import miage.jtreeindex.MainWindow;
import miage.sgbd.DataProvider;
import miage.sgbd.SqlProvider;
import entagged.audioformats.AudioFile;
import entagged.tageditor.actions.BrowseBackwardAction;
import entagged.tageditor.actions.BrowseIntoAction;
import entagged.tageditor.actions.BrowseUpAction;
import entagged.tageditor.actions.CtrlTableSelectionAction;
import entagged.tageditor.actions.FocusRequestAction;
import entagged.tageditor.actions.ReloadAction;
import entagged.tageditor.actions.TableEnterAction;
import entagged.tageditor.listeners.DialogWindowListener;
import entagged.tageditor.listeners.NavigatorListener;
import entagged.tageditor.listeners.TableReselector;
import entagged.tageditor.models.FileTreeModel;
import entagged.tageditor.models.Navigator;
import entagged.tageditor.models.TableSorter;
import entagged.tageditor.models.TagEditorTableModel;
import entagged.tageditor.renderers.TagEditorTableCellRenderer;
import entagged.tageditor.resources.Initialization;
import entagged.tageditor.resources.InitializationMonitor;
import entagged.tageditor.resources.LangageManager;
import entagged.tageditor.resources.PreferencesManager;
import entagged.tageditor.resources.ResourcesRepository;
import entagged.tageditor.util.SelectionRecord;
import entagged.tageditor.util.Utils;

/**
 * Main Class. Entry point for the GUI setup the Components like tree,
 * splitpanes etc $Id: TagEditorFrame.java,v 1.58 2005/01/04 20:55:10 kikidonk
 * Exp $
 * 
 * @author Raphael Slinckx (KiKiDonK) ; Nicolas Velin ; Christophe Suzzoni
 * @version v0.03
 */
public class TagEditorFrame extends JFrame {

	private class AlbumTableSelectionListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			//Ignore extra messages.
			if (e.getValueIsAdjusting())
				return;

			TagEditorFrame.this.getControlPanel().clear();

			int[] rows = table.getSelectedRows();
			for (int i = 0; i < rows.length; i++) {
				File f = tableModel.getFileAt(tableSorter.modelIndex(rows[i]));
				TagEditorFrame.this.getControlPanel().add(f);
			}
			TagEditorFrame.this.getControlPanel().update();
			TagEditorFrame.this.getControlPanel().processFileDifference();
		}
	}
	
	private static ArrayList<String> checkList;
	
	public static ArrayList<String> getCheckList() {
		return checkList;
	}

	private class ExpandSelectedRowMouseAdapter extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			/* FIXME:
			 * Not a good code
			 */
			String realColumnName = (String)table.getColumnModel().getColumn(table.getSelectedColumn()).getHeaderValue();
			if(realColumnName.equals(tableModel.getColumnName(8))) { // Click on Playlist
				int clickedRow = tableSorter.modelIndex(table.getSelectedRow());
				File f = tableModel.getFileAt(clickedRow);
				if(f.isFile()) {
					Boolean past = (Boolean)tableModel.getValueAt(clickedRow, 8);
					if(!past)
						checkList.add(tableModel.getFileAt(clickedRow).getAbsolutePath());
					else
						checkList.remove(tableModel.getFileAt(clickedRow).getAbsolutePath());
				}
			}
			//Open the directory in the table
			if (e.getClickCount() == 2) {
				if(table.getSelectedColumn() != 8) {
					int clickedRow = tableSorter.modelIndex(table.getSelectedRow());
					File f = tableModel.getFileAt(clickedRow);
					if(f.isDirectory()) {
						search.setText("");
						navigator.browseInto(f);
					}
					else if(f.isFile())
						ListeFichiers.play(TagEditorFrame.this, f.getAbsolutePath());
				}
			}
		}
	}

	/**
	 * Listens for a selection change in the Available roots combobox
	 * 
	 * @author Raphael Slinckx (KiKiDonK)
	 * @version v0.03
	 */
	private class RootSelectionListener implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			File selectedRoot = (File) e.getItem();

			if (selectedRoot.canRead()) {
				TagEditorFrame.this.dirChooser.setDirectory(selectedRoot);

				PreferencesManager.putInt("tageditor.tageditorframe.roots",roots.getSelectedIndex());

				TagEditorFrame.this.navigator.setDirectory(selectedRoot);

				TagEditorFrame.this.getControlPanel().clear();
				TagEditorFrame.this.getControlPanel().update();
				
				TagEditorFrame.this.changeJTree(selectedRoot,jTabbedPaneExplo_Index.getSelectedIndex());
			}
			else {
				String msg = LangageManager.getProperty("tageditorframe.drivecouldnotberead").replaceAll("%1",
						selectedRoot.toString().substring(0, 1));

				JOptionPane.showMessageDialog(TagEditorFrame.this, msg);
			}
		}
	}

	public static void main(String[] args) {
		if (!Initialization.isInitialized)
			Initialization.init(new InitializationMonitor() {
				public void setBeginning(String text) {}
				public void setBounds(int min, int max) {}
				public void setFinishing(String text) {}
				public void setStatus(String status, int val) {}
			});

		TagEditorFrame f = new TagEditorFrame();

		f.setVisible(true);
	}

	/**
	 * The Panel that holds the id3v1 and id3v2 panels and also the freedb and
	 * file rename panels
	 */
	protected ControlPanel controlPanel;

	protected DirectoryChooser dirChooser;
	
	protected JTextField_Recherche search;

	/** The Model used for the JTree (this is a view of the filesystem) */
	protected FileTreeModel fileTreeModel;

	/** Holds the MenuBar */
	protected TagEditorMenuBar menuBar;

	/** Stores the fiel history and notifies on changes */
	protected Navigator navigator;

	/** Roots selection combobox */
	protected JComboBox roots;

	/**
	 * The settings of the editor.
	 */
	protected EditorSettings settings;

	protected JTable table;

	//Needed for GUI SAVE
	protected JSplitPane tableInfoSplitPane;

	/** Splits the table and the infopanel */
	protected TagEditorTableModel tableModel;

	protected JScrollPane tableScrollPane;

	/** The selection model for the mp3album table */
	protected ListSelectionModel tableSelectionModel;  //  @jve:decl-index=0:

	protected TableSorter tableSorter;

	private JPanel jContentPane = null;
	private JPanel rootAndTree = null;
	private JPanel IndexOptionPane = null;
	private static JPanel IndexInfoPane = null;
	private JPanel tablePanel;
	

	/*
	 * Modif Chris MIAGE 08
	 */
	private JPanel jPanelOption = null;
	private static JLabel jLabelTauxIndex = null;
	private static JButton jButtonActualiser = null;
	private JLabel jLabelIndexFeature = null;
	private JTabbedPane jTabbedPaneExplo_Index = null;

	private static JProgressBar jProgressBarDossier = null;
	private static JProgressBar jProgressBarReel = null;
	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setSize(new Dimension(800, 600));
		this.setExtendedState(JFrame.MAXIMIZED_BOTH );
		checkList = new ArrayList<String>();

		// Sets the icon for the window manager
		ImageIcon icon = ResourcesRepository.getImageIcon("entagged-icon.png");
		this.setIconImage(icon.getImage());

		// Create the menu bar
		menuBar = new TagEditorMenuBar(this);
		this.setJMenuBar(menuBar);

		this.setContentPane(getJContentPane());

		// Sets some default things, closing behavior, location, size...
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.addWindowListener(new DialogWindowListener() {
			public void windowClosing(WindowEvent e) {
				TagEditorFrame.this.saveGUIPreferences();
				Initialization.exit();
			}
		});
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getTableInfoSplitPane(), "Center");
			jContentPane.add(getRootAndTree(), "North");
		}
		return jContentPane;
	}

	/**
	 * This method initializes rootAndTree
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getRootAndTree() {
		if (rootAndTree == null) {
			// Create container Jpanel for the tree and root selection
			
			rootAndTree = new JPanel();
			rootAndTree.setLayout(new GridBagLayout());

			// Display the root selection combobox + root detection routine, and
			// adds the selection listener
			roots = new JComboBox(File.listRoots());
			buildRootComboBox();
			roots.addItemListener(new RootSelectionListener());
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.insets = new Insets(2, 1, 2, 1);
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.gridwidth = 1;
			gridBagConstraints.anchor = GridBagConstraints.CENTER;
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			rootAndTree.add(roots, gridBagConstraints);
			
			dirChooser = new DirectoryChooser(this);
			gridBagConstraints.insets = new Insets(2, 9, 2, 9);
			gridBagConstraints.gridx = 1;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.weightx = 3;
			rootAndTree.add(dirChooser, gridBagConstraints);
			/*
			search = new JTextField_Recherche(dirChooser,tableModel);
			search.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyPressed(java.awt.event.KeyEvent e) {
					jTabbedPaneExplo_Index.setSelectedIndex(0);
				}
			});*/
			gridBagConstraints.insets = new Insets(2, 1, 2, 1);
			gridBagConstraints.gridx = 2;
			gridBagConstraints.weightx = 1;
			rootAndTree.add(getIndexOptionPane(), gridBagConstraints);
			
			/*JButton_Playlist playList = new JButton_Playlist(this);
			gridBagConstraints.insets = new Insets(2, 1, 2, 1);
			gridBagConstraints.gridx = 3;
			gridBagConstraints.weightx = 0;
			rootAndTree.add(playList, gridBagConstraints);*/
			
			gridBagConstraints.insets = new Insets(2, 9, 2, 9);
			gridBagConstraints.anchor = GridBagConstraints.CENTER;
			gridBagConstraints.gridy = 2;
			gridBagConstraints.gridx = 1;
			//rootAndTree.add(getIndexInfoPane(),gridBagConstraints);
			
			gridBagConstraints.insets = new Insets(2, 1, 2, 1);
			gridBagConstraints.gridy = 2;
			gridBagConstraints.gridx = 2;
			//rootAndTree.add(getIndexOptionPane(), gridBagConstraints);
			
			
			rootAndTree.setBorder(new EmptyBorder(5, 5, 3, 3));
		}
		return rootAndTree;
	}

	/**
	 * This method initializes tableInfoSplitPane
	 * 
	 * @return javax.swing.JSplitPane
	 */
	private JSplitPane getTableInfoSplitPane() {
		if (tableInfoSplitPane == null) {
			//Setup the split Panes
			tableInfoSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, false);
			//tableInfoSplitPane.setLeftComponent(getTablePanel());
			tableInfoSplitPane.setLeftComponent(getJTabbedPaneExplo_Index());
			tableInfoSplitPane.setRightComponent(getControlPanel());
			tableInfoSplitPane.setOneTouchExpandable(true);

			restoreSplitPaneLocations();

			//BORDER TWEAKING
			tableInfoSplitPane.setBorder(new EmptyBorder(5, 0, 0, 0));
		}
		return tableInfoSplitPane;
	}

	/**
	 * This method initializes tablePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getTablePanel() {
		if (tablePanel == null) {
			// Create global settings
			settings = new EditorSettings();

			//Setup right treetable model with multiple selection allowed
			tableModel = new TagEditorTableModel(this);

			//Creates the right treetable, with the above model, turn off
			// autoresize and sets Table selection listeners
			tableSorter = new TableSorter(tableModel); //Provides sorting capabilities
			table = new JTable();
			table.setModel(tableSorter);
			table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
			tableSelectionModel = table.getSelectionModel();
			tableSelectionModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			table.getSelectionModel().addListSelectionListener(new AlbumTableSelectionListener());
			tableSorter.setTableHeader(table.getTableHeader());
			// Register the TableReselector
			TableReselector reselector = new TableReselector(table);
			navigator.addNavigatorListener(reselector);
			
			TagEditorTableCellRenderer tetcr = new TagEditorTableCellRenderer(tableModel, tableSorter);
			//The renderer does not apply on the images icon
			for (int i = 1 ; i < tableModel.getColumnCount() - 1 ; i++)
				table.getColumnModel().getColumn(i).setCellRenderer(tetcr);
			
			table.setRowHeight(22);
			//MAYBE THIS NEEDS TO BE CHANGED TO FIT BETTER ICONs

			table.getTableHeader().setReorderingAllowed(true);
			table.addMouseListener(new ExpandSelectedRowMouseAdapter());
			restoreTableColumnWidth();

			//Remove the lines between the cells
			table.setGridColor(new Color(240,240,240));
			table.setShowHorizontalLines(true);
			table.setShowVerticalLines(false); 
			table.setIntercellSpacing(new Dimension(0,0));
			
			// Move the last visible column so it becomes the first visible column
			int vSrcColIndex = table.getColumnCount()-1;
			int vDstColIndex = 0;
			table.moveColumn(vSrcColIndex, vDstColIndex);

			tableScrollPane = new JScrollPane();
			tableScrollPane.setViewportView(table);
			tableScrollPane.setMinimumSize(new Dimension(600, 200));

			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(0, 1, 1, 1);
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.gridwidth = 1;
			gbc.anchor = GridBagConstraints.CENTER;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.weightx = 1;
			gbc.weighty = 100;
			GridBagConstraints gbc2 = new GridBagConstraints();
			gbc2.insets = new Insets(0, 2, 2, 1);
			gbc2.anchor = GridBagConstraints.SOUTH;
			gbc2.fill = GridBagConstraints.HORIZONTAL;
			gbc2.gridx = 0;
			gbc2.gridy = 2;
			gbc2.weightx = 1;
			gbc2.weighty = 1;
						
			// Create container Jpanel for the table
			tablePanel = new JPanel();
			tablePanel.setLayout(new GridBagLayout());
			tablePanel.add(tableScrollPane, gbc);
			//tablePanel.add(getIndexInfoPane(),gbc2);
			tablePanel.add(getjPanelOption(), gbc2);
			tablePanel.setBorder(new EmptyBorder(3, 5, 5, 3));
		}
		return tablePanel;
	}
	
	private JPanel getjPanelOption () {
		if (jPanelOption == null) {
			jPanelOption = new JPanel();
			jPanelOption.setLayout(new BorderLayout());			
			jPanelOption.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
			JPanel JPan3 = new JPanel();
			JPan3.add(getIndexInfoPane());
			jPanelOption.add(JPan3, BorderLayout.WEST);
			search = new JTextField_Recherche(dirChooser,tableModel);
			search.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyPressed(java.awt.event.KeyEvent e) {
					jTabbedPaneExplo_Index.setSelectedIndex(0);
				}
			});
			JPanel JPan1 = new JPanel();
			JPan1.add(search);
			jPanelOption.add(JPan1, BorderLayout.EAST);
			JButton_Playlist playList = new JButton_Playlist(this);
			JPanel JPan2 = new JPanel();
			JPan2.add(playList);
			jPanelOption.add(JPan2,BorderLayout.CENTER);
		}
		return jPanelOption;
	}
	

	/**
	 * This method initializes IndexInfoPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getIndexInfoPane() {
		if (IndexInfoPane == null) {
			// Create container Jpanel for the index files option
			JLabel jLabelTaux = new JLabel();
			jLabelTaux.setText(LangageManager.getProperty("miage.tauxindex"));
			jLabelTauxIndex = new JLabel();
			jLabelTauxIndex.setHorizontalAlignment(SwingConstants.CENTER);
			jLabelTauxIndex.setPreferredSize(new Dimension(250, 20));
			final String workDir = PreferencesManager.get("tageditor.tageditorframe.workingdir");
			if (workDir != null) {
				Thread performer = new Thread(new Runnable() {
					public void run() {
				File file = new File(workDir);
				TagEditorFrame.updatePourcentage("",1);
				TagEditorFrame.updatePourcentage(file.getAbsolutePath(),0);
					}
				}, "Performer");
				performer.start();
			}
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.gridy = 0;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 2;
			gridBagConstraints2.gridy = 0;			
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints2.gridx = 3;
			gridBagConstraints2.gridy = 0;	
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints2.gridx = 4;
			gridBagConstraints2.gridy = 0;				
			IndexInfoPane = new JPanel();
			IndexInfoPane.setLayout(new GridBagLayout());
			//IndexInfoPane.setMaximumSize(new Dimension(150, 100));
			//IndexInfoPane.setBackground(new Color(30,20,34));
			IndexInfoPane.add(jLabelTaux,new GridBagConstraints());
			IndexInfoPane.add(jLabelTauxIndex, gridBagConstraints1);
			IndexInfoPane.add(getJProgressBarDossier(0), gridBagConstraints2);
			IndexInfoPane.add(getJProgressBarReel(0), gridBagConstraints3);
			IndexInfoPane.add(getJButtonActualiser(), gridBagConstraints4);
			jProgressBarDossier.setVisible(false);
			jProgressBarReel.setVisible(false);
			
		}
		return IndexInfoPane;
	}
	
	public static void updatePourcentage(String path, int etape){
		if (etape == 0){
			int pourcentage = miage.ListeFichiers.StatistiqueDossier(path,1);
			int pourcentage2 = miage.ListeFichiers.StatistiqueDossier(path,2);
			if (pourcentage== -1){
				jLabelTauxIndex.setText(LangageManager.getProperty("miage.folderwithoutmusicfiles"));
				jButtonActualiser.setEnabled(false);
				jLabelTauxIndex.setVisible(true);				
				majJProgressBarDossier(0);
				majJProgressBarReel(0);
			}
			else if (pourcentage == -2) {
				jLabelTauxIndex.setText(LangageManager.getProperty("miage.Foldernoindexed"));
				jButtonActualiser.setEnabled(true);
				jLabelTauxIndex.setVisible(true);
				majJProgressBarDossier(0);
				majJProgressBarReel(0);
				//IndexInfoPane.add(getJProgressBarDossier(0));
			}
			else {
				//String pource = String.valueOf(pourcentage);
				//jLabelTauxIndex.setText("Dossier indéxé à : "+pource+"%");
				jLabelTauxIndex.setVisible(false);
				jButtonActualiser.setEnabled(true);
				majJProgressBarDossier(pourcentage2);
				majJProgressBarReel(pourcentage);
				//IndexInfoPane.add(getJProgressBarDossier(pourcentage));
			}
		}	
		else {
			jLabelTauxIndex.setText(LangageManager.getProperty("miage.calculationprogress"));
			jLabelTauxIndex.setVisible(true);
			try {
				majJProgressBarDossier(0);
				majJProgressBarReel(0);
			}
			catch (Exception e) {}
		}
	}

	private static JProgressBar getJProgressBarDossier(int value) {
		try {
			jProgressBarDossier = new JProgressBar();
			jProgressBarDossier.setToolTipText(LangageManager.getProperty("miage.jprogressDossier"));
			jProgressBarDossier.setValue(value);
            String s= String.valueOf(LangageManager.getProperty("miage.folder")+jProgressBarDossier.getValue()+" %");

            jProgressBarDossier.setBackground(new Color(Color.TRANSLUCENT));
            if(jProgressBarDossier.getValue()<25)
            	jProgressBarDossier.setForeground(Color.red);
                
            else if((jProgressBarDossier.getValue()>=25)&&(jProgressBarDossier.getValue()<45))
                
            	jProgressBarDossier.setForeground(new Color(255,100,0));
            else if((jProgressBarDossier.getValue()>=45)&&(jProgressBarDossier.getValue()<55))
                
            	jProgressBarDossier.setForeground(new Color(255,180,0));
            else if((jProgressBarDossier.getValue()>=55)&&(jProgressBarDossier.getValue()<65))
            	jProgressBarDossier.setForeground(new Color(255,235,0));
            else if((jProgressBarDossier.getValue()>=65)&&(jProgressBarDossier.getValue()<75))
            	jProgressBarDossier.setForeground(Color.yellow);
            else if((jProgressBarDossier.getValue()>=75)&&(jProgressBarDossier.getValue()<85))
            	jProgressBarDossier.setForeground(new Color(182,245,1));
            else if((jProgressBarDossier.getValue()>=85)&&(jProgressBarDossier.getValue()<=100))
            	jProgressBarDossier.setForeground(Color.green);

            jProgressBarDossier.setString(s);
            jProgressBarDossier.setStringPainted(true);
        } catch (java.lang.Throwable e) {
            // TODO: Something
        }
        return jProgressBarDossier;
    }

	private static JProgressBar getJProgressBarReel(int value) {
		
		try {
			jProgressBarReel = new JProgressBar();
			jProgressBarReel.setValue(value);
			jProgressBarReel.setToolTipText(LangageManager.getProperty("miage.jprogressReel"));
            String s= String.valueOf(LangageManager.getProperty("miage.reel")+jProgressBarReel.getValue()+" %");

            jProgressBarReel.setBackground(new Color(Color.TRANSLUCENT));
            if(jProgressBarReel.getValue()<25)
            	jProgressBarReel.setForeground(Color.red);
                
            else if((jProgressBarReel.getValue()>=25)&&(jProgressBarReel.getValue()<45))
                
            	jProgressBarReel.setForeground(new Color(255,100,0));
            else if((jProgressBarReel.getValue()>=45)&&(jProgressBarReel.getValue()<55))
                
            	jProgressBarReel.setForeground(new Color(255,180,0));
            else if((jProgressBarReel.getValue()>=55)&&(jProgressBarReel.getValue()<65))
            	jProgressBarReel.setForeground(new Color(255,235,0));
            else if((jProgressBarReel.getValue()>=65)&&(jProgressBarReel.getValue()<75))
            	jProgressBarReel.setForeground(Color.yellow);
            else if((jProgressBarReel.getValue()>=75)&&(jProgressBarReel.getValue()<85))
            	jProgressBarReel.setForeground(new Color(182,245,1));
            else if((jProgressBarReel.getValue()>=85)&&(jProgressBarReel.getValue()<=100))
            	jProgressBarReel.setForeground(Color.green);

            jProgressBarReel.setString(s);
            jProgressBarReel.setStringPainted(true);
        } catch (java.lang.Throwable e) {
            // TODO: Something
        }
        return jProgressBarReel;
    }
	
	private static void majJProgressBarDossier(int value){
		if (value > 0) {
        jProgressBarDossier.setValue(value);
        String s= String.valueOf(LangageManager.getProperty("miage.folder")+jProgressBarDossier.getValue()+" %");

        jProgressBarDossier.setBackground(new Color(Color.TRANSLUCENT));
        if(jProgressBarDossier.getValue()<25)
            jProgressBarDossier.setForeground(Color.red);
            
        else if((jProgressBarDossier.getValue()>=25)&&(jProgressBarDossier.getValue()<45))
            
            jProgressBarDossier.setForeground(new Color(255,100,0));
        else if((jProgressBarDossier.getValue()>=45)&&(jProgressBarDossier.getValue()<55))
            
            jProgressBarDossier.setForeground(new Color(255,180,0));
        else if((jProgressBarDossier.getValue()>=55)&&(jProgressBarDossier.getValue()<65))
            jProgressBarDossier.setForeground(new Color(255,235,0));
        else if((jProgressBarDossier.getValue()>=65)&&(jProgressBarDossier.getValue()<75))
            jProgressBarDossier.setForeground(Color.yellow);
        else if((jProgressBarDossier.getValue()>=75)&&(jProgressBarDossier.getValue()<85))
            jProgressBarDossier.setForeground(new Color(182,245,1));
        else if((jProgressBarDossier.getValue()>=85)&&(jProgressBarDossier.getValue()<=100))
            jProgressBarDossier.setForeground(Color.green);

        jProgressBarDossier.setString(s);
        jProgressBarDossier.setStringPainted(true);
        jProgressBarDossier.setVisible(true);
		}
		else 
			jProgressBarDossier.setVisible(false);
	}
	
	private static void majJProgressBarReel(int value){
		if (value > 0) {
        jProgressBarReel.setValue(value);
        String s= String.valueOf(LangageManager.getProperty("miage.reel")+jProgressBarReel.getValue()+" %");
        jProgressBarReel.setBackground(new Color(Color.TRANSLUCENT));
        if(jProgressBarReel.getValue()<25)
        	jProgressBarReel.setForeground(Color.red);
            
        else if((jProgressBarReel.getValue()>=25)&&(jProgressBarReel.getValue()<45))
            
        	jProgressBarReel.setForeground(new Color(255,100,0));
        else if((jProgressBarReel.getValue()>=45)&&(jProgressBarReel.getValue()<55))
            
        	jProgressBarReel.setForeground(new Color(255,180,0));
        else if((jProgressBarReel.getValue()>=55)&&(jProgressBarReel.getValue()<65))
        	jProgressBarReel.setForeground(new Color(255,235,0));
        else if((jProgressBarReel.getValue()>=65)&&(jProgressBarReel.getValue()<75))
        	jProgressBarReel.setForeground(Color.yellow);
        else if((jProgressBarReel.getValue()>=75)&&(jProgressBarReel.getValue()<85))
        	jProgressBarReel.setForeground(new Color(182,245,1));
        else if((jProgressBarReel.getValue()>=85)&&(jProgressBarReel.getValue()<=100))
        	jProgressBarReel.setForeground(Color.green);

        jProgressBarReel.setString(s);
        jProgressBarReel.setStringPainted(true);
        jProgressBarReel.setVisible(true);
		}
		else 
			jProgressBarReel.setVisible(false);
	}	
	
	/**
	 * This method initializes IndexOptionPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getIndexOptionPane() {
		if (IndexOptionPane == null) {
			jLabelIndexFeature = new JLabel();
			jLabelIndexFeature.setPreferredSize(new Dimension(200,30));

			if (PreferencesManager.getBoolean("tageditor.table.indexfiles")) {
				jLabelIndexFeature.setText(LangageManager.getProperty("miage.indexeddatasyes"));
				jLabelIndexFeature.setIcon(ResourcesRepository.getImageIcon("yes.gif"));
			}
			else {
				jLabelIndexFeature.setText(LangageManager.getProperty("miage.notindexeddatas"));
				jLabelIndexFeature.setIcon(ResourcesRepository.getImageIcon("no.gif"));
			}
			jLabelIndexFeature.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					if (jLabelIndexFeature.getText().equals(LangageManager.getProperty("miage.indexeddatasyes"))){
						PreferencesManager.putBoolean("tageditor.table.indexfiles", false);
						try {
							PreferencesManager.cleanPreferences();
						}
						catch(Exception exc) {
							exc.printStackTrace();
						}
						jLabelIndexFeature.setText(LangageManager.getProperty("miage.notindexeddatas"));
						jLabelIndexFeature.setIcon(ResourcesRepository.getImageIcon("no.gif"));
						//tableModel.fireTableDataChanged();
						
					}
					else {
						PreferencesManager.putBoolean("tageditor.table.indexfiles", true);
						try {
							PreferencesManager.cleanPreferences();
						}
						catch(Exception exc) {
							exc.printStackTrace();
						}
						//tableModel.fireTableDataChanged();
						jLabelIndexFeature.setText(LangageManager.getProperty("miage.indexeddatasyes"));
						jLabelIndexFeature.setIcon(ResourcesRepository.getImageIcon("yes.gif"));
					}
					/*JScrollBar JB = tableScrollPane.getHorizontalScrollBar();
					System.out.println(JB.getValue());*/
					File dest = new File(dirChooser.getPath());
					if (dest != null && dest.isDirectory() && dest.canRead()) 
						navigator.reload();
					navigator.fireDirectoryChange(NavigatorListener.EVENT_RELOAD);
					

				}
			});
			IndexOptionPane = new JPanel();
			IndexOptionPane.setLayout(new GridBagLayout());
			IndexOptionPane.add(jLabelIndexFeature);
			
		}
		return IndexOptionPane;
	}
	/**
	 * This method initializes jButtonActualiser	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonActualiser() {
		if (jButtonActualiser == null) {
			jButtonActualiser = new JButton();
			jButtonActualiser.setText(LangageManager.getProperty("miage.update"));
			jButtonActualiser.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {		
					if (jButtonActualiser.isEnabled()){
						TagEditorFrame TagEdit = frameTagEditor();
						final ProgressDialog progressDialog = new ProgressDialog(
								TagEdit, LangageManager.getProperty("miage.foldertoupdate"),
								LangageManager.getProperty("miage.updatingfolder"));
						progressDialog.setAbortable(false);
						progressDialog.hideAbortButton();
						progressDialog.setModal(true);
						new Thread(new Runnable() {
							public void run() {
								String dossier = dirChooser.getPath();
								miage.ListeFichiers.ActualiserDossier(dossier);
								progressDialog.dispose();
								TagEditorFrame.updatePourcentage("",1);
								TagEditorFrame.updatePourcentage(dossier,0);
								SqlProvider.DossierAct=null;
								File dest = new File(dirChooser.getPath());
								if (dest != null && dest.isDirectory() && dest.canRead()) 
									navigator.reload();
								navigator.fireDirectoryChange(NavigatorListener.EVENT_RELOAD);
							}
						}, LangageManager.getProperty("miage.updatingfolder")).start();
						progressDialog.setVisible(true);
						progressDialog.dispose();
					}
				}
			});
		}
		return jButtonActualiser;
	}
	
	private TagEditorFrame frameTagEditor() {
		return this;
	}
	/**
	 * This method initializes jTabbedPane	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getJTabbedPaneExplo_Index() {
		if (jTabbedPaneExplo_Index == null) {
			jTabbedPaneExplo_Index = new JTabbedPane();
		
			jTabbedPaneExplo_Index.addTab(LangageManager.getProperty("miage.explorer"), null, getTablePanel(), null);
			
			String workDir = PreferencesManager.get("tageditor.tageditorframe.workingdir");
			if (workDir != null) {
				File file = new File(workDir);
				int i = file.getAbsolutePath().indexOf(File.separatorChar);
				String root = file.getAbsolutePath().substring(0, i+1);
				jTabbedPaneExplo_Index.addTab(LangageManager.getProperty("miage.index"), null, new MainWindow(root, frameTagEditor()), null);
			}
			else {
				jTabbedPaneExplo_Index.addTab(LangageManager.getProperty("miage.index"), null, new MainWindow(), null);
			}

			
			jTabbedPaneExplo_Index.addChangeListener(new javax.swing.event.ChangeListener() {
				public void stateChanged(javax.swing.event.ChangeEvent e) {
					//System.out.println("stateChanged()"); // TODO Auto-generated Event stub stateChanged()
					if (jTabbedPaneExplo_Index.getSelectedIndex()==0) {
						hideInfoIndex(true);
					}
					else {
						hideInfoIndex(false);
					}
					
				}
			});
			jTabbedPaneExplo_Index.setBorder(new EmptyBorder(3, 5, 5, 3));
			
		}
		return jTabbedPaneExplo_Index;
	}
	
	private void hideInfoIndex (boolean action) {
		jLabelTauxIndex.setEnabled(action);
		jButtonActualiser.setEnabled(action);
	}
	
	private void changeJTree(File selectedRoot, int tabbed){
		if(jTabbedPaneExplo_Index.getTabCount()>=2);
			jTabbedPaneExplo_Index.remove(1);
		jTabbedPaneExplo_Index.addTab(LangageManager.getProperty("miage.index"), null, new MainWindow(selectedRoot), null);
		jTabbedPaneExplo_Index.setSelectedIndex(tabbed);
	}
	
	/**
	 * This method initializes controlPanel
	 * 
	 * @return javax.swing.ControlPanel
	 */
	public ControlPanel getControlPanel() {
		if (controlPanel == null) {
			// Setup the bottom Tag Info Panel
			controlPanel = new ControlPanel(this);
			controlPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		}
		return controlPanel;
	}

	/** Build the GUI Components for the Main Class */
	public TagEditorFrame() {
		//Create Main Frame
		super(LangageManager.getProperty("tageditorframe.title"));
		initialize();
		restoreFrameLocation();
		restoreFrameSize();
		restoreWorkingDir();
		registerActions();
	}

	//Try to set as good as possible, to best root disk (especially under
	// windows)
	private void buildRootComboBox() {
		//Old (stored) selected root
		int selectedIndex = PreferencesManager.getInt("tageditor.tageditorframe.roots");

		//If it is the first launch and the root appears to be windows-like, we
		// try to get the index of the "c:\" drive
		if (selectedIndex == 0 && File.listRoots()[0].toString().matches("[a-zA-Z]{1}\\:\\\\"))
			for (int i = 0; i < File.listRoots().length; i++)
				if (File.listRoots()[i].toString().matches("[cC]{1}\\:\\\\"))
					selectedIndex = i;
		/*
		 * Think of a mapped network drive that has been deleted. In this case
		 * we could get a ArrayIndexOutOfBound exception
		 */
		if (selectedIndex >= File.listRoots().length) {
			selectedIndex = 1;
		}
		//We test if we can read this disk (not an empty cdrom..)
		if (File.listRoots()[selectedIndex].canRead()) {
			//Set the combobox, create the model with the given root, and sets
			// it to the left tree
			roots.setSelectedIndex(selectedIndex);
		}
		//We could not read the difk, so it is probably an empty tray, display
		// a message, and don't show a left tree
		else {
			String msg = LangageManager.getProperty(
			"tageditorframe.drivecouldnotberead").replaceAll("%1",
					File.listRoots()[selectedIndex].toString().substring(0, 1));

			JOptionPane.showMessageDialog(this, msg);
			roots.setSelectedIndex(selectedIndex);
			/*
			 * folderTree.setModel( null );
			 */
		}
		/*
		 * folderTree.setSelectionPath(folderTree.getPathForRow(0));
		 */
	}

	public void currentDirDeleted() {
		navigator.fireDirectoryChange(NavigatorListener.EVENT_RELOAD);
	}

	/**
	 * Returns the global settings of the current editor.
	 * @return Settings.
	 */
	public EditorSettings getEditorSettings () {
		return this.settings;
	}

	/**
	 * Returns the navigator of entagged.
	 * 
	 * @return Navigator.
	 */
	public Navigator getNavigator() {
		if (navigator == null) {
			navigator = new Navigator();
		}
		return this.navigator;
	}

	public TagEditorTableModel getTagEditorTableModel() {
		return this.tableModel;
	}

	public void refreshCurrentTableView() {
		getControlPanel().clear();
		SelectionRecord record = new SelectionRecord(this.table, tableSorter, tableModel); 
		
		String str = search.getText();
		if(!str.equals(""))
			tableModel.directoryChanged(new File("c:\\"),DataProvider.getFichiers(str),NavigatorListener.EVENT_JUMPED);
		else
			navigator.reload();
		
		record.applyChange();
		getControlPanel().update();
	}

	/**
	 * This method will create and register the actions.
	 */
	private void registerActions() {
		String browseBack = "bbwk";
		String browseUp = "buk";
		String browseInto = "bik";
		String reload = "reload";
		String enter = "enter";
		String transfer ="transferfocus";

		this.table.getInputMap().put(
				KeyStroke.getKeyStroke(KeyEvent.VK_KP_LEFT, 0), browseBack);
		this.table.getInputMap().put(
				KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), browseBack);
		this.table.getInputMap().put(
				KeyStroke.getKeyStroke(KeyEvent.VK_KP_RIGHT, 0), browseInto);
		this.table.getInputMap().put(
				KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), browseInto);
		this.table.getInputMap().put(
				KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), enter);
		this.table.getInputMap().put(
				KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), browseBack);
		this.table.getInputMap()
		.put(
				KeyStroke.getKeyStroke(KeyEvent.VK_UP,
						InputEvent.ALT_DOWN_MASK), browseUp);
		this.table.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB,0), transfer);
		/*
		 * Since the table is always visible, the f5 operation is assigned to
		 * tageditorframe when focused.
		 */
		this.table.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), reload);

		this.table.getActionMap().put(browseBack,
				new BrowseBackwardAction(navigator));
		this.table.getActionMap().put(browseUp, new BrowseUpAction(navigator));
		this.table.getActionMap().put(browseInto,
				new BrowseIntoAction(navigator, table, tableModel));
		this.table.getActionMap().put(reload, new ReloadAction(navigator));
		this.table.getActionMap().put(enter,
				new TableEnterAction(navigator, controlPanel));
		this.table.getActionMap().put(transfer, new FocusRequestAction(controlPanel));
		/*
		 * Automatic registering
		 */
		CtrlTableSelectionAction.registerCombinations(table);
	}

	private void restoreFrameLocation() {
		this.setLocation(PreferencesManager.getInt("tageditor.tageditorframe.framelocationx",
			(int) (PreferencesManager.getInt("entagged.screen.width") * 0.1)),
				PreferencesManager.getInt("tageditor.tageditorframe.framelocationy",
					(int) (PreferencesManager.getInt("entagged.screen.height") * 0.1)));
	}

	private void restoreFrameSize() {
		this.setSize(PreferencesManager.getInt("tageditor.tageditorframe.framewidth",
			(int) (PreferencesManager.getInt("entagged.screen.width") * 0.8)),
				PreferencesManager.getInt("tageditor.tageditorframe.frameheight",
					(int) (PreferencesManager.getInt("entagged.screen.height") * 0.8)));
	}

	private void restoreSplitPaneLocations() {
		tableInfoSplitPane.setDividerLocation(PreferencesManager.getInt(
				"tageditor.tageditorframe.tableinfosplitpane.dividerlocation",
				500));
		/*
		 * tableSplitPane.setDividerLocation(
		 * PreferencesManager.getInt("tageditor.tageditorframe.treetablesplitpane.dividerlocation",
		 * 300));
		 */
		//System.out.println(tableInfoSplitPane.getDividerLocation());
	}

	private void restoreTableColumnWidth() {
		Vector v = Utils.getColumnsInModelOrder(table);

		//Set the icon to fit the width
		((TableColumn) v.elementAt(0)).setWidth(22);
		((TableColumn) v.elementAt(0)).setPreferredWidth(22);
		((TableColumn) v.elementAt(0)).setMaxWidth(22);

		for (int i = 1; i < v.size(); i++) {
			int width = PreferencesManager
			.getInt("tageditor.tageditorframe.treetable.column." + i
					+ ".width");
			((TableColumn) v.elementAt(i)).setPreferredWidth(width);
			//System.out.println( width );
		}
	}

	private void restoreWorkingDir() {
		this.navigator.fireDirectoryChange(NavigatorListener.EVENT_RELOAD);
	}

	protected void saveGUIPreferences() {
		//-------TABLE PREFERENCES---------
		System.out.print("Exiting: Saving table columns widths ");
		Vector v = Utils.getColumnsInModelOrder(table);
		for (int i = 1; i < v.size(); i++) {
			int width = ((TableColumn) v.elementAt(i)).getPreferredWidth();
			System.out.print("|" + width);
			PreferencesManager
			.putInt("tageditor.tageditorframe.treetable.column." + i
					+ ".width", width);
		}
		System.out.println("| ...");
		//-------------------------------------

		//--------Frame Size-------------------
		System.out.println("Exiting: Saving frame size (" + this.getWidth()
				+ "," + this.getHeight() + ") ...");
		PreferencesManager.putInt("tageditor.tageditorframe.framewidth", this
				.getWidth());
		PreferencesManager.putInt("tageditor.tageditorframe.frameheight", this
				.getHeight());
		//-------------------------------------

		//--------Frame Location-------------------
		System.out.println("Exiting: Saving frame location (" + this.getX()
				+ "," + this.getY() + ") ...");
		PreferencesManager.putInt("tageditor.tageditorframe.framelocationx",
				this.getX());
		PreferencesManager.putInt("tageditor.tageditorframe.framelocationy",
				this.getY());
		//-------------------------------------

		//--------Split Panes divider loc.---------
		//System.out.println("Exiting: Saving split panes locations
		// ("+tableInfoSplitPane.getDividerLocation()+" |
		// "+tableSplitPane.getDividerLocation()+") ...");
		System.out.println("Exiting: Saving split panes locations ("
				+ tableInfoSplitPane.getDividerLocation() + ") ...");
		PreferencesManager.putInt(
				"tageditor.tageditorframe.tableinfosplitpane.dividerlocation",
				tableInfoSplitPane.getDividerLocation());
		/*
		 * PreferencesManager.putInt("tageditor.tageditorframe.treetablesplitpane.dividerlocation",
		 * tableSplitPane.getDividerLocation());
		 */
		//-----------------------------------------
		//--------Control Panel prefs -----------
		this.controlPanel.saveGUIPreferences();
		//-----------------------------------------

		//--------Current Working directory--------
		System.out.println("Exiting: Saving current working directory ("
				+ navigator.getCurrentFolder() + ") ...");
		navigator.storeSettings();
	}

	public void openDouble() {
		ArrayList<ArrayList<Atome>> metaListe = DataProvider.getDoublesInit("fichier");
		new JDialog_Doublons(this,metaListe);
		DataProvider.resetCache();
	}
	
	public void openWrongInput() {
		ArrayList<ArrayList<Atome>> metaListe = DataProvider.getDoublesInit("artiste");
		new JDialog_MauvaisesFrappes(this,metaListe, tableModel);
		DataProvider.resetCache();
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"