package forms;

import org.hibernate.validator.constraints.Email;

import play.data.validation.Constraints;

public class LoginForm {
	
	
	@Constraints.Required
	@Email
    public String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}