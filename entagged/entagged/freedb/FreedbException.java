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
package entagged.freedb;



/**
 *  $Id: FreedbException.java,v 1.1 2007/03/23 14:16:58 nicov1 Exp $
 *
 * @author     Raphael Slinckx (KiKiDonK)
 * @version    15 janvier 2004
 */
public class FreedbException extends Exception {

	public FreedbException( String message ) {
		super( message );
	}


	public FreedbException() {
		super();
	}
}

