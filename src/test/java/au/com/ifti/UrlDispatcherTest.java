package au.com.ifti;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import au.com.ifti.utilities.RouteConfiguration;
import au.com.ifti.utilities.TiramisuRequest;
import au.com.ifti.utilities.TiramisuResponse;

public class UrlDispatcherTest {
	
	TiramisuRequest request = null;
	TiramisuResponse response = null;
	UrlDispatcher dispatcher = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		this.dispatcher = new UrlDispatcher(mock(Session.class));
		this.request = mock(TiramisuRequest.class);
		this.response = new TiramisuResponse(mock(HttpServletResponse.class));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testNotFoundDispatch() {
		// Mock a bad URL for testing.
		when(request.getRequestUri()).thenReturn("Testing");
		
		// Attempt to dispatch a request using this URL.
		this.dispatcher.dispatch(this.request, this.response);
		
		// Assert that the response is 404.
		assertEquals(Integer.valueOf(404), this.response.getStatusCode());
	}
	
	@Test
	public void testBadVerbDispatch() {
		// Add a route, which will have a valid URI, but a bad verb.
		// Not having a valid Controller and Method is fine, because they will never get called.
		RouteConfiguration.addRoute(new Route(Pattern.compile("^.*/test[/]?"), Arrays.asList("GET"), null, null));
		
		// Mock a real URL for testing.
		when(request.getRequestUri()).thenReturn("/test");
		
		// Mock the wrong verb.
		when(request.getMethod()).thenReturn("PATCH");
		
		// Attempt to dispatch a request using this URL.
		this.dispatcher.dispatch(this.request, this.response);
		
		// Assert that the response is 404.
		assertEquals(Integer.valueOf(404), this.response.getStatusCode());
		
		// Mock a completely junk verb
		when(request.getMethod()).thenReturn("FRANK");
		
		// Attempt to dispatch a request using this URL.
		this.dispatcher.dispatch(this.request, this.response);
		
		// Assert that the response is 404.
		assertEquals(Integer.valueOf(404), this.response.getStatusCode());
	}

}
