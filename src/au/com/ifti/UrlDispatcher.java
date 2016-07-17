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

import au.com.ifti.controllers.PostController;
import au.com.ifti.exceptions.BadRequestException;
import au.com.ifti.exceptions.NotFoundException;
import au.com.ifti.utilities.TiramisuRequest;
import au.com.ifti.utilities.TiramisuResponse;

public class UrlDispatcher {
  
  private static final Logger log = Logger.getLogger(UrlDispatcher.class.getName());

  private Session session;

  /**
   * This map is a regex expression pattern to the class to load next.
   */
  private List<Route> routes = new ArrayList<>();

  /**
   * Dispatching constructor, currently building the route list by hand in this function. Need to
   * ensure the velocity engine is initialised.
   */
  public UrlDispatcher(Session session) {
    
    this.setSession(session);

    try {
      
      // Post Routes
      routes.add(new Route(Pattern.compile("^.*/posts[/]?"), Arrays.asList("GET"), PostController.class, PostController.class.getDeclaredMethod("index")));
      routes.add(new Route(Pattern.compile("^.*/posts[/]?"), Arrays.asList("POST"), PostController.class, PostController.class.getDeclaredMethod("create")));
      routes.add(new Route(Pattern.compile("^.*/posts/([0-9]{1,})[/]?"), Arrays.asList("GET"), PostController.class, PostController.class.getDeclaredMethod("read", String.class)));
      routes.add(new Route(Pattern.compile("^.*/posts/([0-9]{1,})[/]?"), Arrays.asList("PUT"), PostController.class, PostController.class.getDeclaredMethod("update", String.class)));
      routes.add(new Route(Pattern.compile("^.*/posts/([0-9]{1,})[/]?"), Arrays.asList("DELETE"), PostController.class, PostController.class.getDeclaredMethod("delete", String.class)));
      
    } catch (NoSuchMethodException | SecurityException e) {
      log.log(Level.SEVERE, "The route definitions defined for the web application failed.");
      log.log(Level.SEVERE, e.toString(), e);
    }

  }

  /**
   * The dispatch function called by the servlet to start the routing process.
   * 
   * @param request
   * @param response
   */
  public TiramisuResponse dispatch(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
    
    // Create the request and response objects, only need to return the response one.
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
          
          // Flag used to output a 404, because the route never matched.
          matched = true;
          
          try {

            // Create the controller (constructor).
            Object controller = route.getController()
                .getDeclaredConstructor(TiramisuRequest.class, TiramisuResponse.class, Session.class)
                .newInstance(tiramisuRequest, tiramisuResponse, this.getSession());

            // Get the arguments.
            // Can't used named groups, have to loop through the counts or its not generic.
            Object[] arguments = new String[m.groupCount()];
            for (int i = 0; i < m.groupCount(); i++) {
              arguments[i] = m.group(i + 1);
            }

            // Invoke the controller function.
            tiramisuResponse = (TiramisuResponse) route.getMethod().invoke(controller, arguments);
            
          }
          catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException e) {
            e.printStackTrace();
          }
          // Innovation exception is the catch-all for inner controller exceptions.
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
