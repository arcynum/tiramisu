package au.com.ifti.exceptions;

/**
 * The application global exception object.
 * This will be the default exception object for the entire application.
 * @author CIH221
 *
 */
public abstract class TiramisuException extends Exception {
	
	/**
	 * Default serial ID.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The response code for the exception.
	 */
	private Integer code = null;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

}
