package au.com.ifti.models.pojo;

import java.util.Set;

public class Tag extends Base {
  private String creator;
  private Set<Report> reports;

  public String getCreator() {
    return creator;
  }

  public void setCreator(String creator) {
    this.creator = creator;
  }

  public Set<Report> getReports() {
    return reports;
  }

  public void setReports(Set<Report> reports) {
    this.reports = reports;
  }
}
