package au.com.ifti.utilities;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class TiramisuRequest {
  
  private String requestUri;
  private String method;
  private Map<String, String[]> parameterMap;
  private Map<String, String> headers = new HashMap<String, String>();
  
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

  public Map<String, String> getHeaders() {
    return headers;
  }

  public void setHeaders(Map<String, String> headers) {
    this.headers = headers;
  }
  
  public void setHeader(String key, String value) {
    this.headers.put(key, value);
  }

}
