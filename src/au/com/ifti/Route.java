package au.com.ifti;

import java.util.regex.Pattern;

public class Route {
  
  private Pattern pattern = null;
  private Class<?> controller = null;
  private String method = null;
  
  public Route(Pattern pattern, Class<?> controller, String method) {
    this.pattern = pattern;
    this.controller = controller;
    this.method = method;
  }

  public Pattern getPattern() {
    return pattern;
  }

  public void setPattern(Pattern pattern) {
    this.pattern = pattern;
  }

  public Class<?> getController() {
    return controller;
  }

  public void setController(Class<?> controller) {
    this.controller = controller;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

}
