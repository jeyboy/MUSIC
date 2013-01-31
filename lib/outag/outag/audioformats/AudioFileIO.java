package outag.audioformats;

import java.io.File;
import java.util.Hashtable;
import outag.audioformats.ape.MonkeyFileReader;
import outag.audioformats.asf.AsfFileReader;
import outag.audioformats.exceptions.CannotReadException;
import outag.audioformats.flac.FlacFileReader;
import outag.audioformats.generic.AudioFileReader;
import outag.audioformats.generic.Utils;
import outag.audioformats.mp3.Mp3FileReader;
import outag.audioformats.mpc.MpcFileReader;
import outag.audioformats.ogg.OggFileReader;
import outag.audioformats.wav.WavFileReader;

/** <p>Example of use:</p>
 * <p>
 * <code>
 *		AudioFile audioFile = AudioFileIO.read(new File("audiofile.mp3")); //Reads the given file.<br/>
 *		int bitrate = audioFile.getBitrate(); //Retreives the bitrate of the file.<br/>
 *		String artist = audioFile.getTag().getArtist(); //Retreive the artist name.<br/>
 *		audioFile.getTag().setGenre("Progressive Rock"); //Sets the genre to Prog. Rock, note the file on disk is still unmodified.<br/>
 *		AudioFileIO.write(audioFile); //Write the modifications in the file on disk.
 *	</code>
 * </p>
 * <p>
 * <code>
 *		AudioFile audioFile = AudioFileIO.read(new File("audiofile.mp3"));<br/>
 *		audioFile.getTag().setGenre("Progressive Rock");<br/>
 *		audioFile.commit(); //Write the modifications in the file on disk.<br/>
 *	</code>
 * </p>
 */
public class AudioFileIO {
	private static AudioFileIO defaultInstance;

	/** @return the default instance for static use. */
	public static AudioFileIO getDefaultAudioFileIO() {
		if (defaultInstance == null) defaultInstance = new AudioFileIO();
		return defaultInstance;
	}

	/** <p>Read the tag contained in the given file.</p>
	 * @param f - The file to read.
	 * @return The AudioFile with the file tag and the file encoding info.
	 * @exception CannotReadException - If the file could not be read, the
	 * extension wasn't recognized, or an IO error occurs during the read.*/
	public static AudioFile read(File f) throws CannotReadException { return getDefaultAudioFileIO().readFile(f); }

	private Hashtable<String, AudioFileReader> readers = new Hashtable<String, AudioFileReader>();

	public AudioFileIO() { prepareReaders(); }

	/** Creates the readers. */
	private void prepareReaders() {
		// Tag Readers
		readers.put("mp3", new Mp3FileReader());
		readers.put("ogg", new OggFileReader());
		readers.put("flac", new FlacFileReader());
		readers.put("wav", new WavFileReader());
		readers.put("mpc", new MpcFileReader());
		readers.put("mp+", readers.get("mpc"));
		readers.put("ape", new MonkeyFileReader());
		readers.put("wma", new AsfFileReader());
	}

	/** Read the tag contained in the given file.
	 * @param f - the file to read.
	 * @return The AudioFile with the file tag and the file encoding info.
	 * @exception CannotReadException - If the file could not be read, the 
	 * extension wasn't recognized, or an IO error occurred during the read. */
	public AudioFile readFile(File f) throws CannotReadException {
		String ext = Utils.getExtension(f);

		Object afr = readers.get(ext);
		if (afr == null)
			throw new CannotReadException(
					"No Reader associated to this extension: " + ext);

		return ((AudioFileReader) afr).read(f);
	}
}