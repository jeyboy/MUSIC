package outag.formats.mp4.util.tag;

import java.util.List;

import outag.file_presentation.JBBuffer;
import outag.formats.generic.TagField;
import outag.formats.mp4.util.box.Mp4Box;
import outag.formats.mp4.util.box.Mp4DataBox;

/** Represents simple text field that contains an array of number, <br>
 * But reads the data content as an array of 16 bit unsigned numbers */
public class Mp4TagTextNumberField extends Mp4TagTextField {
    public static final int NUMBER_LENGTH = 2;

    //Holds the numbers decoded
    protected List<Short> numbers;

    /** Create a new number, already parsed in subclasses
     * @param id
     * @param numberArray */
    public Mp4TagTextNumberField(String id, String numberArray) { super(id, numberArray); }

    public Mp4TagTextNumberField(String id, JBBuffer data) throws Exception {
        super(id, data);
    }

//    /** Recreate the raw data content from the list of numbers */
//    protected byte[] getDataBytes() {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        for (Short number : numbers) {
//            try { baos.write(Utils.getSizeBEInt16(number));
//            }
//            catch (IOException e)
//            {
//                //This should never happen because we are not writing to file at this point.
//                throw new RuntimeException(e);
//            }
//        }
//        return baos.toByteArray();
//    }

    public void copyContent(TagField field) {
        if (field instanceof Mp4TagTextNumberField) {
            this.content = ((Mp4TagTextNumberField) field).getContent();
            this.numbers = ((Mp4TagTextNumberField) field).getNumbers();
        }
    }

    /** @return type numeric */
    public Mp4FieldType getFieldType() { return Mp4FieldType.IMPLICIT; }

    protected void build(JBBuffer data) throws Exception {
        //Data actually contains a 'Data' Box so process data using this
    	Mp4Box header = Mp4Box.init(data, false);
        Mp4DataBox databox = new Mp4DataBox(header, data);
        dataSize = header.getLength();
        content = databox.getContent();
        numbers = databox.getNumbers();
    }

    /** @return the individual numbers making up this field */
    public List<Short> getNumbers() { return numbers; }
}