package au.com.ifti.utilities;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {
  
  private static SessionFactory mORMSessionFactory = null;
  private static StandardServiceRegistry ormServiceRegistry = null;
  
  private static synchronized SessionFactory buildORMSessionFactory() {
      try {
          if (HibernateUtil.ormServiceRegistry == null) {
              HibernateUtil.ormServiceRegistry = new StandardServiceRegistryBuilder()
                  .configure("au/com/ifti/models/hibernate/database.xml").build();
          }
          Metadata metadata = new MetadataSources(HibernateUtil.ormServiceRegistry).getMetadataBuilder().build();
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
  public static synchronized SessionFactory getORMSessionFactory() {
      if (HibernateUtil.mORMSessionFactory == null) {
          HibernateUtil.mORMSessionFactory = buildORMSessionFactory();
      }
      return HibernateUtil.mORMSessionFactory;
  }

  
  /**
   * Close the ORM connection.
   */
  public static void tearDownORM() {
      if (HibernateUtil.ormServiceRegistry != null) {
          StandardServiceRegistryBuilder.destroy(HibernateUtil.ormServiceRegistry);
      }
  }

}
