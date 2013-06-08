package outag.formats.ape.util;

import java.io.UnsupportedEncodingException;

import outag.formats.generic.TagField;

public abstract class ApeTagField implements TagField {
    private String id;
    private boolean binary;
    
    public ApeTagField(String id, boolean binary) {
        this.id = id;
        this.binary = binary;
    }
    
    public String getId() { return this.id; }

    public boolean isBinary() { return binary; }

    public void isBinary(boolean b) { this.binary = b; }

    public boolean isCommon() {
        return id.equals("Title") ||
		  id.equals("Album") ||
		  id.equals("Artist") ||
		  id.equals("Genre") ||
		  id.equals("Track") ||
		  id.equals("Year") ||
		  id.equals("Comment");
    }

	protected byte[] getSize(int size) {
		byte[] b = new byte[4];
		b[3] = (byte) ( ( size & 0xFF000000 ) >> 24 );
		b[2] = (byte) ( ( size & 0x00FF0000 ) >> 16 );
		b[1] = (byte) ( ( size & 0x0000FF00 ) >> 8 );
		b[0] = (byte) (   size & 0x000000FF );
		return b;
	}
	
	protected byte[] getBytes(String s, String encoding) throws UnsupportedEncodingException {
		return s.getBytes(encoding);
	}
	
    public abstract boolean isEmpty();
    public abstract String toString();
    public abstract void copyContent(TagField field);
    public abstract byte[] getRawContent() throws UnsupportedEncodingException;
}
