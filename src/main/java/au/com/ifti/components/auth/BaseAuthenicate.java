package au.com.ifti.components.auth;

import au.com.ifti.models.UserModel;
import au.com.ifti.utilities.TiramisuRequest;
import au.com.ifti.utilities.TiramisuResponse;

public abstract class BaseAuthenicate {
	
	protected String usernameField = "user_username";
	protected String passwordField = "user_password";

	public BaseAuthenicate() {
		
	}
	
	/**
	 * Find the user in the data source.
	 * @param username
	 * @return
	 */
	protected UserModel findUser(String username) {
		return null;
	}
	
	/**
	 * This abstract authenticate method, all authentication subclasses need this function.
	 * @param request The TiramisuRequest object
	 * @param response The TiramisuResponse object
	 * @return Returns a user model on success or null on failure.
	 */
	protected abstract UserModel authenticate(TiramisuRequest request, TiramisuResponse response);
	
	public String getUsernameField() {
		return usernameField;
	}

	public void setUsernameField(String usernameField) {
		this.usernameField = usernameField;
	}

	public String getPasswordField() {
		return passwordField;
	}

	public void setPasswordField(String passwordField) {
		this.passwordField = passwordField;
	}
	
}
