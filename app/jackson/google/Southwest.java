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
@JsonPropertyOrder({ "lat", "lng" })
public class Southwest {

	@JsonProperty("lat")
	private Double lat;
	@JsonProperty("lng")
	private Double lng;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return The lat
	 */
	@JsonProperty("lat")
	public Double getLat() {
		return lat;
	}

	/**
	 * 
	 * @param lat
	 *            The lat
	 */
	@JsonProperty("lat")
	public void setLat(Double lat) {
		this.lat = lat;
	}

	/**
	 * 
	 * @return The lng
	 */
	@JsonProperty("lng")
	public Double getLng() {
		return lng;
	}

	/**
	 * 
	 * @param lng
	 *            The lng
	 */
	@JsonProperty("lng")
	public void setLng(Double lng) {
		this.lng = lng;
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
