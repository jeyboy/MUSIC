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
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import folders.FolderNode;

import service.Common;
import service.Dropper;

public class FileListEvents implements DragSourceListener, DragGestureListener {
	FileList filelist;
	Dropper dropper;
	DragSource ds;
	ArrayList<ListItem> droped_items = new ArrayList<ListItem>();
	boolean drop_in = false;
	
	public FileListEvents(FileList flist, final FolderNode node) { 
		filelist = flist;
        dropper = new Dropper(filelist, new Dropper.Listener() {
        	public void filesDropped(Dropper.Event ev) {
        		if (!(drop_in = (boolean)ev.getSource()))
        			Common._drop_initer.ProceedDrop(node.tab, ev.getFiles());
        	}
        });
        
        ds = new DragSource();
        ds.createDefaultDragGestureRecognizer(filelist, DnDConstants.ACTION_COPY, this);        
        
        //dragging
        filelist.setDragEnabled(true);
        filelist.setTransferHandler(new FileTransferHandler());
        
        filelist.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent mouseEvent) {
        		if (mouseEvent.getClickCount() == 2) {
        			int index = filelist.locationToIndex(mouseEvent.getPoint());
        			if (index >= 0)
        				node.tab.catalog.setPlayed((ListItem) filelist.model.getElementAt(index));
        		}
            }
    		public void mouseEntered(MouseEvent arg0) {}
    		public void mouseExited(MouseEvent arg0) {}
    		public void mousePressed(MouseEvent arg0) {}
    		public void mouseReleased(MouseEvent arg0) {}
        });
        
        filelist.addKeyListener(new KeyListener() {
    		public void keyTyped(KeyEvent e) {}
    		public void keyPressed(KeyEvent e) {   			
    			node.tab.catalog.selection.setMaskState((e.getModifiers() & KeyEvent.SHIFT_MASK) != 0 || (e.getModifiers() & KeyEvent.CTRL_MASK) != 0); 
    			
    			if (e.getKeyCode() == KeyEvent.VK_DELETE) {
    				int last_pos = filelist.getSelectedIndex() - 1;
    				for(Object obj: filelist.getSelectedValuesList())
    					((ListItem)obj).delete();
    				node.calcSelect(last_pos, true);
    			}
    			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
    				node.tab.catalog.setPlayed(filelist.model.getElementAt(filelist.getSelectedIndex()));
    			}
    			
    			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
    				if (filelist.getSelectedIndex() == filelist.getModel().getSize() - 1)
    					node.tab.catalog.moveSelect(true);
    			}
    			
    			if (e.getKeyCode() == KeyEvent.VK_UP) {
    				if (filelist.getSelectedIndex() == 0)
    					node.tab.catalog.moveSelect(false);
    			}     			
    		}
    		public void keyReleased(KeyEvent e) {
    			node.tab.catalog.selection.setMaskState((e.getModifiers() & KeyEvent.SHIFT_MASK) != 0 || (e.getModifiers() & KeyEvent.CTRL_MASK) != 0);
    		}
        });
        
        filelist.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent arg0) {
				node.tab.catalog.selection.setFocus(node);
			}
			
			public void focusLost(FocusEvent arg0) {}});
        
        filelist.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent lse) {
				node.tab.catalog.selection.setInterval(node, filelist.getSelectedIndices());
			}});
	}
	
    private class FileTransferHandler extends TransferHandler {
		private static final long serialVersionUID = 1873106934925755472L;
    	protected Transferable createTransferable(JComponent c) { return null; 	}
    	public int getSourceActions(JComponent c) {
    		return COPY; //MOVE
    	}
    }

    private class FileTransferable implements Transferable {
    	private List<File> files;

    	public FileTransferable(List<File> files) { this.files = files;	}
    	public DataFlavor[] getTransferDataFlavors() {
    		return new DataFlavor[]{DataFlavor.javaFileListFlavor};
    	}
		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return flavor.equals(DataFlavor.javaFileListFlavor);
		}
		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
			if (!isDataFlavorSupported(flavor))
    			throw new UnsupportedFlavorException(flavor);
			
    		return files;
		}
    }

	public void dragGestureRecognized(DragGestureEvent dge) {
		List<File> files = new ArrayList<File>();
		ListItem temp;
		for (Object obj: filelist.getSelectedValuesList()) {
			temp = (ListItem)obj;
			droped_items.add(temp);
			files.add(temp.file());
		}

		ds.startDrag(dge, DragSource.DefaultCopyDrop, new FileTransferable(files), this);
	}
	public void dragEnter(DragSourceDragEvent dsde) {}
	public void dragOver(DragSourceDragEvent dsde) {}
	public void dropActionChanged(DragSourceDragEvent dsde) {}
	public void dragExit(DragSourceEvent dse) {}
	public void dragDropEnd(DragSourceDropEvent dsde) {
	    if (dsde.getDropSuccess() && !drop_in) {
	        for(ListItem li : droped_items)
	        	li.setStatusLiked();
//	        MainWnd.wnd.repaint();
	    }
	    droped_items.clear();
	    droped_items.trimToSize();
	    drop_in = false;
	} 	
}