package au.com.ifti.utilities;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * The web application request object. This object wraps around the servlet
 * HttpServletRequest object to add application specific functionality.
 * 
 * @author Chris Hamilton
 * @see HttpServletRequest
 */
public class TiramisuRequest {

	/**
	 * The HTTP request URI.
	 */
	private String requestUri = null;

	/**
	 * The HTTP request method.
	 */
	private String method = null;

	/**
	 * A map of the HTTP request parameters.
	 */
	private Map<String, String[]> parameterMap;

	/**
	 * A map of the HTTP request headers.
	 */
	private Map<String, String> headers = new HashMap<String, String>();
	
	/**
	 * 
	 */
	private HttpSession session;

	/**
	 * The default constructor for the TiramisuRequest object. This sets the
	 * internal objects for easier consumption.
	 * 
	 * @param request
	 *            the HTTPServletRequest object.
	 */
	public TiramisuRequest(HttpServletRequest request) {
		this.setRequestUri(request.getRequestURI());
		this.setMethod(request.getMethod());
		this.setParameterMap(request.getParameterMap());
		this.setSession(request.getSession());
	}

	public String getRequestUri() {
		return requestUri;
	}

	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Map<String, String[]> getParameterMap() {
		return parameterMap;
	}

	public void setParameterMap(Map<String, String[]> parameterMap) {
		this.parameterMap = parameterMap;
	}

	public String getParameter(String parameter) {
		if (this.parameterMap.get(parameter).length == 1) {
			return this.parameterMap.get(parameter)[0];
		}
		return null;
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

	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}

}
