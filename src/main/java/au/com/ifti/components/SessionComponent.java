package au.com.ifti.components;

import javax.servlet.http.HttpSession;

public class SessionComponent extends Component {
	
	private HttpSession session;
	
	/**
	 * The constructor for the session component.
	 * This needs to extract the session information from the HTTP Request object.
	 * @param request
	 */
	public SessionComponent(HttpSession session) {
		super();
		
		this.setSession(session);
	}
	
	/**
	 * Write a value to the HTTP session.
	 * 
	 * @param key The lookup key
	 * @param value The object to be stored, needs object to be serializable
	 */
	public void write(String key, Object value) {
		this.getSession().setAttribute(key, value);
	}
	
	/**
	 * Returns the object which has been stored in the session.
	 * 
	 * @param key The lookup key
	 * @return Object The object stored in the session key
	 */
	public Object read(String key) {
		return this.getSession().getAttribute(key);
	}
	
	/**
	 * Delete the object from the session.
	 * 
	 * @param key The lookup key
	 */
	public void delete(String key) {
		this.getSession().removeAttribute(key);
	}
	
	/**
	 * Fetch the session attribute and remove it once you do.
	 * @param key The lookup key
	 * @return Object The object stored in the session key
	 */
	public Object consume(String key) {
		Object value = this.getSession().getAttribute(key);
		this.getSession().removeAttribute(key);
		return value;
	}
	
	/**
	 * Check if the session key exists.
	 * @param key The lookup key
	 * @return Object The object stored in the session key
	 */
	public Boolean check(String key) {
		if (this.getSession().getAttribute(key) == null) {
			return false;
		}
		return true;
	}
	
	/**
	 * Return the HTTP Session ID string.
	 * @return String the HTTP Session ID
	 */
	public String getId() {
		return this.getSession().getId();
	}

	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}

}
