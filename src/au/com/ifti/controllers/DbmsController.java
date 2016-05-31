package au.com.ifti.controllers;

import java.util.List;
import java.util.regex.Matcher;

import au.com.ifti.models.DbmsModel;
import au.com.ifti.models.hibernate.Dbms;
import au.com.ifti.utilities.TiramisuRequest;
import au.com.ifti.utilities.TiramisuResponse;

public class DbmsController extends Controller {

  public DbmsController(TiramisuRequest request, TiramisuResponse response) {
    super(request, response);
    this.model = new DbmsModel();
  }

  @Override
  public void run(Matcher matcher) {
    this.response.getWriter().append("<h1>Hit the run method in the dbms controller</h1>");
    this.response.getWriter().append(String.format("<p>Im inside the DbmsController and my dbms is %s</p>", matcher.group("dbms")));
    
    List<?> results = this.model.find("all");
    for (Object o : results) {
      if (o instanceof Dbms) {
        Dbms dbms = (Dbms) o;
        this.response.getWriter().append(dbms.getName());
      }
    }
  }

}
