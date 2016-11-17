package au.com.ifti;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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

}
