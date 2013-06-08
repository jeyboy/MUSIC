package outag.file_presentation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class JBFile extends Parseable  {
	RandomAccessFile base;
	
	public JBFile(String name) throws FileNotFoundException { base = new RandomAccessFile(name, "r"); }
	public JBFile(File file) throws FileNotFoundException { base = new RandomAccessFile(file, "r"); }
	public JBFile(RandomAccessFile file) { base = file; }
	
	public JBBuffer buffer(int length) throws IOException {
		byte [] buffer = new byte[length];
		base.readFully(buffer);
		return new JBBuffer(buffer); 
	}

	public int read() throws IOException { return base.read(); }

	public int read(byte[] array) throws IOException { return base.read(array); }

	public int skip(int n) throws IOException { return base.skipBytes(n);}

	public long available() throws IOException { return base.length() - base.getFilePointer(); }
	
	public long pos() throws IOException { return base.getFilePointer(); }
	
	public long length() throws IOException { return base.length(); }
}