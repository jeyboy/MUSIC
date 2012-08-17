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
package entagged.audioformats.mpc;

import java.io.IOException;
import java.io.RandomAccessFile;

import entagged.audioformats.Tag;
import entagged.audioformats.ape.util.ApeTagWriter;
import entagged.audioformats.exceptions.CannotWriteException;
import entagged.audioformats.generic.AudioFileWriter;
import entagged.audioformats.mp3.Mp3FileWriter;

public class MpcFileWriter extends AudioFileWriter {
	private ApeTagWriter tw = new ApeTagWriter();
	private Mp3FileWriter mp3tw = new Mp3FileWriter();
	
	protected void writeTag(Tag tag, RandomAccessFile raf, RandomAccessFile rafTemp) throws CannotWriteException, IOException {
		tw.write(tag, raf, rafTemp);
	}
	protected void deleteTag(RandomAccessFile raf, RandomAccessFile tempRaf) throws IOException, CannotWriteException {
		mp3tw.delete(raf, tempRaf);
		if(tempRaf.length() > 0)
			tw.delete(tempRaf);
		else
			tw.delete(raf);
	}
}
