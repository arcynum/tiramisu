package au.com.ifti.models;

import java.util.List;

import org.hibernate.Session;

import au.com.ifti.utilities.HibernateUtil;

public abstract class Model {
  protected String name;
  
  public List<?> find(String keyword) {
    Session session = HibernateUtil.getORMSessionFactory().openSession();
    session.beginTransaction();
    List<?> result = session.createQuery(String.format("from %s", this.name)).list();
    session.getTransaction().commit();
    session.close();
    
    return result;
  }
  
}