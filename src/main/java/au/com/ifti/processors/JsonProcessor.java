package au.com.ifti.processors;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

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
		JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;
		
		// The parent node for the entire JSON response.
		ObjectNode parentNode = jsonNodeFactory.objectNode();
		
		// Create an object mapper to extract the data array.
		ObjectMapper dataMapper = new ObjectMapper();
		Object users = this.getTiramisuResponse().getData().get("users");
		parentNode.set("data", dataMapper.valueToTree(users));
		
		// The meta node.
		ObjectNode meta = jsonNodeFactory.objectNode();
		meta.put("meta", "meta");
		parentNode.set("meta", meta);
		
		try {
			this.getServletResponse().getWriter().write(parentNode.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
