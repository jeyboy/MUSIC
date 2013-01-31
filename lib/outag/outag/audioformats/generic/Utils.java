package outag.audioformats.generic;

import java.io.File;
import java.io.UnsupportedEncodingException;

public class Utils {
	public boolean accept(File f) {
		if(f.isDirectory() || !f.canRead())
			return false;
		
		String ext = Utils.getExtension(f);
		if( ext.equals("mp3") ||
			ext.equals("flac")||
			ext.equals("ogg") ||
			ext.equals("mpc") ||
			ext.equals("mp+") ||
			ext.equals("ape") ||
			ext.equals("wav") || 
			ext.equals("wma") )
			return true;	

		return false;
	}	
	
	/** Copies the bytes of <code>srd</code> to <code>dst</code> at the specified offset.
	 * @param src - The byte do be copied.
	 * @param dst - The array to copy to
	 * @param dstOffset - The start offset for the bytes to be copied. */
	public static void copy(byte[] src, byte[] dst, int dstOffset) {
		System.arraycopy(src, 0, dst, dstOffset, src.length);
	}

	/** @return {@link String#getBytes()}.<br>
	 * @param s - The String to call. */
	public static byte[] getDefaultBytes(String s) { return s.getBytes(); }
	
	/** @return The extension of the given file
	 * @param f - file */
	public static String getExtension(File f) {
		String name = f.getName().toLowerCase();
		int i = name.lastIndexOf( "." );
		return (i == -1) ? "" : name.substring(i + 1);
	}

	/** Computes a long number composed of (end-start) bytes in the b array.
	 * @param b - The byte array @param start The starting offset in b
	 * (b[offset]). The less significant byte @param end The end index
	 * (included) in b (b[end]). The most significant byte @return a long number
	 * represented by the byte sequence. */
	public static long getLongNumber(byte[] b, int start, int end) {
		long number = 0;
		for (int i = 0; i < (end - start + 1); i++)
			number += ((b[start + i] & 0xFF) << i * 8);

		return number;
	}

	public static long getLongNumberBigEndian(byte[] b, int start, int end) {
		int number = 0;
		for (int i = 0; i < (end - start + 1); i++)
			number += ((b[end - i] & 0xFF) << i * 8);

		return number;
	}

	/** @return an int number composed of (end-start) bytes in the b array. The byte
	 * array @param start The starting offset in b (b[offset]). The less
	 * significant byte @param end The end index (included) in b (b[end]). The
	 * most significant byte @return a int number represented by the byte
	 * sequence. */
	public static int getNumber(byte[] b, int start, int end) {
		return (int) getLongNumber(b, start, end);
	}

	public static int getNumberBigEndian(byte[] b, int start, int end) {
		return (int) getLongNumberBigEndian(b, start, end);
	}

	public static byte[] getSizeBigEndian(int size) {
		byte[] b = new byte[4];
		b[0] = (byte) ((size >> 24) & 0xFF);
		b[1] = (byte) ((size >> 16) & 0xFF);
		b[2] = (byte) ((size >> 8) & 0xFF);
		b[3] = (byte) (size & 0xFF);
		return b;
	}

	public static String getString(byte[] b, int offset, int length) {
		return new String(b, offset, length);
	}

	public static String getString(byte[] b, int offset, int length,
			String encoding) throws UnsupportedEncodingException {
		return new String(b, offset, length, encoding);
	}

	/** Tries to convert a string into an UTF8 array of bytes If the conversion
	 * fails, return the string converted with the default encoding.
	 * @param s - The string to convert @return The byte array representation of
	 * this string in UTF8 encoding */
	public static byte[] getUTF8Bytes(String s)	throws UnsupportedEncodingException {
		return s.getBytes("UTF-8");
	}
}