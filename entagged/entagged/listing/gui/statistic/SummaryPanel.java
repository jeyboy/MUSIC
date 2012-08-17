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
package entagged.listing.gui.statistic;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import entagged.tageditor.resources.LangageManager;
/**
 * 
 *
 * @author Christian Laireiter (liree)
 */
public class SummaryPanel extends JPanel {

	private JLabel jLabel = null;
	private JLabel jLabel1 = null;
	private JTextField validFileCountLabel = null;
	private JTextField invalidFileCountLabel = null;
	private JLabel jLabel2 = null;
	private JTextField totalMusicTimeLabel = null;
	private JLabel jLabel3 = null;
	private JTextField totalFileSize = null;
	/**
	 * This is the default constructor
	 */
	public SummaryPanel() {
		super();
		initialize();
	}
	/**
	 * This method initializes thiss
	 */
	private  void initialize() {
		jLabel3 = new JLabel();
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
		validFileCountLabel = new JTextField ();
		invalidFileCountLabel = new JTextField ();
		totalMusicTimeLabel = new JTextField ();
        validFileCountLabel.setEditable(false);
        invalidFileCountLabel.setEditable(false);
        totalMusicTimeLabel.setEditable(false);
		jLabel2 = new JLabel();
		jLabel1 = new JLabel();
		jLabel = new JLabel();
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
		GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
		this.setLayout(new GridBagLayout());
		this.setSize(381, 245);
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.gridy = 0;
		gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints1.weightx = 1.0D;
		jLabel.setText(LangageManager.getProperty("statistic.validfilecount"));
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.gridy = 1;
		gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
		jLabel1.setText(LangageManager.getProperty("statistic.invalidfilecount"));
		gridBagConstraints3.gridx = 1;
		gridBagConstraints3.gridy = 0;
		validFileCountLabel.setText("JLabel");
		validFileCountLabel.setName("validFileCount");
		gridBagConstraints4.gridx = 1;
		gridBagConstraints4.gridy = 1;
		invalidFileCountLabel.setText("JLabel");
		invalidFileCountLabel.setName("invalidFileCount");
		gridBagConstraints5.gridx = 0;
		gridBagConstraints5.gridy = 2;
		gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
		jLabel2.setText(LangageManager.getProperty("statistic.totalmediatime"));
		gridBagConstraints6.gridx = 1;
		gridBagConstraints6.gridy = 2;
		totalMusicTimeLabel.setText("JLabel");
		totalMusicTimeLabel.setName("totalMusicTime");
		gridBagConstraints11.gridx = 0;
		gridBagConstraints11.gridy = 3;
		gridBagConstraints11.anchor = java.awt.GridBagConstraints.WEST;
		jLabel3.setText(LangageManager.getProperty("statistic.totalfilesize"));
		gridBagConstraints21.gridx = 1;
		gridBagConstraints21.gridy = 3;
		gridBagConstraints21.weightx = 1.0;
		gridBagConstraints21.fill = java.awt.GridBagConstraints.NONE;
		this.add(jLabel, gridBagConstraints1);
		this.add(jLabel1, gridBagConstraints2);
		this.add(validFileCountLabel, gridBagConstraints3);
		this.add(invalidFileCountLabel, gridBagConstraints4);
		this.add(jLabel2, gridBagConstraints5);
		this.add(totalMusicTimeLabel, gridBagConstraints6);
		this.add(jLabel3, gridBagConstraints11);
		this.add(getTotalFileSize(), gridBagConstraints21);
	}
    /**
     * @return Returns the invalidFileCountLabel.
     */
    public JTextField getInvalidFileCountLabel() {
        return invalidFileCountLabel;
    }
    /**
     * @return Returns the totalMusitTimeLabel.
     */
    public JTextField getTotalMusicTimeLabel() {
        return totalMusicTimeLabel;
    }
    /**
     * @return Returns the validFileCountLabel.
     */
    public JTextField getValidFileCountLabel() {
        return validFileCountLabel;
    }
	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	public JTextField getTotalFileSize() {
		if (totalFileSize == null) {
			totalFileSize = new JTextField();
			totalFileSize.setEditable(false);
			totalFileSize.setText("example");
		}
		return totalFileSize;
	}
 }  //  @jve:decl-index=0:visual-constraint="10,10"
