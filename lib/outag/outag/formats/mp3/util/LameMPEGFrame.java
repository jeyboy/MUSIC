package outag.formats.mp3.util;

/** Representation of a Lame Mpeg frame $Id: LameMPEGFrame.java,v 1.1 2007/03/23 14:17:02 nicov1 Exp $ */
public class LameMPEGFrame {
	/** bitrate of this frame */
	private int bitrate;

	/** Flag indicating if bitset contains a Lame Frame */
	private boolean containsLameMPEGFrame;

	/** file size in bytes of the frame's File */
	private int fileSize;

	/** indicating if this is a correct Lame Frame */
	private boolean isValidLameMPEGFrame = false;

	/** Lame Version number of this frame */
	private String lameVersion;

	/** bitset representing this Lame Frame */
	private boolean containsLameFrame = false;

	/** Creates a Lame Mpeg Frame and checks it's integrity
	 * @param lameHeader - a byte array representing the Lame frame */
	public LameMPEGFrame(byte[] lameHeader) {
		String xing = new String(lameHeader, 0, 4);

		if ( xing.equals("LAME")) {
			isValidLameMPEGFrame = true;

			int[] b = u(lameHeader);

			containsLameFrame = ((b[9]&0xFF) == 0xFF);

			byte[] version = new byte[5];

			version[0] = lameHeader[4];
			version[1] = lameHeader[5];
			version[2] = lameHeader[6];
			version[3] = lameHeader[7];
			version[4] = lameHeader[8];
			lameVersion = new String( version );

			containsLameMPEGFrame = containsLameMPEGFrame();

			if ( containsLameMPEGFrame ) {
				bitrate = b[20];
				fileSize = b[28] * 16777215 + b[29] * 65535 + b[30] * 255 + b[31];
			}
		}
		else isValidLameMPEGFrame = false;
	}
	
	private int[] u(byte[] b) {
		int[] i = new int[b.length];
		for(int j = 0; j<i.length; j++)
			i[j] = b[j] & 0xFF;
		return i;
	}


	/** LameMPEGFrame validation */
	public boolean isValid() { return isValidLameMPEGFrame; }


	/**  Create a string representation of this frame
	 * @return    the string representation of this Lame Frame */
	public String toString() {
		String output;

		if ( isValidLameMPEGFrame ) {
			output = "\n----LameMPEGFrame--------------------\n";
			output += "Lame" + lameVersion;
			if ( containsLameMPEGFrame )
				output += "\tMin.Bitrate:" + bitrate + "\tLength:" + fileSize;
			output += "\n--------------------------------\n";
		}
		else
			output = "\n!!!No Valid Lame MPEG Frame!!!\n";
		return output;
	}

	/** Checks wether this frame is a Lame Frame or Not
	* @return    true if this frame contains a Lame Frame */
	private boolean containsLameMPEGFrame() { return containsLameFrame;	}
}