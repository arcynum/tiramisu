package au.com.ifti.components.auth;

import au.com.ifti.models.UserModel;
import au.com.ifti.utilities.TiramisuRequest;
import au.com.ifti.utilities.TiramisuResponse;

public class FormAuthenticate extends BaseAuthenicate {
	
	/**
	 * Check the fields exist in the HTTP Request.
	 * @param request The TiramisuRequest object
	 * @param usernameField The username field name
	 * @param passwordField The password field name
	 * @return Boolean True if everything is present or false.
	 */
	protected Boolean checkFields(TiramisuRequest request, String usernameField, String passwordField) {
		if (request.getParameter(usernameField) == null || request.getParameter(passwordField) == null) {
			return false;
		}
		return true;
	}
	
	/**
	 * The generic method which is called by all authentication objects.
	 * Checks for the HTML form fields and then calls the find user function (for the database).
	 * @param request
	 * @param response
	 * @return
	 */
	protected UserModel authenticate(TiramisuRequest request, TiramisuResponse response) {
		if (!checkFields(request, this.getUsernameField(), this.getPasswordField())) {
			return null;
		}
		return findUser(request.getParameter("user_username"));
	}

}
