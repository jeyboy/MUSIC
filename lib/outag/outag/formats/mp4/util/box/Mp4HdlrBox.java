package outag.formats.mp4.util.box;

import java.io.IOException;
import java.util.HashMap;

import outag.file_presentation.JBBuffer;

/** HdlrBox ( Handler box), <br>
 * Describes the type of metadata in the following list or minf atom */
public class Mp4HdlrBox {
    public static final int VERSION_FLAG_LENGTH     = 1;
    public static final int OTHER_FLAG_LENGTH       = 3;
    public static final int RESERVED_FLAG_LENGTH    = 4;
    public static final int HANDLER_LENGTH          = 4;
    public static final int RESERVED1_LENGTH        = 4;
    public static final int RESERVED2_LENGTH        = 4;
    public static final int RESERVED3_LENGTH        = 4;
    public static final int NAME_LENGTH             = 2;

    public static final int HANDLER_POS = VERSION_FLAG_LENGTH + OTHER_FLAG_LENGTH + RESERVED_FLAG_LENGTH;
    public static final int RESERVED1_POS    = HANDLER_POS + HANDLER_LENGTH;

    //Size used by iTunes, but other application could use different size because name field is variable
    public static final int ITUNES_META_HDLR_DAT_LENGTH =
        VERSION_FLAG_LENGTH     +
        OTHER_FLAG_LENGTH       +
        RESERVED_FLAG_LENGTH    +
        HANDLER_LENGTH          +
        RESERVED1_LENGTH        +
        RESERVED2_LENGTH        +
        RESERVED3_LENGTH        +
        NAME_LENGTH;

        
    private String          handlerType;     // 4 bytes;
    private MediaDataType   mediaDataType;

    private static HashMap<String, MediaDataType> mediaDataTypeMap;

    static {
        //Create maps to speed up lookup from raw value to enum
        mediaDataTypeMap = new HashMap<String, MediaDataType>();
        for (MediaDataType next : MediaDataType.values())
            mediaDataTypeMap.put(next.getId(), next);
    }
    
    public Mp4HdlrBox(JBBuffer data) throws IOException {
    	data.skip(VERSION_FLAG_LENGTH + OTHER_FLAG_LENGTH + RESERVED_FLAG_LENGTH); //Skip other flags
    	handlerType = data.Str(4, "ISO-8859-1");
        //To getFields human readable name
        mediaDataType = mediaDataTypeMap.get(handlerType);
    }

    public String getHandlerType() { return handlerType; }

    public MediaDataType getMediaDataType() { return mediaDataType; }

    public static enum MediaDataType {
        ODSM("odsm", "ObjectDescriptorStream - defined in ISO/IEC JTC1/SC29/WG11 - CODING OF MOVING PICTURES AND AUDIO"),
        CRSM("crsm", "ClockReferenceStream - defined in ISO/IEC JTC1/SC29/WG11 - CODING OF MOVING PICTURES AND AUDIO"),
        SDSM("sdsm", "SceneDescriptionStream - defined in ISO/IEC JTC1/SC29/WG11 - CODING OF MOVING PICTURES AND AUDIO"),
        M7SM("m7sm", "MPEG7Stream - defined in ISO/IEC JTC1/SC29/WG11 - CODING OF MOVING PICTURES AND AUDIO"),
        OCSM("ocsm", "ObjectContentInfoStream - defined in ISO/IEC JTC1/SC29/WG11 - CODING OF MOVING PICTURES AND AUDIO"),
        IPSM("ipsm", "IPMP Stream - defined in ISO/IEC JTC1/SC29/WG11 - CODING OF MOVING PICTURES AND AUDIO"),
        MJSM("mjsm", "MPEG-J Stream - defined in ISO/IEC JTC1/SC29/WG11 - CODING OF MOVING PICTURES AND AUDIO"),
        MDIR("mdir", "Apple Meta Data iTunes Reader"),
        MP7B("mp7b", "MPEG-7 binary XML"),
        MP7T("mp7t", "MPEG-7 XML"),
        VIDE("vide", "Video Track"),
        SOUN("soun", "Sound Track"),
        HINT("hint", "Hint Track"),
        APPL("appl", "Apple specific"),
        META("meta", "Timed Metadata track - defined in ISO/IEC JTC1/SC29/WG11 - CODING OF MOVING PICTURES AND AUDIO"),
        UNKW(null, "Unknow");

        private String id;
        private String description;

        MediaDataType(String id, String description) {
            this.id = id;
            this.description=description;
        }

        public String getId() { return id; }

        public String getDescription() { return description; }
    }
}