package outag.formats.ogg;

import outag.formats.generic.AbstractTag;
import outag.formats.generic.TagField;
import outag.formats.ogg.util.OggTagField;

//FIXME: Handle previously handled DESCRIPTION|COMMENT and TRACK|TRACKNUMBER
public class OggTag extends AbstractTag {
    private String vendor = "";
    //This is the vendor string that will be written if no other is supplied
	public static final String DEFAULT_VENDOR = "outag - The Musical Box";

    protected TagField createAlbumField(String content) {
        return new OggTagField("ALBUM", content);
    }

    protected TagField createArtistField(String content) {
        return new OggTagField("ARTIST", content);
    }

    protected TagField createCommentField(String content) {
        return new OggTagField("DESCRIPTION", content);
    }

    protected TagField createGenreField(String content) {
        return new OggTagField("GENRE", content);
    }

    protected TagField createTitleField(String content) {
        return new OggTagField("TITLE", content);
    }

    protected TagField createTrackField(String content) {
        return new OggTagField("TRACKNUMBER", content);
    }

    protected TagField createYearField(String content) {
        return new OggTagField("DATE", content);
    }

    protected String getAlbumId() { return "ALBUM"; }

    protected String getArtistId() { return "ARTIST"; }

    protected String getCommentId() { return "DESCRIPTION"; }

    protected String getGenreId() { return "GENRE"; }

    protected String getTitleId() { return "TITLE"; }

    protected String getTrackId() { return "TRACKNUMBER"; }
    
    protected String getYearId() { return "DATE"; }    

    public String getVendor() {
		if( !this.vendor.trim().equals("") )
		    return vendor;
    
		return DEFAULT_VENDOR;
    }

    public void setVendor(String vendor) { this.vendor = vendor == null ? "" : vendor; }
    
    protected boolean isAllowedEncoding(String enc) { return enc.equals("UTF-8"); }
	
    public String toString() { return "OGG " + super.toString(); }
}