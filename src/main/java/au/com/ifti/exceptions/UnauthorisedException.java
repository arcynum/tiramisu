package au.com.ifti.exceptions;

public class UnauthorisedException extends TiramisuException {
	
	/**
	 * Default serial ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The constructor for the exception.
	 */
	public UnauthorisedException() {
		super();
		this.setCode(400);
	}
	
}
