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

import entagged.tageditor.resources.*;

import java.awt.*;
import java.io.File;

import javax.swing.*;
import entagged.tageditor.util.*;

// $Id: ControlPanel.java,v 1.1 2007/03/23 14:17:08 nicov1 Exp $
public class ControlPanel extends JTabbedPane {

	private AbstractControlPanel tagPanel;
	
	private TagInfoPanel tagInfoPanel;

	private FreedbPanel freedbPanel;
	private ManualFreedbPanel manualFreedbPanel;

	private MultipleFieldsMergingTable audioFiles;
	
	private TrackEnumerationPanel enumerationPanel;

	public ControlPanel(TagEditorFrame tagEditorFrame) {
		this.audioFiles = new MultipleFieldsMergingTable();

		this.tagInfoPanel = new TagInfoPanel(tagEditorFrame, audioFiles);
		this.addTab(LangageManager.getProperty("controlpanel.tageditor"), null, tagInfoPanel);

		this.addTab(LangageManager.getProperty("controlpanel.renamefromtag"),null,new RenamePanel(tagEditorFrame, audioFiles));

		this.tagPanel = new TagPanel(tagEditorFrame, audioFiles);
		this.addTab(LangageManager.getProperty("controlpanel.tagfromfilename"),null,tagPanel);

		this.freedbPanel = new FreedbPanel(tagEditorFrame, audioFiles);
		this.addTab(LangageManager.getProperty("controlpanel.freedb"), null, freedbPanel);

		this.manualFreedbPanel =new ManualFreedbPanel(tagEditorFrame, audioFiles);
		this.addTab(LangageManager.getProperty("controlpanel.manualfreedb"), null, manualFreedbPanel);
		
		this.enumerationPanel = new TrackEnumerationPanel(tagEditorFrame,audioFiles);
		this.addTab(LangageManager.getProperty("controlpanel.trackenum"),null,enumerationPanel);

		this.setPreferredSize(new Dimension(300, 450));
		restoreSelectedIndex();
	}

	private void restoreSelectedIndex() {
	    int index = PreferencesManager.getInt("tageditor.tageditorframe.controlpanel.selectedindex");
	    if(index < this.getTabCount())
	        this.setSelectedIndex(index);
			
	}

	public void saveGUIPreferences() {
		//-------TABS PREFERENCES---------
		System.out.println("Exiting: Saving selected control panel index ...");
		PreferencesManager.putInt(
			"tageditor.tageditorframe.controlpanel.selectedindex",
			this.getSelectedIndex());
		//-------------------------------------
	}

	public void add(File f) {
		this.audioFiles.add(f);
	}
	
	/**
	 * Will cause the calculation of those files which were added or removed
	 * by last table selection change.
	 */
	public void processFileDifference() {
	    audioFiles.processFileDifference();
	}
	
	public void update() {
		this.tagInfoPanel.update();
		this.freedbPanel.update();
		this.manualFreedbPanel.update();
	}
	
	public void clear() {
		this.audioFiles.clear();
		update();
	}
    /**
     * @return Returns the audioFiles.
     */
    public MultipleFieldsMergingTable getAudioFiles() {
        return this.audioFiles;
    }
    
    /**
     * This method will make {@link #tagInfoPanel} visible and sets the 
     * focus on its artist field.
     *
     */
    public void focusArtistOnTagInfoPanel() {
        this.setSelectedIndex(0);
        tagInfoPanel.artistF.requestFocus();
    }
    
}
