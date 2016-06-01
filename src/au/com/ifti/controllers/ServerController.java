package au.com.ifti.controllers;

import au.com.ifti.models.ServerModel;
import au.com.ifti.utilities.TiramisuRequest;
import au.com.ifti.utilities.TiramisuResponse;

public class ServerController extends Controller {

  public ServerController(TiramisuRequest request, TiramisuResponse response) {
    super(request, response);
    this.model = new ServerModel();
  }

}
