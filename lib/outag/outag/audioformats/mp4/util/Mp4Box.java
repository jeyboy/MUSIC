package outag.audioformats.mp4.util;

import outag.audioformats.generic.Utils;

public class Mp4Box {
    private String id;
    private int offset;
        
    public void update(byte[] b) {
        this.offset = Utils.getNumberBigEndian(b, 0, 3);       
        this.id = Utils.getString(b, 4, 4);
    }
    
    public String getId() { return id; }
    
    public int getOffset() { return offset; }
    
    public String toString() { return "Box "+id+":"+offset; }
}