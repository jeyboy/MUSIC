package outag.audioformats.ape.util;

import outag.audioformats.generic.Utils;

public class MonkeyDescriptor {
	byte[] b;
	public MonkeyDescriptor(byte[] b) { this.b = b;	}
	
	public int getRiffWavOffset() {
		return getDescriptorLength() + getHeaderLength() + getSeekTableLength();
	}
	
	public int getDescriptorLength() 			{ return Utils.getNumber(b, 0,3); }
	
	public int getHeaderLength() 				{ return Utils.getNumber(b, 4,7); }
	
	public int getSeekTableLength() 			{ return Utils.getNumber(b, 8,11); }
	
	public int getRiffWavLength() 				{ return Utils.getNumber(b, 12,15); }
    
	public long getApeFrameDataLength() 		{ return Utils.getLongNumber(b, 16,19); }
	
	public long getApeFrameDataHighLength() 	{ return Utils.getLongNumber(b, 20,23); }
	
	public int getTerminatingDataLength() 		{ return Utils.getNumber(b, 24,27);	}
    
    //16 bytes cFileMD5 b[28->43]
}