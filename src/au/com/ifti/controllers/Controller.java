package au.com.ifti.controllers;

import au.com.ifti.models.Model;
import au.com.ifti.utilities.TiramisuRequest;
import au.com.ifti.utilities.TiramisuResponse;

public abstract class Controller {
  protected Model model;
  protected TiramisuRequest request;
  protected TiramisuResponse response;
  
  public Controller(TiramisuRequest request, TiramisuResponse response) {
    this.request = request;
    this.response = response;
  }

  public Model getModel() {
    return model;
  }

  public TiramisuRequest getRequest() {
    return request;
  }
  
  public TiramisuResponse getResponse() {
    return response;
  }

  public void setModel(Model model) {
    this.model = model;
  }

  public void setRequest(TiramisuRequest request) {
    this.request = request;
  }

  public void setResponse(TiramisuResponse response) {
    this.response = response;
  }
  
}
