package components;

import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSlider;

import service.Common;
import service.Errorist;

public class PlayerPanel extends JPanel {
	private static final long serialVersionUID = -9051505855046492589L;
	
	Label time;
	JSlider track, volume;

	public void GUI() {
    	setBackground(Common.color_background);
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
//		track.setExtent(extent)
		track.setPaintTicks(false);
		track.setPaintLabels(false);
	}
	
    public PlayerPanel() {
		new DropTarget(this, new DropTargetListener() {
			public void dropActionChanged(DropTargetDragEvent dtde) {}
			public void drop(DropTargetDropEvent evt) {
			      int action = evt.getDropAction();
			      evt.acceptDrop(action);
			      try {
			          Transferable data = evt.getTransferable();
			          if (data.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
			              List<File> files = (List<File>) data.getTransferData(DataFlavor.javaFileListFlavor);
			              for(File file:files) {
			            	  if (!evt.isLocalTransfer())
			            	  {
			            		  //parse droped and add to active list
			            	  }
			              }
			          }
			          evt.dropComplete(true);
			      }
			      catch (Exception e) { evt.dropComplete(false); Errorist.printLog(e); }
			}
			public void dragOver(DropTargetDragEvent dtde) {}
			public void dragExit(DropTargetEvent dte) {}
			public void dragEnter(DropTargetDragEvent dtde) {}
		});
				
		this.add((time = new Label("00:00", 10, 0)));
		this.add(new Label("Progress", 20, 4));
		this.add((track = new JSlider(0, 1000)));
		this.add(new Label("Volume", 20, 4));
		this.add((volume = new JSlider(0, 100)));
		volume.setPreferredSize(new Dimension(40, 20));
		GUI();
		Common.player.setPanel(this);
    }
    
    public void setTrackMax(int length) { track.setMaximum(length); }
    public void setTrackPosition(int curr_pos) { track.setValue(curr_pos); }
    
    public void setVolumePosition(int curr_pos) { volume.setValue(curr_pos); }
    
    public void setTime(String current_time) { time.setText(current_time); }    
}