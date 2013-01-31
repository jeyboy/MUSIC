package outag.audioformats.mp4.util;

import java.io.UnsupportedEncodingException;

import outag.audioformats.generic.Utils;

public class Mp4TagTextNumberField extends Mp4TagTextField {
    
    public Mp4TagTextNumberField(String id, String n) {
        super(id, n);
    }
    
    public Mp4TagTextNumberField(String id, byte[] raw) throws UnsupportedEncodingException {
        super(id, raw);
    }
    
    protected byte[] getDataBytes() {
        return Utils.getSizeBigEndian(Integer.parseInt(content));
    }
    
    protected void build(byte[] raw) throws UnsupportedEncodingException {
        this.content = Utils.getNumberBigEndian(raw, 16, 19)+"";
    }
}