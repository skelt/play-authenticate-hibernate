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
@JsonPropertyOrder({ "northeast", "southwest" })
public class Viewport {

	@JsonProperty("northeast")
	private Northeast_ northeast;
	@JsonProperty("southwest")
	private Southwest_ southwest;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return The northeast
	 */
	@JsonProperty("northeast")
	public Northeast_ getNortheast() {
		return northeast;
	}

	/**
	 * 
	 * @param northeast
	 *            The northeast
	 */
	@JsonProperty("northeast")
	public void setNortheast(Northeast_ northeast) {
		this.northeast = northeast;
	}

	/**
	 * 
	 * @return The southwest
	 */
	@JsonProperty("southwest")
	public Southwest_ getSouthwest() {
		return southwest;
	}

	/**
	 * 
	 * @param southwest
	 *            The southwest
	 */
	@JsonProperty("southwest")
	public void setSouthwest(Southwest_ southwest) {
		this.southwest = southwest;
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
