package au.com.ifti;

import java.io.IOException;
import java.util.regex.Matcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DbmsController extends Controller {

  @Override
  public void run(HttpServletRequest request, HttpServletResponse response, Matcher matcher) {
    try {
      response.getWriter().println("<h1>Hit the run method in the dbms controller</h1>");
      response.getWriter().println(String.format("<p>Im inside the DbmsController and my dbms is %s</p>", matcher.group("dbms")));
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

}
