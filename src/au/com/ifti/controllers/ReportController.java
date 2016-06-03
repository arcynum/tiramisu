package au.com.ifti.controllers;

import java.util.ArrayList;
import java.util.List;
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
  
  public void index(Matcher matcher) {
    
    List<Report> reports = new ArrayList<>();
    List<?> objects = this.model.findAll();
    
    for (Object object : objects) {
      if (object instanceof Report) {
        Report report = (Report) object;
        reports.add(report);
      }
    }
    
    this.response.addViewVariable("reports", reports);
    this.response.setTemplate("/reports/index.vm");
    this.response.setPageTitle("Reports Index");
  }
  
  public void view(Matcher matcher) throws NotFoundException {
    Integer id = Integer.parseInt(matcher.group("id"));
    Report report = this.model.findById(Report.class, id);
    
    if (report == null) {
      throw new NotFoundException();
    }
    
    this.response.addViewVariable("report", report);
    this.response.setTemplate("/reports/view.vm");
    this.response.setPageTitle(report.getName());
  }

}
