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

public class UrlDispatcher {
  
  /**
   * This map is a regex expression pattern to the class to load next.
   */
  private List<Route> routes = new ArrayList<>();
  
  /**
   * Dispatching constructor, currently building the route list by hand in this function.
   */
  public UrlDispatcher() {
    routes.add(new Route(Pattern.compile("^/Router/admin[/]?"), AdminController.class, "run"));
    routes.add(new Route(Pattern.compile("^/Router/(?<dbms>[A-Za-z]{0,})/"), DbmsController.class, "run"));
    routes.add(new Route(Pattern.compile("^/Router/(?<dbms>[A-Za-z]{0,})/(?<server>[A-Za-z]{0,})[/]?"), ServerController.class, "run"));
  }
  
  /**
   * The dispatch function called by the servlet to start the routing process.
   * @param request
   * @param response
   */
  public void dispatch(HttpServletRequest request, HttpServletResponse response) {
    
    Boolean matched = false;
    
    for (Route route : routes) {
      Matcher m = route.getPattern().matcher(request.getRequestURI());
      if (m.matches()) {
        matched = true;
        System.out.println(String.format("Matched on: %s", m.group(0)));
        try {
          Object controller = route.getController().newInstance();
          Method method = controller.getClass().getMethod(route.getMethod(), HttpServletRequest.class, HttpServletResponse.class, Matcher.class);
          method.invoke(controller, request, response, m);
        }
        catch (InstantiationException e) {
          e.printStackTrace();
        }
        catch (IllegalAccessException e) {
          e.printStackTrace();
        }
        catch (NoSuchMethodException e) {
          e.printStackTrace();
        }
        catch (SecurityException e) {
          e.printStackTrace();
        }
        catch (IllegalArgumentException e) {
          e.printStackTrace();
        }
        catch (InvocationTargetException e) {
          e.printStackTrace();
        }
        break;
      }
    }
    
    if (!matched) {
      try {
        response.getWriter().println("<h1>You did not match any routes, available routes are</h1>");
        for (Route route : routes) {
          response.getWriter().println("<ul>");
          response.getWriter().println("<li>" + route.getPattern().toString() + "</li>");
          response.getWriter().println("</ul>");
        }
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
    
  }

}
