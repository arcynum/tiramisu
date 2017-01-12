package au.com.ifti.processors;

import java.io.IOException;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

public class HtmlProcessor extends Processor {
	
	private VelocityContext velocityContext = null;
	private VelocityEngine velocityEngine = null;

	public HtmlProcessor(VelocityEngine velocityEngine) {
		super();
		this.setVelocityEngine(velocityEngine);
	}

	public VelocityContext getVelocityContext() {
		return velocityContext;
	}

	public void setVelocityContext(VelocityContext velocityContext) {
		this.velocityContext = velocityContext;
	}

	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}
	
	public void render() {
		// Set the response code.
		this.status();
		
		// Set the response content type.
		this.getServletResponse().setContentType("text/html; charset=utf-8");
		
		// Set the response headers.
		this.addHeaders();
		
		// Set the flash messages.
		this.flashMessages();
		
		// If the response code is in the 300 range, no need to create and manage velocity contexts.
		if (this.getStatusCode() < 300 || this.getStatusCode() >= 400) {

			// Create the velocity context.
			this.setVelocityContext(new VelocityContext());

			// Fetch the template and combine.
			Template velocityTemplate = this.getVelocityEngine().getTemplate("layout.vm");
			try {
				// Loop through the assigned keys in the response and render them to the template.
				// Using a named array here means you can access by name from the template.
				for (String key : this.getTiramisuResponse().getData().keySet()) {
					this.getVelocityContext().put(key, this.getTiramisuResponse().getData().get(key));
				}
				
				// Write the flash messages to the context.
				this.getVelocityContext().put("messages", this.getServletRequest().getSession().getAttribute("flash"));
				
				// Remove those messages from the session once they have been written.
				this.getServletRequest().getSession().removeAttribute("flash");
				
				this.getVelocityContext().put("content", this.getTiramisuResponse().getTemplate());
				this.getVelocityContext().put("pageTitle", this.getTiramisuResponse().getPageTitle());
				this.getVelocityContext().put("STATIC_ROOT", this.getServletRequest().getContextPath() + "/static");
				
				// Finally merge the velocity context with the response writer.
				velocityTemplate.merge(this.getVelocityContext(), this.getServletResponse().getWriter());
				
			} catch (ResourceNotFoundException | ParseErrorException | MethodInvocationException | IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}

}
