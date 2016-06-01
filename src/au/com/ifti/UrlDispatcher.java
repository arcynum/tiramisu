package au.com.ifti;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import au.com.ifti.controllers.AdminController;
import au.com.ifti.controllers.DbmsController;
import au.com.ifti.controllers.ReportController;
import au.com.ifti.controllers.ServerController;
import au.com.ifti.controllers.TagController;
import au.com.ifti.exceptions.BadRequestException;
import au.com.ifti.exceptions.NotFoundException;
import au.com.ifti.utilities.TiramisuRequest;
import au.com.ifti.utilities.TiramisuResponse;

public class UrlDispatcher {
  
  /**
   * This map is a regex expression pattern to the class to load next.
   */
  private List<Route> routes = new ArrayList<>();
  
  /**
   * Dispatching constructor, currently building the route list by hand in this function.
   */
  public UrlDispatcher() {
    routes.add(new Route(Pattern.compile("^/Tiramisu/admin[/]?"), AdminController.class, "run"));
    routes.add(new Route(Pattern.compile("^/Tiramisu/(?<dbms>[0-9]{1,})[/]?"), DbmsController.class, "run"));
    routes.add(new Route(Pattern.compile("^/Tiramisu/(?<dbms>[0-9]{1,})/(?<server>[0-9]{1,})[/]?"), ServerController.class, "run"));
    
    routes.add(new Route(Pattern.compile("^/Tiramisu/Reports[/]?"), ReportController.class, "run"));
    routes.add(new Route(Pattern.compile("^/Tiramisu/Reports/(?<id>[0-9]{1,})[/]?"), ReportController.class, "view"));
    routes.add(new Route(Pattern.compile("^/Tiramisu/Tags[/]?"), TagController.class, "run"));
  }
  
  /**
   * The dispatch function called by the servlet to start the routing process.
   * @param request
   * @param response
   * @throws IOException 
   */
  public void dispatch(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
    
    Boolean matched = false;
    TiramisuRequest request = null;
    TiramisuResponse response = null;
    
    try {
      request = new TiramisuRequest(servletRequest);
      response = new TiramisuResponse(servletResponse);
    }
    catch (IOException e1) {
      e1.printStackTrace();
    }
    
    for (Route route : routes) {
      Matcher m = route.getPattern().matcher(request.getRequestUri());
      if (m.matches()) {
        matched = true;
        System.out.println(String.format("Matched on: %s", m.group(0)));
        try {
          Object controller = route.getController().getDeclaredConstructor(TiramisuRequest.class, TiramisuResponse.class).newInstance(request, response);
          
          // Get the method defined by the URL router.
          Method method = controller.getClass().getMethod(route.getMethod(), Matcher.class);
          method.invoke(controller, m);
          
          // Call the close method for the controller.
          Method close = controller.getClass().getMethod("close");
          close.invoke(controller);
        }
        catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException e) {
          e.printStackTrace();
        }
        catch (InvocationTargetException e) {
          Throwable cause = e.getCause();
          if (cause instanceof BadRequestException) {
            response.setStatusCode(400);
          }
          if (cause instanceof NotFoundException) {
            response.setStatusCode(404);
          }
        }
        break;
      }
    }
    
    // Nothing matched, so write out a message saying that.
    if (!matched) {
      response.getWriter().append("<h1>You did not match any routes, available routes are</h1>");
      for (Route route : routes) {
        response.getWriter().append("<ul>");
        response.getWriter().append("<li>" + route.getPattern().toString() + "</li>");
        response.getWriter().append("</ul>");
      }
    }
    
    
    // Transfer the custom response string writer into the real response.
    try {
      servletResponse.setStatus(response.getStatusCode());
      servletResponse.getWriter().print(response.getWriter());
    }
    catch (IOException e1) {
      e1.printStackTrace();
    }
    
  }

}
