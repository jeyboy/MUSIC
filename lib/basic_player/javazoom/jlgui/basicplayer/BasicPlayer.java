/*
 * BasicPlayer.
 */
package javazoom.jlgui.basicplayer;

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

/**
 * BasicPlayer is a threaded simple player class based on JavaSound API.
 * It has been successfully tested under J2SE 1.3.x, 1.4.x and 1.5.x.
 */
public class BasicPlayer implements BasicController, Runnable
{
    public static int EXTERNAL_BUFFER_SIZE = 16000;
    public static int SKIP_INACCURACY_SIZE = 1200;
    protected Thread m_thread = null;
    protected Object m_dataSource;
    protected AudioInputStream m_encodedaudioInputStream;
    protected int encodedLength = -1;
    protected AudioInputStream m_audioInputStream;
    protected SourceDataLine m_line;
    protected FloatControl m_gainControl;
    protected FloatControl m_panControl;
    protected String m_mixerName = null;
    protected AudioFormat audioFormat;
    private int m_lineCurrentBufferSize = -1;
    private int lineBufferSize = -1;
    private long threadSleep = -1;
    private static Log log = LogFactory.getLog(BasicPlayer.class);
    /**
     * These variables are used to distinguish stopped, paused, playing states.
     * We need them to control Thread.
     */
    private int m_status = BasicPlayerEvent.UNKNOWN;
    // Listeners to be notified.
    private Collection<BasicPlayerListener> m_listeners = null;
    private Map<Object, Object> empty_map = new HashMap<Object, Object>(0);

    /**
     * Constructs a Basic Player.
     */
    public BasicPlayer() {
        m_dataSource = null;
        m_listeners = new Vector<BasicPlayerListener>();
        reset();
    }

    protected AudioFormat GetAFormat() { return audioFormat;}
    
    protected void reset() {
        m_status = BasicPlayerEvent.UNKNOWN;
        if (m_audioInputStream != null) {
            synchronized (m_audioInputStream){
                closeStream();
            }
        }
        
        m_audioInputStream = null;
        m_encodedaudioInputStream = null;
        encodedLength = -1;
        if (m_line != null) {
        	if (m_line.isOpen()) {
	            m_line.stop();
	            m_line.close();
        	}
            m_line = null;
        }
        m_gainControl = null;
        m_panControl = null;
    }

    /**
     * Notify listeners about a BasicPlayerEvent.
     * @param code event code.
     * @param position in the stream when the event occurs.
     */
    protected void notifyEvent(int code, int position, double value, Object description) {
    	if (code < 6) m_status = code;
    	if (code == BasicPlayerEvent.RESUMED) m_status = BasicPlayerEvent.PLAYING;
    	if (code == BasicPlayerEvent.SEEKED) m_status = BasicPlayerEvent.OPENED;
        new BasicPlayerEventLauncher(code, position, value, description, new Vector<>(m_listeners), this).start();
    }    
    
    /**
     * Add listener to be notified.
     * @param bpl
     */
    public void addBasicPlayerListener(BasicPlayerListener bpl) { m_listeners.add(bpl); }

    /**
     * Return registered listeners.
     * @return
     */
    public Collection<BasicPlayerListener> getListeners() { return m_listeners; }

    /**
     * Remove registered listener.
     * @param bpl
     */
    public void removeBasicPlayerListener(BasicPlayerListener bpl) { m_listeners.remove(bpl); }

    /**
     * Set SourceDataLine buffer size. It affects audio latency.
     * (the delay between line.write(data) and real sound).
     * Minimum value should be over 10000 bytes.
     * @param size -1 means maximum buffer size available.
     */
    public void setLineBufferSize(int size) { lineBufferSize = size; }

    /**
     * Return SourceDataLine buffer size.
     * @return -1 maximum buffer size.
     */
    public int getLineBufferSize() { return lineBufferSize; }
    
    /**
     * Return SourceDataLine current buffer size.
     * @return
     */
    public int getLineCurrentBufferSize() { return m_lineCurrentBufferSize; }

    /**
     * Set thread sleep time.
     * Default is -1 (no sleep time).
     * @param time in milliseconds.
     */
    public void setSleepTime(long time) { threadSleep = time; }

    /**
     * Return thread sleep time in milliseconds.
     * @return -1 means no sleep time.
     */
    public long getSleepTime() { return threadSleep; }
   
    private void openObject(Object o) throws BasicPlayerException {
        if (o != null) {
            m_dataSource = o;
            initAudioInputStream();
        }    	
    }
    
    /**
     * Open file to play.
     */
    public void open(File file) throws BasicPlayerException {
        log.info("open(" + file + ")");
        openObject(file);
    }

    /**
     * Open URL to play.
     */
    public void open(URL url) throws BasicPlayerException {
        log.info("open(" + url + ")");
        openObject(url);
    }

    /**
     * Open inputstream to play.
     */
    public void open(InputStream inputStream) throws BasicPlayerException {
        log.info("open(" + inputStream + ")");
        openObject(inputStream);
    }

    /**
     * Inits AudioInputStream and AudioFileFormat from the data source.
     * @throws BasicPlayerException
     */
    protected void initAudioInputStream() throws BasicPlayerException {
        try {
            reset();
            notifyEvent(BasicPlayerEvent.OPENING, getEncodedStreamPosition(), -1, m_dataSource);
            
            AudioFileFormat m_audioFileFormat;
            if (sourceIsURL())
            	m_audioFileFormat = initAudioInputStream((URL) m_dataSource);
            else if (sourceIsFile())
            	m_audioFileFormat = initAudioInputStream((File) m_dataSource);
            else if (sourceIsInputStream())
            	m_audioFileFormat = initAudioInputStream((InputStream) m_dataSource);
            else throw new BasicPlayerException("Unsupported source");

            createLine();
            // Notify listeners with AudioFileFormat properties.
                       
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
            properties.put("basicplayer.sourcedataline", m_line);
            
            // Tritonus SPI compliant audio format.
            if (GetAFormat() instanceof TAudioFormat)
                properties.putAll(((TAudioFormat)GetAFormat()).properties());
            
            for(BasicPlayerListener listener : m_listeners)
            	listener.opened(m_dataSource, properties);
            
            notifyEvent(BasicPlayerEvent.OPENED, getEncodedStreamPosition(), -1, null);
        }
        catch (LineUnavailableException e) 		{ throw new BasicPlayerException(e); }
        catch (UnsupportedAudioFileException e) { throw new BasicPlayerException(e); }
        catch (IOException e) 					{ throw new BasicPlayerException(e); }
    }

    /**
     * Inits Audio ressources from file.
     */
    protected AudioFileFormat initAudioInputStream(File file) throws UnsupportedAudioFileException, IOException {
        m_audioInputStream = AudioSystem.getAudioInputStream(file);
        return AudioSystem.getAudioFileFormat(file);
    }

    /**
     * Inits Audio ressources from URL.
     */
    protected AudioFileFormat initAudioInputStream(URL url) throws UnsupportedAudioFileException, IOException {
        m_audioInputStream = AudioSystem.getAudioInputStream(url);
        return AudioSystem.getAudioFileFormat(url);
    }

    /**
     * Inits Audio ressources from InputStream.
     */
    protected AudioFileFormat initAudioInputStream(InputStream inputStream) throws UnsupportedAudioFileException, IOException {
        m_audioInputStream = AudioSystem.getAudioInputStream(inputStream);
        return AudioSystem.getAudioFileFormat(inputStream);
    }

    /**
     * Inits Audio ressources from AudioSystem.<br>
     * @throws BasicPlayerException 
     */
    protected void initLine() throws BasicPlayerException {
        log.info("initLine()");
        
        try {
	        if (m_line == null) createLine();
	        if (!m_line.isOpen())
	            openLine();
	        else {
	            AudioFormat lineAudioFormat = m_line.getFormat();
	            AudioFormat audioInputStreamFormat = m_audioInputStream == null ? null : m_audioInputStream.getFormat();
	            if (!lineAudioFormat.equals(audioInputStreamFormat)) {
	            	log.info("!!!!!!!!!!!!!!!!!!!!!!!!!!Formats not compatible");
	                m_line.close();
	                openLine();
	            }
	        }
        }
        catch (LineUnavailableException e) { throw new BasicPlayerException(BasicPlayerException.CANNOTINITLINE, e); }
    }

    /**
     * Inits a DateLine.<br>
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
     * value for the buffer size.
     */
    protected void createLine() throws LineUnavailableException {
        log.info("Create Line");
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
            
            // Keep a reference on encoded stream to progress notification.
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
            
            log.info("Create Line : Source format : " + sourceFormat.toString());
            log.info("Create Line : Target format: " + targetFormat);
            log.info("Line : " + m_line.toString());
            log.debug("Line Info : " + m_line.getLineInfo().toString());
            log.debug("Line AudioFormat: " + m_line.getFormat().toString());
        }
    }

    /**
     * Opens the line.
     */
    protected void openLine() throws LineUnavailableException {
    	if (m_line == null) return;

        AudioFormat audioFormat = m_audioInputStream.getFormat();
        m_lineCurrentBufferSize = lineBufferSize <= 0 ? m_line.getBufferSize() : lineBufferSize;
        
        m_line.open(audioFormat, m_lineCurrentBufferSize);
               
        /*-- Is Gain Control supported ? --*/
        if (m_line.isControlSupported(FloatControl.Type.MASTER_GAIN))
            m_gainControl = (FloatControl) m_line.getControl(FloatControl.Type.MASTER_GAIN);

        /*-- Is Pan control supported ? --*/
        if (m_line.isControlSupported(FloatControl.Type.PAN))
            m_panControl = (FloatControl) m_line.getControl(FloatControl.Type.PAN);
        
        log.info("Open Line : BufferSize=" + m_lineCurrentBufferSize);
        /*-- Display supported controls --*/
        for (Control c : m_line.getControls())
            log.debug("Controls : " + c.toString());       
    }

    /**
     * Stops the playback.<br>
     *
     * Player Status = STOPPED.<br>
     * Thread should free Audio ressources.
     */
    protected void stopPlayback() {
        if (IsPlaying() || IsPaused()) {
            if (m_line != null) {
                m_line.flush();
                m_line.stop();
            }
            notifyEvent(BasicPlayerEvent.STOPPED, getEncodedStreamPosition(), -1, null);
            synchronized (m_audioInputStream) { closeStream(); }
            log.info("stopPlayback() completed");
        }
    }

    /**
     * Pauses the playback.<br>
     *
     * Player Status = PAUSED.
     */
    protected void pausePlayback() {
        if (m_line != null) {
            if (IsPlaying()) {
                m_line.flush();
                m_line.stop();
                log.info("pausePlayback() completed");
                notifyEvent(BasicPlayerEvent.PAUSED, getEncodedStreamPosition(), -1, null);
            }
        }
    }

    /**
     * Resumes the playback.<br>
     *
     * Player Status = PLAYING.
     */
    protected void resumePlayback() {
        if (m_line != null) {
            if (IsPaused()) {
                m_line.start();
                log.info("resumePlayback() completed");
                notifyEvent(BasicPlayerEvent.RESUMED, getEncodedStreamPosition(), -1, null);
            }
        }
    }

    /**
     * Starts playback.
     */
    protected void startPlayback() throws BasicPlayerException {
    	switch(m_status) {
    		case BasicPlayerEvent.STOPPED: initAudioInputStream(); break;
    		case BasicPlayerEvent.OPENED:
                log.info("startPlayback called");
                if (!(m_thread == null || !m_thread.isAlive())) {
                    log.info("WARNING: old thread still running!!");
                    int cnt = 0;
                    while (!IsOpened()) {
                        try {
                            if (m_thread != null) {
                                log.info("Waiting ... " + cnt);
                                cnt++;
                                Thread.sleep(1000);
                                if (cnt > 2)
                                    m_thread.interrupt();
                            }
                        }
                        catch (InterruptedException e) { throw new BasicPlayerException(BasicPlayerException.WAITERROR, e); }
                    }
                }
                
                // Open SourceDataLine.
                initLine();
                log.info("Creating new thread");
                m_thread = new Thread(this, "BasicPlayer");
                m_thread.start();
                
                if (m_line != null) {
                    m_line.start();
                    notifyEvent(BasicPlayerEvent.PLAYING, getEncodedStreamPosition(), -1, null);
                }    			
    	}
    }

    /**
     * Main loop.
     *
     * Player Status == STOPPED || SEEKING => End of Thread + Freeing Audio Ressources.<br>
     * Player Status == PLAYING => Audio stream data sent to Audio line.<br>
     * Player Status == PAUSED => Waiting for another status.
     */
    public void run() {
        log.info("Thread Running");
        int nBytesRead = 1;
        byte[] abData = new byte[EXTERNAL_BUFFER_SIZE];
        // Lock stream while playing.
        synchronized (m_audioInputStream) {
            // Main play/pause loop.
            while ((nBytesRead != -1) && !IsStoped() && !IsSeeking() && !IsUnknow()) {
                if (IsPlaying()) {
                    // Play.
                    try {
                        nBytesRead = m_audioInputStream.read(abData, 0, abData.length);
                        if (nBytesRead >= 0) {
                            if (m_line.available() >= m_line.getBufferSize()) log.debug("Underrun : "+m_line.available()+"/"+m_line.getBufferSize());
                            m_line.write(abData, 0, nBytesRead);
                            
                            if (m_audioInputStream instanceof PropertiesContainer)
                            	empty_map = ((PropertiesContainer) m_audioInputStream).properties();
                            else empty_map.clear();                            
                            
                            for(BasicPlayerListener bpl : m_listeners)
                            	bpl.progress(getEncodedStreamPosition(), m_line.getMicrosecondPosition(), abData, empty_map);
                        }
                    }
                    catch (IOException e) {
                        log.error("Thread cannot run()", e);
                        notifyEvent(BasicPlayerEvent.STOPPED, getEncodedStreamPosition(), -1, null);
                    }
                    // Nice CPU usage.
                    if (threadSleep > 0) {
                        try { Thread.sleep(threadSleep); }
                        catch (InterruptedException e) { log.error("Thread cannot sleep(" + threadSleep + ")", e); }
                    }
                } else {
                    // Pause
                    try { Thread.sleep(1000); }
                    catch (InterruptedException e) { log.error("Thread cannot sleep(1000)", e); }
                }
            }
            
            // Free audio resources.
            if (m_line != null) {
                m_line.drain();
                m_line.stop();
                m_line.close();
                m_line = null;
            }
            
            // Notification of "End Of Media"
            if (nBytesRead == -1)
                notifyEvent(BasicPlayerEvent.EOM, getEncodedStreamPosition(), -1, null);
            // Close stream.
            closeStream();
        }
        
        notifyEvent(BasicPlayerEvent.STOPPED, getEncodedStreamPosition(), -1, null);
        log.info("Thread completed");
    }

    /**
     * Skip bytes in the File inputstream.
     * It will skip N frames matching to bytes, so it will never skip given bytes length exactly.
     * @param bytes
     * @return value>0 for File and value=0 for URL and InputStream
     * @throws BasicPlayerException
     */
    protected long skipBytes(long bytes) throws BasicPlayerException {
        long totalSkipped = 0;
        if (sourceIsFile()) {
            log.info("Bytes to skip : " + bytes);
            int previousStatus = m_status;
            long skipped = 0;
            try {
                synchronized (m_audioInputStream) {
                    notifyEvent(BasicPlayerEvent.SEEKING, getEncodedStreamPosition(), -1, null);
//                    initAudioInputStream();
                    if (m_audioInputStream != null) {
                        // Loop until bytes are really skipped.
                        while (totalSkipped < (bytes - SKIP_INACCURACY_SIZE)) {
                            skipped = m_audioInputStream.skip(bytes - totalSkipped);
                            if (skipped == 0) break;
                            totalSkipped = totalSkipped + skipped;
                            log.info("Skipped : " + totalSkipped + "/" + bytes);
                            if (totalSkipped == -1) throw new BasicPlayerException(BasicPlayerException.SKIPNOTSUPPORTED);
                        }
                    }
                }
                
                notifyEvent(BasicPlayerEvent.SEEKED, getEncodedStreamPosition(), -1, null);
                
                startPlayback();
                if (previousStatus == BasicPlayerEvent.PAUSED)
                    pausePlayback();
            }
            catch (IOException e) { throw new BasicPlayerException(e); }
        }
        return totalSkipped;
    }

    protected int getEncodedStreamPosition() {
        int nEncodedBytes = -1;
        if (m_dataSource instanceof File) {
            try {
                if (m_encodedaudioInputStream != null)
                    nEncodedBytes = encodedLength - m_encodedaudioInputStream.available();
            }
            catch (IOException e) { log.debug("Cannot get m_encodedaudioInputStream.available()",e); }
        }
        return nEncodedBytes;
    }

    protected void closeStream() {
        try {
        	m_audioInputStream.close();
            log.info("Stream closed");
        }
        catch (IOException e) { log.info("Cannot close stream", e); }
    }

    /**
     * Returns true if Gain control is supported.
     */
    public boolean hasGainControl() { return m_gainControl != null; }

    /**
     * Returns Gain value.
     */
    public float getGain() { return hasGainControl() ? m_gainControl.getValue() : 0f; }

    /**
     * Sets Gain value.
     * Line should be opened before calling this method.
     * Linear scale 0.0  <-->  1.0
     * Threshold Coef. : 1/2 to avoid saturation.
     */
    public void setGain(double fGain) throws BasicPlayerException {
        if (hasGainControl()) {
            double minGainDB = getMinimumGain();
            double ampGainDB = ((10.0f / 20.0f) * getMaximumGain()) - getMinimumGain();
            double cste = Math.log(10.0) / 20;
            double valueDB = minGainDB + (1 / cste) * Math.log(1 + (Math.exp(cste * ampGainDB) - 1) * fGain);
            m_gainControl.setValue((float) valueDB);
            notifyEvent(BasicPlayerEvent.GAIN, getEncodedStreamPosition(), fGain, null);
        }
        else throw new BasicPlayerException(BasicPlayerException.GAINCONTROLNOTSUPPORTED);
    }    
    
    /**
     * Gets max Gain value.
     */
    public float getMaximumGain() { return hasGainControl() ? m_gainControl.getMaximum() : 0f; }

    /**
     * Gets min Gain value.
     */
    public float getMinimumGain() { return hasGainControl() ? m_gainControl.getMinimum() : 0f; }

    /**
     * Returns true if Pan control is supported.
     */
    public boolean hasPanControl() { return m_panControl != null; }

    /**
     * Returns Pan precision.
     */
    public float getPanPrecision() { return hasPanControl() ? m_panControl.getPrecision() : 0f; }

    /**
     * Returns Pan value.
     */
    public float getPan() { return hasPanControl() ? m_panControl.getValue() : 0f; }
    
    /**
     * Sets Pan value.
     * Line should be opened before calling this method.
     * Linear scale : -1.0 <--> +1.0
     */
    public void setPan(double fPan) throws BasicPlayerException {
        if (hasPanControl()) {
            log.debug("Pan : " + fPan);
            m_panControl.setValue((float)fPan);
            notifyEvent(BasicPlayerEvent.PAN, getEncodedStreamPosition(), fPan, null);
        }
        else throw new BasicPlayerException(BasicPlayerException.PANCONTROLNOTSUPPORTED);
    }    

    /**
     * @see javazoom.jlgui.basicplayer.BasicController#seek(long)
     */
    public long seek(long bytes) throws BasicPlayerException { return skipBytes(bytes); }

    /**
     * @see javazoom.jlgui.basicplayer.BasicController#play()
     */
    public void play() throws BasicPlayerException { startPlayback(); }

    /**
     * @see javazoom.jlgui.basicplayer.BasicController#stop()
     */
    public void stop() throws BasicPlayerException { stopPlayback(); }

    /**
     * @see javazoom.jlgui.basicplayer.BasicController#pause()
     */
    public void pause() throws BasicPlayerException { pausePlayback(); }

    /**
     * @see javazoom.jlgui.basicplayer.BasicController#resume()
     */
    public void resume() throws BasicPlayerException { resumePlayback(); }
   
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
    
    public String getMixerName() { return m_mixerName; }
    public void setMixerName(String name) { m_mixerName = name; }
    
    public boolean sourceIsFile() { return m_dataSource instanceof File;}
    public boolean sourceIsURL() { return m_dataSource instanceof URL;}
    public boolean sourceIsInputStream() { return m_dataSource instanceof InputStream;}
    
    /**
     * Returns BasicPlayer status.
     * @return status
     */
    public int getStatus() { return m_status; }    
    public boolean IsPlaying()	{ return m_status == BasicPlayerEvent.PLAYING;}
    public boolean IsOpened()	{ return m_status == BasicPlayerEvent.OPENED;}
    public boolean IsPaused()	{ return m_status == BasicPlayerEvent.PAUSED;}
    public boolean IsStoped()	{ return m_status == BasicPlayerEvent.STOPPED;}
    public boolean IsSeeking()	{ return m_status == BasicPlayerEvent.SEEKING;}
    public boolean IsUnknow()	{ return m_status == BasicPlayerEvent.UNKNOWN;} 
}