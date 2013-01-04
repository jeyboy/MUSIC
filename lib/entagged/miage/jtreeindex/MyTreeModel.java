/*
 * Created on 9 mars 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package miage.jtreeindex;

import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

/**
 * @author G909248
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MyTreeModel extends DefaultTreeModel {

	/**
	 * 
	 */
	public MyTreeModel(File f) {
		//création du premier noeud
		
		super(new DefaultMutableTreeNode(f));
		
//		Appele de la fonction addChildren de la classe TreeUtil
		//Elle demande en paramètre DefaultTreeModel, DefaultMutableTreeNode
		TreeUtil.addChildren(this,(DefaultMutableTreeNode)getRoot());
		/*
		 * This représente cette class
		 * getRoot est une méthode de la classe DefaultTreeModel
		 */
	}

	public boolean isLeaf(Object arg0) {
		return !((File)((DefaultMutableTreeNode)arg0).getUserObject()).isDirectory();
		/*
		 * isLeaf permet de savoir si l'objet passé en paramètre est une feuille ou non.
		 * Et ici on va testé si cette objet est un répertoire ou non
		 * on caste notre argument en File afin d'utiliser la methode isDirectory()
		 */
	}
}
