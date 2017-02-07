package controllers;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.feth.play.module.pa.PlayAuthenticate;

import constants.JpaConstants;
import dao.TokenActionHome;
import dao.UserHome;
import models.TokenAction;
import models.User;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.i18n.MessagesApi;
import play.mvc.Controller;
import play.mvc.Result;
import providers.MyLoginUsernamePasswordAuthUser;
import providers.MyUsernamePasswordAuthProvider;
import providers.MyUsernamePasswordAuthProvider.MyIdentity;
import providers.MyUsernamePasswordAuthUser;
import service.UserProvider;
import views.html.account.signup.exists;
import views.html.account.signup.no_token_or_invalid;
import views.html.account.signup.oAuthDenied;
import views.html.account.signup.password_forgot;
import views.html.account.signup.password_reset;
import views.html.account.signup.unverified;

public class Signup extends Controller {

	public static class PasswordReset extends Account.PasswordChange {
		
		public String token;

		public PasswordReset() {
		}

		public PasswordReset(String token) {
			this.token = token;
		}		

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}
	}

	private final Form<PasswordReset> PASSWORD_RESET_FORM;

	private final Form<MyIdentity> FORGOT_PASSWORD_FORM;

	private final PlayAuthenticate auth;

	private final UserProvider userProvider;

	private final MyUsernamePasswordAuthProvider userPaswAuthProvider;

	private final MessagesApi msg;
	
	private final JPAApi jpaApi;

	@Inject
	public Signup(final PlayAuthenticate auth, final UserProvider userProvider,
				  final MyUsernamePasswordAuthProvider userPaswAuthProvider,
				  final FormFactory formFactory, final MessagesApi msg, JPAApi api) {
		this.auth = auth;
		this.userProvider = userProvider;
		this.userPaswAuthProvider = userPaswAuthProvider;
		this.PASSWORD_RESET_FORM = formFactory.form(PasswordReset.class);
		this.FORGOT_PASSWORD_FORM = formFactory.form(MyIdentity.class);

		this.msg = msg;
		
		this.jpaApi = api;
	}

	public Result unverified() {
		com.feth.play.module.pa.controllers.Authenticate.noCache(response());
		return ok(unverified.render(this.userProvider));
	}

	public Result forgotPassword(String email) {
		com.feth.play.module.pa.controllers.Authenticate.noCache(response());
		Form<MyIdentity> form = FORGOT_PASSWORD_FORM;
		if (email != null && !email.trim().isEmpty()) {
			form = FORGOT_PASSWORD_FORM.fill(new MyIdentity(email));
		}
		return ok(password_forgot.render(this.userProvider,form));
	}

	@Transactional
	public Result doForgotPassword() {
		EntityManager em = this.jpaApi.em();
		com.feth.play.module.pa.controllers.Authenticate.noCache(response());
		Form<MyIdentity> filledForm = FORGOT_PASSWORD_FORM.bindFromRequest();
		if (filledForm.hasErrors()) {
			// User did not fill in his/her email
			//em.close();
			return badRequest(password_forgot.render(this.userProvider,filledForm));
		} else {
			// The email address given *BY AN UNKNWON PERSON* to the form - we
			// should find out if we actually have a user with this email
			// address and whether password login is enabled for him/her. Also
			// only send if the email address of the user has been verified.
			String email = filledForm.get().email;

			// We don't want to expose whether a given email address is signed
			// up, so just say an email has been sent, even though it might not
			// be true - that's protecting our user privacy.
			flash(Application.FLASH_MESSAGE_KEY,
					this.msg.preferred(request()).at(
							"playauthenticate.reset_password.message.instructions_sent",
							email));
			
			UserHome userDao = new UserHome();

			User user = userDao.findByEmail(email, em);
			if (user != null) {
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
					flash(Application.FLASH_MESSAGE_KEY,
							this.msg.preferred(request()).at("playauthenticate.reset_password.message.email_not_verified"));

					// You might want to re-send the verification email here...
					provider.sendVerifyEmailMailingAfterSignup(user, ctx());
				}
			}

			//em.close();
			return redirect(routes.Application.index());
		}
	}

	/**
	 * Returns a token object if valid, null if not
	 * 
	 * @param token
	 * @param type
	 * @return
	 */
	
	//@Transactional
	private TokenAction tokenIsValid(String token, String type) {
		EntityManager em = this.jpaApi.em(JpaConstants.DB);
		
		TokenAction ret = null;
		TokenActionHome tokenDao = new TokenActionHome();
		if (token != null && !token.trim().isEmpty()) {
			TokenAction ta = tokenDao.findByToken(token, type, em);
			if (ta != null && ta.isValid()) {
				ret = ta;
			}
		}

		em.close();
		return ret;
	}

	public Result resetPassword(String token) {
		com.feth.play.module.pa.controllers.Authenticate.noCache(response());
		TokenAction ta = tokenIsValid(token, "PASSWORD_RESET");
		if (ta == null) {
			return badRequest(no_token_or_invalid.render(this.userProvider));
		}

		return ok(password_reset.render(this.userProvider,PASSWORD_RESET_FORM
				.fill(new PasswordReset(token))));
	}

	//@Transactional
	public Result doResetPassword() {
		
		EntityManager em = this.jpaApi.em(JpaConstants.DB);
		
		com.feth.play.module.pa.controllers.Authenticate.noCache(response());
		Form<PasswordReset> filledForm = PASSWORD_RESET_FORM
				.bindFromRequest();
		if (filledForm.hasErrors()) {
			em.close();
			return badRequest(password_reset.render(this.userProvider,filledForm));
		} else {
			String token = filledForm.get().token;
			String newPassword = filledForm.get().password;

			TokenAction ta = tokenIsValid(token, "PASSWORD_RESET");
			if (ta == null) {
				em.close();
				return badRequest(no_token_or_invalid.render(this.userProvider));
			}
			
			TokenActionHome tokenDao = new TokenActionHome();
			
			ta = tokenDao.findById(ta.getId(), em);
			
			String email = ta.getUser().getEmail();
			try {
				// Pass true for the second parameter if you want to
				// automatically create a password and the exception never to
				// happen
				UserHome userDao = new UserHome();
				
				userDao.resetPassword(ta.getUser(), new MyUsernamePasswordAuthUser(newPassword),
						false, em);
			} catch (RuntimeException re) {
				Logger.info("User has not yet been set up for password usage", re);
				flash(Application.FLASH_MESSAGE_KEY, this.msg.preferred(request()).at("playauthenticate.reset_password.message.no_password_account"));
			}
			boolean login = this.userPaswAuthProvider
					.isLoginAfterPasswordReset();
			
			em.close();
			if (login) {
				// automatically log in
				flash(Application.FLASH_MESSAGE_KEY,
						this.msg.preferred(request()).at("playauthenticate.reset_password.message.success.auto_login"));

				return this.auth.loginAndRedirect(ctx(),
						new MyLoginUsernamePasswordAuthUser(email));
			} else {
				// send the user to the login page
				flash(Application.FLASH_MESSAGE_KEY,
						this.msg.preferred(request()).at("playauthenticate.reset_password.message.success.manual_login"));
			}
			return redirect(routes.Application.login());
		}
	}

	public Result oAuthDenied(String getProviderKey) {
		com.feth.play.module.pa.controllers.Authenticate.noCache(response());
		return ok(oAuthDenied.render(this.userProvider, getProviderKey));
	}

	public Result exists() {
		com.feth.play.module.pa.controllers.Authenticate.noCache(response());
		return ok(exists.render(this.userProvider));
	}

	//@Transactional
	public Result verify(String token) {
		
		EntityManager em = this.jpaApi.em(JpaConstants.DB);
		com.feth.play.module.pa.controllers.Authenticate.noCache(response());
		TokenAction ta = tokenIsValid(token, "EMAIL_VERIFICATION");
		if (ta == null) {
			em.close();
			return badRequest(no_token_or_invalid.render(this.userProvider));
		}
		TokenActionHome tokenDao = new TokenActionHome();
		
		ta = tokenDao.findById(ta.getId(), em);
		
		String email = ta.getUser().getEmail();
		
		UserHome userDao = new UserHome();
		userDao.verify(ta.getUser(), em);
		flash(Application.FLASH_MESSAGE_KEY,
				this.msg.preferred(request()).at("playauthenticate.verify_email.success", email));
		
		em.close();
		if (this.userProvider.getUser(session()) != null) {
			return redirect(routes.Application.index());
		} else {
			return redirect(routes.Application.login());
		}
	}
}
