package outag.audioformats.ape.util;

import java.io.UnsupportedEncodingException;
import outag.audioformats.generic.TagField;
import outag.audioformats.generic.Utils;

public class ApeTagBinaryField extends ApeTagField  {
    private byte[] content;

    public ApeTagBinaryField(String id, byte[] content) {
        super(id, true);
        this.content = new byte[content.length];
        for(int i = 0; i<content.length; i++)
            this.content[i] = content[i];
    }
    
    public boolean isEmpty() { return this.content.length == 0; }
    
    public String toString() { return "Binary field"; }
    
    public void copyContent(TagField field) {
        if(field instanceof ApeTagBinaryField) {
            this.content = ((ApeTagBinaryField)field).getContent();
        }
    }
    
    public byte[] getContent() { return this.content; }
    
    public byte[] getRawContent() throws UnsupportedEncodingException {
        byte[] idBytes = getBytes(getId(), "ISO-8859-1");
        byte[] buf = new byte[4 + 4 + idBytes.length + 1 + content.length];
		byte[] flags = {0x02,0x00,0x00,0x00};
		
		int offset = 0;
		Utils.copy(getSize(content.length), buf, offset);    	offset += 4;
		Utils.copy(flags, buf, offset);                      	offset += 4;
		Utils.copy(idBytes, buf, offset);                    	offset += idBytes.length;
		buf[offset] = 0;                               			offset += 1;
		Utils.copy(content, buf, offset);                    	offset += content.length;
		
		return buf;
    }
}