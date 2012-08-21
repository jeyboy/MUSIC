package filelist;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import service.Common;
import tabber.Tab;

public class FileList extends JTree {
	
	private static final long serialVersionUID = 2216859386306446869L;
	private static DefaultMutableTreeNode root = new DefaultMutableTreeNode("*******");
	public ConcurrentHashMap<String, DefaultMutableTreeNode> roots = new ConcurrentHashMap<String, DefaultMutableTreeNode>(5);
	Tab parent;
	
	
    //Alter = Vector, ArrayList
	private IconListRenderer listrender = new IconListRenderer();
	
	public FileList(Tab parent_tab) {
		super(root);
		setRootVisible(false);
		parent = parent_tab;
		CommonInitPart();
//		setBackground(Color.black);
	}
	
    private void CommonInitPart() {
    	super.setCellRenderer(listrender);
    	getSelectionModel().setSelectionMode(TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);
    	new FileListEvents(this);
	}

    public void AddAssocIcon(String ext, Icon icon) {
    	if (listrender.icons.containsKey(ext) || icon == null) return; 
    	listrender.icons.put(ext, icon); 
    }
    
    void ElemProceeding(DefaultMutableTreeNode root_node, ListItem elem) {
    	root_node.add(new DefaultMutableTreeNode(elem));
		AddAssocIcon(elem.ext, FileSystemView.getFileSystemView().getSystemIcon(elem.file));
		Common._initer.AddItem(elem);    	
    }
	public void ProceedElem(DefaultMutableTreeNode root_node, ListItem elem) 	{ ElemProceeding(root_node, elem);	}
    
	void LinkRoot(DefaultMutableTreeNode new_root) { root.add(new_root); }
    public DefaultMutableTreeNode GetRoot(String path) {
    	DefaultMutableTreeNode res = roots.get(path);
    	if (res == null) {
    		System.out.println("******* init new root");
    		res = new DefaultMutableTreeNode(path);
    		System.out.println("******* " + res);
//        	res = roots.put(path, res);
        	System.out.println("******* " + res);
        	LinkRoot(res);    		
    	}
    	return res;
    }
   
	// This method is called as the cursor moves within the list.
    public String getToolTipText(MouseEvent evt) {
//      int index = locationToIndex(evt.getPoint());
//      if (index > -1) {
//	      ListItem item = (ListItem)getModel().getElementAt(index);
//	      return item.media_info.toString();
//      }
//      return null;
    	return super.getToolTipText(evt);
    }
    
    public void LockPaint(boolean lock) {
			setEnabled(!lock);
			setVisible(!lock);
			setIgnoreRepaint(lock);
			if (!lock) repaint();
    }



//interactive search
//import javax.swing.DefaultListModel;
//import javax.swing.JList;
//import javax.swing.event.DocumentEvent;
//import javax.swing.event.DocumentListener;
//import javax.swing.text.BadLocationException;
// 
//public class SolarSystemSearchField extends JList implements DocumentListener {
// 
//    private SolarSystemCollection m_collection;
//    private static DefaultListModel m_listModel = new DefaultListModel();
//     
//    /**
//     * 
//     */
//    private static final long serialVersionUID = 1L;
//     
//    SolarSystemSearchField(SolarSystemCollection collection) {
//        super(m_listModel);
//         
//        m_collection = collection;
//         
//        for (int i=0; i<m_collection.GetSize(); ++i)
//            m_listModel.addElement(m_collection.systems.get(i));
//    }
//     
//    public SolarSystem getTopmost() {
//        if (m_listModel.size() > 0)
//            return (SolarSystem) m_listModel.get(0);
//        else
//            return null;
//    }
// 
//    @Override
//    public void changedUpdate(DocumentEvent arg0) {
//        searchForHit(getSearchString(arg0));
//    }
// 
//    @Override
//    public void insertUpdate(DocumentEvent arg0) {
//        searchForHit(getSearchString(arg0));
//    }
// 
//    @Override
//    public void removeUpdate(DocumentEvent arg0) {
//        searchForHit(getSearchString(arg0));
//    }
//     
//    private String getSearchString(DocumentEvent arg0) {
//        try {
//            return arg0.getDocument().getText(0, arg0.getDocument().getLength());
//        } catch (BadLocationException e) {
//    		  Errorist.printLog(e);
//            return "";
//        }
//    }
//     
//    private void searchForHit(String searchStr) {
//        m_listModel.clear();
//        m_listModel.ensureCapacity(m_collection.GetSize());
// 
//        for (int i=0; i<m_collection.GetSize(); ++i) {
//            SolarSystem s = m_collection.systems.get(i);
//            if (s.name.toLowerCase().contains(searchStr.toLowerCase()))
//                m_listModel.addElement(s);
//        }
//        //if (m_listModel.getSize() > 0)
//        //  setSelectedIndex(0);
//    }
// 
//}
}