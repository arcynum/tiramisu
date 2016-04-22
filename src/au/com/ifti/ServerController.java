package au.com.ifti;

import java.io.IOException;
import java.util.regex.Matcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServerController extends Controller {

  @Override
  public void run(HttpServletRequest request, HttpServletResponse response, Matcher matcher) {
    try {
      response.getWriter().println("<h1>Hit the run method in the server controller</h1>");
      response.getWriter().println(String.format("<p>I'm inside the ServerController and my dbms is %s</p>", matcher.group("dbms")));
      response.getWriter().println(String.format("<p>I'm inside the ServerController and my server is %s</p>", matcher.group("server")));
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    
  }

}
