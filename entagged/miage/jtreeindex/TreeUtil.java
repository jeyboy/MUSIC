/*
 * Created on 9 mars 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package miage.jtreeindex;

import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import miage.sgbd.DataProvider;

import entagged.tageditor.ProgressDialog;
import entagged.tageditor.TagEditorFrame;

/**
 * @author G909248
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TreeUtil {
	
	
	/**
	 * 
	 */
	protected static void addChildren(DefaultTreeModel treeModel,DefaultMutableTreeNode parentNode) {
		//Création d'un Fichier avec le nom du répertoire ouvert
		File selectedFile = (File)parentNode.getUserObject();
		
		//on supprime tout les enfants du répertoire ouvert
		parentNode.removeAllChildren();
		//On informe le treeModel que sa Structure à changé
		treeModel.nodeStructureChanged(parentNode);
		
		//Listage du répertoire
		File[] children = selectedFile.listFiles();
		if (children != null)
		{	
			int x = 0;
			
			//On crée en premier les répertoire
			for(int i=0;i<children.length;i++){
				if(children[i].isDirectory() && !children[i].isHidden()){
			//		DefaultMutableTreeNode test = new DefaultMutableTreeNode(new MyFile(children[i].getAbsolutePath()));
			
					//On insert de nouveaux neux dans le treeModel
					treeModel.insertNodeInto(new DefaultMutableTreeNode(new MyFile(children[i].getAbsolutePath())),parentNode,x);
					/*MyFile est une classe qui hérite de File
					 * new MyFile(children[i].getAbsolutePath())) on crée un Fichier avec le chemin complet des fichiers qui on été listé.
					 * Et grâce a ce chemin complet on va pouvoir créer un DefaultMutableTreeNode
					 * 				 
					 */
					x++;
				}
			}
			//On crée les fichiers
			/*for(int i=0;i<children.length;i++){
				if(!children[i].isDirectory()){
					treeModel.insertNodeInto(new DefaultMutableTreeNode(new MyFile(children[i].getAbsolutePath())),parentNode,x);
					x++;
				}
			}*/
		}
	}
	
	protected static void ajoutdossier(String path){
		miage.ListeFichiers.ActualiserDossier(path);
		PersonnalTreeCellRenderer.AL = DataProvider.getDossier();
	}
	
	protected static void supprimerdossier(String path){
		miage.ListeFichiers.SupprimerDossier(path);
		PersonnalTreeCellRenderer.AL = DataProvider.getDossier();		
	}

	

}
