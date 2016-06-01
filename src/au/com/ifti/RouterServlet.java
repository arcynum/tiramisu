package au.com.ifti;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import au.com.ifti.utilities.HibernateUtil;

/**
 * Servlet implementation class Router
 */
@WebServlet("/*")
public class RouterServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;
  
  private VelocityEngine velocityEngine;

  /**
   * @see HttpServlet#HttpServlet()
   */
  public RouterServlet() {
    super();
  }
  
  @Override
  public void init() {
    
    // Create the velocity properties.
    Properties props = new Properties();
    props.setProperty("resource.loader", "webapp");
    props.setProperty("webapp.resource.loader.class", "org.apache.velocity.tools.view.WebappResourceLoader");
    props.setProperty("webapp.resource.loader.path", "/WEB-INF/templates/");
    
    // IMPORTANT: Make sure to add the servlet context before calling init().
    velocityEngine = new VelocityEngine(props);
    velocityEngine.setApplicationAttribute("javax.servlet.ServletContext", this.getServletContext());
    
    // Initialise the engine.
    velocityEngine.init();
    
  }

  /**
   * @see HttpServlet#service()
   */
  protected void service(HttpServletRequest request, HttpServletResponse response) {
    // Create the application URL Router and pass it the servlet request and response.
    UrlDispatcher dispatcher = new UrlDispatcher(request, response);
    
    // Dispatch the request to the application.
    dispatcher.dispatch();
    
    // Render the response to the user.
    dispatcher.render();
    
    // Create the velocity context.
    VelocityContext context = new VelocityContext();
    
    // Fetch the template and combine.
    Template velocityTemplate = velocityEngine.getTemplate("layout.html");
    try {
      context.put("body", dispatcher.getResponse().getTemplate());
      context.put("pageTitle", dispatcher.getResponse().getPageTitle());
      velocityTemplate.merge(context, response.getWriter());
    }
    catch (ResourceNotFoundException | ParseErrorException | MethodInvocationException | IOException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Clean up the database connection from Hibernate.
   */
  @Override
  public void destroy() {
      HibernateUtil.tearDownORM();
  }

}
