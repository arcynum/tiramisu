package au.com.ifti.utilities;

public class TiramisuParameter {
  
  private String parameter = "";
  
  public TiramisuParameter(String param) {
    this.setParameter(param);
  }

  public String getParameter() {
    return parameter;
  }

  public void setParameter(String parameter) {
    this.parameter = parameter;
  }
  
  public Integer getIntegerParameter() {
    return Integer.parseInt(this.parameter);
  }

}
