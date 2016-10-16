package au.com.ifti.exceptions;

/**
 * Not Found Exception. If the object which you are requesting does not exist.
 * This is equivalent to a 404.
 * @author CIH221
 *
 */
public class NotFoundException extends TiramisuException {

	/**
	 * Default serial ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The constructor for the exception.
	 */
	public NotFoundException() {
		super();
		this.setCode(404);
	}

	/**
	 * The constructor for the exception.
	 */
	public NotFoundException(String message) {
		super();
		this.setCode(404);
	}

}
