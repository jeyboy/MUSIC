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
	static public String SitesFilter(String title)				{ return title.replaceAll("([\\(\\[](http:\\/\\/)*(www\\.)*([a-z0-9р-џ])+\\.[a-z]+[\\]\\)])", ""); }
	static public String ForwardNumberPreFilter(String title)	{ return title.replaceAll("\\A\\d{1,}.|\\(\\w*\\d{1,}\\w*\\)", ""); }
	static public String SpacesFilter(String title) 			{ return title.replaceAll("[^0-9A-Za-zР-пр-џР-пр-џ]", ""); }
	static public String ForwardNumberFilter(String title)		{ return title.replaceAll("\\A\\d{1,}", ""); }
	
	List<String> Artists = new Vector<String>();
	public List<String> Titles = new Vector<String>();
	public List<String> Genres = new Vector<String>();
	
	public String Bitrate = "Unknow";
	public String Channels = "Unknow";
	public String Type = "Unknow";
	public String SampleRate = "Unknow";
	public Integer TimeLength = -1;
	public boolean VariableBitrate = false;
	
	void AddTitleHelper(String gipoTitle) {
		String temp = SitesFilter(gipoTitle);  
		temp = SpacesFilter(ForwardNumberPreFilter(temp));
		if (!Titles.contains(temp)) Titles.add(temp);
		temp = ForwardNumberFilter(temp);
		if (!Titles.contains(temp)) Titles.add(temp);
	}
	
	public MediaInfo(File f) {
		InitInfo(f);
		Genres.add("default");
		String title = f.getName().toLowerCase();
		String ext = IOOperations.extension(title);
		
		if (ext.length() == 0) AddTitleHelper(title);
		else AddTitleHelper(IOOperations.name_without_extension(title, ext));
	}
	
	public MediaInfo(	String bitrate, String channels, String type, String samplerate,
						String length, Boolean variable_bitrate, List<String> genres	) {
		Bitrate = bitrate;	Channels = channels;	Type = type;	SampleRate = samplerate;
		TimeLength = Integer.parseInt(length);	VariableBitrate = variable_bitrate;	Genres = genres;
	}
	
	@Override
	public String toString() {
		return 	"Type \t\t: " + Type + "\n" +
				"Bitrate \t: " + (VariableBitrate ? "~" : "") + Bitrate + "\n" +
				"Channels \t: " + Channels + "\n" +
				"SampleRate \t: " + SampleRate + "\n" +
				"Time \t\t: " + Utils.TimeFormatter(TimeLength) + "\n";
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
				
			Genres = tag.getGenre();
			
			Bitrate = "" + f.getBitrate();
			Channels = "" + f.getChannelNumber();
			
			SampleRate = "" + f.getSamplingRate();
			TimeLength = f.getLength();
			
			Type = f.getEncodingType();
			VariableBitrate = f.isVbr();
		} 
		catch (CannotReadException e) { Errorist.printLog(e); }
	}
}