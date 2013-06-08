package outag.formats.mp4.util.tag;

import outag.file_presentation.JBBuffer;
import outag.formats.mp4.util.box.Mp4Box;
import outag.formats.mp4.util.box.Mp4DataBox;
import outag.reference.GenreTypes;

/** Represents the Genre field , when user has selected from the set list of genres<br>
 * This class allows you to retrieve either the internal genre id, or the display value */
public class Mp4GenreField extends Mp4TagTextNumberField {
    public Mp4GenreField(String id, JBBuffer data) throws Exception {
        super(id, data);
    }

    protected void build(JBBuffer data) throws Exception {
        //Data actually contains a 'Data' Box so process data using this
    	Mp4Box header = Mp4Box.init(data, false);
        Mp4DataBox databox = new Mp4DataBox(header, data);
        dataSize = header.getLength();
        numbers = databox.getNumbers();
        content = databox.getContent();

        if (numbers != null) {
	        int genreId = numbers.get(0) - 1;
	        //Get value, we have to adjust index by one because iTunes labels from one instead of zero
	        if (genreId <= GenreTypes.getMaxGenreId())
	        	content = GenreTypes.getNameByCode(genreId);
        }

        //Some apps set genre to invalid value, we don't disguise this by setting content to empty string we leave
        //as null so apps can handle if they wish, but we do display a warning to make them aware.
//        if (content == null) warning
    }
}