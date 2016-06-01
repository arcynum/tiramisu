package au.com.ifti.utilities;

import javax.servlet.http.HttpServletRequest;

public class TiramisuRequest {
  
  private String requestUri;
  private String method;
  
  public TiramisuRequest(HttpServletRequest request) {
    this.setRequestUri(request.getRequestURI());
    this.setMethod(request.getMethod());
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

}
