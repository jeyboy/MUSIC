package outag.formats.ape.util;

import java.io.UnsupportedEncodingException;

import outag.formats.generic.TagField;
import outag.formats.generic.TagTextField;
import outag.formats.generic.Utils;

public class ApeTagTextField extends ApeTagField implements TagTextField  {   
    private String content;

    public ApeTagTextField(String id, String content) {
        super(id, false);
        this.content = content;
    }
    
    public boolean isEmpty() { return this.content.equals(""); }
    
    public String toString() { return this.content; }
    
    public void copyContent(TagField field) {
        if(field instanceof ApeTagTextField)
            this.content = ((ApeTagTextField)field).getContent();
    }
    
    public String getContent() 			{ return this.content; }
    public void setContent(String s) 	{ this.content = s; }    

    public String getEncoding() 		{ return "UTF-8"; }
    public void setEncoding(String s)	{ /* Not allowed */ }
    
    public byte[] getRawContent() throws UnsupportedEncodingException {
        byte[] idBytes = getBytes(getId(), "ISO-8859-1");
        byte[] contentBytes = getBytes(content, getEncoding());
		byte[] buf = new byte[4 + 4 + idBytes.length + 1 + contentBytes.length];
		byte[] flags = {0x00,0x00,0x00,0x00};
		
		int offset = 0;
		Utils.copy(getSize(contentBytes.length), buf, offset);	offset += 4;
		Utils.copy(flags, buf, offset);                       	offset += 4;
		Utils.copy(idBytes, buf, offset);                     	offset += idBytes.length;
		buf[offset] = 0;                                		offset += 1;
		Utils.copy(contentBytes, buf, offset);                	offset += contentBytes.length;
		
		return buf;
    }
}