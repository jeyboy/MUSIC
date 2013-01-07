package components;

import java.awt.Dimension;
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

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSlider;

import service.Common;
import service.Utils;
import service.Errorist;

public class PlayerPanel extends JPanel implements ActionObserver {
	private static final long serialVersionUID = -9051505855046492589L;
	
	Label time;
	JSlider track, volume;
	ToggleButton pause;

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
		
		this.add(new ToggleButton(Utils.GetIcon("player/play.png"),
				Utils.GetIcon("player/stop.png"),
				this, ActionObserver.STOP, ActionObserver.PLAY
				));
		
		pause = new ToggleButton(Utils.GetIcon("player/pause.png"),
				Utils.GetIcon("player/play.png"),
				this, ActionObserver.PLAY, ActionObserver.PAUSE);
		pause.setVisible(false);
		this.add(pause);
		
		RoundButton curr = new RoundButton(Utils.GetIcon("player/shuffle.png"));
		curr.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent arg0) { Common.tabber.GetCurrentTab().Shuffle(); } });
		this.add(curr);
		
		this.add((time = new Label("00:00", 10, 0)));
		this.add(new Label("", 20, 4));
		this.add((track = new JSlider(0, 1000)));
		track.setUI(new SliderUI(track));
		this.add(new Label("Volume", 20, 4));
		this.add((volume = new JSlider(0, 100)));
		volume.setUI(new SliderUI(volume));
		volume.setPreferredSize(new Dimension(60, 20));
		volume.setMaximumSize(new Dimension(120, 20));
		GUI();
		Common.player.setPanel(this);
    }
    
    public void setTrackMax(int length) { track.setMaximum(length); }
    public void setTrackPosition(int curr_pos) { track.setValue(curr_pos); }
    
    public void setVolumePosition(int curr_pos) { volume.setValue(curr_pos); }
    
    public void setTime(String current_time) { time.setText(current_time); }

	@Override
	public void notify(int state) {
		switch(state) {
			case ActionObserver.PLAY:
				if (Common.player.isPaused()) 
					Common.player.resume();
				else {
					Common.raw_flag = true;
					Common.tabber.MoveSelectAndInit(true);
				}
				break;
			case ActionObserver.STOP:
				Common.raw_flag = false;
				Common.player.stop();				
				break;
			case ActionObserver.PAUSE:
				Common.player.pause();
				break;			
		}
		
		pause.setVisible(Common.raw_flag);
	}    
}