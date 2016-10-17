package au.com.ifti.models;

import java.util.Date;

/**
 * Abstract Data Model for the framework. All models in the application need to
 * extend this base model. This model also tacks on created/modified
 * functionality to each object.
 * 
 * @author Chris Hamilton
 *
 */
public abstract class Model {

	/**
	 * Primary key for the model.
	 */
	protected Integer id;
	
	/**
	 * Created timestamp for the model.
	 */
	protected Date created;
	
	/**
	 * Modified timestamp for the model.
	 */
	protected Date modified;

	/**
	 * Default constructor for the Model.
	 */
	public Model() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

}