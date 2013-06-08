package outag.reference;

import java.util.HashMap;
import java.util.Map;

/** Pictures types for Attached Pictures <br>
 * Note this list is used by APIC and PIC frames within ID3v2. It is also used by Flac 
 * format Picture blocks and WMA Picture fields. */
public class PictureTypes {
    public static final int PICTURE_TYPE_FIELD_SIZE = 1;
    public static final String DEFAULT_VALUE = "Cover (front)";
    public static final Integer DEFAULT_ID = 3;

    private static int MAX_STANDARD_GENRE_ID = 125;

    /** @return the maximum genreId that is part of the official Standard, genres above
     *  this were added by Winamp later. */
    public static int getMaxStandardGenreId() { return MAX_STANDARD_GENRE_ID; }

    private static Map<Integer, Script> codeMap;
    private static Map<String, Script> descriptionMap;

    static {
        codeMap = new HashMap<Integer, Script>();
        for (Script script : Script.values())
            codeMap.put(script.code, script);

        descriptionMap = new HashMap<String, Script>();
        for (Script script : Script.values())
            descriptionMap.put(script.description, script);
    }

    /** @param code
     * @return enum with this two letter code */
    public static Script getScriptByCode(String code) { return codeMap.get(code); }

    /** @param description
     * @return enum with this description */
    public static Script getScriptByDescription(String description) {
        return descriptionMap.get(description);
    }

    public static enum Script {
        OTHER(0, "Other"),
        PNG(1, "32x32 pixels 'file icon' (PNG only)"),
        ICON(2, "Other file icon"),
        FRONT_COVER(3, "Cover (front)"),
        BACK_COVER(4, "Cover (back)"),
        LEAFLET(5, "Leaflet page"),
        MEDIA(6, "Media (e.g. label side of CD)"),
        SOLOIST(7, "Lead artist/lead performer/soloist"),
        ARTIST(8, "Artist/performer"),
        CONDUCTOR(9, "Conductor"),
        ORCHESTRA(10, "Band/Orchestra"),
        COMPOSER(11, "Composer"),
        LYRICIST(12, "Lyricist/text writer"),
        RECORDING_LOCATION(13, "Recording Location"),
        DURING_RECORDING(14, "During recording"),
        DURING_PERFOMANCE(15, "During performance"),
        SCREEN_CAPTURE(16, "Movie/video screen capture"),
        COLOURED_FISH(17, "A bright coloured fish"),
        ILLUSTRATION(18, "Illustration"),
        ARTIST_LOGOTYPE(19, "Band/artist logotype"),
        STUDIO_LOGOTYPE(20, "Publisher/Studio logotype");    	

        private int code;
        private String description;

        Script(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public int getCode() { return code; }

        public String getDescription() { return description; }

        public String toString() { return getDescription(); }
    }    
}