package outag.audioformats.flac.util;

public class MetadataBlockDataApplication implements MetadataBlockData {
	private byte[] data;
	
	public MetadataBlockDataApplication(byte[] b) {
		data = new byte[b.length];
		for(int i = 0; i<b.length; i++)
			data[i] = b[i];
	}
	
	public byte[] getBytes() 	{ return data;	}
	
	public int getLength() 		{ return data.length; }
}