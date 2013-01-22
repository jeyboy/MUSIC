package Media;

import com.xuggle.mediatool.event.IVideoPictureEvent;

public class Snapper extends MediaEvents {
	String output;
	long MICRO_SECONDS_BETWEEN_FRAMES;
	
	public Snapper(String output_path, int StreamIndex, long step) { 
		super(StreamIndex);
		output = output_path + "/";
		currStamp = -(MICRO_SECONDS_BETWEEN_FRAMES = step);
	}

	public void onVideoPicture(IVideoPictureEvent event) { 
		if (event.getStreamIndex() != streamIndex) return;
 
		if (event.getTimeStamp() - currStamp >= MICRO_SECONDS_BETWEEN_FRAMES) {
			Media.imageToFile(output + System.currentTimeMillis(), event.getImage());
			currStamp += MICRO_SECONDS_BETWEEN_FRAMES;
		}
	}
}
