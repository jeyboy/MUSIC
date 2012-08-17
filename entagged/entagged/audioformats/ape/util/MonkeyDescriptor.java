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

import entagged.audioformats.generic.Utils;

public class MonkeyDescriptor {
	
	byte[] b;
	public MonkeyDescriptor(byte[] b) {
		this.b = b;
	}
	
	public int getRiffWavOffset() {
		return getDescriptorLength() + getHeaderLength() + getSeekTableLength();
	}
	
	public int getDescriptorLength() {
		return Utils.getNumber(b, 0,3);
	}
	
	public int getHeaderLength() {
		return Utils.getNumber(b, 4,7);
	}
	
	public int getSeekTableLength() {
		return Utils.getNumber(b, 8,11);
	}
	
	public int getRiffWavLength() {
		return Utils.getNumber(b, 12,15);
	}
    
	public long getApeFrameDataLength() {
		return Utils.getLongNumber(b, 16,19);
	}
	
	public long getApeFrameDataHighLength() {
		return Utils.getLongNumber(b, 20,23);
	}
	
	public int getTerminatingDataLength() {
		return Utils.getNumber(b, 24,27);
	}
    
    //16 bytes cFileMD5 b[28->43]
}
