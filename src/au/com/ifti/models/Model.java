package au.com.ifti.models;

import java.util.Date;

public abstract class Model {
  
  protected Integer id;
  protected Date created;
  protected Date modified;
  
  public Model() {
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public Date getModified() {
    return modified;
  }

  public void setModified(Date modified) {
    this.modified = modified;
  }
  
}