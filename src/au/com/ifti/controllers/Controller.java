package au.com.ifti.controllers;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import au.com.ifti.models.Model;
import au.com.ifti.utilities.TiramisuRequest;
import au.com.ifti.utilities.TiramisuResponse;

/**
 * This is the abstract controller for the entire application. All controllers
 * need to extend this controller.
 * 
 * @author Chris Hamilton
 */
public abstract class Controller {

	/**
	 * The application wrapped HTTP request object.
	 */
	protected TiramisuRequest request;
	
	/**
	 * The application wrapped HTTP response object.
	 */
	protected TiramisuResponse response;
	
	/**
	 * The session object, which will be passed in from the dispatcher.
	 */
	protected Session session = null;

	/**
	 * The default constructor for the Controller objects.
	 * @param request
	 * @param response
	 * @param session
	 */
	public Controller(TiramisuRequest request, TiramisuResponse response, Session session) {
		this.setRequest(request);
		this.setResponse(response);
		this.setSession(session);
	}

	/**
	 * Find all the objects in the database of the specified type/class.
	 * @param classIdentifier The object class you want returned.
	 * @return Returns a casted List of objects returned from the database.
	 */
	public List<?> findAll(Class<?> classIdentifier) {
		this.getSession().beginTransaction();
		List<?> result = this.getSession().createCriteria(classIdentifier).list();
		this.getSession().getTransaction().commit();
		return result;
	}

	/**
	 * A type casted object, of the specified id, from the database.
	 * @param classIdentifier The class of the object you are requesting.
	 * @param id The primary key of the object you are requesting.
	 * @return A single object of the type passed into the function.
	 */
	public <T extends Model> T findById(Class<T> classIdentifier, Integer id) {
		this.getSession().beginTransaction();
		Object result = this.getSession().get(classIdentifier, id);
		this.getSession().getTransaction().commit();

		try {
			return classIdentifier.cast(result);
		} catch (ClassCastException e) {
			return null;
		}
	}

	/**
	 * Save the object to the database and updated the timestamps.
	 * @param object A object which extends from the Model class.
	 * @return Whether the object saved or not.
	 */
	public Boolean save(Model object) {
		this.getSession().beginTransaction();
		// So the timestamps match. Create a single object.
		Date now = new Date();
		object.setCreated(now);
		object.setModified(now);
		this.getSession().save(object);
		this.getSession().getTransaction().commit();
		return true;
	}

	/**
	 * Update an object in the database.
	 * @param object A object which extends from the Model class.
	 * @return Whether the object updated or not.
	 */
	public Boolean update(Model object) {
		this.getSession().beginTransaction();
		object.setModified(new Date());
		this.getSession().save(object);
		this.getSession().getTransaction().commit();
		return true;
	}

	/**
	 * Add a view variable to the response object.
	 * View variables will be available to be output in the velocity template.
	 * @param key The view variable key. Make sure this is unique.
	 * @param value The value you want to set. This can be any valid Java object.
	 */
	public void set(String key, Object value) {
		this.getResponse().addViewVariable(key, value);
	}

	/**
	 * Redirect to the provided URL.
	 * @param url Where to redirect to.
	 * @return The TiramisuResponse object.
	 */
	public TiramisuResponse redirect(String url) {
		this.getResponse().setStatusCode(303);
		this.getResponse().setHeader("Location", url);
		return this.getResponse();
	}

	/**
	 * Redirect to the provided URL with a custom HTTP code.
	 * @param url Where to redirect to.
	 * @param code The HTTP code you want to present.
	 * @return The TiramisuResponse object.
	 */
	public TiramisuResponse redirect(String url, Integer code) {
		this.getResponse().setStatusCode(code);
		this.getResponse().setHeader("Location", url);
		return this.getResponse();
	}
	
	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public TiramisuRequest getRequest() {
		return request;
	}

	public TiramisuResponse getResponse() {
		return response;
	}

	public void setRequest(TiramisuRequest request) {
		this.request = request;
	}

	public void setResponse(TiramisuResponse response) {
		this.response = response;
	}
	
}
