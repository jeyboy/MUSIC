package outag.formats.flac.util;

public class MetadataBlockDataPadding implements MetadataBlockData {
	private int length;
	
	public MetadataBlockDataPadding(int length) { this.length = length;	}
	
	public byte[] getBytes() {
		assert false;
		return null;
	}
	
	public int getLength() { return length;	}
}