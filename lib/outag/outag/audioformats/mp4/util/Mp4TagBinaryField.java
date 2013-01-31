package outag.audioformats.mp4.util;

import java.io.UnsupportedEncodingException;

import outag.audioformats.generic.TagField;
import outag.audioformats.generic.Utils;

public class Mp4TagBinaryField extends Mp4TagField {
    protected byte[] dataBytes;
    protected boolean isBinary = false;
    
    public Mp4TagBinaryField(String id) { super(id); }
    
    public Mp4TagBinaryField(String id, byte[] raw) throws UnsupportedEncodingException {
        super(id, raw);
    }

    public byte[] getRawContent() {
        byte[] data = dataBytes;
        byte[] b = new byte[4 + 4 + 4 + 4 + 4 + 4 + data.length];
        
        int offset = 0;
		Utils.copy(Utils.getSizeBigEndian(b.length), b, offset);
		offset += 4;
		
		Utils.copy(Utils.getDefaultBytes(getId()), b, offset);
		offset += 4;
		
		Utils.copy(Utils.getSizeBigEndian(4+4+4+4+data.length), b, offset);
		offset += 4;
		
		Utils.copy(Utils.getDefaultBytes("data"), b, offset);
		offset += 4;
		
		Utils.copy(new byte[] {0, 0, 0, (byte) (isBinary() ? 0 : 1) }, b, offset);
		offset += 4;
		
		Utils.copy(new byte[] {0, 0, 0, 0}, b, offset);
		offset += 4;
		
		Utils.copy(data, b, offset);
		offset += data.length;
		
		return b;
    }
    
    protected void build(byte[] raw) {
        int dataSize = Utils.getNumberBigEndian(raw, 0, 3);
        
        this.dataBytes = new byte[dataSize - 16];
        for(int i = 16; i<dataSize; i++)
            this.dataBytes[i-16] = raw[i];
        
        this.isBinary = ((raw[11]&0x01) == 0);
    }
    
    public boolean isBinary() { return isBinary; }
    
    public boolean isEmpty() { return this.dataBytes.length == 0; }
    
    public byte[] getData() { return this.dataBytes; }
    
    public void setData(byte[] d) { this.dataBytes = d; }
    
    public void copyContent(TagField field) {
        if(field instanceof Mp4TagBinaryField) {
            this.dataBytes = ((Mp4TagBinaryField) field).getData();
            this.isBinary = ((Mp4TagBinaryField) field).isBinary();
        }
    }
}