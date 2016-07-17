package au.com.ifti.controllers;

import org.hibernate.Session;

import au.com.ifti.utilities.TiramisuRequest;
import au.com.ifti.utilities.TiramisuResponse;

public class CommentController extends Controller {

  public CommentController(TiramisuRequest request, TiramisuResponse response, Session session) {
    super(request, response, session);
  }

}
