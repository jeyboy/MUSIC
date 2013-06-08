package outag.formats.mp3.util.id3frames;

import java.io.UnsupportedEncodingException;

import outag.formats.generic.TagField;

public class GenericId3Frame extends Id3Frame {
	
	private byte[] data;
	private String id;
	
	/*
	 * 0,1| frame flags
	 * 2,..,0X00| Owner ID
	 * xx,...| identifier (binary)
	 */
	
	public GenericId3Frame(String id, byte[] raw, byte version) throws UnsupportedEncodingException {
		super(raw, version);
		this.id = id;
	}
	
	public String getId() {	return this.id;	}
	
	public boolean isBinary() {	return true; }
	
	public byte[] getData() { return data; }
	
	public boolean isCommon() { return false; }
	
	public void copyContent(TagField field) {
	    if(field instanceof GenericId3Frame)
	        this.data = ((GenericId3Frame)field).getData();
	}
	
	public boolean isEmpty() { return this.data.length == 0; }
	
	protected void populate(byte[] raw) {
		this.data = new byte[raw.length - flags.length];
		for(int i = 0; i<data.length; i++)
			data[i] = raw[i + flags.length];
	}
	
	protected byte[] build() {
		byte[] b = new byte[4 + 4 + data.length + flags.length];
		
		int offset = 0;
		copy(getIdBytes(), b, offset);        offset += 4;
		copy(getSize(b.length-10), b, offset); offset += 4;
		copy(flags, b, offset);               offset += flags.length;	
		copy(data, b, offset);
		
		return b;
	}
	
	public String toString() { return this.id+" : No associated view"; }
}