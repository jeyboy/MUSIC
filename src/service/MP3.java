package service;

import java.io.File;

import components.MenuBar;

import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

public class MP3 extends PlaybackListener implements Runnable {
    private AdvancedPlayer player;
    private Thread playerThread;
    AudioDevice audio_device = null;
    boolean played = false;

    public MP3() {}

    public void play(File file) {
    	stop();
        try {
        	if (audio_device != null)
        		if (audio_device.isOpen())
        			audio_device.close();
        	
        	audio_device = javazoom.jl.player.FactoryRegistry.systemRegistry().createAudioDevice();
        	
            this.player = new AdvancedPlayer
            (
                new java.net.URL("file:///" + file.getCanonicalPath()).openStream(),
                audio_device
            );

            this.player.setPlayBackListener(this);
            this.playerThread = new Thread(this, "AudioPlayerThread");
            this.playerThread.start();
        }
        catch (Exception ex) { ex.printStackTrace(); }
    }
    
    public void stop() {
    	try {
    		if (player != null && played)
    			this.player.stop(); 
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
        try { this.player.play(); }
        catch (javazoom.jl.decoder.JavaLayerException ex) { ex.printStackTrace(); }
    }

	public boolean isPlayed() {	return played; }
}