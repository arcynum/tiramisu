package au.com.ifti.components;

import org.hibernate.Session;

import au.com.ifti.components.auth.FormAuthenticate;
import au.com.ifti.models.UserModel;
import au.com.ifti.utilities.TiramisuRequest;
import au.com.ifti.utilities.TiramisuResponse;

public class AuthComponent extends Component {
	
	private static final String sessionKey = "Auth.User";
	
	private TiramisuRequest request = null;
	private TiramisuResponse response = null;
	private Session hibernateSession = null;
	private SessionComponent sessionComponent = null;
	
	public AuthComponent(TiramisuRequest request, TiramisuResponse response, Session hibernateSession) {
		super();
		
		this.setRequest(request);
		this.setResponse(response);
		this.setHibernateSession(hibernateSession);
		
		// AuthComponents need to make use of a the session component.
		this.sessionComponent = new SessionComponent(request.getSession());
	}
	
	/**
	 * Function to login the user.
	 * @return
	 */
	public Boolean login() {
		UserModel user = identify();
		if (user != null) {
			// Writing the user object to the session.
			this.getSessionComponent().write(AuthComponent.sessionKey, user);
			return true;
		}
		return false;
	}
	
	/**
	 * The logout function will destroy the Auth key in the visitors session.
	 * It will then return the location to redirect the user to.
	 * @return
	 */
	public String logout() {
		this.getSessionComponent().delete(AuthComponent.sessionKey);
		return "/tiramisu/login";
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

	public SessionComponent getSessionComponent() {
		return sessionComponent;
	}

	public void setSessionComponent(SessionComponent sessionComponent) {
		this.sessionComponent = sessionComponent;
	}
	
}
