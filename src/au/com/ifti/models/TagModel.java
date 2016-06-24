package au.com.ifti.models;

import java.util.Set;

public class TagModel extends Model {
  
  private String creator;
  private Set<ReportModel> reports;
  
  public TagModel() {
    super();
  }

  public String getCreator() {
    return creator;
  }

  public void setCreator(String creator) {
    this.creator = creator;
  }

  public Set<ReportModel> getReports() {
    return reports;
  }

  public void setReports(Set<ReportModel> reports) {
    this.reports = reports;
  }

}
