package outag.audioformats.mp4.util;

import java.io.IOException;
import java.io.RandomAccessFile;

import outag.audioformats.EncodingInfo;
import outag.audioformats.exceptions.CannotReadException;

public class Mp4InfoReader {
    public EncodingInfo read( RandomAccessFile raf ) throws CannotReadException, IOException {
        EncodingInfo info = new EncodingInfo();
        Mp4Box box = new Mp4Box();
        
        //Get to the facts
        //1-Searching for "moov"
        seek(raf, box, "moov");
        
        //2-Searching for "udta"
        seek(raf, box, "mvhd");
        
        byte[] b = new byte[box.getOffset()-8];
        raf.read(b);
        
        Mp4MvhdBox mvhd = new Mp4MvhdBox(b);
        info.setLength(mvhd.getLength());

        return info;
    }
    
    private void seek(RandomAccessFile raf, Mp4Box box, String id) throws IOException {
        byte[] b = new byte[8];
        raf.read(b);
        box.update(b);
        while(!box.getId().equals(id)) {
            raf.skipBytes(box.getOffset()-8);
            raf.read(b);
            box.update(b);
        }
    }
}