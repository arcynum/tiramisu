package au.com.ifti.processors;

public class JsonProcessor extends Processor {
	
	public JsonProcessor() {
		super();
		
		// JSON Processor specific constructor options.
		this.setContentType("application/vnd.api+json");
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub

	}

}
