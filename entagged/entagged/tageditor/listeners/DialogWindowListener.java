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

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;


/**
 *  This Class is a very simple implementation of a Window Listener, used to close a Dialog when user click on the X $Id: DialogWindowListener.java,v 1.1 2007/03/23 14:16:44 nicov1 Exp $
 *
 * @author     Raphaël Slinckx (KiKiDonK)
 * @version    v0.03
 */
public class DialogWindowListener implements WindowListener {

	public void windowActivated( WindowEvent e ) { }


	public void windowClosed( WindowEvent e ) { }


	/**
	 *  Set the caller frame invisible, thus allowing it to exit
	 *
	 * @param  e  the event
	 */
	public void windowClosing( WindowEvent e ) {
		e.getWindow().setVisible( false );
	}


	public void windowDeactivated( WindowEvent e ) { }


	public void windowDeiconified( WindowEvent e ) { }


	public void windowIconified( WindowEvent e ) { }


	public void windowOpened( WindowEvent e ) { }
}

