package service;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import entagged.audioformats.AudioFileIO;
import entagged.audioformats.exceptions.CannotReadException;

public class MediaInfo {
//	AudioSystem.getAudioFileTypes()
	static public String SitesFilter(String title)	{ return title.replaceAll("([\\(\\[](http:\\/\\/)*(www\\.)*([a-z0-9р-џ])+\\.[a-z]+[\\]\\)])", ""); }
	static public String ForwardNumberPreFilter(String title)	{ return title.replaceAll("\\A\\d{1,}.|\\(\\w*\\d{1,}\\w*\\)", ""); }
	static public String SpacesFilter(String title) { return title.replaceAll("[^0-9A-Za-zР-пр-џР-пр-џ]", ""); }
	static public String ForwardNumberFilter(String title)	{ return title.replaceAll("\\A\\d{1,}", ""); }
	
	static Vector<String> InitExtsList() {
		Vector<String> ret = new Vector<String>();
		
		// entagged // 		"flac", "ape", "mp3", "ogg", "wma", "wav", "mpc", "mp+"
		// jaudiotagger //	
		for(String ext : new String [] {"flac", "ape", "mp3", "mp4", "m4a", "m4p", "ogg", "wma", "wav", "asf", "mpc", "mp+", "rmf"})
			ret.add(ext);
	
		return ret;
	}
	
	static String subform(String num) { return num.length() > 1 ? num : ("0" + num); }
	
	public static String TimeFormatter(long time) {
		long t_h = TimeUnit.SECONDS.toHours(time);
		long t_m = TimeUnit.SECONDS.toMinutes(time -= TimeUnit.HOURS.toSeconds(t_h));
		long t_s = time - TimeUnit.MINUTES.toSeconds(t_m);
		
		return (t_h > 0 ? subform(t_h + "") + " h  " : "") + (t_m > 0 ? subform(t_m + "") + " m  " : "") + (t_s > 0 ? subform(t_s + "") + " s  " : "");
	} 
	
	public Vector<String> exts = InitExtsList();

	List<String> Artists = new Vector<String>();
	public List<String> Titles = new Vector<String>();
	public List<String> Genres = new Vector<String>();
	
	public String Bitrate = "Unknow";
	public String Channels = "Unknow";
	public String Type = "Unknow";
	public String SampleRate = "Unknow";
	public Integer TimeLength = -1;
	public boolean VariableBitrate = false;
	
	public boolean CheckFormat(String title, String ext) {
		return exts.contains(ext);
	}
	
	void AddTitleHelper(String gipoTitle) {
		String temp = SitesFilter(gipoTitle);  
		temp = SpacesFilter(ForwardNumberPreFilter(temp));
		if (!Titles.contains(temp)) Titles.add(temp);
		temp = ForwardNumberFilter(temp);
		if (!Titles.contains(temp)) Titles.add(temp);
	}
	
	public MediaInfo(File f) {
		Genres.add("default");
		String title = f.getName().toLowerCase();
		String ext = IOOperations.extension(title);
		
		if (ext.length() == 0) AddTitleHelper(title);
		else AddTitleHelper(title.substring(0, title.length() - (ext.length() + 1)));
		
		if (CheckFormat(f.getName(), ext))
			InitInfo(f);
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
				"Time \t\t: " + TimeFormatter(TimeLength) + "\n";
	}
	
//	public String Pack() {
//		return  (VariableBitrate ? "~" : "-") + "*" + Type + "*" + Bitrate + "*" + Channels + "*" + SampleRate + "*" + TimeLength;
//	}
//	
//	public void Unpack(String info) {
//		String[] temp = info.split("*");
//		
//		VariableBitrate = temp[0] == "~";
//		Type = temp[1];
//		Bitrate = temp[2];
//		Channels = temp[3];
//		SampleRate = temp[4];
//		TimeLength = Integer.parseInt(temp[5]);
//	}
	
	@SuppressWarnings("unchecked")
	void InitInfo(File file) {
		try {
			entagged.audioformats.AudioFile f = AudioFileIO.read(file);
			entagged.audioformats.Tag tag = f.getTag();
			
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
		catch (CannotReadException e) {
			TryGetInfo(file);
			Errorist.printLog(e); 
		}
	}
	
	void TryGetInfo(File file) {
		org.jaudiotagger.audio.AudioFile f;
		try {
			f = org.jaudiotagger.audio.AudioFileIO.read(file);
			Tag tag = f.getTag();

			String temp;
			for(int n = 0;; n++) {
				temp = tag.getValue(FieldKey.ARTIST, n);
				temp = new String(temp.getBytes(),Charset.forName("windows-1251")).toLowerCase();
				if (temp.isEmpty()) break;
				if (Artists.contains(temp)) break;
				Artists.add(temp);
			}
			
			for(int n = 0;; n++) {
				temp = tag.getValue(FieldKey.GENRE, n).toLowerCase();
				if (temp.isEmpty()) break;
				if (Genres.contains(temp)) break;
				Genres.add(temp);
			}

			for(int n = 0;; n++) {
				temp = tag.getValue(FieldKey.TITLE, n).toLowerCase();
				if (temp.isEmpty()) break;
				
				if (Artists.size() == 0)
					AddTitleHelper(temp);
				else
					for(String s: Artists)
						AddTitleHelper(s + temp);
			}
			
			AudioHeader head = f.getAudioHeader();
			
			Bitrate = head.getBitRate();
			Channels = head.getChannels();
			Type = head.getEncodingType();
			SampleRate = head.getSampleRate();
			TimeLength = head.getTrackLength();
			VariableBitrate = head.isVariableBitRate();			
		}
		catch (org.jaudiotagger.audio.exceptions.CannotReadException e) {
			GetApeInfo(file);
			Errorist.printLog(e);
		}		
		catch (Exception e) { Errorist.printLog(e); }
	}
	
	void GetApeInfo(File file) {
//	AudioInputStream audioInputStream;
//	try {
//		audioInputStream = AudioSystem.getAudioInputStream(file);
//		AudioFormat audioFormat = audioInputStream.getFormat();
//		
//		properties.put("Type", audioFormat.getEncoding());
//		properties.put("SampleRate", audioFormat.getSampleRate());
//		properties.put("BitRate", audioFormat.getSampleSizeInBits());
//		properties.put("Channels", audioFormat.getChannels());
//		properties.put("FrameRate", audioFormat.getFrameRate());
//		properties.put("FrameSize", audioFormat.getFrameSize());
//	
////		return audioFormat.properties();
//	} 
//	catch (Exception e) { Errorist.printLog(e); }
	}	
}
