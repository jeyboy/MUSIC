package jb_player;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;
import java.util.Map;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Control;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javazoom.spi.PropertiesContainer;

///////////////Errorist////////////////
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
//////////////////////////////
import org.tritonus.share.sampled.TAudioFormat;
import org.tritonus.share.sampled.file.TAudioFileFormat;

/** Threaded simple player class based on JavaSound API. */
public class JBPlayer implements Runnable {
    public static int EXTERNAL_BUFFER_SIZE = 8192;
    protected Thread m_thread = null;
    
    protected Object m_dataSource;
    protected AudioInputStream m_audioInputStream, m_encodedaudioInputStream;
    
    protected SourceDataLine m_line;
    protected AudioFormat audioFormat;
    
    protected int encodedLength = -1;
    protected FloatControl m_gainControl, m_panControl, m_volumeControl, m_sampleRateControl;
    protected String m_mixerName = null;
    
    private boolean close_player = false;
    private int m_status;
    
    private int lineBufferSize = -1;
    private static Log log = LogFactory.getLog(JBPlayer.class);

    // Listeners to be notified.
    private Collection<JBPlayerListener> m_listeners = null;
    private Map<Object, Object> empty_map = new HashMap<Object, Object>(0);

    /** Constructs a Basic Player. */
    public JBPlayer() {
        m_listeners = new Vector<JBPlayerListener>();       
        (m_thread = new Thread(this, "JBPlayer")).start();
    }
    
    /** Main loop. */
    public void run() {
        int nBytesRead;
        byte[] abData = new byte[EXTERNAL_BUFFER_SIZE];    	
    	
    	while(!close_player)
    		switch(m_status) {
    			case JBPlayerEvent.PLAYING :
    		        synchronized (m_audioInputStream) {
	                    try {
	                        nBytesRead = m_audioInputStream.read(abData, 0, abData.length);
	                        if (nBytesRead >= 0) {
	                        	m_line.write(abData, 0, nBytesRead);
                            
	                            for(JBPlayerListener bpl : m_listeners)
	                            	bpl.progress(getStreamPosition(), m_line.getMicrosecondPosition(), abData, empty_map);
	                        } 
	                        else notifyEvent(JBPlayerEvent.EOM, getStreamPosition(), -1, null);
	                    }
	                    catch (IOException e) {
	                        log.error("Thread cannot run()", e);
	                        notifyEvent(JBPlayerEvent.STOPPED, getStreamPosition(), -1, null);
	                    }
    		        }
    				
    				break;
    			case JBPlayerEvent.STOPPED :
    				CloseLine();	
    				closeStream();
    				break;
    			case JBPlayerEvent.SEEKING : break;    			
    			case JBPlayerEvent.OPENED :
                    if (m_audioInputStream instanceof PropertiesContainer)
                    	empty_map = ((PropertiesContainer) m_audioInputStream).properties();
                    else empty_map.clear();
                    sleep(10);
    			break;
    			
    			case JBPlayerEvent.UNKNOWN : sleep(10); break;
				case JBPlayerEvent.PAUSED :
					PauseLine();
					sleep(10);
					break;
    		}
    }
    
    
    //////////////////////////////////////////////////////////
    // Notify
    //////////////////////////////////////////////////////////          

    
    /** Notify listeners about a JBPlayerEvent.
     * @param code event code.
     * @param position in the stream when the event occurs. */
    protected void notifyEvent(int code, int position, double value, Object description) {
    	if (code < 6) m_status = code;
    	if (code == JBPlayerEvent.RESUMED) m_status = JBPlayerEvent.PLAYING;
    	if (code == JBPlayerEvent.SEEKED) m_status = JBPlayerEvent.OPENED;
    	if (code == JBPlayerEvent.EOM) m_status = JBPlayerEvent.UNKNOWN;
        new JBPlayerEventLauncher(code, position, value, description, new Vector<>(m_listeners), this).start();
    }    
    
    /** Add listener to be notified.
     * @param bpl*/
    public void addJBPlayerListener(JBPlayerListener bpl) 	{ m_listeners.add(bpl); }

    /** Return registered listeners. */
    public Collection<JBPlayerListener> getListeners() 			{ return m_listeners; }

    /** Remove registered listener.
     * @param bpl*/
    public void removeJBPlayerListener(JBPlayerListener bpl) 	{ m_listeners.remove(bpl); }

    
    //////////////////////////////////////////////////////////
    // Attributes
    //////////////////////////////////////////////////////////          
    
    
    /** Set SourceDataLine buffer size. It affects audio latency.
     * (the delay between line.write(data) and real sound).
     * Minimum value should be over 10000 bytes.
     * @param size -1 means maximum buffer size available.*/
    public void setLineBufferSize(int size) { lineBufferSize = size; }

    /** Return SourceDataLine buffer size.
     * @return -1 maximum buffer size.*/
    public int getLineBufferSize() { return lineBufferSize; }
    
    
    //////////////////////////////////////////////////////////
    // Streaming
    //////////////////////////////////////////////////////////    
    
    
    /** Inits Audio ressources from file. */
    protected AudioFileFormat initAudioInputStream(File file) throws UnsupportedAudioFileException, IOException {
        m_audioInputStream = AudioSystem.getAudioInputStream(file);
        return AudioSystem.getAudioFileFormat(file);
    }

    /** Inits Audio ressources from URL. */
    protected AudioFileFormat initAudioInputStream(URL url) throws UnsupportedAudioFileException, IOException {
        m_audioInputStream = AudioSystem.getAudioInputStream(url);
        return AudioSystem.getAudioFileFormat(url);
    }

    /** Inits Audio ressources from InputStream. */
    protected AudioFileFormat initAudioInputStream(InputStream inputStream) throws UnsupportedAudioFileException, IOException {
        m_audioInputStream = AudioSystem.getAudioInputStream(inputStream);
        return AudioSystem.getAudioFileFormat(inputStream);
    }    
    
    /** Inits AudioInputStream and AudioFileFormat from the data source.
     * @throws JBPlayerException */
    protected void initStream() throws JBPlayerException {
        try {
            reset();
            notifyEvent(JBPlayerEvent.OPENING, getStreamPosition(), -1, m_dataSource);
            
            AudioFileFormat m_audioFileFormat;
            if (sourceIsURL())
            	m_audioFileFormat = initAudioInputStream((URL) m_dataSource);
            else if (sourceIsFile())
            	m_audioFileFormat = initAudioInputStream((File) m_dataSource);
            else if (sourceIsInputStream())
            	m_audioFileFormat = initAudioInputStream((InputStream) m_dataSource);
            else throw new JBPlayerException("Unsupported source");

            createLine();
                       
            Map<String, Object> properties = (m_audioFileFormat instanceof TAudioFileFormat) ?
                new HashMap<String, Object>(((TAudioFileFormat) m_audioFileFormat).properties()) : new HashMap<String, Object>();
            
            // Add JavaSound properties.
            if (m_audioFileFormat.getByteLength() > 0) properties.put("audio.length.bytes", new Long(m_audioFileFormat.getByteLength()));
            if (m_audioFileFormat.getFrameLength() > 0) properties.put("audio.length.frames", new Integer(m_audioFileFormat.getFrameLength()));
            if (m_audioFileFormat.getType() != null) properties.put("audio.type", (m_audioFileFormat.getType().toString()));
            // Audio format.
            audioFormat = m_audioFileFormat.getFormat();
            if (GetAFormat().getFrameRate() > 0) properties.put("audio.framerate.fps", new Float(GetAFormat().getFrameRate()));
            if (GetAFormat().getFrameSize() > 0) properties.put("audio.framesize.bytes", new Integer(GetAFormat().getFrameSize()));
            if (GetAFormat().getSampleRate() > 0) properties.put("audio.samplerate.hz", new Float(GetAFormat().getSampleRate()));
            if (GetAFormat().getSampleSizeInBits() > 0) properties.put("audio.samplesize.bits", new Integer(GetAFormat().getSampleSizeInBits()));
            if (GetAFormat().getChannels() > 0) properties.put("audio.channels", new Integer(GetAFormat().getChannels()));
//            properties.put("JBPlayer.sourcedataline", m_line);
            
            // Tritonus SPI compliant audio format.
            if (GetAFormat() instanceof TAudioFormat)
                properties.putAll(((TAudioFormat)GetAFormat()).properties());
            
            for(JBPlayerListener listener : m_listeners)
            	listener.opened(m_dataSource, properties);
            
            notifyEvent(JBPlayerEvent.OPENED, getStreamPosition(), -1, null);
        }
        catch (Exception e) 		{ throw new JBPlayerException(e); }
    }

    protected void closeStream() {
        try {
      		m_audioInputStream.close();
        	m_status = JBPlayerEvent.UNKNOWN;
            log.info("Stream closed");
        }
        catch (IOException e) { log.info("Cannot close stream", e); }
    }
    
    /** return position in range 0..1000*/
    protected int getStreamPosition() {
        if (sourceIsFile()) {
            try {
                if (m_encodedaudioInputStream != null)
                    return encodedLength - m_encodedaudioInputStream.available();
            }
            catch (IOException e) { }
        }
        return 0;    	
    }    
    
    /** Skip bytes in the File inputstream.
     * It will skip N frames matching to bytes, so it will never skip given bytes length exactly.
     * @param bytes
     * @return value>0 for File and value=0 for URL and InputStream
     * @throws JBPlayerException */
    protected long skipBytes(long bytes) throws Exception {
    	bytes -= getStreamPosition();
        long totalSkipped = 0;
        if (sourceIsFile()) {
            log.info("Bytes to skip : " + bytes);
            int previousStatus = m_status;
            long skipped = 0;
            try {
            	pausePlayback();
//                synchronized (m_audioInputStream) {
                    notifyEvent(JBPlayerEvent.SEEKING, getStreamPosition(), -1, null);
                    if (m_audioInputStream != null) {
                        while (true) {
                            skipped = m_audioInputStream.skip(bytes - totalSkipped);
                            if (skipped == 0) break;
                            totalSkipped = totalSkipped + skipped;
                            if (totalSkipped == -1) throw new JBPlayerException(JBPlayerException.SKIPNOTSUPPORTED);
                        }
                    }
//                }
                
                notifyEvent(JBPlayerEvent.SEEKED, getStreamPosition(), -1, null);
                
                startPlayback();
                if (previousStatus == JBPlayerEvent.PAUSED)
                    pausePlayback();
            }
            catch (IOException e) { throw new JBPlayerException(e); }
        }
        return totalSkipped;
    }
    
    
    //////////////////////////////////////////////////////////
    // DataLine
    //////////////////////////////////////////////////////////    
    
    /** Pause audio resources. */    
    protected void PauseLine() {
        if (m_line != null) {
        	m_line.flush();
            m_line.stop();
        }    		    	
    }    
    
    /** Free audio resources. */    
    protected void CloseLine() {
        if (m_line != null) {
        	if (m_line.isOpen()) {
//	            m_line.drain();        		
        		PauseLine();
	            m_line.close();
        	}
            m_line = null;
        }    		    	
    }

    /** Inits a DateLine.<br>
     *
     * We check if the line supports Gain and Pan controls.
     *
     * From the AudioInputStream, i.e. from the sound file, we
     * fetch information about the format of the audio data. These
     * information include the sampling frequency, the number of
     * channels and the size of the samples. There information
     * are needed to ask JavaSound for a suitable output line
     * for this audio file.
     * Furthermore, we have to give JavaSound a hint about how
     * big the internal buffer for the line should be. Here,
     * we say AudioSystem.NOT_SPECIFIED, signaling that we don't
     * care about the exact size. JavaSound will use some default
     * value for the buffer size. */
    protected void createLine() throws LineUnavailableException {
        if (m_line == null)
        {
            AudioFormat sourceFormat = m_audioInputStream.getFormat();
            int nSampleSizeInBits = sourceFormat.getSampleSizeInBits();
            if (nSampleSizeInBits <= 0 || 
            		(sourceFormat.getEncoding() == AudioFormat.Encoding.ULAW) || 
            		(sourceFormat.getEncoding() == AudioFormat.Encoding.ALAW)) 
            	nSampleSizeInBits = 16;
            if (nSampleSizeInBits != 8) nSampleSizeInBits = 16;            
            
            AudioFormat targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sourceFormat.getSampleRate(), nSampleSizeInBits, sourceFormat.getChannels(), sourceFormat.getChannels() * (nSampleSizeInBits / 8), sourceFormat.getSampleRate(), false);
            
            m_encodedaudioInputStream = m_audioInputStream;
            // Get total length in bytes of the encoded stream.
            try { encodedLength = m_encodedaudioInputStream.available(); }
            catch (IOException e) { log.error("Cannot get encoded length", e); }
            
            // Create decoded stream.
            m_audioInputStream = AudioSystem.getAudioInputStream(targetFormat, m_audioInputStream);
            AudioFormat audioFormat = m_audioInputStream.getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat, AudioSystem.NOT_SPECIFIED);
            
            Mixer mixer = getMixer(m_mixerName);
            if (mixer != null) {
                log.info("Mixer : "+mixer.getMixerInfo().toString());
                m_line = (SourceDataLine) mixer.getLine(info);
            } else {
                m_line = (SourceDataLine) AudioSystem.getLine(info);
                m_mixerName = null;
            }
            
            m_line.open(audioFormat, m_line.getBufferSize());
            InitControls();
        }
    }

    /** Opens the line. */
    protected void InitControls() throws LineUnavailableException {
    	if (m_line == null) return;
               
        if (m_line.isControlSupported(FloatControl.Type.MASTER_GAIN))
            m_gainControl = (FloatControl) m_line.getControl(FloatControl.Type.MASTER_GAIN);

        if (m_line.isControlSupported(FloatControl.Type.PAN))
            m_panControl = (FloatControl) m_line.getControl(FloatControl.Type.PAN);
        
        if (m_line.isControlSupported(FloatControl.Type.VOLUME))
            m_volumeControl = (FloatControl) m_line.getControl(FloatControl.Type.VOLUME);

        if (m_line.isControlSupported(FloatControl.Type.SAMPLE_RATE))
            m_sampleRateControl = (FloatControl) m_line.getControl(FloatControl.Type.SAMPLE_RATE);        
        
        /*-- Display supported controls --*/
        for (Control c : m_line.getControls())
            log.debug("Controls : " + c.toString());       
    }
       
    
    //////////////////////////////////////////////////////////
    // Playback controls
    //////////////////////////////////////////////////////////    
    
    
    /** Stops the playback. */
    protected void stopPlayback() {
        notifyEvent(JBPlayerEvent.STOPPED, getStreamPosition(), -1, null);
        log.info("stopPlayback() completed");
    }

    /** Pauses the playback. */
    protected void pausePlayback() {
        if (m_line != null)
            if (IsPlaying()) {
                log.info("pausePlayback() completed");
                notifyEvent(JBPlayerEvent.PAUSED, getStreamPosition(), -1, null);
            }
    }

    /** Resumes the playback. */
    protected void resumePlayback() {
        if (m_line != null)
            if (IsPaused()) {
                m_line.start();
                log.info("resumePlayback() completed");
                notifyEvent(JBPlayerEvent.RESUMED, getStreamPosition(), -1, null);
            }
    }

    /** Starts playback. */
    protected void startPlayback() throws JBPlayerException {
    	switch(m_status) {
    		case JBPlayerEvent.STOPPED:	initStream(); break;
    		case JBPlayerEvent.OPENED:
                m_line.start();
                notifyEvent(JBPlayerEvent.PLAYING, getStreamPosition(), -1, null);   			
    	}
    }    
    
    
    //////////////////////////////////////////////////////////
    // Controls
    //////////////////////////////////////////////////////////    
    
    
    public boolean hasGainControl() { return m_gainControl != null; }
    
    /** Returns Gain value. */
    public float getGain() { return hasGainControl() ? m_gainControl.getValue() : 0f; }

    /** Sets Gain value.
     * Line should be opened before calling this method.
     * Linear scale 0.0  <-->  1.0
     * Threshold Coef. : 1/2 to avoid saturation. */
    public void setGain(double fGain) throws JBPlayerException {
        if (hasGainControl()) {
            double minGainDB = getMinimumGain();
            double ampGainDB = ((10.0f / 20.0f) * getMaximumGain()) - getMinimumGain();
            double cste = Math.log(10.0) / 20;
            double valueDB = minGainDB + (1 / cste) * Math.log(1 + (Math.exp(cste * ampGainDB) - 1) * fGain);
            m_gainControl.setValue((float) valueDB);
            notifyEvent(JBPlayerEvent.GAIN, getStreamPosition(), fGain, null);
        }
        else throw new JBPlayerException(JBPlayerException.GAINCONTROLNOTSUPPORTED);
    }
    
    /** Returns true if Gain control is supported. */
    public boolean hasVolumeControl() { return m_volumeControl != null; }    
    
    /** Returns Volume value. */
    public float getVolume() { return hasVolumeControl() ? m_volumeControl.getValue() : 0f; }

    /** Sets Volume value. */
    public void setVolume(double fVolume) throws JBPlayerException {
        if (hasVolumeControl()) {
            m_volumeControl.setValue((float) fVolume);
            notifyEvent(JBPlayerEvent.VOLUME, getStreamPosition(), fVolume, null);
        }
    }
    
    /** Gets max Volume value. */
    public float getMaximumVolume() { return hasVolumeControl() ? m_volumeControl.getMaximum() : 0f; }

    /** Gets min Volume value. */
    public float getMinimumVolume() { return hasVolumeControl() ? m_volumeControl.getMinimum() : 0f; }    
    
    /** Gets max Gain value. */
    public float getMaximumGain() { return hasGainControl() ? m_gainControl.getMaximum() : 0f; }

    /** Gets min Gain value. */
    public float getMinimumGain() { return hasGainControl() ? m_gainControl.getMinimum() : 0f; }

    /** Returns true if Pan control is supported. */
    public boolean hasPanControl() { return m_panControl != null; }

    /** Returns Pan precision. */
    public float getPanPrecision() { return hasPanControl() ? m_panControl.getPrecision() : 0f; }

    /** Returns Pan value. */
    public float getPan() { return hasPanControl() ? m_panControl.getValue() : 0f; }
    
    /** Sets Pan value.
     * Line should be opened before calling this method.
     * Linear scale : -1.0 <--> +1.0 */
    public void setPan(double fPan) throws JBPlayerException {
        if (hasPanControl()) {
            log.debug("Pan : " + fPan);
            m_panControl.setValue((float)fPan);
            notifyEvent(JBPlayerEvent.PAN, getStreamPosition(), fPan, null);
        }
        else throw new JBPlayerException(JBPlayerException.PANCONTROLNOTSUPPORTED);
    }
    
    
    //////////////////////////////////////////////////////////
    // PlayerControls
    //////////////////////////////////////////////////////////      
    
    
    private void openObject(Object o) throws JBPlayerException {
        if (o != null) {
        	log.info("open(" + o + ")");
            m_dataSource = o;
            initStream();
        }    	
    }
    
    /** Open file to play. */
    public void open(File file) throws JBPlayerException { openObject(file); }

    /** Open URL to play. */
    public void open(URL url) throws JBPlayerException { openObject(url); }

    /** Open inputstream to play. */
    public void open(InputStream inputStream) throws JBPlayerException { openObject(inputStream); }    

    /** @throws Exception 
     * @see javazoom.jlgui.JBPlayer.BasicController#seek(long) */
    public long seek(long bytes) throws Exception { return skipBytes(bytes); }

    /** @see javazoom.jlgui.JBPlayer.BasicController#play() */
    public void play() throws JBPlayerException { startPlayback(); }

    /** @see javazoom.jlgui.JBPlayer.BasicController#stop() */
    public void stop() throws JBPlayerException { stopPlayback(); }

    /** @see javazoom.jlgui.JBPlayer.BasicController#pause() */
    public void pause() throws JBPlayerException { pausePlayback(); }

    /** @see javazoom.jlgui.JBPlayer.BasicController#resume() */
    public void resume() throws JBPlayerException { resumePlayback(); }
    
    public void exit() { close_player = true; }
   
    
    //////////////////////////////////////////////////////////
    // Helpers
    //////////////////////////////////////////////////////////      
    
    
//    protected void initCadrLength() {
//    	log.info("**** cadr length ****");
//    	if (GetAFormat().getFrameSize() > 0 && GetAFormat().getFrameRate() > 0)
//    		EXTERNAL_BUFFER_SIZE = Math.round(GetAFormat().getFrameRate()) * GetAFormat().getFrameSize();
//    	else EXTERNAL_BUFFER_SIZE = 524288; // 128Kb //16000;
//    	log.info("**** cadr length ****");
//    }
       
    private void sleep(long millis) {
        try { Thread.sleep(millis); }
        catch (InterruptedException e) { log.error("Thread cannot sleep(1000)", e); }     	
    }    
    
    protected AudioFormat GetAFormat() { return audioFormat;}
    
    protected void reset() {
    	m_status = JBPlayerEvent.UNKNOWN;
        if (m_audioInputStream != null)
            synchronized (m_audioInputStream) { closeStream(); }    	
    	CloseLine();

    	encodedLength = 0;
        m_audioInputStream = m_encodedaudioInputStream = null;
        m_sampleRateControl = m_volumeControl = m_gainControl = m_panControl = null;
    }    
    
    public Vector<String> getMixers() {
        Vector<String> mixers = new Vector<String>();
        Line.Info lineInfo = new Line.Info(SourceDataLine.class);
        
        for(Mixer.Info m_info : AudioSystem.getMixerInfo()) {
            Mixer mixer = AudioSystem.getMixer(m_info);
            if (mixer.isLineSupported(lineInfo))
                mixers.add(m_info.getName());       	
        }        

        return mixers;        
    }
    
    public Mixer getMixer(String name) {
        if (name != null) {
        	for(Mixer.Info m_info :  AudioSystem.getMixerInfo())
                if (m_info.getName().equals(name))
                    return AudioSystem.getMixer(m_info);           
        }
        return null;
    }
    
    public String getMixerName() 			{ return m_mixerName; }
    public void setMixerName(String name) 	{ m_mixerName = name; }
    
    public boolean sourceIsFile() 			{ return m_dataSource instanceof File;}
    public boolean sourceIsURL() 			{ return m_dataSource instanceof URL;}
    public boolean sourceIsInputStream() 	{ return m_dataSource instanceof InputStream;}   
    
    /** Returns JBPlayer status. */
    public int getStatus() { return m_status; }    
    public boolean IsPlaying()	{ return m_status == JBPlayerEvent.PLAYING;}
    public boolean IsPaused()	{ return m_status == JBPlayerEvent.PAUSED;} 
}