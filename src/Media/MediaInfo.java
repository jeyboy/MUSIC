package media;

import java.io.File;
import java.util.List;
import java.util.Vector;

import service.Errorist;
import service.IOOperations;
import service.Utils;

import outag.formats.AudioFile;
import outag.formats.AudioFileIO;
import outag.formats.Tag;
import outag.formats.exceptions.CannotReadException;

public class MediaInfo {
	static public String sitesFilter(String title)				{ return title.replaceAll("([\\(\\[](http:\\/\\/)*(www\\.)*([a-z0-9р-џ])+\\.[a-z]+[\\]\\)])", ""); }
	static public String forwardNumberPreFilter(String title)	{ return title.replaceAll("\\A\\d{1,}.|\\(\\w*\\d{1,}\\w*\\)", ""); }
	static public String spacesFilter(String title) 			{ return title.replaceAll("[^0-9A-Za-zР-пр-џР-пр-џ]", ""); }
	static public String forwardNumberFilter(String title)		{ return title.replaceAll("\\A\\d{1,}", ""); }
	
	List<String> Artists = new Vector<String>();
	public List<String> titles = new Vector<String>();
	public List<String> genres = new Vector<String>();
	
	public String bitrate = "Unknow";
	public String channels = "Unknow";
	public String type = "Unknow";
	public String sampleRate = "Unknow";
	public Integer timeLength = 0;
	public boolean variableBitrate = false;
	
	void AddTitleHelper(String gipoTitle) {
		String temp = sitesFilter(gipoTitle);  
		temp = spacesFilter(forwardNumberPreFilter(temp));
		if (!titles.contains(temp)) titles.add(temp);
		temp = forwardNumberFilter(temp);
		if (!titles.contains(temp)) titles.add(temp);
	}
	
	public MediaInfo(File f) {
		InitInfo(f);
		genres.add("default");
		String title = f.getName().toLowerCase();
		String ext = IOOperations.extension(title);
		
		if (ext.length() == 0) AddTitleHelper(title);
		else AddTitleHelper(IOOperations.name_without_extension(title, ext));
	}
	
	public MediaInfo(	String mBitrate, String mChannels, String mType, String samplerate,
						String length, Boolean variable_bitrate, List<String> mGenres	) {
		bitrate = mBitrate;	channels = mChannels;	type = mType;	sampleRate = samplerate;
		timeLength = Integer.parseInt(length);	variableBitrate = variable_bitrate;	genres = mGenres;
	}
	
	@Override
	public String toString() {
		return 	"<html> " + titles.get(0) + "<br>" +
				"Genre : " + genres.get(0) + "<br>" +
				"Type : " + type + "<br>" +
				"Bitrate : " + (variableBitrate ? "~" : "") + bitrate + "<br>" +
				"Channels : " + channels + "<br>" +
				"SampleRate : " + sampleRate + "<br>" +
				"Time : " + Utils.TimeFormatter(timeLength) + "</html>";
	}
	
	@SuppressWarnings("unchecked")
	void InitInfo(File file) {
		try {
			AudioFile f = AudioFileIO.read(file);
			Tag tag = f.getTag();
			
			String t1;
			Artists = tag.getArtist();
			List<Object> tlist = tag.getTitle();
			for(Object t_title : tlist) {
				t1 = t_title.toString().toLowerCase();
				for(Object t_art : Artists)
					AddTitleHelper(t_art.toString().toLowerCase() + "" + t1);
			}
				
			genres = tag.getGenre();
			
			bitrate = "" + f.getBitrate();
			channels = "" + f.getChannelNumber();
			
			sampleRate = "" + f.getSamplingRate();
			timeLength = f.getLength();
			
			type = f.getEncodingType();
			variableBitrate = f.isVbr();
		} 
		catch (CannotReadException e) { Errorist.printLog(e); }
	}
}