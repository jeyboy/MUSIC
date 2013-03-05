package filelist;

import java.awt.Color;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileSystemView;

import components.MainWnd;
import service.Common;
import tabber.Tab;

public class FileList extends JList<ListItem> {
	private static final long serialVersionUID = 2216859386306446869L;
	Tab parent;
	private ListItem played = null;
	
	public MyListModel<ListItem> model = new MyListModel<ListItem>(this);
	private IconListRenderer listrender = new IconListRenderer();
	
	public FileList(Tab parent_tab) {
		parent = parent_tab;
		setModel(model);
    	setCellRenderer(listrender);
    	setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    	new FileListEvents(this);
    	setComponentPopupMenu(new ListPopUp(this));
    	setSelectionForeground(Color.white);
		setBackground(Color.black);
	}
	
	public void SetPlayed(ListItem item) 
	{	
		if (played != null)
			played.SetStatusUnPlayed();
		
		if ((played = item) != null) {
			played.SetStatusPlayed();
			played.Exec();
			setSelectedValue(played, false);
		}
		MainWnd.wnd.repaint();
	}
	public ListItem GetPlayed() { return played; }
	public int GetPlayedIndex() { return model.indexOf(played); }
	public ListItem getItemFromCursor() {
		int index = locationToIndex(getMousePosition());
		return index < 0 ? null : model.get(index); 
	}

    public void ProceedElem(ListItem elem) {
        AddAssocIcon(elem.ext, FileSystemView.getFileSystemView().getSystemIcon(elem.file));
        model.addElement(elem);
        Common._initer.AddItem(elem);
    }
	
    public void AddAssocIcon(String ext, Icon icon) {
    	if (listrender.icons.containsKey(ext) || icon == null) return; 
    	listrender.icons.put(ext, icon); 
    }
    
    public void ChangeViewToVertical() { setLayoutOrientation(JList.VERTICAL); }
    public void ChangeViewToVerticalWrap() { setLayoutOrientation(JList.VERTICAL_WRAP); }
    public void ChangeViewToHorizontalWrap() { setLayoutOrientation(JList.HORIZONTAL_WRAP); }
   
	// This method is called as the cursor moves within the list.
    public String getToolTipText(MouseEvent evt) {
      int index = locationToIndex(evt.getPoint());
      if (index > -1) {
	      ListItem item = (ListItem)getModel().getElementAt(index);
	      return item == null ? "" : item.media_info.toString();
      }
      return null;
    }	

	/// Helper methods

	private int CheckRange(int index) {
		if (index >= model.getSize()) index = (model.getSize() - 1);
		return (index < 0) ? 0 : index;
	}    
    
	private int InverseCheckRange(int index) {
		if (index >= model.getSize()) index = 0;
		return (index < 0) ? (model.getSize() - 1) : index;
	}

	public int CalcSelect(int curr, boolean next) {
		int index = InverseCheckRange(curr + (next ? 1 : -1));
		setSelectedIndex(index);
		return index;
	}	
	
	public void PlaySelectedOrFirst() 				{	SetPlayed(model.elementAt(CheckRange(GetPlayedIndex())));	}	
	public int MoveSelect(int index, boolean next) 	{ 	return CalcSelect(index, next); }
	public void MoveSelectAndInit(boolean next) 	{	SetPlayed(model.elementAt(MoveSelect(GetPlayedIndex(), next)));	}
	public void DeleteSelectAndInit() {
		int selected = GetPlayedIndex();
		if (selected == -1) {
			MoveSelectAndInit(true);
			return;
		}
		if (parent.options.delete_files)
			Common._trash.AddElem(played.file, parent.options.delete_empty_folders);
		model.remove(selected);
		if ((selected = InverseCheckRange(selected)) == -1) return;
		ensureIndexIsVisible(selected);
		SetPlayed(model.elementAt(selected));
	}
	
	public void SetStatus(String status) {parent.SetStatus(status);}
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
//
//}