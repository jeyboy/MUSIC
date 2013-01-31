package outag.audioformats.wav;

import outag.audioformats.generic.GenericTag;

public class WavTag extends GenericTag {
	public String toString() {
		String output = "WAV "+super.toString();
		return output;
	}
}