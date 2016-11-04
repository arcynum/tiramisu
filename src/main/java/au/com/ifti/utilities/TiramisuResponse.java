package au.com.ifti.utilities;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

/**
 * The web application response object. This object wraps around the servlet
 * HttpServletResponse object to add application specific functionality.
 * 
 * @author Chris Hamilton
 * @see HttpServletResponse
 */
public class TiramisuResponse {

	/**
	 * The output response writer.
	 */
	private StringWriter writer = new StringWriter();
	
	/**
	 * The HTTP response status code.
	 */
	private Integer statusCode = 200;
	
	/**
	 * The HTTP response status code.
	 */
	private String template = "200.vm";
	
	/**
	 * The current page title.
	 */
	private String pageTitle = "";
	
	/**
	 * The map of view objects for the template engine.
	 */
	private Map<String, Object> data = new HashMap<>();
	
	/**
	 * The map of HTTP headers for the response.
	 */
	private Map<String, String> headers = new HashMap<String, String>();
	
	/**
	 * This is the container to store flash messages that should be added to the session.
	 * These messages are written to the session variable in the servlet itself.
	 */
	private List<String> flashMessages = new ArrayList<String>();

	/**
	 * Default response constructor.
	 * @param response The HttpServletResponse object.
	 */
	public TiramisuResponse(HttpServletResponse response) {

	}

	public StringWriter getWriter() {
		return writer;
	}

	public void setWriter(StringWriter writer) {
		this.writer = writer;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public void addViewVariable(String key, Object value) {
		this.data.put(key, value);
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public void setHeader(String key, String value) {
		this.headers.put(key, value);
	}

	public List<String> getFlashMessages() {
		return flashMessages;
	}

	public void setFlashMessages(List<String> flashMessages) {
		this.flashMessages = flashMessages;
	}
	
	public void addFlashMessage(String message) {
		this.getFlashMessages().add(message);
	}

}
