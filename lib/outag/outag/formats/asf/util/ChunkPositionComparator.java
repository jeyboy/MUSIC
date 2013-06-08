package outag.formats.asf.util;

import java.util.Comparator;

import outag.formats.asf.data.Chunk;

/** This class is needed for ordering all types of
 * {@link outag.formats.asf.data.Chunk}s ascending by their Position. */
public class ChunkPositionComparator implements Comparator<Object> {
	public int compare(Object o1, Object o2) {
		if (o1 instanceof Chunk && o2 instanceof Chunk) {
			Chunk c1 = (Chunk) o1;
			Chunk c2 = (Chunk) o2;
			return (int) (c1.getPosition() - c2.getPosition());
		}
		return 0;
	}
}