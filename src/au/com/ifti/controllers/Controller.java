package au.com.ifti.controllers;

import java.util.regex.Matcher;

import au.com.ifti.utilities.TiramisuRequest;
import au.com.ifti.utilities.TiramisuResponse;

public abstract class Controller {
  protected TiramisuRequest request;
  protected TiramisuResponse response;
  
  public Controller(TiramisuRequest request, TiramisuResponse response) {
    this.request = request;
    this.response = response;
  }
  
  public TiramisuRequest getRequest() {
    return request;
  }

  public void setRequest(TiramisuRequest request) {
    this.request = request;
  }

  public TiramisuResponse getResponse() {
    return response;
  }

  public void setResponse(TiramisuResponse response) {
    this.response = response;
  }

  public abstract void run(Matcher matcher);
}
