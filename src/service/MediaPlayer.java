package service;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Map;

import components.PlayerPanel;
import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import javazoom.jlgui.basicplayer.BasicPlayerListener;

public class MediaPlayer implements BasicPlayerListener {
    private BasicPlayer player;
    private PlayerPanel panel;
    
    private int byteLength;
    
    private long duration;
//    private int frameCount;    
//    private int frameSize;

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
    	catch (BasicPlayerException e) { Errorist.printLog(e); } 
    }
    public void pause() { 
    	try { player.pause(); }
    	catch (BasicPlayerException e) { Errorist.printLog(e); } 
    }    
    
    public void stop() {
    	try {
    		if (isPlayed())
    			this.player.stop(); 
    	}
    	catch(Exception e) { e.printStackTrace(); }
    }

	public boolean isPlayed() {	return player.getStatus() == BasicPlayer.PLAYING; }
	public boolean isPaused() {	return player.getStatus() == BasicPlayer.PAUSED; }

	public void setPanel(PlayerPanel p) { panel = p;}
	public void opened(Object stream, Map properties) {
		if (properties.containsKey("audio.length.bytes"))
			byteLength = (int)properties.get("audio.length.bytes");
		else
			byteLength = 0;	
		
		
//		System.out.println("properties : ");
//	    for (Object me : properties.entrySet()) {
//	        System.out.print("\t" + me);    	
//	    }
		
//		properties.get("audio.framerate.fps");
//		properties.get("audio.samplerate.hz");
//		properties.get("audio.framesize.bytes");
//		properties.get("audio.length.bytes");
//		properties.get("audio.channels");
//		properties.get("audio.length.bytes");
//		properties.get("mp3.length.bytes");
//		properties.get("mp3.frequency.hz");
//		properties.get("vbr");
//		properties.get("bitrate");
//		properties.get("author");
		
		if (properties.containsKey("duration")) {
			Object raw_duration = properties.get("duration");
			if (raw_duration instanceof Long)
				duration = (long) properties.get("duration");
			else
				duration = (int) properties.get("duration");
		}
		else
			duration = Math.round((float)(int)properties.get("audio.length.frames")/(float)properties.get("audio.framerate.fps") * 1000000);
//		
//		System.out.println("duration : " + duration);
		panel.setTrackMax(byteLength);
	}
	public void progress(int bytesread, long microseconds, byte[] pcmdata, Map properties) {
		panel.setTrackPosition(bytesread);
		panel.setTime(Utils.MilliToTime(duration - microseconds));
	}
	public void setController(BasicController controller) {}
	public void stateUpdated(BasicPlayerEvent event) {
		switch(event.getCode()) {
			case BasicPlayerEvent.PLAYING :
		        System.out.println("playbackStarted()");				
				break;
			case BasicPlayerEvent.STOPPED :
		        System.out.println("playbackEnded()");
		        panel.setTrackPosition(0);
		        if (Common.raw_flag)
		        	Common.tabber.MoveSelectAndInit(true);				
				break;
			case BasicPlayerEvent.GAIN :
				panel.setVolumePosition(Math.round(player.getGainValue() * 100));
				break;
			case BasicPlayerEvent.PAN :
				System.out.println("pan - value : " + event.getValue() + " - position : " + event.getPosition());
				break;
		}
	}
}

//protected void processSeek(double rate)
//{
//    try
//    {
//        if ((audioInfo != null) && (audioInfo.containsKey("audio.type")))
//        {
//            String type = (String) audioInfo.get("audio.type");
//            // Seek support for MP3.
//            if ((type.equalsIgnoreCase("mp3")) && (audioInfo.containsKey("audio.length.bytes")))
//            {
//                long skipBytes = (long) Math.round(((Integer) audioInfo.get("audio.length.bytes")).intValue() * rate);
//                log.debug("Seek value (MP3) : " + skipBytes);
//                theSoundPlayer.seek(skipBytes);
//            }
//            // Seek support for WAV.
//            else if ((type.equalsIgnoreCase("wave")) && (audioInfo.containsKey("audio.length.bytes")))
//            {
//                long skipBytes = (long) Math.round(((Integer) audioInfo.get("audio.length.bytes")).intValue() * rate);
//                log.debug("Seek value (WAVE) : " + skipBytes);
//                theSoundPlayer.seek(skipBytes);
//            }
//            else posValueJump = false;
//        }
//        else posValueJump = false;
//    }
//    catch (BasicPlayerException ioe)
//    {
//        log.error("Cannot skip", ioe);
//        posValueJump = false;
//    }
//}