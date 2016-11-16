package au.com.ifti.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;

import au.com.ifti.components.Component;
import au.com.ifti.components.SessionComponent;
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
	 * The logging system.
	 */
	private static final Logger log = Logger.getLogger(Controller.class.getName());

	/**
	 * The application wrapped HTTP request object.
	 */
	protected TiramisuRequest request = null;
	
	/**
	 * The application wrapped HTTP response object.
	 */
	protected TiramisuResponse response = null;
	
	/**
	 * The Hibernate session object, which will be passed in from the dispatcher.
	 */
	protected Session hibernateSession = null;
	
	/**
	 * An array of components which controllers can use.
	 * As long as the key exists, the component can be used.
	 * This might be better of as a bunch of individual members, rather than a map.
	 */
	protected Map<String, Component> components = new HashMap<String, Component>();
	
	/**
	 * The session component will be used universally.
	 * Adding this utility component as a directly owned object.
	 */
	protected SessionComponent session = null;

	/**
	 * The default constructor for the Controller objects.
	 * @param request
	 * @param response
	 * @param hibernateSession
	 */
	public Controller(TiramisuRequest request, TiramisuResponse response, Session hibernateSession) {
		this.setRequest(request);
		this.setResponse(response);
		this.setHibernateSession(hibernateSession);
		
		// Initialise some components.
		this.addComponent("session", new SessionComponent(request.getSession()));
	}
	
	/**
	 * Generic function which is used fetch all entities of a type from the database. 
	 * @param classIdentifier The class which is being fetched.
	 * @return List<T> A list of the entities retrieved. 
	 */
	public <T extends Model> List<T> findAll(Class<T> classIdentifier) {
		this.getHibernateSession().beginTransaction();
		CriteriaQuery<T> criteriaQuery = this.getHibernateSession().getCriteriaBuilder().createQuery(classIdentifier);
		Root<T> root = criteriaQuery.from(classIdentifier);
		criteriaQuery.select(root);
		List<T> result = this.getHibernateSession().createQuery(criteriaQuery).getResultList();
		this.getHibernateSession().getTransaction().commit();
		return result;
	}

	/**
	 * A type casted object, of the specified id, from the database.
	 * @param classIdentifier The class of the object you are requesting.
	 * @param id The primary key of the object you are requesting.
	 * @return A single object of the type passed into the function.
	 */
	public <T extends Model> T findById(Class<T> classIdentifier, Integer id) {
		this.getHibernateSession().beginTransaction();
		Object result = this.getHibernateSession().get(classIdentifier, id);
		this.getHibernateSession().getTransaction().commit();

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
		this.getHibernateSession().beginTransaction();
		// So the timestamps match. Create a single object.
		Date now = new Date();
		object.setCreated(now);
		object.setModified(now);
		this.getHibernateSession().save(object);
		this.getHibernateSession().getTransaction().commit();
		return true;
	}

	/**
	 * Update an object in the database.
	 * @param object A object which extends from the Model class.
	 * @return Whether the object updated or not.
	 */
	public Boolean update(Model object) {
		this.getHibernateSession().beginTransaction();
		object.setModified(new Date());
		this.getHibernateSession().save(object);
		this.getHibernateSession().getTransaction().commit();
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
	
	public void beforeMethod() {
		
	}
	
	public void afterMethod() {
		
	}
	
	public Session getHibernateSession() {
		return hibernateSession;
	}

	public void setHibernateSession(Session session) {
		this.hibernateSession = session;
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

	public Map<String, Component> getComponents() {
		return components;
	}

	public void setComponents(Map<String, Component> components) {
		this.components = components;
	}
	
	/**
	 * Get a component
	 * @param key The name of the component.
	 * @return Returns the component or null if the component is not found.
	 */
	public Component getComponent(String key) {
		if (this.components.containsKey(key)) {
			return this.components.get(key);
		}
		return null;
	}
	
	/**
	 * Add a component to the Map
	 * @param key The name of the component
	 * @param component The component to be added.
	 */
	public void addComponent(String key, Component component) {
		if (!this.components.containsKey(key)) {
			this.components.put(key, component);
		} else {
			log.warning("Trying to add a component which already exists");
		}
	}
	
}
