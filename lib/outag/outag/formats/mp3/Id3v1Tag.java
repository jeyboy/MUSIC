package outag.formats.mp3;

import outag.formats.generic.GenericTag;
import outag.reference.GenreTypes;

public class Id3v1Tag extends GenericTag {
	protected boolean isAllowedEncoding(String enc) { return enc.equals("ISO-8859-1"); }
	
	public String translateGenre(byte b) {
		int i = b & 0xFF;

		return (i == 255 || i > GenreTypes.getMaxGenreId()) ? "" : GenreTypes.getNameByCode(i);
	}
	
	public String toString() { return "Id3v1 "+super.toString(); }
}