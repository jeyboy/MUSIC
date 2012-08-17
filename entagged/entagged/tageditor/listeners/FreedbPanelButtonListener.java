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
package entagged.tageditor.listeners;

import entagged.tageditor.*;
import entagged.tageditor.exceptions.*;
import entagged.audioformats.*;
import entagged.audioformats.AudioFile;
import entagged.audioformats.exceptions.*;
import entagged.tageditor.util.*;
import java.util.*;
import java.io.*;

public abstract class FreedbPanelButtonListener extends ControlPanelButtonListener {
	
	protected MultipleFieldsMergingTable audioFiles;
	protected TagEditorFrame tagEditorFrame;
	private static AudioFileFilter aff = new AudioFileFilter();
	
	public FreedbPanelButtonListener( TagEditorFrame tagEditorFrame, MultipleFieldsMergingTable audioFiles ) {
		super(tagEditorFrame,audioFiles);
	}
	
	protected abstract void prepareAction();
	protected abstract void finalizeAction();
	protected abstract void directoryAction(List files) throws OperationException;
	
	protected void audioFileAction(AudioFile af) {}
	
	protected int countFiles(File f) {
		if(f.isDirectory()) {
			int count = 1;
			File[] files = f.listFiles(aff);
			for(int i = 0; i<files.length; i++) {
				if(files[i].isDirectory()) {
					count += countFiles(files[i]);
				}
			}
			return count;
		}
		else
			return 0;
	}
	
	protected List recursiveAction(File f) {
		List errorFiles = new LinkedList();
		
		if(f.isDirectory()) {
			List freedbAlbum = new LinkedList();
			
			File[] files = f.listFiles(aff);
			for(int i = 0; i<files.length; i++) {
				if(files[i].isDirectory()) {
					errorFiles.addAll(recursiveAction(files[i]));
				}
				else {
					try{
						freedbAlbum.add(  AudioFileIO.read(files[i]) );
					}catch(CannotReadException e) {
						//If a file cannot be read, there is no point querying freedb !
						addException(errorFiles, files[i], e);
						freedbAlbum.clear();
						return errorFiles;
					}
				}
			}
			
			try {
				//If there is at least one file !
				if(freedbAlbum.size() != 0)
					directoryAction(freedbAlbum);
			} catch(OperationException e) {
				//The directory cannot be tagged with freedb
				addException(errorFiles, f, e);
			}
			
			freedbAlbum.clear();
		}
		
		return errorFiles;
	}
}
