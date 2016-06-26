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

import au.com.ifti.controllers.ReportController;
import au.com.ifti.exceptions.BadRequestException;
import au.com.ifti.exceptions.NotFoundException;
import au.com.ifti.utilities.TiramisuRequest;
import au.com.ifti.utilities.TiramisuResponse;

public class UrlDispatcher {
  
  private static final Logger log = Logger.getLogger(UrlDispatcher.class.getName());

  private TiramisuRequest request;
  private TiramisuResponse response;
  private Session session;

  /**
   * This map is a regex expression pattern to the class to load next.
   */
  private List<Route> routes = new ArrayList<>();

  /**
   * Dispatching constructor, currently building the route list by hand in this function. Need to
   * ensure the velocity engine is initialised.
   */
  public UrlDispatcher(HttpServletRequest servletRequest, HttpServletResponse servletResponse, Session session) {
    this.request = new TiramisuRequest(servletRequest);
    this.response = new TiramisuResponse(servletResponse);
    this.setSession(session);

    try {
      routes.add(new Route(Pattern.compile("^.*/reports[/]?"), Arrays.asList("GET"),
          ReportController.class, ReportController.class.getDeclaredMethod("index")));
      routes.add(new Route(Pattern.compile("^.*/reports/([0-9]{1,})[/]?"), Arrays.asList("GET"),
          ReportController.class, ReportController.class.getDeclaredMethod("view", String.class)));
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
  public void dispatch() {
    Boolean matched = false;

    // Loop through the routes looking for a match.
    for (Route route : routes) {
      Matcher m = route.getPattern().matcher(this.request.getRequestUri());
      // If the URL pattern matches.
      if (m.matches()) {

        // If the HTTP method matches.
        if (route.getHttpMethods().contains(this.request.getMethod())) {
          matched = true;

          System.out.println(
              String.format("Matched on: %s over %s", m.group(0), this.request.getMethod()));
          try {

            // Create the controller (constructor).
            Object controller = route.getController()
                .getDeclaredConstructor(TiramisuRequest.class, TiramisuResponse.class)
                .newInstance(this.request, this.response);

            // Get the arguments.
            Object[] arguments = new String[m.groupCount()];
            for (int i = 0; i < m.groupCount(); i++) {
              arguments[i] = m.group(i + 1);
            }

            // Invoke the function.
            route.getMethod().invoke(controller, arguments);
          } catch (InstantiationException | IllegalAccessException | NoSuchMethodException
              | SecurityException | IllegalArgumentException e) {
            e.printStackTrace();
          } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            log.log(Level.SEVERE, cause.toString(), cause);
            if (cause instanceof BadRequestException) {
              System.out.println("Bad request exception");
              this.response.setStatusCode(400);
              this.response.setTemplate("400.vm");
              this.response.setPageTitle("400");
            }
            if (cause instanceof NotFoundException) {
              System.out.println("Not found exception");
              this.response.setStatusCode(404);
              this.response.setTemplate("404.vm");
              this.response.setPageTitle("404");
            }
          }
          break;
        }
      }
    }

    // Nothing matched, 404.
    if (!matched) {
      this.response.setStatusCode(404);
      this.response.setTemplate("404.vm");
      this.response.setPageTitle("404");
    }

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

  public Session getSession() {
    return session;
  }

  public void setSession(Session session) {
    this.session = session;
  }

}
