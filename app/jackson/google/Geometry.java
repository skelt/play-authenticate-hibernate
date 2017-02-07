package jackson.google;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "bounds", "location", "location_type", "viewport" })
public class Geometry {

	@JsonProperty("bounds")
	private Bounds bounds;
	@JsonProperty("location")
	private Location location;
	@JsonProperty("location_type")
	private String locationType;
	@JsonProperty("viewport")
	private Viewport viewport;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return The bounds
	 */
	@JsonProperty("bounds")
	public Bounds getBounds() {
		return bounds;
	}

	/**
	 * 
	 * @param bounds
	 *            The bounds
	 */
	@JsonProperty("bounds")
	public void setBounds(Bounds bounds) {
		this.bounds = bounds;
	}

	/**
	 * 
	 * @return The location
	 */
	@JsonProperty("location")
	public Location getLocation() {
		return location;
	}

	/**
	 * 
	 * @param location
	 *            The location
	 */
	@JsonProperty("location")
	public void setLocation(Location location) {
		this.location = location;
	}

	/**
	 * 
	 * @return The locationType
	 */
	@JsonProperty("location_type")
	public String getLocationType() {
		return locationType;
	}

	/**
	 * 
	 * @param locationType
	 *            The location_type
	 */
	@JsonProperty("location_type")
	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	/**
	 * 
	 * @return The viewport
	 */
	@JsonProperty("viewport")
	public Viewport getViewport() {
		return viewport;
	}

	/**
	 * 
	 * @param viewport
	 *            The viewport
	 */
	@JsonProperty("viewport")
	public void setViewport(Viewport viewport) {
		this.viewport = viewport;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}
