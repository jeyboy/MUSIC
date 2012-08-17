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
import entagged.tageditor.resources.*;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Dimension;


/**
 *  Dialog Showing the options available for entagged. $Id: EntaggedOptionsDialog.java,v 1.2
 *  2003/10/01 20:25:17 kikidonk Exp $
 *
 * @author     Raphael Slinckx (KiKiDonK)
 * @version    v0.08
 */
public class OptionDialog extends JDialog {
	
	private int toolbarHeight, toolbarWidth;
	private CardLayout cardLayout;
	private JToolBar jtb;
	private JPanel options;
	private OptionPanelInterface[] optionPanels;
	
	/**
	 *  Creates the dialog
	 *
	 * @param  f              Description of the Parameter
	 */
	public OptionDialog( JFrame f, OptionPanelInterface[] optionPanels ) {
		super(f);
		
        this.optionPanels = optionPanels;
		
		initialize();		
	}
	
	
	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		// Center the dialog within the bounds of it's parent.
        this.setSize(new Dimension(600, 300));
        this.setLocationRelativeTo(this.getParent());
        this.setTitle(LangageManager.getProperty( "entaggedoptionsdialog.title" ));
        this.setModal(true);
        
		this.jtb = new JToolBar(SwingConstants.VERTICAL);
		this.jtb.setLayout(new GridLayout(optionPanels.length, 1, 5, 5) );
		this.jtb.setBorder(null);
		
		this.options = new JPanel();
		this.cardLayout = new CardLayout();
		options.setLayout(cardLayout);
		
		for(int i = 0; i<optionPanels.length; i++)
			addPanel(optionPanels[i]);
		
		JPanel tool = new JPanel();
		tool.add(jtb, "Center");
		
		JScrollPane jsp = new JScrollPane(tool);
		jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setBorder(null);
		jsp.setMinimumSize(new Dimension(toolbarWidth, toolbarHeight));
		
		JSplitPane jsplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jsp, options);
		jsplit.setDividerLocation(0.3);
		jsplit.setOneTouchExpandable(false);
		
		JButton save = new JButton(LangageManager.getProperty( "common.dialog.save" ));
		save.addActionListener( new SaveButtonListener() );
		
		getContentPane().add(jsplit, "Center");
		getContentPane().add(save, "South");			
	}


	private void addPanel(OptionPanelInterface p ) {
		JButton b = p.getButton();
		b.setVerticalAlignment(SwingConstants.BOTTOM);
		b.setHorizontalAlignment(SwingConstants.CENTER);
		b.setVerticalTextPosition(SwingConstants.BOTTOM);
		b.setHorizontalTextPosition(SwingConstants.CENTER);
		
		this.toolbarHeight =  (b.getPreferredSize().getHeight() > this.toolbarHeight) ? (int) b.getPreferredSize().getHeight() : this.toolbarHeight;
		this.toolbarWidth = (b.getPreferredSize().getWidth() > this.toolbarWidth) ? (int) b.getPreferredSize().getWidth() : this.toolbarWidth;
		
		b.addActionListener(new OptionPanelSelectionListener(p));
		
		this.jtb.add(b);
		
		this.options.add(p.getOptionName(), p.getPanel());
	}
	
	private boolean saveOptions() {
		boolean needRestart = false;
		for(int i = 0; i<optionPanels.length; i++)
			needRestart = optionPanels[i].saveOptions()  || needRestart;
		return needRestart;
	}
	
	private class OptionPanelSelectionListener implements ActionListener {
		
		private OptionPanelInterface p;
		
		public OptionPanelSelectionListener(OptionPanelInterface p) {
			this.p = p;
		}
		
		public void actionPerformed( ActionEvent e ) {
			OptionDialog.this.cardLayout.show( OptionDialog.this.options, p.getOptionName() );
		}
	}
	
	private class SaveButtonListener implements ActionListener {
		public void actionPerformed( ActionEvent e) {
			if(saveOptions())
				JOptionPane.showMessageDialog( OptionDialog.this, LangageManager.getProperty( "common.dialog.mayneedrestart" ), "OptionDialog", JOptionPane.INFORMATION_MESSAGE );
			OptionDialog.this.setVisible(false);
		}
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"

