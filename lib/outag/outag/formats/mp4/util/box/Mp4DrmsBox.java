package outag.formats.mp4.util.box;

import outag.file_presentation.JBBuffer;

/** DrmsBox Replaces mp4a box on drm files <br>
 * Need to skip over data in order to find esds atom <br>
 * Specification not known, so just look for byte by byte 'esds' and then step back four bytes for size */
public class Mp4DrmsBox {
    /** @param buffer data of box (doesn't include header data) */
    public Mp4DrmsBox(JBBuffer buffer) {
        while (buffer.available() > 0) {
            byte next = buffer.get();
            if (next != (byte) 'e')
                continue;

            //Have we found esds identifier, if so adjust buffer to start of esds atom
            if ((buffer.get() == (byte) 's') & (buffer.get() == (byte) 'd') & (buffer.get() == (byte) 's')) {
            	buffer.skip(-8);
                return;
            }
        }
    }
}