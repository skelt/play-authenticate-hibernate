package lib;

public class GoogleResponse {

	private GoogleResult[] results;
	private String status;

	public GoogleResult[] getResults() {
		return results;
	}

	public void setResults(GoogleResult[] results) {
		this.results = results;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
