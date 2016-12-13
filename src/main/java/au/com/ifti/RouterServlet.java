package au.com.ifti;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.ifti.controllers.UserController;
import au.com.ifti.utilities.HibernateUtil;
import au.com.ifti.utilities.TiramisuConfiguration;
import au.com.ifti.utilities.TiramisuRequest;
import au.com.ifti.utilities.TiramisuResponse;

/**
 * Primary Servlet for the application.
 * 
 * @author Chris Hamilton
 */
public class RouterServlet extends HttpServlet {
	
	/**
	 * Default serial id for the class.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Class logger.
	 */
	private final Logger log = LoggerFactory.getLogger(RouterServlet.class);
	
	/**
	 * The Velocity Template Engine.
	 * The servlet owns and makes use of this object. The rest of the application cannot touch templates.
	 */
	private VelocityEngine velocityEngine = null;
	
	/**
	 * Routes container, which contains all of the URL endpoints for the application.
	 * This is static because all application users need the same route list.
	 */
	private ArrayList<Route> routeList = new ArrayList<Route>();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RouterServlet() {
		super();
	}

	/**
	 * Initialise the velocity and hibernate engine.
	 * @see HttpServlet#init()
	 */
	@Override
	public void init() {

		log.info("Initialising servlet");

		// Create the velocity properties.
		Properties props = new Properties();
		props.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogChute");
		props.setProperty("resource.loader", "webapp");
		props.setProperty("webapp.resource.loader.class", "org.apache.velocity.tools.view.WebappResourceLoader");
		props.setProperty("webapp.resource.loader.path", "/WEB-INF/templates/");

		// IMPORTANT: Make sure to add the servlet context before calling
		// init().
		velocityEngine = new VelocityEngine(props);
		velocityEngine.setApplicationAttribute("javax.servlet.ServletContext", this.getServletContext());

		// Initialise the engine.
		velocityEngine.init();

		// Attempt to initialise hibernate.
		// Doing this in servlet initialisation means the first request doesn't have to perform this action.
		HibernateUtil.getSessionFactory().openSession().close();
		
		// Add the routes to the application.
		// These are controlled by the 
		try {
			// User Index Method
			routeList.add(new Route(Pattern.compile("^.*/users[/]?"), Arrays.asList("GET"), UserController.class, UserController.class.getDeclaredMethod("index")));
			
			// User Read Method
			routeList.add(new Route(Pattern.compile("^.*/users/([0-9]{1,})[/]?"), Arrays.asList("GET"), UserController.class, UserController.class.getDeclaredMethod("read", String.class)));
			
			// User Register Method
			routeList.add(new Route(Pattern.compile("^.*/users/register[/]?"), Arrays.asList("GET", "POST"), UserController.class, UserController.class.getDeclaredMethod("register")));
			
			// User Login Method
			routeList.add(new Route(Pattern.compile("^.*/login[/]?"), Arrays.asList("GET", "POST"), UserController.class, UserController.class.getDeclaredMethod("login")));
			
			// User Logout Method.
			routeList.add(new Route(Pattern.compile("^.*/logout[/]?"), Arrays.asList("GET"), UserController.class, UserController.class.getDeclaredMethod("logout")));
		
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Because the applications handles its own routing. It needs to grab service, rather the GET/POST/etc.
	 * @see HttpServlet#service()
	 */
	protected void service(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
		
		log.info("Processing request");
		
		// Wrap the standard HttpRequest in the Application Version.
		TiramisuRequest tiramisuRequest = new TiramisuRequest(servletRequest);
		TiramisuResponse tiramisuResponse = new TiramisuResponse(servletResponse);
		
		// Fetch the pepper from the servlet.
		// Load up any context parameters and put them into the configuration object.
		TiramisuConfiguration.pepper = getServletContext().getInitParameter("pepper");
		
		// Temporarily printing session data.
		Enumeration<String> en = servletRequest.getSession().getAttributeNames();
		while (en.hasMoreElements()) {
			String key = (String)en.nextElement();
			System.out.println(key + " = " + servletRequest.getSession().getAttribute(key));
		}

		// Open a database session and pass it to the dispatcher.
		// Manual handling the session for the request lifecycle is better.
		Session session = HibernateUtil.getSessionFactory().openSession();

		// Create the application URL Router and pass it the servlet request and
		// response.
		UrlDispatcher dispatcher = new UrlDispatcher(routeList, session);

		// Dispatch the request to the application.
		// The response object will be modified in the children, no need to return it.
		dispatcher.dispatch(tiramisuRequest, tiramisuResponse);

		// Apply the generic response headers for this application.
		// This is not ideal.
		addGenericHeaders(servletResponse);

		// Add generated headers
		addGeneratedHeaders(servletResponse, tiramisuResponse);

		// Set the response status code.
		servletResponse.setStatus(tiramisuResponse.getStatusCode());
		
		// New flash array.
		List<String> newFlashList = new ArrayList<String>();
		
		// Add all of the old messages.
		Object flashSessionList = servletRequest.getSession().getAttribute("flash");
		if (flashSessionList instanceof ArrayList<?>) {
			ArrayList<?> innerFlashSessionList = (ArrayList<?>) flashSessionList;
			for (Object item : innerFlashSessionList) {
				if (item instanceof String) {
					newFlashList.add((String) item);
				}
			}
		}
		
		// Add all of the new messages.
		newFlashList.addAll(tiramisuResponse.getFlashMessages());
		
		// Overwrite the old session variable with the new session variable.
		servletRequest.getSession().setAttribute("flash", newFlashList);
		
		// If the response code is in the 300 range, no need to create and manage velocity contexts.
		if (servletResponse.getStatus() < 300 || servletResponse.getStatus() >= 400) {

			// Create the velocity context.
			VelocityContext context = new VelocityContext();

			// Fetch the template and combine.
			Template velocityTemplate = velocityEngine.getTemplate("layout.vm");
			try {
				// Loop through the assigned keys in the response and render them to
				// the template.
				// Using a named array here means you can access by name from the
				// template.
				for (String key : tiramisuResponse.getData().keySet()) {
					context.put(key, tiramisuResponse.getData().get(key));
				}
				
				// Write the flash messages to the context.
				context.put("messages", servletRequest.getSession().getAttribute("flash"));
				
				// Remove those messages from the session once they have been written.
				servletRequest.getSession().removeAttribute("flash");
				
				context.put("content", tiramisuResponse.getTemplate());
				context.put("pageTitle", tiramisuResponse.getPageTitle());
				context.put("STATIC_ROOT", servletRequest.getContextPath() + "/static");
				velocityTemplate.merge(context, servletResponse.getWriter());
			} catch (ResourceNotFoundException | ParseErrorException | MethodInvocationException | IOException e) {
				e.printStackTrace();
			}
			
		}

		// Close the hibernate session.
		session.close();
	}

	/**
	 * Function to add generic headers to the application.
	 * This will need to change to response with alternate rest types.
	 * @param response
	 */
	private void addGenericHeaders(HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
	}
	
	/**
	 * Function to merge custom headers with the original HttpServletResponse.
	 * @param servletResponse
	 * @param tiramisuResponse
	 */
	private void addGeneratedHeaders(HttpServletResponse servletResponse, TiramisuResponse tiramisuResponse) {
		for (String key : tiramisuResponse.getHeaders().keySet()) {
			servletResponse.setHeader(key, tiramisuResponse.getHeaders().get(key));
		}
	}

	/**
	 * Clean up the database connection from Hibernate.
	 * @see HttpServlet#service()
	 */
	@Override
	public void destroy() {
		HibernateUtil.destroyRegistry();
	}

}
