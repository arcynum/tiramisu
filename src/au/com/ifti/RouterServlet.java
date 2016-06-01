package au.com.ifti;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import au.com.ifti.utilities.HibernateUtil;

/**
 * Servlet implementation class Router
 */
@WebServlet("/*")
public class RouterServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  /**
   * @see HttpServlet#HttpServlet()
   */
  public RouterServlet() {
    super();
  }

  /**
   * @see HttpServlet#service()
   */
  protected void service(HttpServletRequest request, HttpServletResponse response) {
    UrlDispatcher dispatcher = new UrlDispatcher();
    dispatcher.dispatch(request, response);
  }
  
  /**
   * Clean up the database connection from Hibernate.
   */
  @Override
  public void destroy() {
      HibernateUtil.tearDownORM();
  }

}
