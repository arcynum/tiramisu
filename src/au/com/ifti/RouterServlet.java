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
    // Apply the generic headers
    addGenericHeaders(response);
    
    // Create the application URL Router and pass it the servlet request and response.
    UrlDispatcher dispatcher = new UrlDispatcher(request, response);
    
    // Dispatch the request to the application.
    dispatcher.dispatch();
    
    // Render the response to the user.
    dispatcher.render();
    
    // Create the velocity context.
    VelocityContext context = new VelocityContext();
    
    // Fetch the template and combine.
    Template velocityTemplate = velocityEngine.getTemplate("layout.vm");
    try {
      // Loop through the assigned keys in the response and render them to the template.
      // Using a named array here means you can access by name from the template.
      for (String key : dispatcher.getResponse().getData().keySet()) {
        context.put(key, dispatcher.getResponse().getData().get(key));
      }
      context.put("content", dispatcher.getResponse().getTemplate());
      context.put("pageTitle", dispatcher.getResponse().getPageTitle());
      velocityTemplate.merge(context, response.getWriter());
    }
    catch (ResourceNotFoundException | ParseErrorException | MethodInvocationException | IOException e) {
      e.printStackTrace();
    }
  }
  
  private void addGenericHeaders(HttpServletResponse response) {
    response.setCharacterEncoding("utf-8");
    response.setContentType("text/html;charset=UTF-8");
  }
  
  /**
   * Clean up the database connection from Hibernate.
   */
  @Override
  public void destroy() {
    HibernateUtil.tearDownORM();
  }

}
