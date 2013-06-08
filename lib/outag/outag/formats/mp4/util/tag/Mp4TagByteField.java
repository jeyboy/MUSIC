package outag.formats.mp4.util.tag;

import outag.file_presentation.JBBuffer;
import outag.formats.mp4.util.box.Mp4Box;
import outag.formats.mp4.util.box.Mp4DataBox;

/** Represents a single byte as a number <br>
 * Usually single byte fields are used as a boolean field, but not always so we don't do this conversion */
public class Mp4TagByteField extends Mp4TagTextField {
//    public static String TRUE_VALUE = "1";  //when using this field to hold a boolean
	public byte[] bytedata;

    /** Construct from raw data from audio file
     * @param id
     * @param raw
     * @throws Exception 
     * @throws UnsupportedEncodingException */
    public Mp4TagByteField(String id, JBBuffer raw) throws Exception { super(id, raw); }

    public Mp4FieldType getFieldType() { return Mp4FieldType.INTEGER; }

    protected void build(Mp4Box head, JBBuffer data) throws Exception {
        //Data actually contains a 'Data' Box so process data using this
    	Mp4Box header = Mp4Box.init(data, false);
        Mp4DataBox databox = new Mp4DataBox(header, data);
        dataSize = header.getLength();
        bytedata = databox.getByteData();
        content = databox.getContent();
    }
}