package au.com.ifti.controllers;

import java.util.List;

import org.hibernate.Session;

import au.com.ifti.exceptions.NotFoundException;
import au.com.ifti.models.PostModel;
import au.com.ifti.utilities.TiramisuRequest;
import au.com.ifti.utilities.TiramisuResponse;

public class PostController extends Controller {

  public PostController(TiramisuRequest request, TiramisuResponse response, Session session) {
    super(request, response, session);
  }
  
  public TiramisuResponse index() {
    System.out.println("Post Index");
    List<?> posts = findAll(PostModel.class);
    this.set("posts", posts);
    this.getResponse().setTemplate("/posts/index.vm");
    this.getResponse().setPageTitle("Posts Index");
    return this.getResponse();
  }
  
  public TiramisuResponse create() throws NotFoundException {
    System.out.println("Post Create");
    
    if (this.request.getMethod() == "POST") {
      PostModel post = new PostModel();
      post.setTitle(this.getRequest().getParameter("post_title"));
      post.setBody(this.getRequest().getParameter("post_body"));
      this.save(post);
      return this.redirect("/tiramisu/posts", 303);
    }
    
    // Render the create form.
    this.getResponse().setTemplate("/posts/create.vm");
    this.getResponse().setPageTitle("Create new post");
    
    return this.getResponse();
  }
  
  public TiramisuResponse read(String id) throws NotFoundException {
    System.out.println("Post Read");
    PostModel post = findById(PostModel.class, Integer.parseInt(id));
    if (post == null) {
      throw new NotFoundException();
    }
    this.getResponse().addViewVariable("post", post);
    this.getResponse().setTemplate("/posts/read.vm");
    this.getResponse().setPageTitle(post.getTitle());
    return this.getResponse();
  }
  
  public TiramisuResponse update(String id) throws NotFoundException {
    // Update a post here or render form, same as create.
    return this.getResponse();
  }
  
  public TiramisuResponse delete(String id) throws NotFoundException {
    if (this.request.getMethod() == "DELETE") {
      // Delete a post here.
    }
    return this.getResponse();
  }

}
