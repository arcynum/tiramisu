package au.com.ifti.components;

import org.hibernate.Session;

import au.com.ifti.components.auth.FormAuthenticate;
import au.com.ifti.models.UserModel;
import au.com.ifti.utilities.TiramisuRequest;
import au.com.ifti.utilities.TiramisuResponse;

public class AuthComponent extends Component {
	
	private TiramisuRequest request = null;
	private TiramisuResponse response = null;
	private Session hibernateSession = null;
	
	public AuthComponent(TiramisuRequest request, TiramisuResponse response, Session hibernateSession) {
		super();
		
		this.setRequest(request);
		this.setResponse(response);
		this.setHibernateSession(hibernateSession);
	}
	
	/**
	 * Function to login the user.
	 * @return
	 */
	public Boolean login() {
		UserModel user = identify();
		if (user != null) {
			// Write a session key to indicate being logged on.
			return true;
		}
		return false;
	}
	
	/**
	 * Normally this function will loop through all the available authentication systems.
	 * At the moment, this will only call the form authentication.
	 * This should be a good polymorphic candidate.
	 * @return
	 */
	private UserModel identify() {
		FormAuthenticate formAuthenticate = new FormAuthenticate(this.getHibernateSession());
		UserModel user = formAuthenticate.authenticate(this.getRequest(), this.getResponse());
		if (user != null) {
			return user;
		}
		return null;
	}

	public TiramisuRequest getRequest() {
		return request;
	}

	public void setRequest(TiramisuRequest request) {
		this.request = request;
	}

	public TiramisuResponse getResponse() {
		return response;
	}

	public void setResponse(TiramisuResponse response) {
		this.response = response;
	}

	public Session getHibernateSession() {
		return hibernateSession;
	}

	public void setHibernateSession(Session hibernateSession) {
		this.hibernateSession = hibernateSession;
	}
	
}
