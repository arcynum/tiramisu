package au.com.ifti;

import java.io.IOException;
import java.util.Properties;

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

import au.com.ifti.utilities.HibernateUtil;
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
	private VelocityEngine velocityEngine;

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
		HibernateUtil.getSessionFactory().openSession().close();
	}

	/**
	 * Because the applications handles its own routing. It needs to grab service, rather the GET/POST/etc.
	 * @see HttpServlet#service()
	 */
	protected void service(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {

		log.info("Processing request");

		// Open a database session and pass it to the dispatcher.
		// Manual handling the session for the request lifecycle is better.
		Session session = HibernateUtil.getSessionFactory().openSession();

		// Create the application URL Router and pass it the servlet request and
		// response.
		UrlDispatcher dispatcher = new UrlDispatcher(session);

		// Dispatch the request to the application.
		// I really only need the response here, so just return it, durr.
		TiramisuResponse tiramisuResponse = dispatcher.dispatch(servletRequest, servletResponse);

		// Apply the generic response headers for this application.
		// This is not ideal.
		addGenericHeaders(servletResponse);

		// Add generated headers
		addGeneratedHeaders(servletResponse, tiramisuResponse);

		// Set the response status code.
		servletResponse.setStatus(tiramisuResponse.getStatusCode());

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
			context.put("content", tiramisuResponse.getTemplate());
			context.put("pageTitle", tiramisuResponse.getPageTitle());
			context.put("STATIC_ROOT", servletRequest.getContextPath() + "/static");
			velocityTemplate.merge(context, servletResponse.getWriter());
		} catch (ResourceNotFoundException | ParseErrorException | MethodInvocationException | IOException e) {
			e.printStackTrace();
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
		response.setCharacterEncoding("utf-8");
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
