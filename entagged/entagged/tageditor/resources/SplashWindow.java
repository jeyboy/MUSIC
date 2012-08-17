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
package entagged.tageditor.resources;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import java.net.URL;

/**
 *  $Id: SplashWindow.java,v 1.1 2007/03/23 14:17:20 nicov1 Exp $
 *
 * @author     Raphael Slinckx (KiKiDonK)
 * @version    4 janvier 2004
 */
public class SplashWindow extends JWindow implements InitializationMonitor{
	private JProgressBar progress;

	public SplashWindow( URL image ) {
		super();

		progress = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100);
		progress.setValue(0);
		progress.setStringPainted(true); 
		
		ImageIcon splashImage = new ImageIcon( image );
		int imageHeight = splashImage.getIconHeight();
		int imageWidth = splashImage.getIconWidth();

		this.getContentPane().add( new JLabel( splashImage ), "Center" );
		this.getContentPane().add( progress, "South" );

		this.setSize( imageWidth, imageHeight );

		Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();

		this.setLocation( ( screenDim.width - imageWidth ) / 2, ( screenDim.height - imageHeight ) / 2 );
	}


	public void setStatus( String text, int val ) {
		progress.setString(text);
		
		/*//DEBUG-----
		int currentValue = progress.getValue();
		if(currentValue+val > progress.getMaximum())
			System.out.println("Warning, exceeding 100%");
		//-----------*/
		
		
		progress.setValue(progress.getValue()+val);
	}
	
	public void setBounds(int min, int max) {
		progress.setMinimum(min);
		progress.setMaximum(max);
	}
	
	public void setFinishing(String text) {
		progress.setString(text);
		
		/*//DEBUG----
		System.out.println("Finished loading, current value: "+progress.getValue());
		//---------*/
		
		progress.setValue(progress.getMaximum());
	}
	
	public void setBeginning(String text) {
		progress.setString(text);
		progress.setValue(progress.getMinimum());
	}

}

