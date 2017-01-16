package au.com.ifti.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonProcessor extends Processor {
	
	private final Logger log = LoggerFactory.getLogger(JsonProcessor.class);
	
	public JsonProcessor() {
		super();
		
		// JSON Processor specific constructor options.
		this.setContentType("application/vnd.api+json; charset=utf-8");
	}

	@Override
	public void render() {
		
		log.info("Rendering using the JsonProcessor");
		
		// Set the response code.
		this.status();
		
		// Set the response content type.
		this.getServletResponse().setContentType(this.getContentType());
		
		// Set the response headers.
		this.addHeaders();
		
		// Output same fake data to make sure the swap is working.
		
		// Jackson sample.
		/*
		JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;
		
		ObjectNode node = jsonNodeFactory.objectNode();
		ObjectNode child = jsonNodeFactory.objectNode();
		
		child.put("message", "test");
		node.set("notification", child);
		
		try {
			this.getServletResponse().getWriter().write(node.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
	}

}
