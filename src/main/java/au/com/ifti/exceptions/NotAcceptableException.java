package au.com.ifti.exceptions;

public class NotAcceptableException extends TiramisuException {
	
	/**
	 * Randomly generated serial ID.
	 */
	private static final long serialVersionUID = -3282695120396911502L;

	/**
	 * The constructor for this exception type.
	 */
	NotAcceptableException() {
		this.setCode(406);
	}

}
