package forms;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lib.AddressConverter;
import lib.GoogleResponse;
import lib.GoogleResult;
import play.Logger;
import play.data.validation.Constraints;
import play.data.validation.ValidationError;

public class ProfileForm {

	//@Constraints.Required
	public String salutation;
	@Constraints.Required
    public String address1;
    @Constraints.Required
    public String address2;
    @Constraints.Required
    public String city;
    @Constraints.Required
    public String county;
    @Constraints.Required
    public String postcode;
    @Constraints.Required
    public String country;
    @Constraints.Required
    public String mobileNumber;
    @Constraints.Required
    public String homeNumber;
    @Constraints.Required
    public String gender;
    @Constraints.Required
    public Date dob;
    
    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<ValidationError>();
        
        Pattern p = Pattern.compile("^(([gG][iI][rR] {0,}0[aA]{2})|((([a-pr-uwyzA-PR-UWYZ][a-hk-yA-HK-Y]?[0-9][0-9]?)|(([a-pr-uwyzA-PR-UWYZ][0-9][a-hjkstuwA-HJKSTUW])|([a-pr-uwyzA-PR-UWYZ][a-hk-yA-HK-Y][0-9][abehmnprv-yABEHMNPRV-Y]))) {0,}[0-9][abd-hjlnp-uw-zABD-HJLNP-UW-Z]{2}))$");
        Matcher m = p.matcher(postcode);
        
        if (!m.find()) {
            errors.add(new ValidationError("postcode", "This is not a valid postcode"));
        }
        
        AddressConverter convt = new AddressConverter();
		
		BigDecimal lat = null;
		BigDecimal lng = null;
    	
    	try {
			GoogleResponse googleQuery = convt.convertToLatLong(this.getPostcode());
		    GoogleResult[] bob = googleQuery.getResults();
			
			for(GoogleResult res: bob)
			{
				
				lat = new BigDecimal(Double.parseDouble(res.getGeometry().getLocation().getLat()));
				lng = new BigDecimal(Double.parseDouble(res.getGeometry().getLocation().getLng()));
				
				break;
			}
			
		} catch (IOException e) {
			Logger.error("Error converting postcode to lat/long from google ", e);
		}
    	
    	if(lat == null && lng == null)
    	{
    		errors.add(new ValidationError("postcode", "This postcode could not be converted to lattitude and longitude"));
    	}
        
        return errors.isEmpty() ? null : errors;
    }
    
    
    public ProfileForm()
    {
    	
    }
    
    
	public ProfileForm(String salutation, String address1, String address2, String city,
			String county, String postcode, String country,
			String mobileNumber, String homeNumber, String gender, Date dob) {
		super();
		this.salutation = salutation;
		this.address1 = address1;
		this.address2 = address2;
		this.city = city;
		this.county = county;
		this.postcode = postcode;
		this.country = country;
		this.mobileNumber = mobileNumber;
		this.homeNumber = homeNumber;
		this.gender = gender;
		this.dob = dob;
	}
	
	public String getSalutation() {
		return salutation;
	}

	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}

	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getHomeNumber() {
		return homeNumber;
	}
	public void setHomeNumber(String homeNumber) {
		this.homeNumber = homeNumber;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}
	
}