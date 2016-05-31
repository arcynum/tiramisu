package au.com.ifti.controllers;

import java.util.regex.Matcher;

import au.com.ifti.utilities.TiramisuRequest;
import au.com.ifti.utilities.TiramisuResponse;

public class ServerController extends Controller {

  public ServerController(TiramisuRequest request, TiramisuResponse response) {
    super(request, response);
  }

  @Override
  public void run(Matcher matcher) {
    response.getWriter().append("<h1>Hit the run method in the server controller</h1>");
    response.getWriter().append(String.format("<p>I'm inside the ServerController and my dbms is %s</p>", matcher.group("dbms")));
    response.getWriter().append(String.format("<p>I'm inside the ServerController and my server is %s</p>", matcher.group("server")));
  }

}
