package au.com.ifti.controllers;

import java.util.List;

import org.apache.commons.codec.digest.HmacUtils;
import org.hibernate.Session;
import org.mindrot.jbcrypt.BCrypt;

import au.com.ifti.Route;
import au.com.ifti.components.AuthComponent;
import au.com.ifti.components.annotations.LoginRequired;
import au.com.ifti.exceptions.NotFoundException;
import au.com.ifti.models.UserModel;
import au.com.ifti.utilities.TiramisuConfiguration;
import au.com.ifti.utilities.TiramisuRequest;
import au.com.ifti.utilities.TiramisuResponse;

public class UserController extends Controller {
	
	/**
	 * Import the auth component to be used in this controller.
	 * If this was needed for all controllers, it would be in the parent controller instead.
	 * Handing in the request and response objects.
	 */
	private AuthComponent authComponent = new AuthComponent(this.getRequest(), this.getResponse(), this.getHibernateSession());

	/**
	 * AuthComponent constructor.
	 * @param request TiramisuRequest object
	 * @param response TiramisuResponse object
	 * @param hibernateSession The database session.
	 */
	public UserController(TiramisuRequest request, TiramisuResponse response, Session hibernateSession) {
		super(request, response, hibernateSession);
	}
	
	/**
	 * Before method callback for the controller.
	 * @param route
	 */
	public void beforeMethod(Route route) {
		super.beforeMethod();
		
		if (route.getMethod().isAnnotationPresent(LoginRequired.class)) {
			System.out.println("The login annotation is present");
			// Need to check if the user is logged in here.
			if (this.getAuthComponent().loggedIn()) {
				
			}
		}
	}
	
	/**
	 * After method callback for the controller.
	 */
	public void afterMethod() {
		super.afterMethod();
	}

	@LoginRequired
	public TiramisuResponse index() {
		System.out.println("User Controller Index Method");
		List<UserModel> users = findAll(UserModel.class);
		this.set("users", users);
		this.getResponse().setTemplate("/users/index.vm");
		this.getResponse().setPageTitle("Users Index");
		return this.getResponse();
	}

	public TiramisuResponse register() throws NotFoundException {
		System.out.println("User Controller Register Method");

		if (this.request.getMethod() == "POST") {
			
			UserModel user = new UserModel();
			user.setUsername(this.getRequest().getParameter("user_username"));
			user.setEmail(this.getRequest().getParameter("user_email"));
			user.setActive(false);
			
			// Use the salt and pepper, plus hash, to save the password.
			String salt = BCrypt.gensalt();
			user.setSalt(salt);
			
			// Using the apache commons codec library, convert the password and pepper to a hashed password.
			// Have to use 256 here, because 512 exceeds the max length of BCrypt (which is 
			String hmacPassword = HmacUtils.hmacSha256Hex(TiramisuConfiguration.pepper, this.getRequest().getParameter("user_password"));
			
			// Hash the password.
			String hash = BCrypt.hashpw(hmacPassword, salt);
			
			// Store the hash.
			user.setPassword(hash);
			
			// Finally save the user.
			this.save(user);
			
			// Add a flash message.
			this.getResponse().addFlashMessage("New user created");
			
			// Redirect to the users index.
			return this.redirect("/tiramisu/users", 303);
		}

		// Render the create form.
		this.getResponse().setTemplate("/users/create.vm");
		this.getResponse().setPageTitle("Create new user");

		return this.getResponse();
	}

	public TiramisuResponse read(String id) throws NotFoundException {
		System.out.println("User Controller Index Method");
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
	
	public TiramisuResponse login() throws NotFoundException {
		System.out.println("User Controller Login Method");

		if (this.request.getMethod() == "POST") {
			
			if (this.getAuthComponent().login()) {
				System.out.println("User Controller: Login Successful");
				this.getResponse().addFlashMessage("User Controller: Login Successful");
				return this.redirect("/tiramisu/users", 303);
			}
			
			System.out.println("Login Error");
			this.getResponse().addFlashMessage("Login Error");
		}

		// Render the create form.
		this.getResponse().setTemplate("/users/login.vm");
		this.getResponse().setPageTitle("Login");

		return this.getResponse();
	}
	
	/**
	 * Log the user out of the system.
	 * The AuthComponent will return the location where to redirect to after logout.
	 * @return TiramisuResponse The redirection location.
	 */
	public TiramisuResponse logout() {
		return this.redirect(this.getAuthComponent().logout());
	}

	public AuthComponent getAuthComponent() {
		return authComponent;
	}
	
}
