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
package entagged.tageditor.renderers;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;

import miage.sgbd.SqlProvider;

import entagged.audioformats.AudioFile;
import entagged.audioformats.Tag;
import entagged.tageditor.models.TableSorter;
import entagged.tageditor.models.TagEditorTableModel;
import entagged.tageditor.resources.PreferencesManager;


public class TagEditorTableCellRenderer extends DefaultTableCellRenderer {
	
	public static final Color NO_TAG = new Color(245,245,255);
	public static final Color TAGGED = new Color(245,255,245);
	public static final Color READ_ERROR = new Color(252,245,245);
	public static final Color DIRECTORIES = Color.WHITE;
	public static final Color SELECTED = new Color(180,180,180);
	public static final Color SELECTED_NOFOCUS = new Color(230,230,230); 
	public static final Color INDEX = new Color (25,106,168);
	public static final Color COLOR1 = new Color (0,229,238);
	public static final Color COLOR2 = new Color (0,197,205);
	
	/**
	 * This border will be applied to a row if it is the lead selection of
	 * the table.
	 */
	private static final Border LEAD_SEL_BORDER = new LineBorder(Color.BLACK,1);
	
	private static final Border DEFAULT_BORDER = BorderFactory.createEmptyBorder();
	
	private TagEditorTableModel tableModel;
	private TableSorter tableSorter;
	
	public TagEditorTableCellRenderer(TagEditorTableModel tableModel, TableSorter tableSorter) {
		this.tableModel = tableModel;
		this.tableSorter = tableSorter;
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,int row, int column) {
		JComponent renderer = (JComponent) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
		renderer.setOpaque(true);//!!!!
		
		//We don't want border in the cells
		renderer.setBorder(DEFAULT_BORDER);
		
		//Get the selected file
		File f = tableModel.getFileAt(tableSorter.modelIndex(row));
		
		//Null file ?? should not happen
		assert f != null;
	
		//A selected item
		if(isSelected) {
		    if (table.hasFocus()) {
		        renderer.setBackground(SELECTED);
				// If the current row is the lead selection of the table, add a border
				if (table.getSelectedRows().length > 1 && 
				        row == table.getSelectionModel().getLeadSelectionIndex()) {
				    renderer.setBorder(LEAD_SEL_BORDER);
				}
		    }
		    else
		        renderer.setBackground(SELECTED_NOFOCUS);
		}
		//A directory
		else if( f.isDirectory() ) {
			//We have an unselected Directory
			//Default color for directories
			renderer.setBackground(DIRECTORIES);
			renderer.setForeground(Color.BLACK);
		}
		//A File
		else {
			if(f instanceof AudioFile) {
				//We have an audiofile
				Tag tag = ((AudioFile) f).getTag();
				if(PreferencesManager.getBoolean("tageditor.table.indexfiles"))
				{
					if (SqlProvider.FichierExist(f.getAbsolutePath()))
						renderer.setForeground(Color.GREEN);
					else 
						renderer.setForeground(Color.RED);
				}
				if(!tag.isEmpty())
					renderer.setBackground(TAGGED);
				else
					renderer.setBackground(NO_TAG);
			} else {
				//A File that could not be read
				renderer.setBackground(READ_ERROR);
			}
		}
		
		return renderer;
	}
}
