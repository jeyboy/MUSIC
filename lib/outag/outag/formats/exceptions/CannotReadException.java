package outag.formats.exceptions;

/** Thrown if an audio file cannot be read.<br>
 * Causes may be invalid data or IO errors. */
public class CannotReadException extends Exception {
	private static final long serialVersionUID = 866944644557838449L;

	/** Creates an instance. */
	public CannotReadException() { super(); }

	/** Creates an instance.
	 * @param message - error message. */
	public CannotReadException(String message) { super(message); }

	/** Creates an instance.
	 * @param message - error message.
	 * @param cause - The throwable causing this exception. */
	public CannotReadException(String message, Throwable cause) {
		super(message, cause);
	}
}