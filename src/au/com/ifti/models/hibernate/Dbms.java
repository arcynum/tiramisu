package au.com.ifti.models.hibernate;

import java.util.Date;

public class Dbms {
  private Integer id;
  private String name;
  private String description;
  private Date created;
  private Date modified;
  private String img;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
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

  public String getImg() {
    return img;
  }

  public void setImg(String img) {
    this.img = img;
  }
}
