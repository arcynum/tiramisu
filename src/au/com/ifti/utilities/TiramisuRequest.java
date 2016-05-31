package au.com.ifti.utilities;

import javax.servlet.http.HttpServletRequest;

public class TiramisuRequest {
  
  private String requestUri;
  
  public TiramisuRequest(HttpServletRequest request) {
    setRequestUri(request.getRequestURI());
  }

  public String getRequestUri() {
    return requestUri;
  }

  public void setRequestUri(String requestUri) {
    this.requestUri = requestUri;
  }

}
