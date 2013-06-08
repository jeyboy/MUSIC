package outag.formats.wav.util;

public class WavFormatHeader {
	private boolean isValid = false;
	private int channels, sampleRate, bytesPerSecond, bitrate;

	public WavFormatHeader(byte[] b) {
		String fmt = new String(b,0,3);
		//System.err.println(fmt);
		if(fmt.equals("fmt") && b[8]==1) {
			channels = b[10];
			//System.err.println(channels);
			sampleRate = u(b[15])*16777216 + u(b[14])*65536 + u(b[13])*256 + u(b[12]);
			//System.err.println(sampleRate);
			bytesPerSecond = u(b[19])*16777216 + u(b[18])*65536 + u(b[17])*256 + u(b[16]);
			//System.err.println(bytesPerSecond);
			bitrate = u(b[22]);
			isValid = true;
		}
	}

	public boolean isValid() 		{ return isValid; }
	
	public int getChannelNumber() 	{ return channels; }
	
	public int getSamplingRate() 	{ return sampleRate; }
	
	public int getBytesPerSecond() 	{ return bytesPerSecond; }
	
	public int getBitrate() 		{ return bitrate; }

	private int u(int n) 			{ return n & 0xff; }
	
	public String toString() {
		String out = "RIFF-WAVE Header:\n";
		out += "Is valid?: " + isValid;
		return out;
	}
}