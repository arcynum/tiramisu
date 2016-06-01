package au.com.ifti.controllers;

import java.util.List;
import java.util.regex.Matcher;

import au.com.ifti.models.ServerModel;
import au.com.ifti.models.pojo.Server;
import au.com.ifti.utilities.TiramisuRequest;
import au.com.ifti.utilities.TiramisuResponse;

public class ServerController extends Controller {

  public ServerController(TiramisuRequest request, TiramisuResponse response) {
    super(request, response);
    this.model = new ServerModel();
  }

  @Override
  public void run(Matcher matcher) {
    response.getWriter().append("<h1>Hit the run method in the server controller</h1>");
    response.getWriter().append(String.format("<p>I'm inside the ServerController and my dbms id is %s</p>", matcher.group("dbms")));
    response.getWriter().append(String.format("<p>I'm inside the ServerController and my server id is %s</p>", matcher.group("server")));
    
    List<?> results = this.model.find("all");
    for (Object o : results) {
      if (o instanceof Server) {
        Server server = (Server) o;
        this.response.getWriter().append(server.getName());
        this.response.getWriter().append(server.getDbms().getName());
      }
    }
    
  }


}
