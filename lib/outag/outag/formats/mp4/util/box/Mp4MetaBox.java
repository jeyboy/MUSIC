package outag.formats.mp4.util.box;

import java.io.IOException;

import outag.file_presentation.JBBuffer;
import outag.formats.exceptions.CannotReadException;

/** This MP4 MetaBox is the parent of meta data, it usually contains four bytes of data
 * that needs to be processed before we can examine the children. But I also have a file that contains
 * meta (and no udta) that does not have this children data. */
public class Mp4MetaBox {
    public Mp4MetaBox(JBBuffer data) throws CannotReadException, IOException {
        //4-skip the meta flags and check they are the meta flags
        byte[] b = data.Array(4);
        if (b[0] != 0)
            throw new CannotReadException("Meta header block miss");
    }
}