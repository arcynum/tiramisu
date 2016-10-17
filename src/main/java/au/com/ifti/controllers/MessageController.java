package au.com.ifti.controllers;

import java.util.List;

import org.hibernate.Session;

import au.com.ifti.exceptions.NotFoundException;
import au.com.ifti.models.MessageModel;
import au.com.ifti.utilities.TiramisuRequest;
import au.com.ifti.utilities.TiramisuResponse;

public class MessageController extends Controller {

  public MessageController(TiramisuRequest request, TiramisuResponse response, Session session) {
    super(request, response, session);
  }
  
  public TiramisuResponse index() {
    System.out.println("Message Index");
    List<?> messages = findAll(MessageModel.class);
    this.set("messages", messages);
    this.getResponse().setTemplate("/messages/index.vm");
    this.getResponse().setPageTitle("Messages Index");
    return this.getResponse();
  }
  
  public TiramisuResponse read(String id) throws NotFoundException {
    System.out.println("Message Read");
    MessageModel message = findById(MessageModel.class, Integer.parseInt(id));
    if (message == null) {
      throw new NotFoundException();
    }
    this.set("message", message);
    this.getResponse().setTemplate("/messages/read.vm");
    this.getResponse().setPageTitle(message.getTitle());
    return this.getResponse();
  }

}
