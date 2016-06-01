package au.com.ifti.utilities;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

public class TiramisuResponse {
  
  private StringWriter writer = new StringWriter();
  private Integer statusCode = 200;
  private String template = "";
  private String pageTitle = "";
  private HashMap<String, Object> data = new HashMap<>();
  
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

  public String getTemplate() {
    return template;
  }

  public void setTemplate(String template) {
    this.template = template;
  }

  public String getPageTitle() {
    return pageTitle;
  }

  public void setPageTitle(String pageTitle) {
    this.pageTitle = pageTitle;
  }

  public HashMap<String, Object> getData() {
    return data;
  }

  public void setData(HashMap<String, Object> data) {
    this.data = data;
  }

}
