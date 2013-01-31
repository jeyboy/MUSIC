package outag.audioformats.mp3.util;

public class VBRIMPEGFrame implements VbrInfoFrame {
	/**  the filesize in bytes */
	private int fileSize = 0;

	/**  The number of mpeg frames in the mpeg file */
	private int frameCount = 0;

	/** is a valid VBRI Mpeg frame */
	private boolean isValidVBRIMPEGFrame = true;

	public VBRIMPEGFrame(byte[] bytes) {
		String vbri = new String( bytes, 0, 4 );
		if ( vbri.equals( "VBRI" )) {
			int offset = 4+6;
			fileSize = (bytes[offset] << 24)&0xFF000000 | (bytes[offset+1] << 16)&0x00FF0000 | (bytes[offset+2] << 8)&0x0000FF00 | bytes[offset+3]&0x000000FF;
			
			offset += 4;
			frameCount = (bytes[offset] << 24)&0xFF000000 | (bytes[offset+1] << 16)&0x00FF0000 | (bytes[offset+2] << 8)&0x0000FF00 | bytes[offset+3]&0x000000FF;
		}
		else
			//No frame VBR MP3 XING
			isValidVBRIMPEGFrame = false;
	}

	public int getFrameCount() { return frameCount; }

	public boolean isValid() { return isValidVBRIMPEGFrame; }

	public int getFileSize() { return this.fileSize; }
	
	public String toString() {
		String output;

		if ( isValidVBRIMPEGFrame ) {
			output = "\n----VBRIMPEGFrame--------------------\n";
			output += "Frame count:" + frameCount + "\tFile Size:" + fileSize + "\n";
			output += "--------------------------------\n";
		}
		else
			output = "\n!!!No Valid VBRI MPEG Frame!!!\n";
		return output;
	}

	public boolean isVbr() {
		return true;
	}
}