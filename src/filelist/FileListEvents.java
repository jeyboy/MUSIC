package filelist;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.TransferHandler;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import filelist.ListItem.STATUS;

import service.Common;
import service.Dropper;
import service.IOOperations;

public class FileListEvents  implements DragSourceListener, DragGestureListener {
	FileList filelist;
	Dropper dropper;
	DragSource ds;
	List<ListItem> droped_items = new Vector<ListItem>();
	
	public FileListEvents(FileList flist) { 
		filelist = flist;
		Init();
	}
	
	void Init() {
//		dropTarget = new DropTarget(component, dropTargetListener);
		
        dropper = new Dropper(filelist, new Dropper.Listener() {
        	public void filesDropped(Dropper.Event ev) {
        		if (!(boolean)ev.getSource())
        			//filelist.AddElemsF(IOOperations.ScanDirectoriesF(ev.getFiles()));
        			Common._drop_initer.ProceedDrop(filelist, ev.getFiles());
        	}
        });
        
        ds = new DragSource();
        ds.createDefaultDragGestureRecognizer(filelist, DnDConstants.ACTION_COPY, this);        
        
        //dragging
        filelist.setDragEnabled(true);
        filelist.setTransferHandler(new TransferHandler(null));
//        filelist.setTransferHandler(new FileTransferHandler());
        
//    	void setSelectedIndex(int index)   
//    	void setSelectedIndices(int[] indices)   
//    	void setSelectedValue(Object object, boolean shouldScroll)	
//    	public void ensureIndexIsVisible(int index) {};
    	filelist.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
//              System.out.println("First index: " + listSelectionEvent.getFirstIndex());
//              System.out.println(", Last index: " + listSelectionEvent.getLastIndex());
//              boolean adjust = listSelectionEvent.getValueIsAdjusting();
//              System.out.println(", Adjusting? " + adjust);
//              if (!adjust) {
//                JList list = (JList) listSelectionEvent.getSource();
//                int selections[] = list.getSelectedIndices();
//                Object selectionValues[] = list.getSelectedValues();
//                for (int i = 0, n = selections.length; i < n; i++) {
//                  if (i == 0) {
//                    System.out.println(" Selections: ");
//                  }
//                  System.out.println(selections[i] + "/" + selectionValues[i] + " ");
//                }
//              }
	       }
	    });
        
        filelist.addMouseListener(new MouseListener() {
        	@Override
            public void mouseClicked(MouseEvent mouseEvent) {
              JList<?> theList = (JList<?>) mouseEvent.getSource();
              if (mouseEvent.getClickCount() == 2) {
                int index = theList.locationToIndex(mouseEvent.getPoint());
                if (index >= 0)
                  ((ListItem) theList.getModel().getElementAt(index)).Exec();
              }
            }
    		@Override
    		public void mouseEntered(MouseEvent arg0) {}
    		@Override
    		public void mouseExited(MouseEvent arg0) {}
    		@Override
    		public void mousePressed(MouseEvent arg0) {}
    		@Override
    		public void mouseReleased(MouseEvent arg0) {}
        });
        
        filelist.addKeyListener(new KeyListener() {
    		@Override
    		public void keyTyped(KeyEvent e) {}
    		@Override
    		public void keyPressed(KeyEvent e) {
    			if (e.getKeyCode() == KeyEvent.VK_DELETE) {
    				int last_pos = filelist.getSelectedIndex() - 1;
    				for(Object obj: filelist.getSelectedValuesList()) {
	    				if (filelist.parent.options.delete_files)
	    					IOOperations.deleteFile(((ListItem)obj).file);
    					filelist.model.removeElement(obj);
    				}
    				filelist.CalcSelect(last_pos, true);
    			}
    			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
    				for(int index:filelist.getSelectedIndices())
    					filelist.model.elementAt(index).Exec();
    			}
    		}
    		@Override
    		public void keyReleased(KeyEvent e) {}
        });
	}
	
//    private class FileTransferHandler extends TransferHandler {
//		private static final long serialVersionUID = 1873106934925755472L;
//
//		@Override
//    	protected Transferable createTransferable(JComponent c) {
//    		JList<Object> list = (JList<Object>)c;
//    		List<File> files = new ArrayList<File>();
//    		for (Object obj: list.getSelectedValuesList()) {
//    			files.add(((ListItem)obj).file);
////    			Common.library.Set(title, down)ProceedItem((ListItem)obj);
//    		}
//    		return new FileTransferable(c, files);
//    	}
//
//    	@Override
//    	public int getSourceActions(JComponent c) {
//    		return COPY; //MOVE
//    	}
//    }

    private class FileTransferable implements Transferable {
    	private List<File> files;

    	public FileTransferable(List<File> files) { this.files = files;	}
    	@Override
    	public DataFlavor[] getTransferDataFlavors() {
    		return new DataFlavor[]{DataFlavor.javaFileListFlavor};
    	}
		@Override
		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return flavor.equals(DataFlavor.javaFileListFlavor);
		}
		@Override
		public Object getTransferData(DataFlavor flavor)
				throws UnsupportedFlavorException, IOException {
			if (!isDataFlavorSupported(flavor)) {
    			throw new UnsupportedFlavorException(flavor);
    		}
    		return files;
		}
    }
    
    
    

	@Override
	public void dragGestureRecognized(DragGestureEvent dge) {
		JList<?> list = (JList<?>)filelist;
		List<File> files = new ArrayList<File>();
		ListItem temp;
		for (Object obj: list.getSelectedValuesList()) {
			temp = (ListItem)obj;
			files.add(temp.file);
			droped_items.add(temp);
		}
	
	    ds.startDrag(dge, DragSource.DefaultCopyDrop, new FileTransferable(files), this);
	}

	@Override
	public void dragEnter(DragSourceDragEvent dsde) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dragOver(DragSourceDragEvent dsde) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dropActionChanged(DragSourceDragEvent dsde) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dragExit(DragSourceEvent dse) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dragDropEnd(DragSourceDropEvent dsde) {
	    if (dsde.getDropSuccess()) {
	        for(ListItem li : droped_items)
	        	li.SetState(STATUS.LIKED);
	    }
	    droped_items.clear();
	} 	
}
