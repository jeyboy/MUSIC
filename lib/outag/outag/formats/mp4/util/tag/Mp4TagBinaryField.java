package outag.formats.mp4.util.tag;

import java.io.UnsupportedEncodingException;

import outag.file_presentation.JBBuffer;
import outag.formats.generic.TagField;
import outag.formats.mp4.util.box.Mp4Box;

/** Represents binary data <br>
 * Subclassed by cover art field,
 * TODO unaware of any other binary fields at the moment */
public class Mp4TagBinaryField extends Mp4TagField {
    protected int dataSize;
    protected byte[] dataBytes;
    protected boolean isBinary = false;

    /** Construct binary field from raw data of audio file
     * @param id
     * @param raw
     * @throws Exception */
    public Mp4TagBinaryField(String id, JBBuffer raw) throws Exception {
        super(id, raw);
    }

    public Mp4FieldType getFieldType() {
        //TODO dont know what value this should be do we actually have any binary fields other
        //than cover art
        return Mp4FieldType.IMPLICIT;
    }

    /** Used when creating raw content
     * @return
     * @throws UnsupportedEncodingException */
    protected byte[] getDataBytes() throws Exception { return dataBytes; }

    protected void build(JBBuffer raw) throws Exception {
    	Mp4Box header = Mp4Box.init(raw, false);
        dataSize = header.getLength();

        //Skip the version and length fields
        raw.pos((int)raw.pos() + header.getHeadLength());

        //Read the raw data into byte array
        this.dataBytes = new byte[dataSize - header.getHeadLength()];
        for (int i = 0; i < dataBytes.length; i++)
            this.dataBytes[i] = raw.get();

        //After returning buffers position will be after the end of this atom
    }

    public boolean isBinary() { return isBinary; }

    public boolean isEmpty() { return this.dataBytes.length == 0; }

    public int getDataSize() { return dataSize; }

    public byte[] getData() { return this.dataBytes; }

    public void setData(byte[] d) { this.dataBytes = d; }

    public void copyContent(TagField field) {
        if (field instanceof Mp4TagBinaryField) {
            this.dataBytes = ((Mp4TagBinaryField) field).getData();
            this.isBinary = field.isBinary();
        }
    }

	public byte[] getRawContent() throws UnsupportedEncodingException {
		return null;
	}
}