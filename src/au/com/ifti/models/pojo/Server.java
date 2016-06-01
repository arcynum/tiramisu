package au.com.ifti.models.pojo;

public class Server extends Base {
  
  private String img;
  private Dbms dbms;

  public String getImg() {
    return img;
  }

  public void setImg(String img) {
    this.img = img;
  }

  public Dbms getDbms() {
    return dbms;
  }

  public void setDbms(Dbms dbms) {
    this.dbms = dbms;
  }
}
