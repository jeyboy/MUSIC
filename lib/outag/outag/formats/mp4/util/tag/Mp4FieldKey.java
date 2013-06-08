package outag.formats.mp4.util.tag;

import outag.formats.mp4.util.tag.Mp4FieldType;

/** Starting list of known mp4 metadata fields that follow the Parent,Data or ---,issuer,name,data
 * convention. Atoms that contain metadata in other formats are not listed here because they need to be processed
 * specially. <br>
 * Simple metaitems use the parent atom id as their identifier whereas reverse dns (----) atoms use
 * the reversedns,issuer and name fields as their identifier. When the atom is non-0standard but follws the rules
 * we list it here with an additional Tagger field to indicate where the field was originally designed. <br>
 * From:
 * http://www.hydrogenaudio.org/forums/index.php?showtopic=29120&st=0&p=251686&#entry251686
 * http://wiki.musicbrainz.org/PicardQt/TagMapping
 * http://atomicparsley.sourceforge.net/mpeg-4files.html */
public enum Mp4FieldKey {
    ARTIST("©ART", Mp4TagFieldSubType.TEXT, Mp4FieldType.TEXT),
    ALBUM("©alb", Mp4TagFieldSubType.TEXT, Mp4FieldType.TEXT),
    ALBUM_ARTIST("aART", Mp4TagFieldSubType.TEXT, Mp4FieldType.TEXT),
    GENRE_CUSTOM("©gen", Mp4TagFieldSubType.TEXT, Mp4FieldType.TEXT),
    GENRE("gnre", Mp4TagFieldSubType.GENRE, Mp4FieldType.IMPLICIT),
    TITLE("©nam", Mp4TagFieldSubType.TEXT, Mp4FieldType.TEXT),
    TRACK("trkn", Mp4TagFieldSubType.TRACK_NO, Mp4FieldType.IMPLICIT),
    BPM("tmpo", Mp4TagFieldSubType.BYTE, Mp4FieldType.INTEGER, 2),
    DAY("©day", Mp4TagFieldSubType.TEXT, Mp4FieldType.TEXT),
    COMMENT("©cmt", Mp4TagFieldSubType.TEXT, Mp4FieldType.TEXT),
    COMPOSER("©wrt", Mp4TagFieldSubType.TEXT, Mp4FieldType.TEXT),
    GROUPING("©grp", Mp4TagFieldSubType.TEXT, Mp4FieldType.TEXT),
    DISCNUMBER("disk", Mp4TagFieldSubType.DISC_NO, Mp4FieldType.IMPLICIT),
    LYRICS("©lyr", Mp4TagFieldSubType.TEXT, Mp4FieldType.TEXT),
    RATING("rtng", Mp4TagFieldSubType.BYTE, Mp4FieldType.INTEGER,1),   //AFAIK Cant be setField in itunes, but if setField to explicit itunes will show as explicit
    ENCODER("©too", Mp4TagFieldSubType.TEXT, Mp4FieldType.TEXT),
    COMPILATION("cpil", Mp4TagFieldSubType.BYTE, Mp4FieldType.INTEGER, 1),
    COPYRIGHT("cprt", Mp4TagFieldSubType.TEXT, Mp4FieldType.TEXT),
    CATEGORY("catg", Mp4TagFieldSubType.TEXT, Mp4FieldType.TEXT),
    KEYWORD("keyw", Mp4TagFieldSubType.TEXT, Mp4FieldType.TEXT),
    DESCRIPTION("desc", Mp4TagFieldSubType.TEXT, Mp4FieldType.TEXT),
    ARTIST_SORT("soar", Mp4TagFieldSubType.TEXT, Mp4FieldType.TEXT),
    ALBUM_ARTIST_SORT("soaa", Mp4TagFieldSubType.TEXT, Mp4FieldType.TEXT),
    ALBUM_SORT("soal", Mp4TagFieldSubType.TEXT, Mp4FieldType.TEXT),
    TITLE_SORT("sonm", Mp4TagFieldSubType.TEXT, Mp4FieldType.TEXT),
    COMPOSER_SORT("soco", Mp4TagFieldSubType.TEXT, Mp4FieldType.TEXT),
    SHOW_SORT("sosn", Mp4TagFieldSubType.TEXT, Mp4FieldType.TEXT),
    SHOW("tvsh", Mp4TagFieldSubType.TEXT, Mp4FieldType.TEXT),      //tv show but also used just as show
    ARTWORK("covr", Mp4TagFieldSubType.ARTWORK, Mp4FieldType.COVERART_JPEG),
    PURCHASE_DATE("purd", Mp4TagFieldSubType.TEXT, Mp4FieldType.TEXT),
    MUSICBRAINZ_ARTISTID("com.apple.iTunes", "MusicBrainz Artist Id", Mp4FieldType.TEXT),
    MUSICBRAINZ_ALBUMID("com.apple.iTunes", "MusicBrainz Album Id", Mp4FieldType.TEXT),
    MUSICBRAINZ_ALBUMARTISTID("com.apple.iTunes", "MusicBrainz Album Artist Id", Mp4FieldType.TEXT),
    MUSICBRAINZ_ORIGINALALBUMID("com.apple.iTunes", "MusicBrainz Original Album Id", Mp4FieldType.TEXT),
    MUSICBRAINZ_RELEASE_GROUPID("com.apple.iTunes", "MusicBrainz Release Group Id", Mp4FieldType.TEXT),
    MUSICBRAINZ_TRACKID("com.apple.iTunes", "MusicBrainz Track Id", Mp4FieldType.TEXT),
    MUSICBRAINZ_WORKID("com.apple.iTunes", "MusicBrainz Work Id", Mp4FieldType.TEXT),
    MUSICBRAINZ_DISCID("com.apple.iTunes", "MusicBrainz Disc Id", Mp4FieldType.TEXT),
    MUSICIP_PUID("com.apple.iTunes", "MusicIP PUID", Mp4FieldType.TEXT),
    ASIN("com.apple.iTunes", "ASIN", Mp4FieldType.TEXT),
    MUSICBRAINZ_ALBUM_STATUS("com.apple.iTunes", "MusicBrainz Album Status", Mp4FieldType.TEXT),
    MUSICBRAINZ_ALBUM_TYPE("com.apple.iTunes", "MusicBrainz Album Type", Mp4FieldType.TEXT),
    RELEASECOUNTRY("com.apple.iTunes", "MusicBrainz Album Release Country", Mp4FieldType.TEXT),
    PART_OF_GAPLESS_ALBUM("pgap", Mp4TagFieldSubType.BYTE, Mp4FieldType.INTEGER),
    ITUNES_SMPB("com.apple.iTunes", "iTunSMPB", Mp4FieldType.TEXT),
    ITUNES_NORM("com.apple.iTunes", "iTunNORM", Mp4FieldType.TEXT),
    CDDB_1("com.apple.iTunes", "iTunes_CDDB_1", Mp4FieldType.TEXT),
    CDDB_TRACKNUMBER("com.apple.iTunes", "iTunes_CDDB_TrackNumber", Mp4FieldType.TEXT),
    CDDB_IDS("com.apple.iTunes", "iTunes_CDDB_IDs", Mp4FieldType.TEXT),
    LANGUAGE("com.apple.iTunes", "LANGUAGE", Mp4FieldType.TEXT),
    KEY("com.apple.iTunes", "KEY", Mp4FieldType.TEXT),
    FBPM("com.apple.iTunes", "fBPM", Mp4FieldType.TEXT),
    ACOUSTID_FINGERPRINT("com.apple.iTunes", "Acoustid Fingerprint", Mp4FieldType.TEXT),
    ACOUSTID_FINGERPRINT_OLD("com.apple.iTunes", "AcoustId Fingerprint", Mp4FieldType.TEXT),
    ACOUSTID_ID("com.apple.iTunes", "Acoustid Id", Mp4FieldType.TEXT),
    COUNTRY("com.apple.iTunes", "Country", Mp4FieldType.TEXT),

    //AFAIK These arent actually used by Audio Only files, but there is nothing to prevent them being used
    CONTENT_TYPE("stik",Mp4TagFieldSubType.BYTE, Mp4FieldType.INTEGER, 1),
    TOOL("tool",Mp4TagFieldSubType.BYTE, Mp4FieldType.INTEGER, 4),
    PODCAST_KEYWORD("keyw",Mp4TagFieldSubType.TEXT, Mp4FieldType.TEXT),
    PODCAST_URL("purl",Mp4TagFieldSubType.NUMBER, Mp4FieldType.IMPLICIT),   //TODO Actually seems to store Mp4FieldType.TEXT but is marked as numeric!
    EPISODE_GLOBAL_ID("egid",Mp4TagFieldSubType.NUMBER, Mp4FieldType.IMPLICIT),   //TODO Actually seems to store Mp4FieldType.TEXT but is marked as numeric!
    TV_NETWORK("tvnn",Mp4TagFieldSubType.TEXT, Mp4FieldType.TEXT),
    TV_EPISODE_NUMBER("tven",Mp4TagFieldSubType.TEXT, Mp4FieldType.TEXT),
    TV_SEASON("tvsn",Mp4TagFieldSubType.BYTE, Mp4FieldType.INTEGER, 1),
    TV_EPISODE("tves",Mp4TagFieldSubType.BYTE, Mp4FieldType.INTEGER, 1),

    //These seem to be used in DRM Files, of type byte , we need to know the byte length to allow them to be written
    //back correctly on saving them, we don't provides options to modify them as may break drm
    AP_ID("apID",Mp4TagFieldSubType.UNKNOWN, Mp4FieldType.TEXT),
    AT_ID("atID",Mp4TagFieldSubType.UNKNOWN, Mp4FieldType.INTEGER, 4),
    CN_ID("cnID",Mp4TagFieldSubType.UNKNOWN, Mp4FieldType.INTEGER, 4),
    PL_ID("plID",Mp4TagFieldSubType.UNKNOWN, Mp4FieldType.INTEGER, 8),
    GE_ID("geID",Mp4TagFieldSubType.UNKNOWN, Mp4FieldType.INTEGER, 4),
    SF_ID("sfID",Mp4TagFieldSubType.UNKNOWN, Mp4FieldType.INTEGER, 4),
    AK_ID("akID",Mp4TagFieldSubType.UNKNOWN, Mp4FieldType.INTEGER, 1),

    //Media Monkey3 beta
    LYRICIST_MM3BETA("lyrc",Mp4TagFieldSubType.TEXT, Mp4FieldType.TEXT),
    CONDUCTOR_MM3BETA("cond",Mp4TagFieldSubType.TEXT, Mp4FieldType.TEXT),
    ISRC_MMBETA("isrc",Mp4TagFieldSubType.TEXT, Mp4FieldType.TEXT),
    MOOD_MM3BETA("mood",Mp4TagFieldSubType.TEXT, Mp4FieldType.TEXT),
    SCORE("rate",Mp4TagFieldSubType.TEXT, Mp4FieldType.TEXT),    //As in mark out of 100
    ORIGINAL_ARTIST("oart",Mp4TagFieldSubType.TEXT, Mp4FieldType.TEXT),
    ORIGINAL_ALBUM_TITLE("otit",Mp4TagFieldSubType.TEXT, Mp4FieldType.TEXT),
    ORIGINAL_LYRICIST("olyr",Mp4TagFieldSubType.TEXT, Mp4FieldType.TEXT),
    INVOLVED_PEOPLE("peop",Mp4TagFieldSubType.TEXT, Mp4FieldType.TEXT),
    TEMPO("empo",Mp4TagFieldSubType.TEXT, Mp4FieldType.TEXT),
    OCCASION("occa",Mp4TagFieldSubType.TEXT, Mp4FieldType.TEXT),
    QUALITY("qual",Mp4TagFieldSubType.TEXT, Mp4FieldType.TEXT),
    CUSTOM_1("cus1",Mp4TagFieldSubType.TEXT, Mp4FieldType.TEXT),
    CUSTOM_2("cus2",Mp4TagFieldSubType.TEXT, Mp4FieldType.TEXT),
    CUSTOM_3("cus3",Mp4TagFieldSubType.TEXT, Mp4FieldType.TEXT),
    CUSTOM_4("cus4",Mp4TagFieldSubType.TEXT, Mp4FieldType.TEXT),
    CUSTOM_5("cus5",Mp4TagFieldSubType.TEXT, Mp4FieldType.TEXT),

    //Media Monkey 3.0.6 Onwards
    MM_PUBLISHER("com.apple.iTunes", "ORGANIZATION", Mp4FieldType.TEXT),
    MM_ORIGINAL_ARTIST("com.apple.iTunes", "ORIGINAL ARTIST", Mp4FieldType.TEXT),
    MM_ORIGINAL_ALBUM_TITLE("com.apple.iTunes", "ORIGINAL ALBUM", Mp4FieldType.TEXT),
    MM_ORIGINAL_LYRICIST("com.apple.iTunes", "ORIGINAL LYRICIST", Mp4FieldType.TEXT),
    MM_INVOLVED_PEOPLE("com.apple.iTunes", "INVOLVED PEOPLE", Mp4FieldType.TEXT),
    MM_ORIGINAL_YEAR("com.apple.iTunes", "ORIGINAL YEAR", Mp4FieldType.TEXT),
    MM_TEMPO("com.apple.iTunes", "TEMPO", Mp4FieldType.TEXT),
    MM_OCCASION("com.apple.iTunes", "OCCASION", Mp4FieldType.TEXT),
    MM_QUALITY("com.apple.iTunes", "QUALITY", Mp4FieldType.TEXT),
    MM_CUSTOM_1("com.apple.iTunes", "CUSTOM1", Mp4FieldType.TEXT),
    MM_CUSTOM_2("com.apple.iTunes", "CUSTOM2", Mp4FieldType.TEXT),
    MM_CUSTOM_3("com.apple.iTunes", "CUSTOM3", Mp4FieldType.TEXT),
    MM_CUSTOM_4("com.apple.iTunes", "CUSTOM4", Mp4FieldType.TEXT),
    MM_CUSTOM_5("com.apple.iTunes", "CUSTOM5", Mp4FieldType.TEXT),

    //Picard Qt
    LYRICIST("com.apple.iTunes", "LYRICIST", Mp4FieldType.TEXT),
    CONDUCTOR("com.apple.iTunes", "CONDUCTOR", Mp4FieldType.TEXT),
    REMIXER("com.apple.iTunes", "REMIXER", Mp4FieldType.TEXT),
    ENGINEER("com.apple.iTunes", "ENGINEER", Mp4FieldType.TEXT),
    PRODUCER("com.apple.iTunes", "PRODUCER", Mp4FieldType.TEXT),
    DJMIXER("com.apple.iTunes", "DJMIXER", Mp4FieldType.TEXT),
    MIXER("com.apple.iTunes", "MIXER", Mp4FieldType.TEXT),
    ARRANGER("com.apple.iTunes", "ARRANGER", Mp4FieldType.TEXT),
    MOOD("com.apple.iTunes", "MOOD", Mp4FieldType.TEXT),
    ISRC("com.apple.iTunes", "ISRC", Mp4FieldType.TEXT),
    MEDIA("com.apple.iTunes", "MEDIA", Mp4FieldType.TEXT),
    LABEL("com.apple.iTunes", "LABEL", Mp4FieldType.TEXT),
    CATALOGNO("com.apple.iTunes", "CATALOGNUMBER", Mp4FieldType.TEXT),
    BARCODE("com.apple.iTunes", "BARCODE", Mp4FieldType.TEXT),

    //Jaikoz
    URL_LYRICS_SITE("com.apple.iTunes", "URL_LYRICS_SITE", Mp4FieldType.TEXT),
    URL_OFFICIAL_RELEASE_SITE("com.apple.iTunes", "URL_OFFICIAL_RELEASE_SITE", Mp4FieldType.TEXT),
    URL_DISCOGS_RELEASE_SITE("com.apple.iTunes", "URL_DISCOGS_RELEASE_SITE", Mp4FieldType.TEXT),
    URL_WIKIPEDIA_RELEASE_SITE("com.apple.iTunes", "URL_WIKIPEDIA_RELEASE_SITE", Mp4FieldType.TEXT),
    URL_OFFICIAL_ARTIST_SITE("com.apple.iTunes", "URL_OFFICIAL_ARTIST_SITE", Mp4FieldType.TEXT),
    URL_DISCOGS_ARTIST_SITE("com.apple.iTunes", "URL_DISCOGS_ARTIST_SITE", Mp4FieldType.TEXT),
    URL_WIKIPEDIA_ARTIST_SITE("com.apple.iTunes", "URL_WIKIPEDIA_ARTIST_SITE", Mp4FieldType.TEXT),
    SCRIPT("com.apple.iTunes", "SCRIPT", Mp4FieldType.TEXT),
    TAGS("com.apple.iTunes", "TAGS", Mp4FieldType.TEXT),
    ARTISTS("com.apple.iTunes", "ARTISTS", Mp4FieldType.TEXT),

    //Winamp
    WINAMP_PUBLISHER("com.nullsoft.winamp", "publisher", Mp4FieldType.TEXT),

    //Unknown
    KEYS("keys",Mp4TagFieldSubType.TEXT, Mp4FieldType.TEXT);
    
    private String fieldName;
    private Mp4TagFieldSubType subclassType;
    private String issuer;
    private String identifier;
    private Mp4FieldType fieldType;
    private int fieldLength;

    /** For usual metadata fields that use a data field
     * @param fieldName
     * @param fieldType of data atom */
    Mp4FieldKey(String fieldName, Mp4TagFieldSubType subclassType, Mp4FieldType fieldType) {
        this.fieldName      = fieldName;
        this.subclassType   = subclassType;
        this.fieldType      = fieldType;
    }

    /** For usual metadata fields that use a data field where the field length is fixed such as Byte fields
     * @param fieldName
     * @param fieldType
     * @param fieldLength */
    Mp4FieldKey(String fieldName, Mp4TagFieldSubType subclassType,Mp4FieldType fieldType, int fieldLength) {
        this.fieldName = fieldName;
        this.subclassType   = subclassType;
        this.fieldType = fieldType;
        this.fieldLength = fieldLength;
    }

    /** For reverse dns fields that use an internal fieldname of '----' and have  additional issuer
     * and identifier fields, we use all three seperated by a ':' ) to give us a unique key
     * @param issuer
     * @param identifier
     * @param fieldType  of data atom */
    Mp4FieldKey(String issuer, String identifier, Mp4FieldType fieldType) {
        this.issuer = issuer;
        this.identifier = identifier;
        this.fieldName = Mp4TagReverseDnsField.IDENTIFIER + ":" + issuer + ":" + identifier;
        this.subclassType = Mp4TagFieldSubType.REVERSE_DNS;
        this.fieldType = fieldType;
    }

    /** This is the value of the fieldname that is actually used to write mp4 */
    public String getFieldName() { return fieldName; }

    /** @return fieldtype */
    public Mp4FieldType getFieldType() { return fieldType; }

     /** @return subclassType */
    public Mp4TagFieldSubType getSubClassFieldType() { return subclassType; }

    /** @return true if this is a reverse dns key */
    public boolean isReverseDnsType() { return identifier.startsWith(Mp4TagReverseDnsField.IDENTIFIER); }

    /** @return issuer (Reverse Dns Fields Only) */
    public String getIssuer() { return issuer; }

    /** @return identifier (Reverse Dns Fields Only) */
    public String getIdentifier() { return identifier; }

    /** @return field length (currently only used by byte fields) */
    public int getFieldLength() { return fieldLength; }
}