package au.com.ifti.exceptions;

/**
 * A bad request Java Exception.
 * This corresponds with the HTTP 400 code.
 * @author CIH221
 *
 */
public class BadRequestException extends TiramisuException {

	/**
	 * Default serial ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The constructor for the exception.
	 */
	public BadRequestException() {
		super();
		this.setCode(400);
	}

}
