package drop_panel;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import service.Common;
import service.Errorist;
import service.IOOperations;

public class DropPanelItem implements DropTargetListener {
	protected DropTarget dropTarget;
	public File folder = null;
	public Button button; 
	
	public void setPath(String new_path) {
		try {
			folder = new File(new_path);
			dropTarget = folder.exists() ? new DropTarget(button, DND.DROP_COPY) : null;
//			dropTarget.setTransfer(types);
		}
		catch(Exception e) {
			Errorist.printLog(e);
			dropTarget = null;
			folder = null;
		}
		
		button.setEnabled(dropTarget != null);
	}
	
	public DropPanelItem(Composite wnd, String text, String path) {
		button = new Button(wnd, SWT.CENTER);
		button.setText(text);
		button.setSize(20, 20);
		setPath(path);
		
		button.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event arg0) { 
				try { IOOperations.open(folder); } 
				catch (IOException ee) { Errorist.printLog(ee); }				
			}
		});
	}

	@Override
	public void dragEnter(DropTargetEvent arg0) {}
	@Override
	public void dragLeave(DropTargetEvent arg0) {}
	@Override
	public void dragOperationChanged(DropTargetEvent arg0) {}
	@Override
	public void dragOver(DropTargetEvent arg0) {}
	@Override
	public void drop(DropTargetEvent arg0) {
        if (arg0.data == null) {
        	arg0.detail = DND.DROP_NONE;
	        return;
	    }
        
        boolean is_local = arg0.getSource() != null;
        List<File> files = (List<File>)arg0.data;
        for(File file:files) {
      	  try { if (!IOOperations.copyFile(file, folder)) break;  } 
      	  catch (IOException e) {Errorist.printLog(e);}
      	  if (is_local)
      		  Common.library.ProceedFile(file);
        }        
	}
	@Override
	public void dropAccept(DropTargetEvent arg0) {}
}
