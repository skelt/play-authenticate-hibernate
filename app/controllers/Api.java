package controllers;

import java.io.IOException;
import java.math.BigDecimal;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.feth.play.module.pa.PlayAuthenticate;

import actions.BasicAuth;
import dao.UserHome;
import forms.ProfileForm;
import forms.RegistrationForm;
import lib.AddressConverter;
import lib.GoogleResponse;
import lib.GoogleResult;
import models.User;
import models.UserProfile;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.i18n.MessagesApi;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import providers.MyUsernamePasswordAuthProvider;
import providers.MyUsernamePasswordAuthUser;
import service.UserProvider;

public class Api extends Controller {
	
	public static final String USER_ROLE = "mobile";
	
	//private final PlayAuthenticate auth;

	//private final UserProvider userProvider;

	private final MyUsernamePasswordAuthProvider userPaswAuthProvider;

	//private final MessagesApi msg;
	
	private final JPAApi jpaApi;
	private final FormFactory formFactory;
	
	@Inject
	public Api(final PlayAuthenticate auth, final UserProvider userProvider,
				  final MyUsernamePasswordAuthProvider userPaswAuthProvider,
				  final FormFactory formFactory, final MessagesApi msg, final JPAApi api, final FormFactory factory) {
		//this.auth = auth;
		//this.userProvider = userProvider;
		this.userPaswAuthProvider = userPaswAuthProvider;
		//this.PASSWORD_RESET_FORM = formFactory.form(PasswordReset.class);
		//this.FORGOT_PASSWORD_FORM = formFactory.form(MyIdentity.class);

		//this.msg = msg;
		
		this.jpaApi = api;
		this.formFactory = factory;
	}

	@BodyParser.Of(BodyParser.Json.class)
	@Transactional
	public Result forgotPassword() {
		EntityManager em = this.jpaApi.em();
		
		JsonNode json = request().body().asJson();
		String email = json.findPath("email").textValue();

		if (email == null) {
			//em.close();
			return badRequest("Missing parameter [email]");
		}
		
		UserHome userDao = new UserHome();

		User user = userDao.findByEmail(email, em);
		
		if(user == null)
		{
			//em.close();
			ObjectNode result = Json.newObject();
			result.put("msg", "Account not found");
			return status(404, result);
		}
		
		// yep, we have a user with this email that is active - we do
		// not know if the user owning that account has requested this
		// reset, though.
		MyUsernamePasswordAuthProvider provider = this.userPaswAuthProvider;
		// User exists
		if (user.getEmailValidated()) {
			provider.sendPasswordResetMailing(user, ctx());
			// In case you actually want to let (the unknown person)
			// know whether a user was found/an email was sent, use,
			// change the flash message
		} else {
			// We need to change the message here, otherwise the user
			// does not understand whats going on - we should not verify
			// with the password reset, as a "bad" user could then sign
			// up with a fake email via OAuth and get it verified by an
			// a unsuspecting user that clicks the link.
			//flash(Application.FLASH_MESSAGE_KEY,Messages.get("playauthenticate.reset_password.message.email_not_verified"));

			// You might want to re-send the verification email here...
			provider.sendVerifyEmailMailingAfterSignup(user, ctx());
		}

		ObjectNode result = Json.newObject();
		result.put("msg", "Please check your email to reset your password.");

		return ok(result);
	}

	@BasicAuth
	@Transactional
	public Result getProfile() throws JsonProcessingException {
		User userAPI = (User) ctx().args.get("api-user");
		userAPI = this.jpaApi.em().find(models.User.class, userAPI.getId());

		UserProfile profileDB = userAPI.getUserProfile();

		ObjectMapper mapper = new ObjectMapper();

		ProfileForm profile = new ProfileForm();
		profile.setSalutation(profileDB.getSalutation());
		profile.setAddress1(profileDB.getAddress1());
		profile.setAddress2(profileDB.getAddress2());
		profile.setCity(profileDB.getCity());
		profile.setCounty(profileDB.getCounty());
		profile.setDob(profileDB.getDob());
		profile.setGender(profileDB.getGender());
		profile.setHomeNumber(profileDB.getHomeNumber());
		profile.setMobileNumber(profileDB.getMobileNumber());
		profile.setPostcode(profileDB.getPostcode());

		String jsonString = mapper.writeValueAsString(profile);

		JsonNode result = Json.parse(jsonString);

		return ok(result);
	}

	@BasicAuth
	@BodyParser.Of(BodyParser.Json.class)
	@Transactional
	public Result editProfile() {
		User userAPI = (User) ctx().args.get("api-user");
		userAPI = this.jpaApi.em().find(models.User.class, userAPI.getId());

		Form<ProfileForm> form = this.formFactory.form(ProfileForm.class);

		form = form.bindFromRequest();

		if (form.hasErrors()) {
			return status(406, form.errorsAsJson());
		} else {
			ProfileForm input = form.get();

			AddressConverter convt = new AddressConverter();

			BigDecimal lat = null;
			BigDecimal lng = null;

			try {
				GoogleResponse googleQuery = convt.convertToLatLong(input
						.getPostcode());
				GoogleResult[] bob = googleQuery.getResults();

				for (GoogleResult res : bob) {

					lat = new BigDecimal(Double.parseDouble(res.getGeometry()
							.getLocation().getLat()));
					lng = new BigDecimal(Double.parseDouble(res.getGeometry()
							.getLocation().getLng()));

					break;
				}

			} catch (IOException e) {
				Logger.error("Google error getting lat and long from postcode ", e);
			}

			String queryStr = "SELECT c FROM Country c WHERE c.name = :country";
			Query query = this.jpaApi.em().createQuery(queryStr);
			query.setParameter("country", input.getCountry());

			UserProfile profile = userAPI.getUserProfile();
			profile.setSalutation(input.getSalutation());
			profile.setAddress1(input.getAddress1());
			profile.setAddress2(input.getAddress2());
			profile.setCity(input.getCity());
			profile.setCounty(input.getCounty());
			profile.setDob(input.getDob());
			profile.setGender(input.getGender());
			profile.setHomeNumber(input.getHomeNumber());
			profile.setMobileNumber(input.getMobileNumber());
			profile.setPostcode(input.getPostcode());
			profile.setLat(lat);
			profile.setLng(lng);

			this.jpaApi.em().persist(profile);

			ObjectNode result = Json.newObject();
			result.put("msg", "Profile updated");

			return ok(result);
		}

	}

	@BodyParser.Of(BodyParser.Json.class)
	public Result register() {
		
		Form<RegistrationForm> form = this.formFactory.form(RegistrationForm.class);

		form = form.bindFromRequest();

		if (form.hasErrors()) {
			return status(406, form.errorsAsJson());
		} else {
		    RegistrationForm input = form.get();
			
			MyUsernamePasswordAuthUser authUser = new MyUsernamePasswordAuthUser(input);
			
			EntityManager em = this.jpaApi.em();
			
			UserHome userDao = new UserHome();
			
			User u = userDao.findByAuthUserIdentity(authUser, em);
			
			if (u != null) {
				if (u.getEmailValidated()) {
					// This user exists, has its email validated and is active
					//em.close();
					ObjectNode result = Json.newObject();
					result.put("msg", "This user exists, has its email validated and is active");

					return status(406, result);
				} else {
					// this user exists, is active but has not yet validated its
					// email
					//em.close();
					ObjectNode result = Json.newObject();
					result.put("msg", "this user exists, is active but has not yet validated its email");

					return status(406, result);
				}
			}
			
			/*TitaniumCloud titaniumCloud = new TitaniumCloud();

			CreateUserResponse tiUser;
			User newUser = null;

			try {
				tiUser = titaniumCloud.createTiUser(input.getEmail(),
						input.getFirstName(), input.getLastName(),
						input.getPassword1());

				String tiUserId = tiUser.getResponse().getUsers().get(0)
						.getId();
				
				newUser = userDao.createByRole(authUser, controllers.Api.USER_ROLE, tiUserId, em);

			} catch (Exception e) {
				Logger.error("Problem creating Titanium user!", e);

				ObjectNode result = Json.newObject();
				result.put("msg", "Problem creating Titanium user!");

				return status(406, result);
			}*/
			
			User newUser = userDao.createByRole(authUser, controllers.Api.USER_ROLE, null, em);
			//em.close();
			
			MyUsernamePasswordAuthProvider provider = this.userPaswAuthProvider;
			
			provider.sendVerifyEmailMailingAfterSignup(newUser, ctx()); 
			
			ObjectNode result = Json.newObject();
			result.put("msg", "Please check your email to complete registration");
			return ok(result);
			
		}
	}
}
