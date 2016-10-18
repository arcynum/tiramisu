package au.com.ifti.models;

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
	 * The user specific salt used for their password.
	 */
	private String salt;

	/**
	 * A boolean to indicate whether the user is active or not. This will be
	 * used for email validation.
	 */
	private Boolean active;

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
}