package au.com.ifti.utilities;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.http.HttpServletResponse;

public class TiramisuResponse {
  
  private StringWriter writer = new StringWriter();
  
  public TiramisuResponse(HttpServletResponse response) throws IOException {
    
  }

  public StringWriter getWriter() {
    return writer;
  }

  public void setWriter(StringWriter writer) {
    this.writer = writer;
  }

}
