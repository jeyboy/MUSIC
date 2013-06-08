package outag.formats.mp4.util.tag;

import java.io.UnsupportedEncodingException;
import outag.file_presentation.JBBuffer;
import outag.formats.generic.TagField;
import outag.formats.generic.TagTextField;
import outag.formats.mp4.util.box.Mp4Box;
import outag.formats.mp4.util.box.Mp4DataBox;

/** Represents a single text field <p/>
 * <p>Mp4 metadata normally held as follows:
 * <pre>
 * MP4Box Parent contains
 *      :length (includes length of data child)  (4 bytes)
 *      :name         (4 bytes)
 *      :child with
 *          :length          (4 bytes)
 *          :name 'Data'     (4 bytes)
 *          :atom version    (1 byte)
 *          :atom type flags (3 bytes)
 *          :null field      (4 bytes)
 *          :data
 * </pre>
 * <p/>
 * <p>Note:This class is initialized with the child data atom only, the parent data has already been processed, this may
 * change as it seems that code should probably be encapsulated into this. Whereas the raw content returned by the
 * getRawContent() contains the byte data for parent and child. */
public class Mp4TagTextField extends Mp4TagField implements TagTextField {
    protected int dataSize;
    protected String content;

    /**Construct from File
     * @param head   parent id
     * @param data atom data
     * @throws Exception */
    public Mp4TagTextField(String id, JBBuffer raw) throws Exception {
        super(id, raw);
    }

    /** Construct new Field
     * @param id      parent id
     * @param content data atom data */
    public Mp4TagTextField(String id, String content) {
        super(id);
        this.content = content;
    }

    protected void build(JBBuffer data) throws Exception {
    	Mp4Box header = Mp4Box.init(data, false);
        Mp4DataBox databox = new Mp4DataBox(header, data);
        dataSize = header.getLength();
        content = databox.getContent();
    }

    public void copyContent(TagField field) {
        if (field instanceof Mp4TagTextField)
            this.content = ((Mp4TagTextField) field).getContent();
    }

    public String getContent() { return content; }

    protected byte[] getDataBytes() throws UnsupportedEncodingException {
        return content.getBytes(getEncoding());
    }

    public Mp4FieldType getFieldType() { return Mp4FieldType.TEXT; }

    public String getEncoding() { return "UTF-8"; }

    public boolean isBinary() { return false; }

    public boolean isEmpty() { return content == null || content.trim().length() == 0; }

    public void setContent(String s) { this.content = s; }

    public void setEncoding(String s) { /* Not allowed */ }

    public String toString() { return content; }

	public byte[] getRawContent() throws UnsupportedEncodingException { return null; }
}