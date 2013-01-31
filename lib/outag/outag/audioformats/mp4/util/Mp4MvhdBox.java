package outag.audioformats.mp4.util;

import outag.audioformats.generic.Utils;

public class Mp4MvhdBox {
    private int timeScale;
    private long timeLength;
    private byte version;
    
    public Mp4MvhdBox(byte[] raw) {
        this.version = raw[0];
        
        if(version == 1) {
            this.timeScale = Utils.getNumberBigEndian(raw, 20 , 23);
            this.timeLength = Utils.getLongNumberBigEndian(raw, 24 , 31);
        } 
        else {
            this.timeScale = Utils.getNumberBigEndian(raw, 12 , 15);
            this.timeLength = Utils.getNumberBigEndian(raw, 16 , 19);
        }
    }
    
    public int getLength() { return (int) (this.timeLength / this.timeScale); }
}