package service;

import java.io.File;
import java.util.List;

import components.MenuBar;

import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;
import javazoom.jlgui.basicplayer.BasicPlayer;

public class MP3 extends PlaybackListener implements Runnable {
    private BasicPlayer player;
    AudioDevice audio_device = null;
    boolean played = false;

    public MP3() {
        player = new BasicPlayer();
        List<String> mixers = player.getMixers();
        if (mixers != null)
//            for(String h : mixers)
//                player.setMixerName(h);
        	player.setMixerName(mixers.get(0));
        	
//        // Register the front-end to low-level player events.
//        bplayer.addBasicPlayerListener(mp);
//        // Adds controls for front-end to low-level player.
//        mp.setController(bplayer);    	
    }

    public void play(File file) {
        stop();   	
    	
        try {
            player.open(file);
//            else
//            {
//                player.open(new URL(currentFileOrURL));
//            }
            player.play();
            playbackStarted(null);
        }
        catch (Exception ex) { Errorist.printLog(ex); }
    }
    
    public void stop() {
    	try {
    		if (player != null && played)
    			this.player.stop(); 
//    		playbackFinished(null);
    		played = false;
    	}
    	catch(Exception e) { e.printStackTrace(); }
    }

    // PlaybackListener members

    public void playbackStarted(PlaybackEvent playbackEvent) {
    	played = true;
    	MenuBar.SetPlay();
        System.out.println("playbackStarted()");
    }

    public void playbackFinished(PlaybackEvent playbackEvent) {
    	played = false;
        System.out.println("playbackEnded()");
        
        if (Common.raw_flag)
        	Common.tabber.MoveSelectAndInit(true);
        else MenuBar.SetStop();	
    }    

    // Runnable members

    public void run() {
//        try { this.player.play(); }
//        catch (javazoom.jl.decoder.JavaLayerException ex) { ex.printStackTrace(); }
    }

	public boolean isPlayed() {	return played; }
}