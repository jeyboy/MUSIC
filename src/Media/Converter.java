package Media;

import com.xuggle.mediatool.event.IAddStreamEvent;
import com.xuggle.xuggler.IStreamCoder;

public class Converter extends MediaEvents {
	int kbps;
	public Converter(int kbps) { super(-1); this.kbps = kbps;}

    public void onAddStream(IAddStreamEvent event) {
    	if (kbps > 23) {
	        IStreamCoder streamCoder = event.getSource().getContainer().getStream(event.getStreamIndex()).getStreamCoder();
	        streamCoder.setFlag(IStreamCoder.Flags.FLAG_QSCALE, false);
	        streamCoder.setBitRate(kbps);
	        streamCoder.setBitRateTolerance(0);
    	}
    }
}
