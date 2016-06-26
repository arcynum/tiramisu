package au.com.ifti.utilities;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {
  
  private static SessionFactory sessionFactory = null;
  private static StandardServiceRegistry serviceRegistry = null;
  
  private static synchronized SessionFactory buildSessionFactory() {
      try {
          if (HibernateUtil.serviceRegistry == null) {
              HibernateUtil.serviceRegistry = new StandardServiceRegistryBuilder()
                  .configure("au/com/ifti/models/hibernate/database.xml").build();
          }
          Metadata metadata = new MetadataSources(HibernateUtil.serviceRegistry).getMetadataBuilder().build();
          return metadata.getSessionFactoryBuilder().build();
      }
      catch (Throwable ex) {
          throw new ExceptionInInitializerError(ex);
      }
  }
  
  /**
   * Returns the ORM session factory for Hibernate.
   * 
   * @return the ORM session factory
   */
  public static synchronized SessionFactory getSessionFactory() {
      if (HibernateUtil.sessionFactory == null) {
          HibernateUtil.sessionFactory = buildSessionFactory();
      }
      return HibernateUtil.sessionFactory;
  }

  
  /**
   * Close the ORM connection.
   */
  public static void destroyRegistry() {
      if (HibernateUtil.serviceRegistry != null) {
          StandardServiceRegistryBuilder.destroy(HibernateUtil.serviceRegistry);
      }
  }

}
