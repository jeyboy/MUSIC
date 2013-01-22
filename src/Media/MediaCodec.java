package Media;
import com.xuggle.xuggler.IStreamCoder;

public class MediaCodec {
	public IStreamCoder codec;
	public int id;
	
	public MediaCodec(IStreamCoder codec, int index) {
		this.codec = codec;
		this.id = index;
	}
}
