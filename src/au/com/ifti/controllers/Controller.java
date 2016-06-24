package au.com.ifti.controllers;

import java.util.List;

import au.com.ifti.models.Model;
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

  public TiramisuResponse getResponse() {
    return response;
  }

  public void setRequest(TiramisuRequest request) {
    this.request = request;
  }

  public void setResponse(TiramisuResponse response) {
    this.response = response;
  }
  
  public List<?> findAll(Class<?> classIdentifier) {
    this.response.getSession().beginTransaction();
    List<?> result = this.response.getSession().createCriteria(classIdentifier).list();
    this.response.getSession().getTransaction().commit();
    return result;
  }

  public <T extends Model> T findById(Class<T> classIdentifier, Integer id) {
    this.response.getSession().beginTransaction();
    Object result = this.response.getSession().get(classIdentifier, id);
    this.response.getSession().getTransaction().commit();

    try {
      return classIdentifier.cast(result);
    } catch (ClassCastException e) {
      return null;
    }
  }

}
