package outag.formats;

import java.io.File;

import outag.formats.generic.GenericTag;

public class AudioFile extends File {
	private static final long serialVersionUID = -1701440380394082212L;
	
	private EncodingInfo info;
	private Tag tag;
	private int id;
	
	public AudioFile(File f, EncodingInfo info) {
		super(f.getAbsolutePath());
		this.info = info;
		this.tag = new GenericTag();
	}

	public AudioFile(File f, EncodingInfo info, Tag tag) {
		super(f.getAbsolutePath());
		this.info = info;
		this.tag = tag;
	}
	

	/** @return	the bitrate */
	public int getBitrate() 				{ return info.getBitrate();	}
	
	/** @return	the number of audio channels */
	public int getChannelNumber() 			{ return info.getChannelNumber();	}

	/** @return	the encoding type */
	public String getEncodingType() 		{ return info.getEncodingType(); }

	/** @return	the extra encoding info */
	public String getExtraEncodingInfos() 	{ return info.getExtraEncodingInfos(); }

	/** @return	the duration in seconds */
	public int getLength() 					{ return info.getLength(); }
	
	/** @return the duration in seconds with fractions. */
	public float getPreciseLength() 		{ return info.getPreciseLength(); }
	
	/** @return	the sampling rate */
	public int getSamplingRate() 			{ return info.getSamplingRate(); }
	
	/** @return	the tag contained in this AudioFile, or a new one if file hasn't any tag. */
	public Tag getTag() 					{ return (tag == null) ? new GenericTag() : tag; }
	
	/** @return <code>true</code> if variable bitrate otherwise constant bitrate*/
	public boolean isVbr() 					{ return info.isVbr(); }
	
	/** @return	a multi-line string with the file path, the encoding informations, and the tag contents. */
	public String toString() 				{ return 
			"AudioFile " + getAbsolutePath() + 
			"  --------\n" + 
			info.toString() + "\n" +
			( (tag == null) ? "" : tag.toString()) + 
			"\n-------------------";	}
	
	public void setID(int id) 				{ this.id = id; }
	public int getID() 						{ return id;	}
}