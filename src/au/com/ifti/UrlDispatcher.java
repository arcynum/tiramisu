package au.com.ifti;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
  
  private HttpServletRequest servletRequest;
  private HttpServletResponse servletResponse;
  
  /**
   * Dispatching constructor, currently building the route list by hand in this function.
   * Need to ensure the velocity engine is initialised.
   */
  public UrlDispatcher(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
    // Assigning the servlet objects to the dispatcher.
    this.servletRequest = servletRequest;
    this.servletResponse = servletResponse;
    
    routes.add(new Route(Pattern.compile("^/reports[/]?"), ReportController.class, "index"));
    routes.add(new Route(Pattern.compile("^/reports/(?<id>[0-9]{1,})[/]?"), ReportController.class, "view"));
    routes.add(new Route(Pattern.compile("^/reports/add[/]?"), ReportController.class, "add"));
    routes.add(new Route(Pattern.compile("^/reports/edit/(?<id>[0-9]{1,})[/]?"), ReportController.class, "edit"));
    routes.add(new Route(Pattern.compile("^/reports/delete/(?<id>[0-9]{1,})[/]?"), ReportController.class, "delete"));
  }

  /**
   * The dispatch function called by the servlet to start the routing process.
   * @param request
   * @param response
   */
  public void dispatch() {
    
    Boolean matched = false;
    
    this.request = new TiramisuRequest(servletRequest);
    this.response = new TiramisuResponse(servletResponse);
    
    System.out.println(this.request.getRequestUri());
    
    for (Route route : routes) {
      Matcher m = route.getPattern().matcher(this.request.getRequestUri());
      if (m.matches()) {
        matched = true;
        System.out.println(String.format("Matched on: %s", m.group(0)));
        try {
          Object controller = route.getController().getDeclaredConstructor(TiramisuRequest.class, TiramisuResponse.class).newInstance(this.request, this.response);
          
          // Get the method defined by the URL router.
          Method method = controller.getClass().getDeclaredMethod(route.getMethod(), Matcher.class);
          method.invoke(controller, m);
          
          // Call the close method for the controller.
          Method close = controller.getClass().getMethod("close");
          close.invoke(controller);
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

  public void render() {
    servletResponse.setStatus(response.getStatusCode());
  }
  
  public void setRequest(TiramisuRequest request) {
    this.request = request;
  }
  
  public void setResponse(TiramisuResponse response) {
    this.response = response;
  }

}
