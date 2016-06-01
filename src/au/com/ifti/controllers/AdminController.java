package au.com.ifti.controllers;

import java.util.regex.Matcher;

import au.com.ifti.utilities.TiramisuRequest;
import au.com.ifti.utilities.TiramisuResponse;

public class AdminController extends Controller {

  public AdminController(TiramisuRequest request, TiramisuResponse response) {
    super(request, response);
  }

  @Override
  public void run(Matcher matcher) {
    this.response.getWriter().append("<h1>Hit the run method in the admin controller</h1>");
  }
  
}
