package au.com.ifti.utilities;

import javax.servlet.http.HttpServletRequest;

import java.util.Map;

public class TiramisuRequest {
  
  private String requestUri;
  private String method;
  private Map<String, String[]> parameterMap;
  
  public TiramisuRequest(HttpServletRequest request) {
    this.setRequestUri(request.getRequestURI());
    this.setMethod(request.getMethod());
    this.setParameterMap(request.getParameterMap());
  }

  public String getRequestUri() {
    return requestUri;
  }

  public void setRequestUri(String requestUri) {
    this.requestUri = requestUri;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public Map<String, String[]> getParameterMap() {
    return parameterMap;
  }

  public void setParameterMap(Map<String, String[]> parameterMap) {
    this.parameterMap = parameterMap;
  }
  
  public String getParameter(String parameter) {
    if (this.parameterMap.get(parameter).length == 1) {
      return this.parameterMap.get(parameter)[0];
    }
    return "";
  }

}
