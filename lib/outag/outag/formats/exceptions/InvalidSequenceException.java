package outag.formats.exceptions;

/** Thrown if chunk header not valid. */
public class InvalidSequenceException extends Exception {
	public InvalidSequenceException(String message) { super(message); }
}