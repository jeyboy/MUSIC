package outag.formats.mp4.util.box;

import java.io.IOException;

import outag.file_presentation.JBBuffer;

public class Mp4MvhdBox {
    private int timeScale;
    private long timeLength;
    private byte version;
    
    public Mp4MvhdBox(JBBuffer data) throws IOException {
        version = data.get();
        
        if(version == 1) {
        	data.skip(19);
            timeScale = data.UBEInt();
            timeLength = data.UBELong();
        } 
        else {
        	data.skip(11);
            timeScale = data.UBEInt();
            timeLength = data.UBEInt();
        }
    }
    
    public int getLength() { return (int) (timeLength / timeScale); }
}