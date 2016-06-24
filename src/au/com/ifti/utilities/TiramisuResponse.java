package au.com.ifti.utilities;

import java.io.StringWriter;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

/**
 * This is the application specific version of the HTTP response object.
 * 
 * 
 * @author Chris Hamilton
 * @see HttpServletResponse
 */
public class TiramisuResponse {
  
  private StringWriter writer = new StringWriter();
  private Integer statusCode = 200;
  private String template = "200.vm";
  private String pageTitle = "";
  private HashMap<String, Object> data = new HashMap<>();
  private Session session = null;
  
  public TiramisuResponse(HttpServletResponse response) {
    
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
  
  public void addViewVariable(String key, Object value) {
    this.data.put(key, value);
  }

  public Session getSession() {
    return session;
  }

  public void setSession(Session session) {
    this.session = session;
  }

}
