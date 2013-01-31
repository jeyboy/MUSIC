package outag.audioformats.asf.data;

import java.math.BigInteger;

/** This class represents a chunk within asf streams. <br>
 * Each chunk starts with a 16byte guid identifying the type. After that a
 * number (represented by 8 bytes) follows which shows the size in bytes of the
 * chunk. Finally there is the data of the chunk. */
public class Chunk {
    /** The length of current chunk. */
    protected final BigInteger chunkLength;

    /** The guid of represented chunk header.*/
    protected final GUID guid;

    /** The position of current header object within file or stream. */
    protected final long position;

    /** Creates an instance
     * @param headerGuid The GUID of header object.
     * @param pos Position of header object within stream or file.
     * @param chunkLen Length of current chunk. */
    public Chunk(GUID headerGuid, long pos, BigInteger chunkLen) {
        if (headerGuid == null) {
            throw new IllegalArgumentException(
                    "GUID must not be null nor anything else than "
                            + GUID.GUID_LENGTH + " entries long.");
        }
        if (pos < 0) {
            throw new IllegalArgumentException(
                    "Position of header can't be negative.");
        }
        if (chunkLen == null || chunkLen.compareTo(BigInteger.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "chunkLen must not be null nor negative.");
        }
        this.guid = headerGuid;
        this.position = pos;
        this.chunkLength = chunkLen;
    }

    /** This method returns the End of the current chunk introduced by current header object.
     * @return Position after current chunk. */
    public long getChunckEnd() { return position + chunkLength.longValue(); }

    /** @return Returns the chunkLength. */
    public BigInteger getChunkLength() { return chunkLength; }

    /** @return Returns the guid. */
    public GUID getGuid() { return guid; }

    /** @return Returns the position. */
    public long getPosition() { return position; }

    /** This method creates a String containing usefull information prepared to be printed on stdout. <br>
     * @return Information of current Chunk Object. */
    public String prettyPrint() {
        StringBuffer result = new StringBuffer();
        result.append("GUID: " + GUID.getGuidDescription(guid));
        result.append("\n   Starts at position: " + getPosition() + "\n");
        result.append("   Last byte at: " + (getChunckEnd() - 1) + "\n\n");
        return result.toString();
    }

    public String toString() { return prettyPrint(); }
}