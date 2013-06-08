package outag.formats.asf.data;

import java.math.BigInteger;

import outag.formats.asf.util.Utils;

/** This class is the base for all handled stream contents. <br>
 * A Stream chunk delivers information about a audio or video stream. Because of
 * this the stream chunk identifies in one field what type of stream it is
 * describing and so other data is provided. However some information is common
 * to all stream chunks which are stored in this hierarchy of the class tree. */
public class StreamChunk extends Chunk {

    /** @return <code>true</code> if the stream data is encrypted. */
    private boolean contentEncrypted;

    /** Stores the number of the current stream. */
    private int streamNumber;

    private long streamSpecificDataSize;

    /** Format time in 100-ns steps. */
    private long timeOffset;

    /** Stores the size of type specific data structure within chunk. */
    private long typeSpecificDataSize;

    /** Creates an instance
     * @param pos - Position of chunk within file or stream.
     * @param chunkLen - length of chunk */
    public StreamChunk(long pos, BigInteger chunkLen) { super(GUID.GUID_AUDIOSTREAM, pos, chunkLen); }

    /** @return Returns the streamNumber. */
    public int getStreamNumber() { return streamNumber; }

    /** @return Returns the streamSpecificDataSize. */
    public long getStreamSpecificDataSize() { return streamSpecificDataSize; }

    /** @return Returns the timeOffset. */
    public long getTimeOffset() { return timeOffset; }

    /** @return Returns the typeSpecificDataSize. */
    public long getTypeSpecificDataSize() { return typeSpecificDataSize; }

    /** @return Returns the contentEncrypted. */
    public boolean isContentEncrypted() { return contentEncrypted; }


    public String prettyPrint() {
        StringBuffer result = new StringBuffer(super.prettyPrint());
        result.insert(0, Utils.LINE_SEPARATOR + "Stream Data:"
                + Utils.LINE_SEPARATOR);
        result.append("   Stream number: " + getStreamNumber()
                + Utils.LINE_SEPARATOR);
        result.append("   Type specific data size  : "
                + getTypeSpecificDataSize() + Utils.LINE_SEPARATOR);
        result.append("   Stream specific data size: "
                + getStreamSpecificDataSize() + Utils.LINE_SEPARATOR);
        result.append("   Time Offset              : " + getTimeOffset()
                + Utils.LINE_SEPARATOR);
        result.append("   Content Encryption       : " + isContentEncrypted()
                + Utils.LINE_SEPARATOR);
        return result.toString();
    }

    /** @param cntEnc - The contentEncrypted to set. */
    public void setContentEncrypted(boolean cntEnc) { this.contentEncrypted = cntEnc; }

    /** @param streamNum - The streamNumber to set. */
    public void setStreamNumber(int streamNum) { this.streamNumber = streamNum; }

    /** @param strSpecDataSize - The streamSpecificDataSize to set. */
    public void setStreamSpecificDataSize(long strSpecDataSize) { this.streamSpecificDataSize = strSpecDataSize; }

    /** @param timeOffs - sets the time offset */
    public void setTimeOffset(long timeOffs) { this.timeOffset = timeOffs; }

    /** @param typeSpecDataSize - The typeSpecificDataSize to set. */
    public void setTypeSpecificDataSize(long typeSpecDataSize) { this.typeSpecificDataSize = typeSpecDataSize; }
}