package au.com.ifti.models;

import org.hibernate.Session;

public class ReportModel extends Model {
  
  public ReportModel(Session session) {
    super(session);
    this.name = "Report";
  }

}
