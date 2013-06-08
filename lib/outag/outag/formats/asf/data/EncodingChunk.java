package outag.formats.asf.data;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import outag.formats.asf.util.Utils;

/** This class was intended to store the data of a chunk which contained the
 * encoding parameters in textual form. <br>
 * Since the needed parameters were found in other chunks the implementation of
 * this class was paused. <br>
 * TODO complete analysis. */
public class EncodingChunk extends Chunk {
	/** The read strings.*/
	private final ArrayList<String> strings;

	/** Creates an instance.
	 * @param pos Position of the chunk within file or stream
	 * @param chunkLen Length of current chunk. */
	public EncodingChunk(long pos, BigInteger chunkLen) {
		super(GUID.GUID_ENCODING, pos, chunkLen);
		this.strings = new ArrayList<String>();
	}

	/** This method appends a String. 
	 * @param toAdd String to add. */
	public void addString(String toAdd) { strings.add(toAdd); }

	/** Returns a collection of all {@link String}s which were added due {@link #addString(String)}. */
	public Collection<String> getStrings() { return new ArrayList<String>(strings); }

	public String prettyPrint() {
		StringBuffer result = new StringBuffer(super.prettyPrint());
		result.insert(0, Utils.LINE_SEPARATOR + "Encoding:"
				+ Utils.LINE_SEPARATOR);
		Iterator<String> iterator = this.strings.iterator();
		while (iterator.hasNext()) {
			result.append("   " + iterator.next() + Utils.LINE_SEPARATOR);
		}
		return result.toString();
	}
}