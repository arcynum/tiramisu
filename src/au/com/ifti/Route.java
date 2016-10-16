package au.com.ifti;

import java.lang.reflect.Method;
import java.util.List;
import java.util.regex.Pattern;

/**
 * The route class is used to wrap up a web endpoint in a single object. The web
 * application then checks all routes to determine what the URL matched.
 * 
 * @author CIH221
 */
public class Route {

	/**
	 * The regular expression to match against.
	 */
	private Pattern pattern = null;
	
	/**
	 * The list of HTTP methods this route accepts.
	 */
	private List<String> httpMethods = null;
	
	/**
	 * The controller which the requests will be routed too.
	 * @see Controller
	 */
	private Class<?> controller = null;
	
	/**
	 * The method which will be called in the controller.
	 */
	private Method method = null;

	/**
	 * Constructor for the route object.
	 * @param pattern
	 * @param httpMethods
	 * @param controller
	 * @param method
	 */
	public Route(Pattern pattern, List<String> httpMethods, Class<?> controller, Method method) {
		this.setPattern(pattern);
		this.setHttpMethods(httpMethods);
		this.setController(controller);
		this.setMethod(method);
	}

	public Pattern getPattern() {
		return pattern;
	}

	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}

	public Class<?> getController() {
		return controller;
	}

	public void setController(Class<?> controller) {
		this.controller = controller;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public List<String> getHttpMethods() {
		return httpMethods;
	}

	public void setHttpMethods(List<String> httpMethods) {
		this.httpMethods = httpMethods;
	}

}
