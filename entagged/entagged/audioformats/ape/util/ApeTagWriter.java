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

import entagged.audioformats.*;
import entagged.audioformats.exceptions.*;
import entagged.audioformats.generic.Utils;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;

public class ApeTagWriter {
	
	private ApeTagCreator tc = new ApeTagCreator();
	
	public void delete(RandomAccessFile raf) throws IOException {
		if(!tagExists(raf))
			return;
		
		raf.seek( raf.length() - 20 );
		
		byte[] b = new byte[4];
		raf.read( b );
		long tagSize = Utils.getLongNumber(b, 0,3);
		
		raf.setLength(raf.length() - tagSize);
		
		if(!tagExists(raf))
			return;
		
		raf.setLength(raf.length() - 32);
	}
	
	private boolean tagExists(RandomAccessFile raf) throws IOException {
		raf.seek( raf.length() - 32 );
		
		byte[] b = new byte[8];
		raf.read(b);
		
		return new String( b ).equals( "APETAGEX" );
	}

	public void write(Tag tag, RandomAccessFile raf, RandomAccessFile rafTemp) throws CannotWriteException, IOException {
		FileChannel fc = raf.getChannel();

		ByteBuffer tagBuffer = tc.convert(tag, 0);

		if (!tagExists(raf)) {
			fc.position(fc.size());
			fc.write(tagBuffer);
		} else {
			raf.seek( raf.length() - 32 + 8 );
		
			//Version
			byte[] b = new byte[4];
			raf.read( b );
			int version = Utils.getNumber(b, 0,3);
			if(version != 2000) {
				throw new CannotWriteException("APE Tag other than version 2.0 are not supported");
			}
			
			//Size
			b = new byte[4];
			raf.read( b );
			long oldSize = Utils.getLongNumber(b, 0,3) + 32;
			
			int tagSize = tagBuffer.capacity();
			
			if(oldSize <= tagSize) {
				//Overwrite old tag
				System.err.println("Overwriting old tag in mpc file");
				fc.position(fc.size() - oldSize);
				fc.write(tagBuffer);
			} else {
				//We have to shrink the file
				System.err.println("Shrinking mpc file");

				FileChannel tempFC = rafTemp.getChannel();

				tempFC.position(0);
				fc.position(0);
				tempFC.transferFrom(fc, 0, fc.size() - oldSize);
				tempFC.position(tempFC.size());
				tempFC.write(tagBuffer);

			}
		}
	}
}
