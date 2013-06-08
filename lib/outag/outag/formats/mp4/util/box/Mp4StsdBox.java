package outag.formats.mp4.util.box;

import outag.file_presentation.JBBuffer;

/** StsdBox (sample (frame encoding) description box) <br>
 * 4 bytes version/flags = byte hex version + 24-bit hex flags (current = 0)
 * - > 4 bytes number of descriptions = long unsigned total (default = 1)
 * Then if audio contains mp4a, alac or drms box */
public class Mp4StsdBox {
    public static final int VERSION_FLAG_POS = 0;
    public static final int OTHER_FLAG_POS = 1;
    public static final int NO_OF_DESCRIPTIONS_POS = 4;

    public static final int VERSION_FLAG_LENGTH = 1;
    public static final int OTHER_FLAG_LENGTH = 3;
    public static final int NO_OF_DESCRIPTIONS_POS_LENGTH = 4;

    public Mp4StsdBox(JBBuffer data) {
    	data.skip(VERSION_FLAG_LENGTH + OTHER_FLAG_LENGTH + NO_OF_DESCRIPTIONS_POS_LENGTH);
    }
}