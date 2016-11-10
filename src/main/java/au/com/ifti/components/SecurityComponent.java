package au.com.ifti.components;

public class SecurityComponent extends Component {
	
	/**
	 * Indicates whether a CSRF check is required.
	 */
	private Boolean csrfCheck = true;
	
	/**
	 * The time, in minutes, until the CSRF token expires.
	 */
	private Integer csrfExpires = 30;

	public Boolean getCsrfCheck() {
		return csrfCheck;
	}

	public void setCsrfCheck(Boolean csrfCheck) {
		this.csrfCheck = csrfCheck;
	}

	public Integer getCsrfExpires() {
		return csrfExpires;
	}

	public void setCsrfExpires(Integer csrfExpires) {
		this.csrfExpires = csrfExpires;
	}

}
