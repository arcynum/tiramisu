package au.com.ifti;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
  
  private TiramisuRequest request;
  private TiramisuResponse response;
  
  /**
   * This map is a regex expression pattern to the class to load next.
   */
  private List<Route> routes = new ArrayList<>();
  
  /**
   * Dispatching constructor, currently building the route list by hand in this function.
   * Need to ensure the velocity engine is initialised.
   */
  public UrlDispatcher(HttpServletRequest servletRequest, HttpServletResponse servletResponse, Session session) {
    this.request = new TiramisuRequest(servletRequest);
    this.response = new TiramisuResponse(servletResponse);
    
    // Set the session in the response object.
    this.response.setSession(session);

    
    routes.add(new Route(Pattern.compile("^.*/reports[/]?"), Arrays.asList("GET"), ReportController.class, "index"));
    routes.add(new Route(Pattern.compile("^.*/reports/([0-9]{1,})[/]?"), Arrays.asList("GET"), ReportController.class, "view"));

  }

  /**
   * The dispatch function called by the servlet to start the routing process.
   * @param request
   * @param response
   */
  public void dispatch() {
    
    Boolean matched = false;
    
    System.out.println(this.request.getRequestUri());
    System.out.println(this.request.getMethod());
    
    for (Route route : routes) {
      Matcher m = route.getPattern().matcher(this.request.getRequestUri());
      // If the URL pattern matches.
      if (m.matches()) {
        
        // If the HTTP method matches.
        if (route.getHttpMethods().contains(this.request.getMethod())) {
          matched = true;
          
          System.out.println(String.format("Matched on: %s over %s", m.group(0), this.request.getMethod()));
          try {
            
            // Create the controller (constructor).
            Object controller = route.getController().getDeclaredConstructor(TiramisuRequest.class, TiramisuResponse.class).newInstance(this.request, this.response);
            
            String[] arguments = new String[m.groupCount()];
            for (int i = 1; i < m.groupCount(); i++) {
              arguments[i] = m.group(i);
            }
            
            switch (m.groupCount()) {
              case 0: {
                controller.getClass().getDeclaredMethod(route.getMethod()).invoke(controller);
                break;
              }
              case 1: {
                controller.getClass().getDeclaredMethod(route.getMethod(), String.class).invoke(controller, m.group(1));
                break;
              }
              case 2: {
                controller.getClass().getDeclaredMethod(route.getMethod(), String.class, String.class).invoke(controller, m.group(1), m.group(2));
                break;
              }
              case 3: {
                controller.getClass().getDeclaredMethod(route.getMethod(), String.class, String.class, String.class).invoke(controller, m.group(1), m.group(2), m.group(3));
                break;
              }
              case 4: {
                controller.getClass().getDeclaredMethod(route.getMethod(), String.class, String.class, String.class, String.class).invoke(controller, m.group(1), m.group(2), m.group(3), m.group(4));
                break;
              }
            }
            
          }
          catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException e) {
            e.printStackTrace();
          }
          catch (InvocationTargetException e) {
            System.out.println(e);
            Throwable cause = e.getCause();
            System.out.println(cause);
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

}
