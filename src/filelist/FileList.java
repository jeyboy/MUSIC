package filelist;

import java.awt.Color;
import java.awt.event.MouseEvent;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

import folders.FolderNode;

public class FileList extends JList<ListItem> {
	static final long serialVersionUID = 1L;

	public MyListModel model;
	
	@SuppressWarnings("unchecked")
	public FileList(FolderNode node) {
		setModel((model = new MyListModel(node)));
    	setCellRenderer(node.listrender);
    	setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    	new FileListEvents(this, node);
    	setComponentPopupMenu(new ListPopUp(this));
    	setSelectionForeground(Color.white);
		setBackground(Color.black);
}
	
    public void ChangeViewToVertical() 			{ setLayoutOrientation(JList.VERTICAL); }
    public void ChangeViewToVerticalWrap() 		{ setLayoutOrientation(JList.VERTICAL_WRAP); }
    public void ChangeViewToHorizontalWrap() 	{ setLayoutOrientation(JList.HORIZONTAL_WRAP); }
    
	public ListItem getItemFromCursor() {
		int index = locationToIndex(getMousePosition());
		return index < 0 ? null : model.getElementAt(index); 
	}
   
	// This method is called as the cursor moves within the list.
    public String getToolTipText(MouseEvent evt) {
//      int index = locationToIndex(evt.getPoint());
//      if (index > -1) {
//	      ListItem item = (ListItem)getModel().getElementAt(index);
//	      return item == null ? "" : item.media_info.toString();
//      }
//      return null;
    	return "Temporary disabled";
    }	
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
//  private SolarSystemCollection m_collection;
//  private static DefaultListModel m_listModel = new DefaultListModel();
//   
//  /**
//   * 
//   */
//  private static final long serialVersionUID = 1L;
//   
//  SolarSystemSearchField(SolarSystemCollection collection) {
//      super(m_listModel);
//       
//      m_collection = collection;
//       
//      for (int i=0; i<m_collection.GetSize(); ++i)
//          m_listModel.addElement(m_collection.systems.get(i));
//  }
//   
//  public SolarSystem getTopmost() {
//      if (m_listModel.size() > 0)
//          return (SolarSystem) m_listModel.get(0);
//      else
//          return null;
//  }
//
//  @Override
//  public void changedUpdate(DocumentEvent arg0) {
//      searchForHit(getSearchString(arg0));
//  }
//
//  @Override
//  public void insertUpdate(DocumentEvent arg0) {
//      searchForHit(getSearchString(arg0));
//  }
//
//  @Override
//  public void removeUpdate(DocumentEvent arg0) {
//      searchForHit(getSearchString(arg0));
//  }
//   
//  private String getSearchString(DocumentEvent arg0) {
//      try {
//          return arg0.getDocument().getText(0, arg0.getDocument().getLength());
//      } catch (BadLocationException e) {
//  		  Errorist.printLog(e);
//          return "";
//      }
//  }
//   
//  private void searchForHit(String searchStr) {
//      m_listModel.clear();
//      m_listModel.ensureCapacity(m_collection.GetSize());
//
//      for (int i=0; i<m_collection.GetSize(); ++i) {
//          SolarSystem s = m_collection.systems.get(i);
//          if (s.name.toLowerCase().contains(searchStr.toLowerCase()))
//              m_listModel.addElement(s);
//      }
//      //if (m_listModel.getSize() > 0)
//      //  setSelectedIndex(0);
//  }
//}