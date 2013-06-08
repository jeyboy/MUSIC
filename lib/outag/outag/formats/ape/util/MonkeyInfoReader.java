package outag.formats.ape.util;

import java.io.IOException;
import java.io.RandomAccessFile;

import outag.formats.EncodingInfo;
import outag.formats.exceptions.*;
import outag.formats.generic.Utils;

public class MonkeyInfoReader {
	public EncodingInfo read( RandomAccessFile raf ) throws CannotReadException, IOException {	
		if ( raf.length()==0 ) {
			System.err.println("Error: File empty");	
			throw new CannotReadException("File is empty");
		}
		
		EncodingInfo info = new EncodingInfo();
		raf.seek(0);
	
		//MP+ Header string
		byte[] b = new byte[4];
		raf.read(b);
		String mpc = new String(b);
		if (!mpc.equals("MAC "))
			throw new CannotReadException("'MAC ' Header not found");
		
		b = new byte[4];
		raf.read(b);
		int version = Utils.getNumber(b, 0,3);
		if(version < 3970)
			throw new CannotReadException("Monkey Audio version <= 3.97 is not supported");
		
		
		b = new byte[44];
		raf.read(b);
//		MonkeyDescriptor md = new MonkeyDescriptor(b);
		
		b = new byte[24];
		raf.read(b);
		MonkeyHeader mh = new MonkeyHeader(b);
		
//		if (md.getRiffWavOffset() != 0 ) {
//			raf.seek(md.getRiffWavOffset());
//			if (md.getRiffWavLength() != 0) {
//				b = new byte[md.getRiffWavLength()];
//				raf.read(b);
//				WavRIFFHeader wrh = new WavRIFFHeader(b);
//				if(!wrh.isValid())
//					throw new CannotReadException("No valid RIFF Header found");
//			}
//		}
		
//		b = new byte[24];
//		raf.read(b);
//		WavFormatHeader wfh = new WavFormatHeader(b);
//		if(!wfh.isValid())
//			throw new CannotReadException("No valid WAV Header found");
		
		info.setLength( mh.getLength());
		info.setPreciseLength(mh.getPreciseLength());
		info.setChannelNumber( mh.getChannelNumber() );
		info.setSamplingRate( mh.getSamplingRate() );
		info.setBitrate( computeBitrate(info.getLength(), raf.length()) );
		
		info.setEncodingType("Monkey Audio v" + (((double)version)/1000)+", compression level "+mh.getCompressionLevel());
		info.setExtraEncodingInfos("");
		
		return info;
	}
	
	private int computeBitrate( int length, long size ) { return (int) ( ( size / 1000 ) * 8 / length ); }
}