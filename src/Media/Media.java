package Media;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import javax.activation.FileDataSource;
import javax.imageio.ImageIO;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.Global;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IMetaData;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;

public class Media {
	IContainer container;
	String path;
	
	Media() { container = IContainer.make(); }
	public Media(File file) throws Exception {
		this();
		path = file.getPath();
		if (!open(file))
			throw new Exception("Fails while open");
	}
	public Media(String url) throws Exception {
		this();
		if (!open(path=url))
			throw new Exception("Fails while open");
	}	

	boolean prepareOnlineMedia(InputStream stream) throws Exception {
		return container.open(stream, null, true, false) > -1;
	}	
	boolean prepareMedia(InputStream stream) throws Exception {
		int y = container.open(stream, null);
		return y > -1;
	}
		
	boolean open(File file) {
		try { return prepareMedia(new FileDataSource(file).getInputStream()); }
		catch (Exception e) { e.printStackTrace(); }
		return false;
	}
	
	boolean open(String url) {
	    InputStream in = null;
	    try {
	      URL u = new URL(url);
	      in = u.openStream();
	      return prepareOnlineMedia(in);
	    } 
	    catch (Exception ex) {
	    	try { return prepareMedia(new FileDataSource(url).getInputStream()); }
	    	catch (Exception e) { e.printStackTrace(); }
//	    	System.err.println("not a URL Java understands."); 
	    }
	    finally { if (in != null)
			try { in.close(); }
	    	catch (IOException e) {	e.printStackTrace();}
	    }
	    return false;
	}
	
	public void close() { container.close(); }
	
//	void seek(long streamID, long start_pos) {
//		container.seekKeyFrame();
//	}
	
	Vector<MediaCodec> prepareStreams(ICodec.Type type) {
		Vector<MediaCodec> streams = new Vector<MediaCodec>();
		int numStreams = container.getNumStreams();
		if (numStreams != 0) {
			IStreamCoder coder;
			IStream stream;
			 
			for (int i = 0; i < numStreams; i++) {
				stream = container.getStream(i);
				coder = stream.getStreamCoder();
				if (coder.getCodecType() == type)
					streams.add(new MediaCodec(coder, i)); break;
			}
		}
		
		return streams;	
	}	
	public Vector<MediaCodec> auPrepareStreams() { return prepareStreams(ICodec.Type.CODEC_TYPE_AUDIO); }
	public Vector<MediaCodec> suPrepareStreams() { return prepareStreams(ICodec.Type.CODEC_TYPE_SUBTITLE); }
	public Vector<MediaCodec> viPrepareStreams() { return prepareStreams(ICodec.Type.CODEC_TYPE_VIDEO); }
	public Vector<MediaCodec> atPrepareStreams() { return prepareStreams(ICodec.Type.CODEC_TYPE_ATTACHMENT); }	
	
/////////////////////////////////////User funcs/////////////////////////////////////////////////////////////////	
	
	public HashMap<String, Object> getProperties() {
		HashMap<String, Object> res = new HashMap();
		
		res.put("bitrate", container.getBitRate());
		res.put("duration", container.getDuration());
		res.put("length", container.getFileSize());
		
		IMetaData meta = container.getMetaData();
		Collection<String> keys = meta.getKeys();
		for(String key : keys)
			res.put(key, meta.getValue(key));
		
		return res;
	}
	
	public void Snapper(String output_folder, long step) {
		IMediaReader mediaReader = ToolFactory.makeReader(container);
		Vector<MediaCodec> codecs = viPrepareStreams();
		if (codecs.size() > 0) {
			mediaReader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);
			mediaReader.addListener(new Snapper(output_folder, codecs.firstElement().id, calcSecInGlobalPts(step)));
			while (mediaReader.readPacket() == null) ;
		}
	}
	
	public void Convert(String new_format) {
	    IMediaReader mediaReader = ToolFactory.makeReader(container);
	    IMediaWriter mediaWriter = ToolFactory.makeWriter(path + "." + new_format, mediaReader);
	    mediaReader.addListener(mediaWriter);
	    mediaWriter.addListener(new Converter(0));

	    while (mediaReader.readPacket() == null);
	}
////////////////////////////////////Service////////////////////////////////////////////////////////////////////
	
	public static long calcSecInGlobalPts(long sec) { return (long)(Global.DEFAULT_PTS_PER_SECOND * sec);}
	
    public static String imageToFile(String save_path,BufferedImage image) {
    	try {
    		String outputFilename = save_path + ".png";
    		ImageIO.write(image, "png", new File(outputFilename));
    		return outputFilename;
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    		return null;
    	}
    }
    
    public static BufferedImage convertToType(BufferedImage sourceImage, int targetType) {	    
    	if (sourceImage.getType() == targetType)
    		return sourceImage;
    	else
    	{
    		BufferedImage image = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), targetType);
    		image.getGraphics().drawImage(sourceImage, 0, 0, null);
    		return image;
    	}
    }    
}