package au.com.ifti;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import au.com.ifti.controllers.MessageController;
import au.com.ifti.controllers.UserController;
import au.com.ifti.exceptions.BadRequestException;
import au.com.ifti.exceptions.NotFoundException;
import au.com.ifti.utilities.TiramisuRequest;
import au.com.ifti.utilities.TiramisuResponse;

/**
 * The class which handles the routing for the application. The URLDispatcher
 * contains a list of routes which are checked against the HTTP request. If a
 * match is found, it uses Java Reflection to initialise and then call the
 * appropriate controller.
 * 
 * @author Chris Hamilton
 */
public class UrlDispatcher {

	/**
	 * Class logger.
	 */
	private static final Logger log = Logger.getLogger(UrlDispatcher.class.getName());
	
	/**
	 * The hibernate session which this particular request will use.
	 * This does not need to be initialised, as it will be getting assigned.
	 */
	private Session session = null;

	/**
	 * The container of routes which the current request will be checked against.
	 */
	private List<Route> routes = new ArrayList<>();
	
	public UrlDispatcher(Session session) {

		this.setSession(session);

		try {

			// Message Index Method
			routes.add(new Route(Pattern.compile("^.*/messages[/]?"), Arrays.asList("GET"), MessageController.class,
					MessageController.class.getDeclaredMethod("index")));
			
			// Message Read Method
			routes.add(new Route(Pattern.compile("^.*/messages/([0-9]{1,})[/]?"), Arrays.asList("GET"),
					MessageController.class, MessageController.class.getDeclaredMethod("read", String.class)));
			
			// User Index Method
			routes.add(new Route(Pattern.compile("^.*/users[/]?"), Arrays.asList("GET"),
					UserController.class, UserController.class.getDeclaredMethod("index")));
			
			// User Read Method
			routes.add(new Route(Pattern.compile("^.*/users/([0-9]{1,})[/]?"), Arrays.asList("GET"),
					UserController.class, UserController.class.getDeclaredMethod("read", String.class)));
			
			// User Register Method
			routes.add(new Route(Pattern.compile("^.*/users/register[/]?"), Arrays.asList("GET", "POST"),
					UserController.class, UserController.class.getDeclaredMethod("register")));
			
			// User Login Method
			routes.add(new Route(Pattern.compile("^.*/login[/]?"), Arrays.asList("GET", "POST"),
					UserController.class, UserController.class.getDeclaredMethod("login")));
			
			// User Logout Method.
			routes.add(new Route(Pattern.compile("^.*/logout[/]?"), Arrays.asList("GET"),
					UserController.class, UserController.class.getDeclaredMethod("logout")));

		} catch (NoSuchMethodException | SecurityException e) {
			log.log(Level.SEVERE, "The route definitions defined for the web application failed.");
			log.log(Level.SEVERE, e.toString(), e);
		}

	}

	/**
	 * The dispatch function called by the servlet to start the routing process.
	 * This function will check the current request with the list of available routes and HTTP methods.
	 * If a match is found, it will create the controller object and call its method with reflection.
	 * If no match is found, the it will set the response to 404.
	 * This function also needs to handle exceptions coming back from the controller.
	 * 
	 * @param request
	 * @param response
	 */
	public TiramisuResponse dispatch(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {

		// Create the request and response objects, only need to return the
		// response one.
		TiramisuRequest tiramisuRequest = new TiramisuRequest(servletRequest);
		TiramisuResponse tiramisuResponse = new TiramisuResponse(servletResponse);

		Boolean matched = false;

		// Loop through the routes looking for a match.
		for (Route route : routes) {

			// Do any of the defined routes match the current URI?
			Matcher m = route.getPattern().matcher(tiramisuRequest.getRequestUri());

			// If the URL pattern matches.
			if (m.matches()) {

				// If the HTTP method matches as well.
				if (route.getHttpMethods().contains(tiramisuRequest.getMethod())) {

					// Flag used to output a 404, because the route never
					// matched.
					matched = true;

					try {

						// Create the controller (constructor).
						Object controller = route.getController()
								.getDeclaredConstructor(TiramisuRequest.class, TiramisuResponse.class, Session.class)
								.newInstance(tiramisuRequest, tiramisuResponse, this.getSession());

						// Get the arguments.
						// Can't used named groups, have to loop through the
						// counts or its not generic.
						Object[] arguments = new String[m.groupCount()];
						for (int i = 0; i < m.groupCount(); i++) {
							arguments[i] = m.group(i + 1);
						}

						// Invoke the controller function.
						tiramisuResponse = (TiramisuResponse) route.getMethod().invoke(controller, arguments);

					} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException
							| IllegalArgumentException e) {
						e.printStackTrace();
					}
					// Innovation exception is the catch-all for inner
					// controller exceptions.
					// These are easily caught an handled here.
					catch (InvocationTargetException e) {
						Throwable cause = e.getCause();
						log.log(Level.SEVERE, cause.toString(), cause);
						if (cause instanceof BadRequestException) {
							log.severe("Bad request exception");
							tiramisuResponse.setStatusCode(400);
							tiramisuResponse.setTemplate("400.vm");
							tiramisuResponse.setPageTitle("400");
						}
						if (cause instanceof NotFoundException) {
							log.severe("Not found exception");
							tiramisuResponse.setStatusCode(404);
							tiramisuResponse.setTemplate("404.vm");
							tiramisuResponse.setPageTitle("404");
						}
					}
					break;
				}
			}
		}

		// Nothing matched any of the routes, 404.
		if (!matched) {
			tiramisuResponse.setStatusCode(404);
			tiramisuResponse.setTemplate("404.vm");
			tiramisuResponse.setPageTitle("404");
		}

		// Return the response.
		return tiramisuResponse;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

}
