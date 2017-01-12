package au.com.ifti.processors;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import au.com.ifti.utilities.TiramisuResponse;

public abstract class Processor {
	
	private Integer statusCode = null;
	private String contentType = null;
	private HttpServletRequest servletRequest = null;
	private HttpServletResponse servletResponse = null;
	private TiramisuResponse tiramisuResponse = null;
	private Object flashSessions = null;
	
	public Processor() {
		
	}
	
	protected void addHeaders() {
		for (String key : this.getTiramisuResponse().getHeaders().keySet()) {
			this.getServletResponse().setHeader(key, this.getTiramisuResponse().getHeaders().get(key));
		}
	}
	
	protected void flashMessages() {
		
		// New flash array.
		List<String> newFlashList = new ArrayList<String>();
		
		// Add all of the old messages.
		Object flashSessionList = this.getServletRequest().getSession().getAttribute("flash");
		if (flashSessionList instanceof ArrayList<?>) {
			ArrayList<?> innerFlashSessionList = (ArrayList<?>) flashSessionList;
			for (Object item : innerFlashSessionList) {
				if (item instanceof String) {
					newFlashList.add((String) item);
				}
			}
		}
		
		// Add all of the new messages.
		newFlashList.addAll(tiramisuResponse.getFlashMessages());
		
		// Overwrite the old session variable with the new session variable.
		this.getServletRequest().getSession().setAttribute("flash", newFlashList);
		
		// Make sure not to clear the flash messages if they are not rendered.
		// When they are rendered, remove them from the session.
	}
	
	protected void status() {
		this.getServletResponse().setStatus(this.getStatusCode());
	}
	
	public abstract void render();

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}
	
	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public HttpServletRequest getServletRequest() {
		return servletRequest;
	}

	public void setServletRequest(HttpServletRequest servletRequest) {
		this.servletRequest = servletRequest;
	}

	public HttpServletResponse getServletResponse() {
		return servletResponse;
	}

	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}

	public TiramisuResponse getTiramisuResponse() {
		return tiramisuResponse;
	}

	public void setTiramisuResponse(TiramisuResponse tiramisuResponse) {
		this.tiramisuResponse = tiramisuResponse;
	}

	public Object getFlashSessions() {
		return flashSessions;
	}

	public void setFlashSessions(Object flashSessions) {
		this.flashSessions = flashSessions;
	}

}
