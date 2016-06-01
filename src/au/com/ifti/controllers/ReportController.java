package au.com.ifti.controllers;

import java.util.regex.Matcher;

import au.com.ifti.exceptions.NotFoundException;
import au.com.ifti.models.ReportModel;
import au.com.ifti.models.pojo.Report;
import au.com.ifti.utilities.TiramisuRequest;
import au.com.ifti.utilities.TiramisuResponse;

public class ReportController extends Controller {
  
  public ReportController(TiramisuRequest request, TiramisuResponse response) {
    super(request, response);
    this.model = new ReportModel();
  }

  @Override
  public void run(Matcher matcher) {
    this.response.getWriter().append("<h1>Hit the run method in the report controller</h1>");
  }
  
  public void view(Matcher matcher) throws NotFoundException {
    Integer id = Integer.parseInt(matcher.group("id"));
    this.response.getWriter().append(String.format("<h1>Hit the view method in the report controller for %d.</h1>", id));
    
    Object object = this.model.findById(id);
    if (object == null) {
      throw new NotFoundException("Object not found in database");
    }
    if (object instanceof Report) {
      Report report = (Report) object;
      this.response.getWriter().append(report.getName());
    }
  }

}
