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
import au.com.ifti.controllers.ServerController;
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
    routes.add(new Route(Pattern.compile("^/Tiramisu/(?<dbms>[A-Za-z]{0,})[/]?"), DbmsController.class, "run"));
    routes.add(new Route(Pattern.compile("^/Tiramisu/(?<dbms>[A-Za-z]{0,})/(?<server>[A-Za-z]{0,})[/]?"), ServerController.class, "run"));
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
          Method method = controller.getClass().getMethod(route.getMethod(), Matcher.class);
          method.invoke(controller, m);
        }
        catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException  e) {
          e.printStackTrace();
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
      servletResponse.getWriter().print(response.getWriter());
    }
    catch (IOException e1) {
      e1.printStackTrace();
    }
    
  }

}
