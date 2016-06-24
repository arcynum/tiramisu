package au.com.ifti.models;

import org.hibernate.Session;

public class TagModel extends Model {
  
  public TagModel(Session session) {
    super(session);
    this.name = "Tag";
  }

}
