package titanium;

public class Response {
	
	Integer responseCode;
	String responseBody;
	
	public Response(Integer responseCode, String responseBody) {
		super();
		this.responseCode = responseCode;
		this.responseBody = responseBody;
	}

	public Integer getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(Integer responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

}
