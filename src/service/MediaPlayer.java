package service;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Map;

import components.MenuBar;
import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerListener;

public class MediaPlayer implements BasicPlayerListener {
    private BasicPlayer player;

    public MediaPlayer() {
        player = new BasicPlayer();
        List<String> mixers = player.getMixers();
        if (mixers != null)
//            for(String h : mixers)
//                player.setMixerName(h);
        	player.setMixerName(mixers.get(0));
        	
        player.addBasicPlayerListener(this);
    }

    public void play(File file) {
        stop();   	
    	
        try {
            player.open(file);
            player.play();
        }
        catch (Exception ex) { Errorist.printLog(ex); }
    }
    
    public void play(String url) {
        stop();   	
    	
        try {
            player.open(new URL(url));
            player.play();
        }
        catch (Exception ex) { Errorist.printLog(ex); }
    }    
    
    public void stop() {
    	try {
    		if (isPlayed())
    			this.player.stop(); 
    	}
    	catch(Exception e) { e.printStackTrace(); }
    }

	public boolean isPlayed() {	return player.getStatus() == BasicPlayer.PLAYING; }

	public void opened(Object stream, Map properties) {}
	public void progress(int bytesread, long microseconds, byte[] pcmdata, Map properties) {}
	public void setController(BasicController controller) {}
	public void stateUpdated(BasicPlayerEvent event) {
		switch(event.getCode()) {
			case BasicPlayerEvent.PLAYING :
		    	MenuBar.SetPlay();
		        System.out.println("playbackStarted()");				
				break;
			case BasicPlayerEvent.STOPPED :
		        System.out.println("playbackEnded()");
		        
		        if (Common.raw_flag)
		        	Common.tabber.MoveSelectAndInit(true);
		        else MenuBar.SetStop();					
				break;
		}
	}
}