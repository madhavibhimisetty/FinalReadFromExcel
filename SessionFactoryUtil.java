package omniwyse.read;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SessionFactoryUtil {
	private static SessionFactory seesionFactory;
   
	static 
	{
      seesionFactory = new Configuration().configure().buildSessionFactory();
    } 
     
	public static SessionFactory getSessionFactory()
	{
     return   seesionFactory;
  }
	public static Session getSession()
	{
     return   seesionFactory.openSession();
  }
  private SessionFactoryUtil()
  {

  }

}