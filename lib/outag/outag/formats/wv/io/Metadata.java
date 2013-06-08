package outag.formats.wv.io;

import headers.RIFF;

import outag.file_presentation.Parseable;


//METADATA TAGS
//
//These tags are not to be confused with the metadata sub-blocks described above
//but are specialized tags for storing user data on many formats of audio files.
//The tags recommended for use with WavPack files (and the ones that the WavPack
//supplied plugins and programs will work with) are ID3v1 and APEv2. The ID3v1
//tags are somewhat primitive and limited, but are supported for legacy purposes.
//The more recommended tagging format is APEv2 because of its rich functionality
//and broad software support (it is also used on Monkey's Audio and Musepack
//files). Both the APEv2 tags and/or ID3v1 tags must come at the end of the
//WavPack file, with the ID3v1 coming last if both are present.
//
//For the APEv2 tags, the following field names are officially supported and
//recommended by WavPack (although there are no restrictions on what field names
//may be used):
//
//    Artist
//    Title
//    Album
//    Track
//    Year
//    Genre
//    Comment
//    Cuesheet (note: may include replay gain info as remarks)
//    Replaygain_Track_Gain
//    Replaygain_Track_Peak
//    Replaygain_Album_Gain
//    Replaygain_Album_Peak
//    Cover Art (Front)
//    Cover Art (Back)
//    Log



public class Metadata {
	
//	Flags for ID
	final short IGNORE_FOR_DECODER = 32; //decoder may ignore data contained here
	final short ODD_DATA_SIZE = 64;    // actual data byte length is 1 less
	final short LARGE_DATA_SIZE = 128;    // large block (> 255 words)	
	
	
//	ID
	final short DUMMY = 0;     // could be used to pad WavPack blocks
	final short DECORR_TERMS = 2;     // decorrelation terms & deltas (fixed)
	final short DECORR_WEIGHTS = 3;     // initial decorrelation weights
	final short DECORR_SAMPLES = 4;     // decorrelation sample history
	final short ENTROPY_VARS = 5;     // initial entropy variables
	final short HYBRID_PROFILE = 6;     // entropy variables specific to hybrid mode
	final short SHAPING_WEIGHTS = 7;     // info needed for hybrid lossless (wvc) mode
	final short FLOAT_INFO = 8;     // specific info for floating point decode
	final short INT32_INFO = 9;     // specific info for decoding integers > 24
//	                            // bits, or data requiring shift after decode
	final short WV_BITSTREAM = 10;     // normal compressed audio bitstream (wv file)
	final short WVC_BITSTREAM = 11;     // correction file bitstream (wvc file)
	final short WVX_BITSTREAM = 12;     // special extended bitstream for floating
//	                            // point data or integers > 24 bit (can be
//	                            // in either wv or wvc file, depending...)
	final short CHANNEL_INFO = 13;     // contains channel count and channel_mask
	
	final short METADATA_FUNCTION = 31; //metadata function
	
	final short RIFF_HEADER =  33;    // RIFF header for .wav files (before audio)
	final short RIFF_TRAILER = 34;    // RIFF trailer for .wav files (after audio)
	final short CONFIG_BLOCK = 35;    // some encoding details for info purposes
	final short MD5_CHECKSUM = 36;    // 16-byte MD5 sum of raw audio data
	final short SAMPLE_RATE = 37;    // non-standard sampling rate info
	
	String t;
	

//    uchar id;                   // mask  meaning
//                                // ----  -------
//                                // 0x1f  metadata function
//                                // 0x20  decoder need not understand metadata
//                                // 0x40  actual data byte length is 1 less
//                                // 0x80  large block (> 255 words)
//
//    uchar word_size;            // small block: data size in words (padded)
//          or...
//    uchar word_size [3];        // large block: data size in words (padded,
//                                    little-endian)	
//    uint16_t data [word_size];  // data, padded to an even # of bytes
	
	RIFF riffHeader;
	
	public Metadata(Parseable p) throws Exception {
		short b = p.UByte();
		int size;
		
		if (b == LARGE_DATA_SIZE)
			size = p.UInt();
		else
			size = p.UIByte();
				
		switch(b) {
			case DECORR_TERMS:
				t = p.Str(size);
//				Decorrelation terms are stored in one byte, lower 5 bits indicate predictor type, high 3 bits contain delta value.
				//
//					Possible predictor values:
				//
//					 0-5 - predictors for stereo, only predictors 2-4 are implemented
//					 6-12 - predictor uses 1-7 samples for prediction
//					 13-16 - reserved
//					 17-18 - predictor does prediction by two samples				
				break;
			
			case DECORR_WEIGHTS:
//				t = p.Str(size);
				
//				 n = getchar() << 3;
//				 if(n > 0) n += (n + 64) >> 7;
				
				break;
		
			case RIFF_HEADER:
				riffHeader = new RIFF(p);				
				break;
				
			
			case RIFF_TRAILER:
				t = p.Str(size);
				break;
			
			case CHANNEL_INFO:
				t = p.Str(size);
				break;
		
		
			case METADATA_FUNCTION:
				t = p.Str(size);
				break;		
			case IGNORE_FOR_DECODER:
				t = p.Str(size);
				break;
			case ODD_DATA_SIZE:
				t = p.Str(size);
				break;
			case LARGE_DATA_SIZE:
				p.skip(size);
				break;
			default:
				p.skip(size);
				break;
		}
	}
}