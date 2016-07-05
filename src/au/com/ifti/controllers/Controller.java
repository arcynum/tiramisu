package au.com.ifti.controllers;

import java.util.List;

import org.hibernate.Session;

import au.com.ifti.models.Model;
import au.com.ifti.utilities.TiramisuRequest;
import au.com.ifti.utilities.TiramisuResponse;

public abstract class Controller {

  protected TiramisuRequest request;
  protected TiramisuResponse response;
  protected Session session;
  
  public Controller(TiramisuRequest request, TiramisuResponse response, Session session) {
    this.setRequest(request);
    this.setResponse(response);
    this.setSession(session);
  }

  public Session getSession() {
    return session;
  }

  public void setSession(Session session) {
    this.session = session;
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
    this.getSession().beginTransaction();
    List<?> result = this.getSession().createCriteria(classIdentifier).list();
    this.getSession().getTransaction().commit();
    return result;
  }

  public <T extends Model> T findById(Class<T> classIdentifier, Integer id) {
    this.getSession().beginTransaction();
    Object result = this.getSession().get(classIdentifier, id);
    this.getSession().getTransaction().commit();

    try {
      return classIdentifier.cast(result);
    } catch (ClassCastException e) {
      return null;
    }
  }
  
  public <T extends Model> T create(Class<T> classIdentifier) {
    this.getSession().beginTransaction();
    Object result = this.getSession().get(classIdentifier, id);
    this.getSession().getTransaction().commit();

    try {
      return classIdentifier.cast(result);
    } catch (ClassCastException e) {
      return null;
    }
  }

}
