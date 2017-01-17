package au.com.ifti.models;

import org.mindrot.jbcrypt.BCrypt;

/**
 * This class represents a single user of the application.
 * 
 * @author Chris Hamilton
 *
 */
public class UserModel extends Model {

	/**
	 * The username the person wishes to use.
	 */
	private String username;

	/**
	 * A valid email address for the user.
	 */
	private String email;

	/**
	 * The password, which is going to be stored as a Hashed(salt + pepper +
	 * password).
	 */
	private String password;

	/**
	 * A boolean to indicate whether the user is active or not. This will be
	 * used for email validation.
	 */
	private Boolean active;
	
	/**
	 * This method checks whether the entered password matches the objects password.
	 * @param hashedPassword A String which contains the hashed requester password.
	 * @return Boolean true if password matches, false if it does not.
	 */
	public Boolean checkPassword(String hashedPassword) {
		if (BCrypt.checkpw(hashedPassword, this.getPassword())) {
			return true;
		}
		return false;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	private String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
}