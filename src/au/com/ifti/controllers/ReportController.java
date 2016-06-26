package au.com.ifti.controllers;

import java.util.List;

import org.hibernate.Session;

import au.com.ifti.exceptions.NotFoundException;
import au.com.ifti.models.ReportModel;
import au.com.ifti.utilities.TiramisuRequest;
import au.com.ifti.utilities.TiramisuResponse;

public class ReportController extends Controller {
  
  public ReportController(TiramisuRequest request, TiramisuResponse response, Session session) {
    super(request, response, session);
  }
  
  public void index() {    
    List<?> reports = findAll(ReportModel.class);
    this.getResponse().addViewVariable("reports", reports);
    this.getResponse().setTemplate("/reports/index.vm");
    this.getResponse().setPageTitle("Reports Index");
  }
  
  public void view(String id) throws NotFoundException {
    ReportModel report = findById(ReportModel.class, Integer.parseInt(id));
    if (report == null) {
      throw new NotFoundException();
    }
    this.getResponse().addViewVariable("report", report);
    this.getResponse().setTemplate("/reports/view.vm");
    this.getResponse().setPageTitle(report.getName());
  }

}
