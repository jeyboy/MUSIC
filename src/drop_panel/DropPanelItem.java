package drop_panel;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import service.Common;
import service.Errorist;
import service.IOOperations;

public class DropPanelItem extends JButton implements DropTargetListener {
	private static final long serialVersionUID = 7946767162934576766L;
	protected DropTarget dropTarget;
	public File folder = null;
	
	public void setPath(String new_path) {
		try {
			folder = new File(new_path);
			dropTarget = folder.exists() ? new DropTarget(this, this) : null;
		}
		catch(Exception e) {
			Errorist.printLog(e);
			dropTarget = null;
			folder = null;
		}
		
		setEnabled(dropTarget != null);
	}
	
	public DropPanelItem(String text, String path) {
//		setBorder(BorderFactory.createEmptyBorder());
		setFocusPainted(false);
		setPath(path);
		super.setText(text);
		addActionListener(new ActionListener() {          
			public void actionPerformed(ActionEvent e) { IOOperations.open(folder); }
		});
	}
	
	public void dragEnter(DropTargetDragEvent dtde) {}
	public void dragOver(DropTargetDragEvent dtde) 	{}
	public void dropActionChanged(DropTargetDragEvent dtde) {}
	public void dragExit(DropTargetEvent dte) {}
	public void drop(DropTargetDropEvent evt) {
      int action = evt.getDropAction();
      evt.acceptDrop(action);
      try {
          Transferable data = evt.getTransferable();
          if (data.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
              List<File> files = (List<File>) data.getTransferData(DataFlavor.javaFileListFlavor);
              for(File file:files) {
            	  if (!IOOperations.copyFile(file, folder)) break;
            	  if (!evt.isLocalTransfer())
            		  Common.library.ProceedFile(file);
              }
          }
          evt.dropComplete(true);
      }
      catch (Exception e) { evt.dropComplete(false); Errorist.printLog(e); }	
	}
}
