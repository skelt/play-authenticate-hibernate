package controllers;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.user.AuthUser;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import constants.JpaConstants;
import dao.UserHome;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.data.format.Formats.NonEmpty;
import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Required;
import play.db.jpa.JPAApi;
import play.i18n.Messages;
import play.i18n.MessagesApi;
import play.mvc.Controller;
import play.mvc.Result;
import providers.MyUsernamePasswordAuthProvider;
import providers.MyUsernamePasswordAuthUser;
import service.UserProvider;
import views.html.account.ask_link;
import views.html.account.ask_merge;
import views.html.account.link;
import views.html.account.password_change;
import views.html.account.unverified;

public class Account extends Controller {

	public static class Accept {

		@Required
		@NonEmpty
		public Boolean accept;

		public Boolean getAccept() {
			return accept;
		}

		public void setAccept(Boolean accept) {
			this.accept = accept;
		}

	}

	public static class PasswordChange {
		@MinLength(5)
		@Required
		public String password;

		@MinLength(5)
		@Required
		public String repeatPassword;

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getRepeatPassword() {
			return repeatPassword;
		}

		public void setRepeatPassword(String repeatPassword) {
			this.repeatPassword = repeatPassword;
		}

		public String validate() {
			if (password == null || !password.equals(repeatPassword)) {
				return Messages
						.get("playauthenticate.change_password.error.passwords_not_same");
			}
			return null;
		}
	}

	private final Form<Accept> ACCEPT_FORM;
	private final Form<Account.PasswordChange> PASSWORD_CHANGE_FORM;

	private final PlayAuthenticate auth;
	private final UserProvider userProvider;
	private final MyUsernamePasswordAuthProvider myUsrPaswProvider;

	private final MessagesApi msg;
	
	private final JPAApi jpaApi;

	@Inject
	public Account(final PlayAuthenticate auth, final UserProvider userProvider,
				   final MyUsernamePasswordAuthProvider myUsrPaswProvider,
				   final FormFactory formFactory, final MessagesApi msg, final JPAApi api) {
		this.auth = auth;
		this.userProvider = userProvider;
		this.myUsrPaswProvider = myUsrPaswProvider;

		this.ACCEPT_FORM = formFactory.form(Accept.class);
		this.PASSWORD_CHANGE_FORM = formFactory.form(Account.PasswordChange.class);

		this.msg = msg;
		
		this.jpaApi = api;
	}
	
	@SubjectPresent
	public Result link() {
		com.feth.play.module.pa.controllers.Authenticate.noCache(response());
		return ok(link.render(this.userProvider, this.auth));
	}

	@Restrict(@Group(Application.USER_ROLE))
	public Result verifyEmail() {
		com.feth.play.module.pa.controllers.Authenticate.noCache(response());
		User user = this.userProvider.getUser(session());
		if (user.getEmailValidated()) {
			// E-Mail has been validated already
			flash(Application.FLASH_MESSAGE_KEY,
					this.msg.preferred(request()).at("playauthenticate.verify_email.error.already_validated"));
		} else if (user.getEmail() != null && !user.getEmail().trim().isEmpty()) {
			flash(Application.FLASH_MESSAGE_KEY, this.msg.preferred(request()).at(
					"playauthenticate.verify_email.message.instructions_sent",
					user.getEmail()));
			this.myUsrPaswProvider.sendVerifyEmailMailingAfterSignup(user, ctx());
		} else {
			flash(Application.FLASH_MESSAGE_KEY, this.msg.preferred(request()).at(
					"playauthenticate.verify_email.error.set_email_first",
					user.getEmail()));
		}
		return redirect(routes.Application.profile());
	}

	@Restrict({@Group(Application.USER_ROLE), @Group(Admin.USER_ROLE)})
	public Result changePassword() {
		com.feth.play.module.pa.controllers.Authenticate.noCache(response());
		User u = this.userProvider.getUser(session());

		if (!u.getEmailValidated()) {
			return ok(unverified.render(this.userProvider));
		} else {
			return ok(password_change.render(this.userProvider,PASSWORD_CHANGE_FORM));
		}
	}
	
	@Restrict({@Group(Application.USER_ROLE), @Group(Admin.USER_ROLE)})
	//@Transactional
	public Result doChangePassword() {
		EntityManager em = this.jpaApi.em(JpaConstants.DB);
		com.feth.play.module.pa.controllers.Authenticate.noCache(response());
		Form<Account.PasswordChange> filledForm = PASSWORD_CHANGE_FORM
				.bindFromRequest();
		if (filledForm.hasErrors()) {
			// User did not select whether to link or not link
			em.close();
			return badRequest(password_change.render(this.userProvider,filledForm));
		} else {
			User user = this.userProvider.getUser(session());
			String newPassword = filledForm.get().password;
			
			UserHome userDao = new UserHome();
			
			userDao.changePassword(user, new MyUsernamePasswordAuthUser(newPassword), false, em);
		    em.close();
			flash(Application.FLASH_MESSAGE_KEY,
					this.msg.preferred(request()).at("playauthenticate.change_password.success"));
			return redirect(routes.Application.profile());
		}
	}

	@SubjectPresent
	public Result askLink() {
		com.feth.play.module.pa.controllers.Authenticate.noCache(response());
		AuthUser u = this.auth.getLinkUser(session());
		if (u == null) {
			// account to link could not be found, silently redirect to login
			return redirect(routes.Application.index());
		}
		return ok(ask_link.render(this.userProvider,ACCEPT_FORM, u));
	}

	@SubjectPresent
	public Result doLink() {
		com.feth.play.module.pa.controllers.Authenticate.noCache(response());
	    AuthUser u = this.auth.getLinkUser(session());
		if (u == null) {
			// account to link could not be found, silently redirect to login
			return redirect(routes.Application.index());
		}

		Form<Accept> filledForm = ACCEPT_FORM.bindFromRequest();
		if (filledForm.hasErrors()) {
			// User did not select whether to link or not link
			return badRequest(ask_link.render(this.userProvider,filledForm, u));
		} else {
			// User made a choice :)
			boolean link = filledForm.get().accept;
			if (link) {
				flash(Application.FLASH_MESSAGE_KEY,
						this.msg.preferred(request()).at("playauthenticate.accounts.link.success"));
			}
			return this.auth.link(ctx(), link);
		}
	}

	@SubjectPresent
	public Result askMerge() {
		com.feth.play.module.pa.controllers.Authenticate.noCache(response());
		// this is the currently logged in user
		AuthUser aUser = this.auth.getUser(session());

		// this is the user that was selected for a login
		AuthUser bUser = this.auth.getMergeUser(session());
		if (bUser == null) {
			// user to merge with could not be found, silently redirect to login
			return redirect(routes.Application.index());
		}

		// You could also get the local user object here via
		// User.findByAuthUserIdentity(newUser)
		return ok(ask_merge.render(this.userProvider,ACCEPT_FORM, aUser, bUser));
	}

	@SubjectPresent
	public Result doMerge() {
		com.feth.play.module.pa.controllers.Authenticate.noCache(response());
		// this is the currently logged in user
		AuthUser aUser = this.auth.getUser(session());

		// this is the user that was selected for a login
		AuthUser bUser = this.auth.getMergeUser(session());
		if (bUser == null) {
			// user to merge with could not be found, silently redirect to login
			return redirect(routes.Application.index());
		}

		Form<Accept> filledForm = ACCEPT_FORM.bindFromRequest();
		if (filledForm.hasErrors()) {
			// User did not select whether to merge or not merge
			return badRequest(ask_merge.render(this.userProvider,filledForm, aUser, bUser));
		} else {
			// User made a choice :)
			boolean merge = filledForm.get().accept;
			if (merge) {
				flash(Application.FLASH_MESSAGE_KEY,
						this.msg.preferred(request()).at("playauthenticate.accounts.merge.success"));
			}
			return this.auth.merge(ctx(), merge);
		}
	}

}
