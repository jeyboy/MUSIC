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

import entagged.tageditor.util.*;
import javax.swing.*;

public abstract class AbstractControlPanel extends JPanel {

	protected MultipleFieldsMergingTable audioFiles;
	protected TagEditorFrame tagEditorFrame;
	
	protected static final String VARIES = MultipleFieldsMergingTable.VARIES;
	
	public AbstractControlPanel( TagEditorFrame tagEditorFrame, MultipleFieldsMergingTable audioFiles ) {
		this.audioFiles = audioFiles;
		this.tagEditorFrame = tagEditorFrame;
		createPanel();
	}
	
	protected abstract void createPanel();

	public abstract void update();
}

