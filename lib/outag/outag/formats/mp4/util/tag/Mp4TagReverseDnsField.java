package outag.formats.mp4.util.tag;

import java.io.UnsupportedEncodingException;

import outag.file_presentation.JBBuffer;
import outag.formats.generic.TagField;
import outag.formats.generic.TagTextField;
import outag.formats.mp4.util.box.Mp4Box;
import outag.formats.mp4.util.box.Mp4DataBox;
import outag.formats.mp4.util.box.Mp4MeanBox;
import outag.formats.mp4.util.box.Mp4NameBox;

/** Represents reverse dns field, used for custom information
 * <p/>
 * <p>Originally only used by Itunes for information that was iTunes specific but now used in a wide range of uses,
 * for example Musicbrainz uses it for many of its fields.
 * <p/>
 * These fields have a more complex setup
 * Box ----  shows this is a reverse dns metadata field
 * Box mean  the issuer in the form of reverse DNS domain (e.g com.apple.iTunes)
 * Box name  descriptor identifying the type of contents
 * Box data  contents
 * <p/>
 * The raw data passed starts from the mean box */
public class Mp4TagReverseDnsField extends Mp4TagField implements TagTextField {
    public static final String IDENTIFIER = "----";

    protected int dataSize;

    //Issuer
    private String issuer;

    //Descriptor
    private String descriptor;

    //Data Content,
    //TODO assuming always text at the moment
    protected String content;

    /** Construct from existing file data
     * @param parentHeader
     * @param data
     * @throws Exception */
    public Mp4TagReverseDnsField(String id, JBBuffer data) throws Exception {
        super(id, data);
    }

    public Mp4FieldType getFieldType() {
        //TODO always assuming text at moment but may not always be the case (though dont have any concrete
        //examples)
        return Mp4FieldType.TEXT;
    }

    protected void build(JBBuffer data) throws Exception {
        //Read mean box, set the issuer and skip over data
    	Mp4Box meanHeader = Mp4Box.init(data, false);
        Mp4MeanBox meanBox = new Mp4MeanBox(meanHeader, data);
        setIssuer(meanBox.getIssuer());
        data.pos(data.ipos() + meanHeader.getLength());

        //Read name box, identify what type of field it is
        Mp4Box nameHeader = Mp4Box.init(data, false);
        Mp4NameBox nameBox = new Mp4NameBox(nameHeader, data);
        setDescriptor(nameBox.getName());
        data.pos(data.ipos() + nameHeader.getLength());

//        //Issue 198:There is not actually a data atom there cannot can't be because no room for one
//        if (parentHeader.getLength() == meanHeader.getLength() + nameHeader.getLength())
//        {
//            id = IDENTIFIER + ":" + issuer + ":" + descriptor;
//            setContent("");
////            logger.warning(ErrorMessage.MP4_REVERSE_DNS_FIELD_HAS_NO_DATA.getMsg(id));
//        }
//        //Usual Case
//        else {
            //Read data box, identify the data
        	Mp4Box header = Mp4Box.init(data, false);
            Mp4DataBox dataBox = new Mp4DataBox(header, data);
            setContent(dataBox.getContent());
            data.pos(data.ipos() + header.getLength());

            //Now calculate the id which in order to be unique needs to use all htree values
//            id = IDENTIFIER + ":" + issuer + ":" + descriptor;
//        }
    }

    public void copyContent(TagField field) {
        if (field instanceof Mp4TagReverseDnsField) {
            this.issuer = ((Mp4TagReverseDnsField) field).getIssuer();
            this.descriptor = ((Mp4TagReverseDnsField) field).getDescriptor();
            this.content = ((Mp4TagReverseDnsField) field).getContent();
        }
    }

    public String getContent() { return content; }

    protected byte[] getDataBytes() throws UnsupportedEncodingException { return content.getBytes(getEncoding()); }

    public String getEncoding() { return "UTF_8"; }

    /** Convert back to raw content, includes ----,mean,name and data atom as views as one thing externally
     * @return
     * @throws UnsupportedEncodingException */
    public byte[] getRawContent() throws UnsupportedEncodingException {
//        try {
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//
//            //Create Meanbox data
//            byte[] issuerRawData = issuer.getBytes(getEncoding());
//            baos.write(Utils.getSizeBEInt32(Mp4BoxHeader.HEADER_LENGTH + Mp4MeanBox.PRE_DATA_LENGTH + issuerRawData.length));
//            baos.write(Utils.getDefaultBytes(Mp4MeanBox.IDENTIFIER, "ISO-8859-1"));
//            baos.write(new byte[]{0, 0, 0, 0});
//            baos.write(issuerRawData);
//
//            //Create Namebox data
//            byte[] nameRawData = descriptor.getBytes(getEncoding());
//            baos.write(Utils.getSizeBEInt32(Mp4BoxHeader.HEADER_LENGTH + Mp4NameBox.PRE_DATA_LENGTH + nameRawData.length));
//            baos.write(Utils.getDefaultBytes(Mp4NameBox.IDENTIFIER, "ISO-8859-1"));
//            baos.write(new byte[]{0, 0, 0, 0});
//            baos.write(nameRawData);
//
//            //Create DataBox data if we have data only
//            if (content.length() > 0)
//            {
//                baos.write(getRawContentDataOnly());
//            }
//            //Now wrap with reversedns box
//            ByteArrayOutputStream outerbaos = new ByteArrayOutputStream();
//            outerbaos.write(Utils.getSizeBEInt32(Mp4BoxHeader.HEADER_LENGTH + baos.size()));
//            outerbaos.write(Utils.getDefaultBytes(IDENTIFIER, "ISO-8859-1"));
//            outerbaos.write(baos.toByteArray());
//            return outerbaos.toByteArray();
//
//        }
//        catch (IOException ioe)
//        {
//            //This should never happen as were not actually writing to/from a file
//            throw new RuntimeException(ioe);
//        }
    	return null;
    }

    public byte[] getRawContentDataOnly() throws UnsupportedEncodingException {
//        try
//        {
//            //Create DataBox data
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            byte[] dataRawData = content.getBytes(getEncoding());
//            baos.write(Utils.getSizeBEInt32(Mp4BoxHeader.HEADER_LENGTH + Mp4DataBox.PRE_DATA_LENGTH + dataRawData.length));
//            baos.write(Utils.getDefaultBytes(Mp4DataBox.IDENTIFIER, "ISO-8859-1"));
//            baos.write(new byte[]{0});
//            baos.write(new byte[]{0, 0, (byte) getFieldType().getFileClassId()});
//            baos.write(new byte[]{0, 0, 0, 0});
//            baos.write(dataRawData);
//            return baos.toByteArray();
//        }
//        catch (IOException ioe)
//        {
//            //This should never happen as were not actually writing to/from a file
//            throw new RuntimeException(ioe);
//        }
    	return null;
    }

    public boolean isBinary() { return false; }

    public boolean isEmpty() { return this.content.trim().equals(""); }

    public void setContent(String s) { this.content = s; }

    public void setEncoding(String s) { /* Not allowed */ }

    public String toString() { return content; }

    /** @return the issuer */
    public String getIssuer() { return issuer; }

    /** @return the descriptor */
    public String getDescriptor() { return descriptor; }

    /** Set the issuer, usually reverse dns of the Companies domain
     * @param issuer */
    public void setIssuer(String issuer) { this.issuer = issuer; }

    /** Set the descriptor for the data (what type of data it is)
     * @param descriptor */
    public void setDescriptor(String descriptor) { this.descriptor = descriptor; }
}