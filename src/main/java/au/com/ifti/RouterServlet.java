package au.com.ifti;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.app.VelocityEngine;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.ifti.controllers.UserController;
import au.com.ifti.processors.HtmlProcessor;
import au.com.ifti.processors.JsonProcessor;
import au.com.ifti.processors.Processor;
import au.com.ifti.utilities.HibernateUtil;
import au.com.ifti.utilities.MediaType;
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
		
		// Fetch the pepper from the servlet.
		// Load up any context parameters and put them into the configuration object.
		TiramisuConfiguration.pepper = getServletContext().getInitParameter("pepper");
		
		// Add the routes to the application.
		// These are controlled by the 
		try {
			// User Index Method
			routeList.add(new Route(Pattern.compile("^.*\\/users[/]?$"), Arrays.asList("GET"), UserController.class, UserController.class.getDeclaredMethod("index")));
			
			// User Read Method
			routeList.add(new Route(Pattern.compile("^.*/users/([0-9]{1,})[/]?$"), Arrays.asList("GET"), UserController.class, UserController.class.getDeclaredMethod("read", String.class)));
			
			// User Register Method
			routeList.add(new Route(Pattern.compile("^.*/users/register[/]?$"), Arrays.asList("GET", "POST"), UserController.class, UserController.class.getDeclaredMethod("register")));
			
			// User Login Method
			routeList.add(new Route(Pattern.compile("^.*/login[/]?$"), Arrays.asList("GET", "POST"), UserController.class, UserController.class.getDeclaredMethod("login")));
			
			// User Logout Method.
			routeList.add(new Route(Pattern.compile("^.*/logout[/]?$"), Arrays.asList("GET"), UserController.class, UserController.class.getDeclaredMethod("logout")));
		
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
		
		// Temporarily printing session data.
		Enumeration<String> en = servletRequest.getSession().getAttributeNames();
		while (en.hasMoreElements()) {
			String key = (String)en.nextElement();
			log.info(key + " = " + servletRequest.getSession().getAttribute(key));
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
		
		// Now that the request has been dispatched and returned, create the output processor.
		// Don't forget to add the response objects.
		Processor outputProcessor = createProcessor(tiramisuRequest.getMediaType(), velocityEngine);
		outputProcessor.setServletRequest(servletRequest);
		outputProcessor.setServletResponse(servletResponse);
		outputProcessor.setTiramisuResponse(tiramisuResponse);
		outputProcessor.setFlashSessions(servletRequest.getSession().getAttribute("flash"));
		
		// Finally call the render function to actually process the data into a response.
		outputProcessor.render();

		// Close the hibernate session.
		session.close();
	}
	
	private Processor createProcessor(MediaType mediaType, VelocityEngine velocityEngine) {
		Processor processor = null;
		switch (mediaType) {
			case HTML: {
				processor = new HtmlProcessor(velocityEngine);
				break;
			}
			case JSON: {
				processor = new JsonProcessor();
				break;
			}
			default: {
				processor = new HtmlProcessor(velocityEngine);
			}
		}
		return processor;
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
