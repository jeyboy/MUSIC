package outag.audioformats.asf.data.wrapper;

import java.io.UnsupportedEncodingException;

import outag.audioformats.asf.data.ContentDescriptor;
import outag.audioformats.generic.TagField;

/** This class encapsulates a {@link outag.audioformats.asf.data.ContentDescriptor}and provides access to it. <br>
 * The content descriptor used for construction is copied. */
public class ContentDescriptorTagField implements TagField {

    /** This descriptor is wrapped. */
    private ContentDescriptor toWrap;

    /** Creates an instance. 
     * @param source - The descriptor which should be represented as a {@link TagField}. */
    public ContentDescriptorTagField(ContentDescriptor source) { this.toWrap = source.createCopy(); }

    public void copyContent(TagField field) { throw new UnsupportedOperationException("Not implemented yet."); }

    public String getId() { return toWrap.getName(); }

    public byte[] getRawContent() throws UnsupportedEncodingException { return toWrap.getRawData(); }

    public boolean isBinary() { return toWrap.getType() == ContentDescriptor.TYPE_BINARY; }

    public void isBinary(boolean b) {
        if (!b && isBinary()) throw new UnsupportedOperationException("No conversion supported.");
        toWrap.setBinaryValue(toWrap.getRawData());
    }

    public boolean isCommon() { return toWrap.isCommon(); }

    public boolean isEmpty() { return toWrap.isEmpty(); }

    public String toString() { return toWrap.getString(); }
}