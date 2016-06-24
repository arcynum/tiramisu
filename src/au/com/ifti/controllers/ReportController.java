package au.com.ifti.controllers;

import java.util.List;

import au.com.ifti.exceptions.NotFoundException;
import au.com.ifti.models.ReportModel;
import au.com.ifti.utilities.TiramisuRequest;
import au.com.ifti.utilities.TiramisuResponse;

public class ReportController extends Controller {
  
  public ReportController(TiramisuRequest request, TiramisuResponse response) {
    super(request, response);
  }
  
  public void index() {    
    List<?> reports = findAll(ReportModel.class);
    this.response.addViewVariable("reports", reports);
    this.response.setTemplate("/reports/index.vm");
    this.response.setPageTitle("Reports Index");
  }
  
  public void view(String id) throws NotFoundException {
    ReportModel report = findById(ReportModel.class, Integer.parseInt(id));
    if (report == null) {
      throw new NotFoundException();
    }
    this.response.addViewVariable("report", report);
    this.response.setTemplate("/reports/view.vm");
    this.response.setPageTitle(report.getName());
  }

}
