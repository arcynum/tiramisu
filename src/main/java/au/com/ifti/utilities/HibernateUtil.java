package au.com.ifti.utilities;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * Utility factory for building and accessing the hibernate system.
 * 
 * @author Chris Hamilton
 *
 */
public class HibernateUtil {

	/**
	 * The hibernate session factory.
	 */
	private static SessionFactory sessionFactory = null;

	/**
	 * The hibernate service registry.
	 */
	private static StandardServiceRegistry serviceRegistry = null;

	/**
	 * Method to build the hibernate session factory.
	 * 
	 * @return A Hibernate SessionFactory object.
	 */
	private static synchronized SessionFactory buildSessionFactory() {
		try {
			if (HibernateUtil.serviceRegistry == null) {
				HibernateUtil.serviceRegistry = new StandardServiceRegistryBuilder()
						.configure("au/com/ifti/models/hibernate/database.xml").build();
			}
			Metadata metadata = new MetadataSources(HibernateUtil.serviceRegistry).getMetadataBuilder().build();
			return metadata.getSessionFactoryBuilder().build();
		} catch (Throwable ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}

	/**
	 * @return The hibernate session factory.
	 */
	public static synchronized SessionFactory getSessionFactory() {
		if (HibernateUtil.sessionFactory == null) {
			HibernateUtil.sessionFactory = buildSessionFactory();
		}
		return HibernateUtil.sessionFactory;
	}

	/**
	 * Closes and destroys the hibernate connection.
	 */
	public static void destroyRegistry() {
		if (HibernateUtil.serviceRegistry != null) {
			StandardServiceRegistryBuilder.destroy(HibernateUtil.serviceRegistry);
		}
	}

}
