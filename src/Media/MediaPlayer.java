package media;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.Vector;

import service.Common;
import service.Errorist;
import service.Utils;

import jb_player.JBPlayer;
import jb_player.JBPlayerEvent;
import jb_player.JBPlayerException;
import jb_player.JBPlayerListener;

public class MediaPlayer implements JBPlayerListener {
    private JBPlayer player;
    private PlayerPanel panel;
    
    private long duration;


    public MediaPlayer() {
        player = new JBPlayer();
        Vector<String> mixers = player.getMixers();
        if (mixers != null)
//            for(String h : mixers)
//                player.setMixerName(h);
        	player.setMixerName(mixers.get(0));
        	
        player.addJBPlayerListener(this);
    }

    public void play(File file) {
        try {
            player.open(file);
            player.play();
        }
        catch (Exception ex) {
        	Errorist.printLog(ex);
        	Common.tabber.MoveSelectAndInit(true);
        }
    }
    
    public void play(String url) {
        try {
            player.open(new URL(url));
            player.play();
        }
        catch (Exception ex) {
        	Errorist.printLog(ex);
        	Common.tabber.MoveSelectAndInit(true);
        }
    }    
    public void resume() { 
    	try { player.resume(); }
    	catch (JBPlayerException e) { Errorist.printLog(e); } 
    }
    public void pause() { 
    	try { player.pause(); }
    	catch (JBPlayerException e) { Errorist.printLog(e); } 
    }    
    
    public void stop() {
    	try {
    		if (isPlayed())
    			this.player.stop(); 
    	}
    	catch(Exception e) { e.printStackTrace(); }
    }
    public void seek(int percentage) {
    	try { player.seek(percentage); }
    	catch(Exception e) { Errorist.printLog(e);}
    }
    public void exit() { player.exit(); }

	public boolean isPlayed() {	return player.IsPlaying(); }
	public boolean isPaused() {	return player.IsPaused(); }
	
	void InitVolume() {
		panel.blockVolume(player.hasVolumeControl());
		if (player.hasVolumeControl())
			panel.setVolumeRange((int)player.getMinimumVolume(), (int)player.getMaximumVolume());
	}

	public void setPanel(PlayerPanel p) { panel = p;}
	public void setVolume(float volume) { 
		try { player.setVolume(volume);}
		catch (JBPlayerException e) { Errorist.printLog(e); }
	}
	public void opened(Object stream, Map<?, ?> properties) {		  	
	    duration = player.getDuration();
	    panel.unblockTrack(player.isTracking());
		InitVolume();
	}
	public void progress(int progress, long microsecPos) {
		panel.setTrackPosition(progress);
		panel.setTime(Utils.MilliToTime(duration - microsecPos));
	}
	public void stateUpdated(JBPlayerEvent event) {
		switch(event.getCode()) {
			case JBPlayerEvent.PLAYING :
				break;
			case JBPlayerEvent.STOPPED :
				panel.reset();
				break;
			case JBPlayerEvent.PAUSED :
				break;				
			case JBPlayerEvent.EOM :
		        panel.reset();
		        if (Common.raw_flag())
		        	Common.tabber.MoveSelectAndInit(true);				
				break;
			case JBPlayerEvent.GAIN :
				System.out.println("gain - value : " + event.getValue() + " - position : " + event.getPosition());
				break;
			case JBPlayerEvent.PAN :
				System.out.println("pan - value : " + event.getValue() + " - position : " + event.getPosition());
				break;
			case JBPlayerEvent.VOLUME :
				System.out.println("volume - value : " + event.getValue() + " - position : " + event.getPosition());
//				panel.setVolumePosition((int) Math.round(event.getValue() * 100));
				break;
		}
	}
}