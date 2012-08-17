/*
 * Created on 9 mars 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package miage.jtreeindex;

import java.awt.Component;
import java.io.File;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author G909248
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

	import java.awt.Component;
	import java.io.File;

	import javax.swing.event.TreeSelectionEvent;
	import javax.swing.event.TreeSelectionListener;
	import javax.swing.tree.DefaultMutableTreeNode;


	/**
	 * @author G909248
	 *
	 * TODO To change the template for this generated type comment go to
	 * Window - Preferences - Java - Code Style - Code Templates
	 */
	public class myTreeSelectionListener implements	TreeSelectionListener {

		public static String path = "";
		
		public myTreeSelectionListener(){
			super();
		}
		/* (non-Javadoc)
		 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
		 */
		public void valueChanged(TreeSelectionEvent arg0) {
			
			DefaultMutableTreeNode f = (DefaultMutableTreeNode) arg0.getPath().getLastPathComponent();
			File ff = (File) f.getUserObject();

			/*MainWindow mainWindow = (MainWindow) javax.swing.SwingUtilities.windowForComponent((Component)arg0.getSource());*/
			
			if(!ff.isDirectory()){
				//action si ca n'est pas un répertoire
			}else{
				//action si c'est un répertoire	
				path = ff.getAbsolutePath();
			}
			
			/*System.out.println(ff.isDirectory());
			System.out.println(ff.getAbsolutePath());*/
			
		}
		

		


	}