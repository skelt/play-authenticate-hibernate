package lib;

public class BootstrapSelectKeyValue {

	private String id;

	private String text;
	
	private boolean isSelected;
	
	public BootstrapSelectKeyValue(String id, String text, boolean isSelected) {
		super();
		this.id = id;
		this.text = text;
		this.isSelected = isSelected;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

}
