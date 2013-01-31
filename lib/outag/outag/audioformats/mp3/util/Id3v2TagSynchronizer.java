package outag.audioformats.mp3.util;

import java.nio.ByteBuffer;

public class Id3v2TagSynchronizer {
    public ByteBuffer synchronize(ByteBuffer b) {
        int cap = b.capacity();   	
        ByteBuffer bb = ByteBuffer.allocate(cap);
        
        while(b.remaining() >= 1) {
        	byte cur = b.get();
            bb.put(cur);
            
            if((cur&0xFF) == 0xFF && b.remaining() >=1 && b.get(b.position()) == 0x00) //First part of synchronization
                b.get();
        }
        
        //We have finished filling the new bytebuffer, so set the limit, and rewind
        bb.limit(bb.position());
        bb.rewind();
        
        return bb;
    }
}