package Media;

import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.event.IAddStreamEvent;
import com.xuggle.mediatool.event.IAudioSamplesEvent;
import com.xuggle.mediatool.event.ICloseCoderEvent;
import com.xuggle.mediatool.event.ICloseEvent;
import com.xuggle.mediatool.event.IFlushEvent;
import com.xuggle.mediatool.event.IOpenCoderEvent;
import com.xuggle.mediatool.event.IOpenEvent;
import com.xuggle.mediatool.event.IReadPacketEvent;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.mediatool.event.IWriteHeaderEvent;
import com.xuggle.mediatool.event.IWritePacketEvent;
import com.xuggle.mediatool.event.IWriteTrailerEvent;
import com.xuggle.xuggler.Global;

public class MediaEvents extends MediaListenerAdapter {
	protected int streamIndex;
	protected String output; 
	protected long currStamp = Global.NO_PTS;
	
	public MediaEvents(int StreamIndex) { streamIndex = StreamIndex; }
	public void onVideoPicture(IVideoPictureEvent event) { if (event.getStreamIndex() != streamIndex) return; }
	public void onAudioSamples(IAudioSamplesEvent event) {}		
	public void onAddStream(IAddStreamEvent event) {}
	public void onClose(ICloseEvent event) {}
	public void onCloseCoder(ICloseCoderEvent event) {}
	public void onFlush(IFlushEvent event) {}
	public void onOpen(IOpenEvent event) {}
	public void onOpenCoder(IOpenCoderEvent event) {}	
	public void onReadPacket(IReadPacketEvent event) {}
	public void onWriteHeader(IWriteHeaderEvent event) {}
	public void onWritePacket(IWritePacketEvent event) {}
	public void onWriteTrailer(IWriteTrailerEvent event) {} 	
}
