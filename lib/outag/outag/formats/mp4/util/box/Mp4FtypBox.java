package outag.formats.mp4.util.box;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import outag.file_presentation.JBBuffer;

/** Ftyp (File Type) is the first atom, can be used to help identify the mp4 container type */
public class Mp4FtypBox {
    private String majorBrand;
    private int majorBrandVersion;
    private List<String> compatibleBrands = new ArrayList<String>();
    
    public Mp4FtypBox(JBBuffer data) throws IOException {
    	majorBrand = data.Str(4, "ISO-8859-1");
        majorBrandVersion = data.UBEInt();

        String brand;
        while(data.available() >= 4) {
    		brand = data.Str(4, "ISO-8859-1");
    		if (brand != null) {
	    		//Sometimes just extra groups of four nulls
	    		if (!brand.equals("\u0000\u0000\u0000\u0000"))
	    			compatibleBrands.add(brand);
    		}
        }  	
    }

    public String getMajorBrand() { return majorBrand; }

    public int getMajorBrandVersion() { return majorBrandVersion; }

    public List<String> getCompatibleBrands() { return compatibleBrands; }

    /** Major brand, helps identify what's contained in the file, used by major and compatible brands
     * but this is not an exhaustive list, so for that reason we don't force the values read from the file
     * to tie in with this enum. */
    public static enum Brand {
        ISO14496_1_BASE_MEDIA("isom", "ISO 14496-1"),
        ISO14496_12_BASE_MEDIA("iso2", "ISO 14496-12"),
        ISO14496_1_VERSION_1("mp41", "ISO 14496-1"),
        ISO14496_1_VERSION_2("mp42", "ISO 14496-2: Multi track with BIFS scenes"),
        QUICKTIME_MOVIE("qt  ", "Original Quicktime"),
        JVT_AVC("avc1", "JVT"),
        THREEG_MOBILE_MP4("MPA ", "3G Mobile"),
        APPLE_AAC_AUDIO("M4P ", "Apple Audio"),
        AES_ENCRYPTED_AUDIO("M4B ", "Apple encrypted Audio"),
        APPLE_AUDIO("mp71", "Apple Audio"),
        ISO14496_12_MPEG7_METADATA("mp71", "MAIN_SYNTHESIS"),
        APPLE_AUDIO_ONLY("M4A ", "M4A Audio"); //SOmetimes used by protected mutli track audio

        private String id;
        private String description;

        /** @param id          it is stored as in file
         * @param description human readable description */
        Brand(String id, String description) {
            this.id = id;
            this.description = description;
        }

        public String getId() { return id; }

        public String getDescription() { return description; }
    }
}