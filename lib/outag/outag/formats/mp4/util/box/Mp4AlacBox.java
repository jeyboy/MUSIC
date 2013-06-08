package outag.formats.mp4.util.box;

import java.io.IOException;

import outag.file_presentation.Parseable;

/** AlacBox ( Apple Lossless codec information description box), <br>
  * Normally occurs twice, the first ALAC contains the default  values, the second ALAC within contains the real
 * values for this audio. */
public class Mp4AlacBox {
    private int maxSamplePerFrame; // 32bit
    private int unknown1; // 8bit
    private int sampleSize; // 8bit
    private int historyMult; // 8bit
    private int initialHistory; // 8bit
    private int kModifier; // 8bit
    private int channels; // 8bit
    private int unknown2; // 16bit
    private int maxCodedFrameSize; // 32bit
    private int bitRate; // 32bit
    private int sampleRate; // 32bit


    /** buffer must start from from the start of the body
     * @param buffer data of box (doesn't include header data) 
     * @throws IOException */
    public Mp4AlacBox(Parseable data) throws IOException {   	
    	data.skip(4); // skip other flags (4 bytes)
    	maxSamplePerFrame   = data.UBEInt();
    	unknown1            = data.read();
    	sampleSize          = data.read();
    	historyMult         = data.read();
    	initialHistory      = data.read();
    	kModifier           = data.read();
    	channels            = data.read();
    	unknown2            = data.UBEShort();
    	maxCodedFrameSize   = data.UBEInt();
    	bitRate             = data.UBEInt();
    	sampleRate          = data.UBEInt();    	
    }

    public int getMaxSamplePerFrame() { return maxSamplePerFrame; }

    public int getUnknown1() { return unknown1; }

    public int getSampleSize() { return sampleSize; }

    public int getHistoryMult() { return historyMult; }

    public int getInitialHistory() { return initialHistory; }

    public int getKModifier() { return kModifier; }

    public int getChannels() { return channels; }

    public int getUnknown2() { return unknown2; }

    public int getMaxCodedFrameSize() { return maxCodedFrameSize; }

    public int getBitRate() { return bitRate; }

    public int getSampleRate() { return sampleRate; }
}