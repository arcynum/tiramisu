package au.com.ifti.controllers;

import java.util.regex.Matcher;

import au.com.ifti.models.TagModel;
import au.com.ifti.utilities.TiramisuRequest;
import au.com.ifti.utilities.TiramisuResponse;

public class TagController extends Controller {
  
  public TagController(TiramisuRequest request, TiramisuResponse response) {
    super(request, response);
    this.model = new TagModel();
  }

  @Override
  public void run(Matcher matcher) {
    response.getWriter().append("<h1>Hit the run method in the tag controller</h1>");
  }

}
