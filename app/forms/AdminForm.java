package forms;

import java.util.List;

import play.data.validation.Constraints;

public class AdminForm extends RegistrationForm {

	@Constraints.Required(message = "Please select a role")
	public List<Integer> roles;
	
	public AdminForm() {
	}
	
	public AdminForm(String firstName, String lastName, String email, List<Integer> roles) {
		super(firstName, lastName, email);
		this.roles = roles;
	}

	public List<Integer> getRoles() {
		return roles;
	}

	public void setRoles(List<Integer> roles) {
		this.roles = roles;
	}
}