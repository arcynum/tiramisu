package au.com.ifti.controllers;

import au.com.ifti.models.DbmsModel;
import au.com.ifti.utilities.TiramisuRequest;
import au.com.ifti.utilities.TiramisuResponse;

public class DbmsController extends Controller {

  public DbmsController(TiramisuRequest request, TiramisuResponse response) {
    super(request, response);
    this.model = new DbmsModel();
  }

}
