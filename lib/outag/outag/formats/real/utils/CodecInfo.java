package outag.formats.real.utils;

public class CodecInfo {	
	long headerSize;
	int codecFlavor;
	long codecFrameSize;
	int subpacketH;
	int frameSize;
	int subpacketSize;

	int sampleRate;
	int sampleSize;
	int channels;
	
	byte [] codecExtraData;
	
	public long getHeaderSize() { return headerSize; }
	public void setHeaderSize(long val) { headerSize = val; }
	
	public int getcodecFlavor() { return codecFlavor; }
	public void setcodecFlavor(int val) { codecFlavor = val; }
	
	public long getcodecFrameSize() { return codecFrameSize; }
	public void setcodecFrameSize(long val) { codecFrameSize = val; }
	
	public int getsubpacketH() { return subpacketH; }
	public void setsubpacketH(int val) { subpacketH = val; }
	
	public int getframeSize() { return frameSize; }
	public void setframeSize(int val) { frameSize = val; }
	
	public int getsubpacketSize() { return subpacketSize; }
	public void setsubpacketSize(int val) { subpacketSize = val; }
	
	public int getsampleRate() { return sampleRate; }
	public void setsampleRate(int val) { sampleRate = val; }
	
	public int getsampleSize() { return sampleSize; }
	public void setsampleSize(int val) { sampleSize = val; }
	
	public int getchannels() { return channels; }
	public void setchannels(int val) { channels = val; }
	
	public byte [] getcodecExtraData() { return codecExtraData; }
	public void setcodecExtraData(byte [] val) { codecExtraData = val; }	
}