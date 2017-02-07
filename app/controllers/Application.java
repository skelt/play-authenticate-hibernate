package controllers;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.feth.play.module.pa.PlayAuthenticate;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import dao.UserHome;
import models.User;
import play.data.Form;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import providers.MyUsernamePasswordAuthProvider;
import providers.MyUsernamePasswordAuthProvider.MyLogin;
import providers.MyUsernamePasswordAuthProvider.MySignup;
import service.UserProvider;
import views.html.index;
import views.html.login;
import views.html.profile;
import views.html.restricted;
import views.html.signup;

public class Application extends Controller {

	public static final String FLASH_MESSAGE_KEY = "message";
	public static final String FLASH_ERROR_KEY = "error";
	public static final String USER_ROLE = "web";
	
	private final PlayAuthenticate auth;

	private final MyUsernamePasswordAuthProvider provider;

	private final UserProvider userProvider;
	
	private final JPAApi jpaApi;

	@Inject
	public Application(final PlayAuthenticate auth, final MyUsernamePasswordAuthProvider provider,
					   final UserProvider userProvider, final JPAApi api) {
		this.auth = auth;
		this.provider = provider;
		this.userProvider = userProvider;
		this.jpaApi = api;
	}
	
	public Result index() {		
		return ok(index.render(this.userProvider));
	}
	
	public Result file(String filename)
    {
    	Config conf = ConfigFactory.load();
		String uploadPath = conf.getString("application.uploadPath");
    	
		//change the boolean to false if you want files to be downloaded instead of the browser opening the file
    	return ok(new java.io.File(uploadPath + filename), false);
    }

	@Restrict({@Group(Application.USER_ROLE), @Group(Admin.USER_ROLE)})
	public Result restricted() {
		User localUser = this.userProvider.getUser(session());
		return ok(restricted.render(this.userProvider,localUser));
	}

	@Restrict({@Group(Application.USER_ROLE), @Group(Admin.USER_ROLE)})
	@Transactional
	public Result profile() {
		EntityManager em = this.jpaApi.em();
		
		User localUser = this.userProvider.getUser(session());
		
		UserHome userDao = new UserHome();
		localUser = userDao.findById(localUser.getId(), em);
		
		//em.close();
		return ok(profile.render(this.auth, this.userProvider,localUser));
	}

	public Result login() {
		return ok(login.render(this.auth, this.userProvider, this.provider.getLoginForm()));
	}

	public Result doLogin() {
		com.feth.play.module.pa.controllers.Authenticate.noCache(response());
		Form<MyLogin> filledForm = this.provider.getLoginForm().bindFromRequest();
		if (filledForm.hasErrors()) {
			// User did not fill everything properly
			return badRequest(login.render(this.auth, this.userProvider,filledForm));
		} else {
			// Everything was filled
			return this.provider.handleLogin(ctx());
		}
	}

	public Result signup() {
		return ok(signup.render(this.auth, this.userProvider, this.provider.getSignupForm()));
	}

	public Result jsRoutes() {
		return ok(
				play.routing.JavaScriptReverseRouter.create("jsRoutes",
						routes.javascript.Signup.forgotPassword()))
				.as("text/javascript");
	}

	public Result doSignup() {
		com.feth.play.module.pa.controllers.Authenticate.noCache(response());
		Form<MySignup> filledForm = this.provider.getSignupForm()
				.bindFromRequest();
		if (filledForm.hasErrors()) {
			// User did not fill everything properly
			return badRequest(signup.render(this.auth, this.userProvider,filledForm));
		} else {
			// Everything was filled
			// do something with your part of the form before handling the user
			// signup
			return this.provider.handleSignup(ctx());
		}
	}

	public static String formatTimestamp(long t) {
		return new SimpleDateFormat("yyyy-dd-MM HH:mm:ss").format(new Date(t));
	}

}