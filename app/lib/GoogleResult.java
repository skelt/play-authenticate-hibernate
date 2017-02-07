package lib;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class GoogleResult {

	private String formatted_address;

	private boolean partial_match;

	private Geometry geometry;
	
	@JsonIgnore
	private String place_id;

	@JsonIgnore
	private Object address_components;

	@JsonIgnore
	private Object types;

	public String getFormatted_address() {
		return formatted_address;
	}

	public void setFormatted_address(String formatted_address) {
		this.formatted_address = formatted_address;
	}

	public boolean isPartial_match() {
		return partial_match;
	}

	public void setPartial_match(boolean partial_match) {
		this.partial_match = partial_match;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	public Object getAddress_components() {
		return address_components;
	}

	public void setAddress_components(Object address_components) {
		this.address_components = address_components;
	}

	public Object getTypes() {
		return types;
	}

	public void setTypes(Object types) {
		this.types = types;
	}

	public String getPlace_id() {
		return place_id;
	}

	public void setPlace_id(String place_id) {
		this.place_id = place_id;
	}

}