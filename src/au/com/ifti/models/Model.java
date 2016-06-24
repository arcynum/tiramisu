package au.com.ifti.models;

import java.util.List;

import org.hibernate.Session;

import au.com.ifti.models.pojo.Base;

public abstract class Model {
  protected String name;
  protected Session session;
  
  public Model(Session session) {
    this.session = session;
  }
  
  public List<?> findAll() {
    this.session.beginTransaction();
    List<?> result = this.session.createQuery(String.format("from %s", this.name)).list();
    this.session.getTransaction().commit();
    
    return result;
  }
  
  public <T extends Base> T findById(Class<T> classIdentifier, Integer id) {
    this.session.beginTransaction();
    Object result = this.session.get(classIdentifier, id);
    this.session.getTransaction().commit();
    
    try {
      return classIdentifier.cast(result);
    }
    catch (ClassCastException e) {
      return null;
    }
  }
  
}