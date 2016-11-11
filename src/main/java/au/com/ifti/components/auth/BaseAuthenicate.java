package au.com.ifti.components.auth;

import org.apache.commons.codec.digest.HmacUtils;
import org.hibernate.Session;
import org.mindrot.jbcrypt.BCrypt;

import au.com.ifti.models.UserModel;
import au.com.ifti.utilities.TiramisuConfiguration;
import au.com.ifti.utilities.TiramisuRequest;
import au.com.ifti.utilities.TiramisuResponse;

public abstract class BaseAuthenicate {
	
	protected Session hibernateSession = null;
	protected String usernameField = "user_username";
	protected String passwordField = "user_password";

	public BaseAuthenicate(Session hibernateSession) {
		this.setHibernateSession(hibernateSession);
	}
	
	/**
	 * Find the user in the data source.
	 * @param username
	 * @return
	 */
	protected UserModel findUser(String username, String password) {
		
		this.getHibernateSession().beginTransaction();
		UserModel user = this.getHibernateSession().bySimpleNaturalId(UserModel.class).load(username);
		this.getHibernateSession().getTransaction().commit();
		
		if (user != null) {
			String hashedPassword = hashPassword(password);
			if (BCrypt.checkpw(hashedPassword, user.getPassword())) {
				System.out.println("BaseAuthenticate: Login Successful");
			}
		}
		
		return null;
	}
	
	/**
	 * This abstract authenticate method, all authentication subclasses need this function.
	 * @param request The TiramisuRequest object
	 * @param response The TiramisuResponse object
	 * @return Returns a user model on success or null on failure.
	 */
	protected abstract UserModel authenticate(TiramisuRequest request, TiramisuResponse response);
	
	private String hashPassword(String password) {
		return HmacUtils.hmacSha256Hex(TiramisuConfiguration.pepper, password);
	}
	
	public Session getHibernateSession() {
		return hibernateSession;
	}

	public void setHibernateSession(Session hibernateSession) {
		this.hibernateSession = hibernateSession;
	}

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
