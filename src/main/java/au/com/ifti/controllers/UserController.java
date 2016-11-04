package au.com.ifti.controllers;

import java.security.SecureRandom;
import java.util.List;
import java.math.BigInteger;

import org.hibernate.Session;

import au.com.ifti.exceptions.NotFoundException;
import au.com.ifti.models.UserModel;
import au.com.ifti.utilities.TiramisuRequest;
import au.com.ifti.utilities.TiramisuResponse;

public class UserController extends Controller {

	public UserController(TiramisuRequest request, TiramisuResponse response, Session session) {
		super(request, response, session);
	}

	public TiramisuResponse index() {
		System.out.println("user Index");
		List<?> users = findAll(UserModel.class);
		this.set("users", users);
		this.getResponse().setTemplate("/users/index.vm");
		this.getResponse().setPageTitle("Users Index");
		return this.getResponse();
	}

	public TiramisuResponse create() throws NotFoundException {
		System.out.println("user Create");

		if (this.request.getMethod() == "POST") {

			SecureRandom random = new SecureRandom();
			String salt = new BigInteger(70, random).toString();
			
			UserModel user = new UserModel();
			user.setUsername(this.getRequest().getParameter("user_username"));
			user.setEmail(this.getRequest().getParameter("user_email"));
			user.setActive(false);
			
			// User the salt and pepper, plus hash, to save the password.
			String pepper = 
			user.setPassword(this.getRequest().getParameter("user_password"));
			user.setSalt(salt);
			
			this.save(user);
			return this.redirect("/tiramisu/users", 303);
		}

		// Render the create form.
		this.getResponse().setTemplate("/users/create.vm");
		this.getResponse().setPageTitle("Create new user");

		return this.getResponse();
	}

	public TiramisuResponse read(String id) throws NotFoundException {
		System.out.println("user Read");
		UserModel user = findById(UserModel.class, Integer.parseInt(id));
		if (user == null) {
			throw new NotFoundException();
		}
		this.set("user", user);
		this.getResponse().setTemplate("/users/read.vm");
		this.getResponse().setPageTitle(user.getUsername());
		return this.getResponse();
	}

	public TiramisuResponse delete(String id) throws NotFoundException {
		if (this.request.getMethod() == "DELETE") {
			// Delete a user here.
		}
		return this.getResponse();
	}

}
