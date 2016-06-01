package au.com.ifti.models;

import java.util.List;

import org.hibernate.Session;

import au.com.ifti.utilities.HibernateUtil;

public abstract class Model {
  protected String name;
  protected Session session;
  
  public Model() {
    this.session = HibernateUtil.getORMSessionFactory().openSession();
  }
  
  public List<?> findAll() {
    this.session.beginTransaction();
    List<?> result = this.session.createQuery(String.format("from %s", this.name)).list();
    this.session.getTransaction().commit();
    
    return result;
  }
  
  public Object findById(Integer id) {
    this.session.beginTransaction();
    Object result = this.session.createQuery(String.format("from %s where id = %d", this.name, id)).uniqueResult();
    this.session.getTransaction().commit();
    return result;
  }
  
  public void close() {
    this.session.close();
  }
  
}