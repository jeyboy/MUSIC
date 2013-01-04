package miage.jtreeindex;

import java.awt.Component;
import java.io.File;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import entagged.tageditor.resources.ResourcesRepository;

import miage.sgbd.DataProvider;

public class PersonnalTreeCellRenderer extends DefaultTreeCellRenderer {

	public static ArrayList<String> AL = new ArrayList<String>();
	
	public PersonnalTreeCellRenderer(){
		AL = DataProvider.getDossier();
	}
	
    public Component getTreeCellRendererComponent(
            JTree tree, Object value, boolean sel, boolean expanded, boolean leaf,
            int row, boolean hasFocus) {

    	super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		DefaultMutableTreeNode Df = (DefaultMutableTreeNode) value;
		String nom = ((File)Df.getUserObject()).getAbsolutePath();
    	if (AL.contains(nom))
    	{
    		ImageIcon icon = ResourcesRepository.getImageIcon("index.gif");
    		setIcon(icon);
    	}
    	else {
    		ImageIcon icon = ResourcesRepository.getImageIcon("noindex.gif");
    		setIcon(icon);
    	}
    	//System.out.println(value.toString());
    	return this;
    }
}
