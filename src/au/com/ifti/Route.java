package au.com.ifti;

import java.lang.reflect.Method;
import java.util.List;
import java.util.regex.Pattern;

public class Route {
  
  private Pattern pattern = null;
  private List<String> httpMethods = null;
  private Class<?> controller = null;
  private Method method = null;
  
  public Route(Pattern pattern, List<String> httpMethods, Class<?> controller, Method method) {
    this.setPattern(pattern);
    this.setHttpMethods(httpMethods);
    this.setController(controller);
    this.setMethod(method);
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

  public Method getMethod() {
    return method;
  }

  public void setMethod(Method method) {
    this.method = method;
  }

  public List<String> getHttpMethods() {
    return httpMethods;
  }

  public void setHttpMethods(List<String> httpMethods) {
    this.httpMethods = httpMethods;
  }

}
