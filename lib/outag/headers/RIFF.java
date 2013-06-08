package headers;

import outag.file_presentation.JBBuffer;
import outag.file_presentation.Parseable;

public class RIFF {
	
	int channels;
	int sampleRate;
	int bytesPerSecond;
	int bytesPerSample;		
	
	public RIFF(Parseable p) throws Exception {
		p.skip(12);
		
		JBBuffer buff = p.buffer(24);
		String fmt = buff.Str(3);
		if (fmt.equals("fmt") && buff.get(8) == 1) {
			channels = buff.get(10);
			sampleRate = Parseable.toU(buff.get(15)) * 16777216 + Parseable.toU(buff.get(14)) * 65536 + Parseable.toU(buff.get(13)) * 256 + Parseable.toU(buff.get(12));
			bytesPerSecond = Parseable.toU(buff.get(19)) * 16777216 + Parseable.toU(buff.get(18)) * 65536 + Parseable.toU(buff.get(17)) * 256 + Parseable.toU(buff.get(16));
			bytesPerSample = Parseable.toU(buff.get(22));
		} 
		else throw new Exception("RIFF header is not valid");		
	}
	
	public int getBitrate() { return bytesPerSecond * 8 / 1000; }
	
	public int getChannels() { return channels; }
	
	public int getSampleRate() { return sampleRate; }
	
	public int getBytesPerSecond() { return bytesPerSecond; }
	
	public int getBytesPerSample() { return bytesPerSample; }
}