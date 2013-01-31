package outag.audioformats.exceptions;

import java.io.File;

/** This exception is thrown if a
 * {@link outag.audioformats.generic.AudioFileModificationListener} wants to
 * prevent the &quote;outag audio library&quote; from actually finishing its
 * operation.<br>
 * This exception can be used in all methods but
 * {@link outag.audioformats.generic.AudioFileModificationListener#fileOperationFinished(File)}. */
public class ModifyVetoException extends Exception {
	private static final long serialVersionUID = -3279902048787055153L;

	public ModifyVetoException() { super(); }

	/** @see Exception#Exception(java.lang.String) */
	public ModifyVetoException(String message) { super(message); }

	/** @see Exception#Exception(java.lang.String, java.lang.Throwable) */
	public ModifyVetoException(String message, Throwable cause) { super(message, cause); }

	/** @see Exception#Exception(java.lang.Throwable) */
	public ModifyVetoException(Throwable cause) { super(cause); }
}