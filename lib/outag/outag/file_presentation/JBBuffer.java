package outag.file_presentation;

import java.io.IOException;
import java.nio.ByteBuffer;

public class JBBuffer extends Parseable {
	ByteBuffer base;
	
	public JBBuffer(byte[] buf) { base = ByteBuffer.wrap(buf); }
	
	public JBBuffer(ByteBuffer buf) { base = buf; }

	public JBBuffer(int length) { base = ByteBuffer.allocate(length); }
	
	public JBBuffer buffer(int length) { return this.slice(length); }

	public byte get() { return base.get();	}
	
	public byte get(int index) { return base.get(index); }
	
	public byte [] get(byte [] buffer) { base.get(buffer);  return buffer; }
	public byte [] get(byte [] buffer, int offset, int length) { base.get(buffer, offset, length);  return buffer; }
	
	public int read() { return base.get();	}

	public int read(byte[] array) throws IOException { base.get(array); return array.length; }

	public int skip(int n) { base.position(base.position() + n); return n; }
	
	public void pos(int new_pos) { base.position(new_pos); }
	
	public int limit() { return base.limit(); }
	
	public JBBuffer move(int new_pos) { base.position(new_pos); return this; }
	
	public long pos() { return base.position(); }
	public int ipos() { return base.position(); }
	
	public long available() { return base.capacity() - base.position(); }
	
	public long length() { return base.capacity(); }
	
	public JBBuffer slice() { return new JBBuffer(base.slice()); }
	
	public JBBuffer slice(int length) {
		JBBuffer b = new JBBuffer(base.slice());
		b.base.limit(length);
		return b;
	}
			
	public void skipToEnd()	{ skip((int)available()); }
}