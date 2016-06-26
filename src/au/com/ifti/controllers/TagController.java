package au.com.ifti.controllers;

import org.hibernate.Session;

import au.com.ifti.utilities.TiramisuRequest;
import au.com.ifti.utilities.TiramisuResponse;

public class TagController extends Controller {
  
  public TagController(TiramisuRequest request, TiramisuResponse response, Session session) {
    super(request, response, session);
  }
  
}
