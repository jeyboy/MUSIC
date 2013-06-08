package outag.formats.mp4.util.box;

import java.io.IOException;

import outag.file_presentation.JBBuffer;

/** MdhdBox ( media (stream) header), holds the Sampling Rate used. */
public class Mp4MdhdBox {
    public static final int VERSION_FLAG_POS = 0;
    public static final int OTHER_FLAG_POS = 1;
    public static final int CREATED_DATE_SHORT_POS = 4;
    public static final int MODIFIED_DATE_SHORT_POS = 8;
    public static final int TIMESCALE_SHORT_POS = 12;
    public static final int DURATION_SHORT_POS = 16;

    public static final int CREATED_DATE_LONG_POS = 4;
    public static final int MODIFIED_DATE_LONG_POS = 12;
    public static final int TIMESCALE_LONG_POS = 20;
    public static final int DURATION_LONG_POS = 24;

    public static final int VERSION_FLAG_LENGTH = 1;
    public static final int OTHER_FLAG_LENGTH = 3;
    public static final int CREATED_DATE_SHORT_LENGTH = 4;
    public static final int MODIFIED_DATE_SHORT_LENGTH = 4;
    public static final int CREATED_DATE_LONG_LENGTH = 8;
    public static final int MODIFIED_DATE_LONG_LENGTH = 8;
    public static final int TIMESCALE_LENGTH = 4;
    public static final int DURATION_SHORT_LENGTH = 4;
    public static final int DURATION_LONG_LENGTH = 8;

    private static final int LONG_FORMAT = 1;

    private int samplingRate;
    long timeLength;

    public Mp4MdhdBox(JBBuffer data) throws IOException {
        int version = data.read();

        if (version == LONG_FORMAT) {
        	data.pos(TIMESCALE_LONG_POS);
            samplingRate = data.UBEInt();
            timeLength = data.UBELong();
        } else {
        	data.pos(TIMESCALE_SHORT_POS);
            samplingRate = data.UBEInt();
            timeLength = data.UBEInt();
        }
    }

    public int getSampleRate() { return samplingRate; }
}