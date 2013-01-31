package outag.covers;

import java.util.List;

public interface CoverSource {
	public List search(String artist, String album) throws CoverException;
}