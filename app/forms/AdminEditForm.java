package forms;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import play.data.validation.Constraints;
import play.data.validation.ValidationError;

public class AdminEditForm {

	@Constraints.Required(message = "Please enter the users first name")
	public String firstName;
	@Constraints.Required(message = "Please enter the users last name")
	public String lastName;
	@Constraints.Required(message = "Please enter the users email address")
	public String email;
	//@Constraints.Required(message = "Please enter the users password")
	public String password1;
	//@Constraints.Required(message = "Please confirm the users password")
	public String password2;
	
	@Constraints.Required(message = "Please select a role")
	public List<Integer> roles;
	
	public AdminEditForm() {
	}

	public AdminEditForm(String firstName, String lastName, String email, List<Integer> roles) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.roles = roles;
	}

	public List<ValidationError> validate() {
		List<ValidationError> errors = new ArrayList<ValidationError>();
		
		if(password1.length() > 0)
		{
			if (!password1.equals(password2)) {
				errors.add(new ValidationError("password2",
						"The password does not match."));
			}
	
			if (password1.length() < 8) {
				errors.add(new ValidationError("password1",
						"The password must be at least 8 characters long"));
			}
			
			Pattern p = Pattern.compile("(\\d{1,})");
			Matcher m = p.matcher(password1);

			if (!m.find()) {
				errors.add(new ValidationError("password1",
						"The password must contain at least one number"));
			}
		}

		return errors.isEmpty() ? null : errors;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword1() {
		return password1;
	}

	public void setPassword1(String password1) {
		this.password1 = password1;
	}

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}
	
	public List<Integer> getRoles() {
		return roles;
	}

	public void setRoles(List<Integer> roles) {
		this.roles = roles;
	}
}