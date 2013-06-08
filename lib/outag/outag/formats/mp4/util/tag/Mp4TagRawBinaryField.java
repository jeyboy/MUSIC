package outag.formats.mp4.util.tag;

import outag.file_presentation.JBBuffer;
import outag.formats.generic.TagField;

/** Represents raw binary data <br>
 * We use this when we find an atom under the ilst atom that we do not recognize , that does not
 * follow standard conventions in order to save the data without modification so it can be safely
 * written back to file */
public class Mp4TagRawBinaryField extends Mp4TagField {
    protected byte[] dataBytes;

    /** Construct binary field from raw data of audio file
     * @param header
     * @param raw
     * @throws Exception 
     * @throws java.io.UnsupportedEncodingException */
    public Mp4TagRawBinaryField(String id, JBBuffer raw) throws Exception {
        super(id, raw);
    }

    public Mp4FieldType getFieldType() { return Mp4FieldType.IMPLICIT; }

    /**Used when creating raw content
     * @return */
    protected byte[] getDataBytes() { return dataBytes; }


    /** Build from data
     * <br>After returning buffers position will be after the end of this atom
     * @param raw */
    protected void build(JBBuffer raw) {
        //Read the raw data into byte array
        this.dataBytes = new byte[raw.limit()];
        for (int i = 0; i < dataBytes.length; i++)
            this.dataBytes[i] = raw.get();
    }

    public boolean isBinary() { return true; }

    public boolean isEmpty() { return this.dataBytes.length == 0; }

    public int getDataSize() { return dataBytes.length; }

    public byte[] getData() { return this.dataBytes; }

    public void setData(byte[] d) { this.dataBytes = d; }

    public void copyContent(TagField field) {
        throw new UnsupportedOperationException("not done");
    }

    public byte[] getRawContent() {
//        try {
//            ByteArrayOutputStream outerbaos = new ByteArrayOutputStream();
//            outerbaos.write(Utils.getSizeBEInt32(Mp4BoxHeader.HEADER_LENGTH + dataSize));
//            outerbaos.write(Utils.getDefaultBytes(getId(), "ISO-8859-1"));
//            outerbaos.write(dataBytes);
//            System.out.println("SIZE" + outerbaos.size());
//            return outerbaos.toByteArray();
//        }
//        catch (IOException ioe)
//        {
//            //This should never happen as were not actually writing to/from a file
//            throw new RuntimeException(ioe);
//        }
    	return null;
    }
}