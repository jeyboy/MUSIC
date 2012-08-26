package drop_panel;

import java.io.File;
import java.io.IOException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import service.Common;
import service.Errorist;
import service.IOOperations;

public class DropPanelItem {
	protected DropTarget dropTarget;
	public File folder = null;
	public Button button; 
	
	public void setPath(String new_path) {
		try {
			folder = new File(new_path);
			dropTarget = null;
			if (folder.exists()) {
				dropTarget = new DropTarget(button, DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK); 
				dropTarget.setTransfer(new Transfer[] {FileTransfer.getInstance()/*, TextTransfer.getInstance()*/});
				
				dropTarget.addDropListener(new DropTargetAdapter() {
			        public void drop(DropTargetEvent event) {
			            if (event.data == null) {
			            	event.detail = DND.DROP_NONE;
			    	        return;
			    	    }
			            
			            boolean is_local = event.getSource() != null;
			            for(String file : (String [])event.data) {
			            	File temp = new File(file);
			          	  	try { if (!IOOperations.copyFile(temp, folder)) break;  } 
			          	  	catch (IOException e) {Errorist.printLog(e);}
			          	  	if (is_local)
			          	  		Common.library.ProceedFile(temp);
			            }    
			        }
			    });
			}
		}
		catch(Exception e) { Errorist.printLog(e); }
		
		if (dropTarget == null) {
			Image image = new Image(Display.getCurrent(), 200, 30);
		    GC gc = new GC(image);
		    gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		    gc.fillRectangle(image.getBounds());
		    gc.dispose();
		    button.setBackgroundImage(image);
		} 
		else button.setBackgroundImage(null);
	}
	
	public DropPanelItem(Composite wnd, String text, String path) {
		button = new Button(wnd, SWT.CENTER);
		button.setText(text);
//		button.setSize(20, 20);
		setPath(path);
		
		button.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event arg0) {
				if ( arg0.button == 0) {
					if (folder != null) {
						try { IOOperations.open(folder); } 
						catch (Exception ee) { Errorist.printLog(ee); }
					}
				}
			}
		});
	}
}
