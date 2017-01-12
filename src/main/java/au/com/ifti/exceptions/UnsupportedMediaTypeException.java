package au.com.ifti.exceptions;

public class UnsupportedMediaTypeException extends TiramisuException {
	
	/**
	 * Randomly generated serial ID.
	 */
	private static final long serialVersionUID = 1443565184672857076L;

	/**
	 * The constructor for this exception type.
	 */
	UnsupportedMediaTypeException() {
		this.setCode(415);
	}

}
