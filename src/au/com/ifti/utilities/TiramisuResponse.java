package au.com.ifti.utilities;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.http.HttpServletResponse;

public class TiramisuResponse {
  
  private StringWriter writer = new StringWriter();
  private Integer statusCode = 200;
  
  public TiramisuResponse(HttpServletResponse response) throws IOException {
    
  }

  public StringWriter getWriter() {
    return writer;
  }

  public void setWriter(StringWriter writer) {
    this.writer = writer;
  }

  public Integer getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(Integer statusCode) {
    this.statusCode = statusCode;
  }

}
