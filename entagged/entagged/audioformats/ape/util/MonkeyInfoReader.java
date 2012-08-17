/*
 * Entagged Audio Tag library
 * Copyright (c) 2003-2005 Raphaël Slinckx <raphael@slinckx.net>
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *  
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package entagged.audioformats.ape.util;

import entagged.audioformats.EncodingInfo;
import entagged.audioformats.exceptions.*;
import entagged.audioformats.generic.Utils;
import entagged.audioformats.wav.util.WavFormatHeader;
import entagged.audioformats.wav.util.WavRIFFHeader;

import java.io.*;

public class MonkeyInfoReader {

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
		byte[] b = new byte[4];
		raf.read(b);
		String mpc = new String(b);
		if (!mpc.equals("MAC ")) {
			throw new CannotReadException("'MAC ' Header not found");
		}
		
		b = new byte[4];
		raf.read(b);
		int version = Utils.getNumber(b, 0,3);
		if(version < 3970)
			throw new CannotReadException("Monkey Audio version <= 3.97 is not supported");
		
		b = new byte[44];
		raf.read(b);
		MonkeyDescriptor md = new MonkeyDescriptor(b);
		
		b = new byte[24];
		raf.read(b);
		MonkeyHeader mh = new MonkeyHeader(b);
		
		raf.seek(md.getRiffWavOffset());
		b = new byte[12];
		raf.read(b);
		WavRIFFHeader wrh = new WavRIFFHeader(b);
		if(!wrh.isValid())
			throw new CannotReadException("No valid RIFF Header found");
		
		b = new byte[24];
		raf.read(b);
		WavFormatHeader wfh = new WavFormatHeader(b);
		if(!wfh.isValid())
			throw new CannotReadException("No valid WAV Header found");
		
//		info.setLength( mh.getLength());
		info.setPreciseLength(mh.getPreciseLength());
		info.setChannelNumber( wfh.getChannelNumber() );
		info.setSamplingRate( wfh.getSamplingRate() );
		info.setBitrate( computeBitrate(info.getLength(), raf.length()) );
		
		info.setEncodingType( "Monkey Audio v" + (((double)version)/1000)+", compression level "+mh.getCompressionLevel());
		info.setExtraEncodingInfos( "" );
		
		return info;
	}
	
	private int computeBitrate( int length, long size ) {
		return (int) ( ( size / 1000 ) * 8 / length );
	}
}

