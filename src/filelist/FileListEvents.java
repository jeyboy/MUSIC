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
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;

import filelist.ListItem.STATUS;

import service.Common;
import service.Dropper;

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
        			Common._drop_initer.ProceedDrop(filelist.parent, ev.getFiles());
        	}
        });
        
        ds = new DragSource();
        ds.createDefaultDragGestureRecognizer(filelist, DnDConstants.ACTION_COPY, this);        
        
        //dragging
        filelist.setDragEnabled(true);
        filelist.setTransferHandler(new TransferHandler(null));
//        filelist.setTransferHandler(new FileTransferHandler());
        
        filelist.getModel().addTreeModelListener(new TreeModelListener() {

			@Override
			public void treeNodesChanged(TreeModelEvent e) {}

			@Override
			public void treeNodesInserted(TreeModelEvent e) { filelist.parent.UpdateCounter(); }

			@Override
			public void treeNodesRemoved(TreeModelEvent e) { filelist.parent.UpdateCounter(); }

			@Override
			public void treeStructureChanged(TreeModelEvent e) {}
        	
        });

        
        filelist.addMouseListener(new MouseListener() {
        	@Override
            public void mouseClicked(MouseEvent mouseEvent) {
//              JList<?> theList = (JList<?>) mouseEvent.getSource();
//              if (mouseEvent.getClickCount() == 2) {
//                int index = theList.locationToIndex(mouseEvent.getPoint());
//                if (index >= 0)
//                  ((ListItem) theList.getModel().getElementAt(index)).Exec();
//              }
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
    				TreePath last_pos = filelist.getSelectionPath();
    				for(TreePath obj: filelist.getSelectionPaths()) {
	    				if (filelist.parent.options.delete_files)
	    					Common._trash.AddPath(((ListItem)obj.getLastPathComponent()).file);
	    				filelist.removeSelectionPath(obj);
    				}
//    				filelist.CalcSelect(last_pos, true);
    			}
    			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
    				for(TreePath obj : filelist.getSelectionPaths())
    					((ListItem)obj.getLastPathComponent()).Exec();
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
		List<File> files = new ArrayList<File>();
		ListItem temp;
		for (TreePath obj: filelist.getSelectionPaths()) {
			temp = (ListItem)obj.getLastPathComponent();
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
