package au.com.ifti;

import java.io.IOException;
import java.util.regex.Matcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminController extends Controller {

  @Override
  public void run(HttpServletRequest request, HttpServletResponse response, Matcher matcher) {
    try {
      response.getWriter().println("<h1>Hit the run method in the admin controller</h1>");
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

}
