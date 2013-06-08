package outag.formats.mp3.util;

/** Representation of a Xing Mpeg frame */
public class XingMPEGFrame implements VbrInfoFrame {
	/** the filesize in bytes */
	private int fileSize = 0;

	/** The number of mpeg frames in the mpeg file */
	private int frameCount = 0;

	/** is a valid Xing Mpeg frame */
	private boolean isValidXingMPEGFrame = true;

	/** the Xing Encoding quality (0-100) */
	private int quality;

	/** The four flags for this type of mpeg frame */
	private boolean[] vbrFlags = new boolean[4];
	
	private boolean vbr = false;

	/** Creates a Xing Frame with the two given sets of data
	 * @param bytesPart1 - the array of bytes before the 100bytes long Xing central data
	 * @param bytesPart2 - the array of bytes after the 100bytes long Xing central data */
	public XingMPEGFrame( byte[] bytesPart1, byte[] bytesPart2 ) {
		String xing = new String( bytesPart1, 0, 4 );

		if ( xing.equals( "Xing" ) || xing.equals( "Info" ) ) {
		    vbr = xing.equals( "Xing" );
			int[] b = u(bytesPart1);
			int[] q = u(bytesPart2);

			updateVBRFlags(b[7]);

			if ( vbrFlags[0] )
				frameCount = b[8] * 16777215 + b[9] * 65535 + b[10] * 255 + b[11];
			if ( vbrFlags[1] )
				fileSize = b[12] * 16777215 + b[13] * 65535 + b[14] * 255 + b[15];
			if ( vbrFlags[3] )
				quality = q[0] * 16777215 + q[1] * 65535 + q[2] * 255 + q[3];
		}
		else
			//No frame VBR MP3 XING
			isValidXingMPEGFrame = false;

	}
	
	private int[] u(byte[] b) {
		int[] i = new int[b.length];
		for(int j = 0; j<i.length; j++)
			i[j] = b[j] & 0xFF;
		return i;
	}

	/** @return the frames count */
	public int getFrameCount() {
		if (vbrFlags[0]) return frameCount;
		return -1;
	}

	/** @return tag validation */
	public boolean isValid() {	return isValidXingMPEGFrame; }

	public boolean isVbr() { return vbr; }
	
	public int getFileSize() { return this.fileSize; }
	
	/** @return a string representation of this Xing Frame */
	public String toString() {
		String output;

		if ( isValidXingMPEGFrame ) {
			output = "\n----XingMPEGFrame--------------------\n";
			output += "Frame count:" + vbrFlags[0] + "\tFile Size:" + vbrFlags[1] + "\tQuality:" + vbrFlags[3] + "\n";
			output += "Frame count:" + frameCount + "\tFile Size:" + fileSize + "\tQuality:" + quality + "\n";
			output += "--------------------------------\n";
		}
		else
			output = "\n!!!No Valid Xing MPEG Frame!!!\n";
		return output;
	}

	private void updateVBRFlags(int b) {
		vbrFlags[0] = (b&0x01) == 0x01;
		vbrFlags[1] = (b&0x02) == 0x02;
		vbrFlags[2] = (b&0x04) == 0x04;
		vbrFlags[3] = (b&0x08) == 0x08;
	}
}