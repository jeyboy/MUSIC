package outag.audioformats.mpc.util;

import outag.audioformats.EncodingInfo;
import outag.audioformats.exceptions.*;

import java.io.*;

public class MpcInfoReader {
	public EncodingInfo read( RandomAccessFile raf ) throws CannotReadException, IOException {
		EncodingInfo info = new EncodingInfo();
		
		//Begin info fetch-------------------------------------------
		if ( raf.length()==0 ) {
			//Empty File
			System.err.println("Error: File empty");
		
			throw new CannotReadException("File is empty");
		}
		raf.seek( 0 );
		
		//MP+ Header string
		byte[] b = new byte[3];
		raf.read(b);
		String mpc = new String(b);
		if (!mpc.equals("MP+") && mpc.equals("ID3")) {
			//TODO Do we have to do this ??
			//we have an ID3v2 tag at the beginning
			//We quickly jump to MPC data
			raf.seek(6);
			int tagSize = read_syncsafe_integer(raf);
			raf.seek(tagSize+10);
			
			//retry to read MPC stream
			b = new byte[3];
			raf.read(b);
			mpc = new String(b);
			if (!mpc.equals("MP+")) {
				//We could definitely not go there
				throw new CannotReadException("MP+ Header not found");
			}
		} 
		else if (!mpc.equals("MP+"))
			throw new CannotReadException("MP+ Header not found");
		
		b = new byte[25];
		raf.read(b);
		MpcHeader mpcH = new MpcHeader(b);
		//We only support v7 Stream format, so if it isn't v7, then returned values
		//will be bogus, and the file will be ignored
		
		double pcm = mpcH.getSamplesNumber();
//		info.setLength( (int) ( pcm * 1152 / mpcH.getSamplingRate() ) );
		info.setPreciseLength( (float) ( pcm * 1152 / mpcH.getSamplingRate() ) );
		info.setChannelNumber( mpcH.getChannelNumber() );
		info.setSamplingRate( mpcH.getSamplingRate() );
		info.setEncodingType( mpcH.getEncodingType() );
		info.setExtraEncodingInfos( mpcH.getEncoderInfo() );
		info.setBitrate( computeBitrate( info.getLength(), raf.length() ) );

		return info;
	}
	
	private int read_syncsafe_integer(RandomAccessFile raf)	throws IOException {
		int value = 0;

		value += (raf.read()& 0xFF) << 21;
		value += (raf.read()& 0xFF) << 14;
		value += (raf.read()& 0xFF) << 7;
		value += raf.read() & 0xFF;

		return value;
	}

	private int computeBitrate( int length, long size ) {
		return (int) ( ( size / 1000 ) * 8 / length );
	}
}