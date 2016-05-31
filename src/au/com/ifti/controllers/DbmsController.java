package au.com.ifti.controllers;

import java.util.regex.Matcher;

import au.com.ifti.utilities.TiramisuRequest;
import au.com.ifti.utilities.TiramisuResponse;

public class DbmsController extends Controller {

  public DbmsController(TiramisuRequest request, TiramisuResponse response) {
    super(request, response);
  }

  @Override
  public void run(Matcher matcher) {
    this.response.getWriter().append("<h1>Hit the run method in the dbms controller</h1>");
    this.response.getWriter().append(String.format("<p>Im inside the DbmsController and my dbms is %s</p>", matcher.group("dbms")));
  }

}
