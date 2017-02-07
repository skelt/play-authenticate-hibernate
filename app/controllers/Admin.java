package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.feth.play.module.pa.PlayAuthenticate;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import constants.ScetConstants;
import dao.SecurityRoleHome;
import dao.UserHome;
import forms.AdminEditForm;
import forms.AdminForm;
import forms.RoleSelect;
import models.SecurityRole;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.i18n.MessagesApi;
import play.mvc.Controller;
import play.mvc.Result;
import providers.MyUsernamePasswordAuthProvider;
import providers.MyUsernamePasswordAuthUser;
import service.UserProvider;
import views.html.admin.index;
import views.html.admin.users.userEditForm;
import views.html.admin.users.userForm;
import views.html.admin.users.userList;
import views.html.admin.users.userView;

public class Admin extends Controller {

	public static final String USER_ROLE = "admin";
	
	//private final PlayAuthenticate auth;

	private final UserProvider userProvider;

	private final MyUsernamePasswordAuthProvider userPaswAuthProvider;

	//private final MessagesApi msg;
	
	private final JPAApi jpaApi;
	private final FormFactory formFactory;
	
	@Inject
	public Admin(final PlayAuthenticate auth, final UserProvider userProvider,
				  final MyUsernamePasswordAuthProvider userPaswAuthProvider,
				  final FormFactory formFactory, final MessagesApi msg, final JPAApi api, FormFactory factory) {
		//this.auth = auth;
		this.userProvider = userProvider;
		this.userPaswAuthProvider = userPaswAuthProvider;
		//this.PASSWORD_RESET_FORM = formFactory.form(PasswordReset.class);
		//this.FORGOT_PASSWORD_FORM = formFactory.form(MyIdentity.class);

		//this.msg = msg;
		
		this.jpaApi = api;	
		this.formFactory = factory;
		
	}

	@Restrict(@Group(Admin.USER_ROLE))
	@Transactional
	public Result index() {
		// com.feth.play.module.pa.controllers.Authenticate.noCache(response());
		User user = this.userProvider.getUser(session());

		return ok(index.render(user));
	}

	@Restrict(@Group(Admin.USER_ROLE))
	@Transactional
	public Result manageUsers() {
		// com.feth.play.module.pa.controllers.Authenticate.noCache(response());
		User user = this.userProvider.getUser(session());

		Query query = this.jpaApi.em().createQuery("SELECT u FROM User u");
		@SuppressWarnings("unchecked")
		List<User> users = query.getResultList();

		return ok(userList.render(users, user));
	}

	@Restrict(@Group(Admin.USER_ROLE))
	@Transactional
	public Result createUser() {

		EntityManager em = this.jpaApi.em();

		User user = this.userProvider.getUser(session());

		Form<AdminForm> form = this.formFactory.form(AdminForm.class);

		RoleSelect roleSelect = new RoleSelect(em);
		List<lib.BootstrapSelectKeyValue> roles = roleSelect
				.populateVenueSelect();

		if (ScetConstants.POST.equals(request().method())) {
			form = form.bindFromRequest();

			List<Integer> rolesSelected = new ArrayList<Integer>();

			for (String key : form.data().keySet()) {
				if (key.contains("roles[")) {
					String val = form.data().get(key);
					try {
						rolesSelected.add(Integer.parseInt(val));
					} catch (NumberFormatException e) {
						flash("error", val + " is not a valid selection.");
						//em.close();
						return badRequest(userForm.render(form, roles, user));
					}
				}
			}

			roles = roleSelect.updateVenueSelect(rolesSelected, roles);

			if (form.hasErrors()) {
				//em.close();
				return ok(userForm.render(form, roles, user));
			} else {
				AdminForm input = form.get();

				MyUsernamePasswordAuthUser authUser = new MyUsernamePasswordAuthUser(input);

				UserHome userDao = new UserHome();

				User u = userDao.findByAuthUserIdentity(authUser, em);

				if (u != null) {
					if (u.getEmailValidated()) {
						// This user exists, has its email validated and is
						// active
						flash("error",
								"This user exists, has its email validated and is active");
						//em.close();
						return badRequest(userForm.render(form, roles, user));
					} else {
						// this user exists, is active but has not yet validated
						// its
						// email
						flash("error",
								"This user exists, is active but has not yet validated its email");
						//em.close();
						return badRequest(userForm.render(form, roles, user));
					}
				}

				User newUser = userDao.create(authUser, em);
				
				newUser.setFirstName(input.getFirstName());
				newUser.setLastName(input.getLastName());

				SecurityRoleHome roleDao = new SecurityRoleHome();

				Set<SecurityRole> rolesDb = roleDao.findByIds(input.getRoles(),
						em);

				newUser.setSecurityRoles(rolesDb);

				newUser = userDao.merge(newUser, em);


				MyUsernamePasswordAuthProvider provider = this.userPaswAuthProvider;

				provider.sendPasswordResetMailing(newUser, ctx());

				//em.close();
				return redirect(controllers.routes.Admin.manageUsers());
			}
		}

		//em.close();
		return ok(userForm.render(form, roles, user));

	}

	@Restrict(@Group(Admin.USER_ROLE))
	@Transactional
	public Result editUser(Integer id) {
		User user = this.userProvider.getUser(session());

		EntityManager em = this.jpaApi.em();

		UserHome userDao = new UserHome();

		User targetUser = userDao.findById(id, em);

		if (targetUser == null) {
			return notFound("<h1>Page not found</h1>").as("text/html");
		}

		List<Integer> rolesDb = targetUser.getRoleIds();

		AdminEditForm adminFrm = new AdminEditForm(targetUser.getFirstName(),
				targetUser.getLastName(), targetUser.getEmail(), rolesDb);

		Form<AdminEditForm> form = this.formFactory.form(AdminEditForm.class).fill(adminFrm);

		RoleSelect roleSelect = new RoleSelect(em);
		List<lib.BootstrapSelectKeyValue> roles = roleSelect.populateVenueSelect();
		roles = roleSelect.updateVenueSelect(rolesDb, roles);

		if (ScetConstants.POST.equals(request().method())) {
			form = form.bindFromRequest();

			List<Integer> rolesSelected = new ArrayList<Integer>();

			for (String key : form.data().keySet()) {
				if (key.contains("roles[")) {
					String val = form.data().get(key);
					try {
						rolesSelected.add(Integer.parseInt(val));
					} catch (NumberFormatException e) {
						flash("error", val + " is not a valid selection.");
						//em.close();
						return badRequest(userEditForm.render(form, roles,
								user, id));
					}
				}
			}

			roles = roleSelect.updateVenueSelect(rolesSelected, roles);	

			if (form.hasErrors()) {
				//em.close();
				return ok(userEditForm.render(form, roles, user, id));
			} else {
				
				AdminEditForm input = form.get();
				
				MyUsernamePasswordAuthUser authUser = new MyUsernamePasswordAuthUser(input);

				User u = userDao.findByAuthUserIdentity(authUser, em);

				if (u == null) {
					flash("error", "user not found");
					//em.close();
					return badRequest(userEditForm.render(form, roles, user, id));
				}

				//User newUser = userDao.create(authUser, em);

				SecurityRoleHome roleDao = new SecurityRoleHome();

				Set<SecurityRole> rolesToSave = roleDao.findByIds(
						input.getRoles(), em);

				targetUser.setSecurityRoles(rolesToSave);
				targetUser.setEmail(input.getEmail());
				targetUser.setName(input.getFirstName() + " " + input.getLastName());
				targetUser.setFirstName(input.getFirstName());
				targetUser.setLastName(input.getLastName());

				targetUser = userDao.merge(targetUser, em);
				
				if(input.getPassword1() != null)
				{
					userDao.changePassword(targetUser, authUser, false, em);
				}

				/*MyUsernamePasswordAuthProvider provider = MyUsernamePasswordAuthProvider
						.getProvider();

				provider.sendPasswordResetMailing(targetUser, ctx());*/

				//em.close();
				return redirect(controllers.routes.Admin.manageUsers());

			}
		}

		//em.close();
		return ok(userEditForm.render(form, roles, user, id));

	}

	@Restrict(@Group(Admin.USER_ROLE))
	@Transactional
	public Result viewUser(Integer id) {

		User user = this.userProvider.getUser(session());

		User targetUser = this.jpaApi.em().find(User.class, id);

		if (targetUser == null) {
			return notFound("<h1>Page not found</h1>").as("text/html");
		}

		return ok(userView.render(targetUser, user));
	}

	@Restrict(@Group(Admin.USER_ROLE))
	@Transactional
	public Result deleteUser(Integer id) {
		if (ScetConstants.POST.equals(request().method())) {

			User user = this.jpaApi.em().find(User.class, id);

			if (user == null) {
				return notFound("<h1>Page not found</h1>").as("text/html");
			}

			this.jpaApi.em().remove(user);

			return redirect(controllers.routes.Admin.manageUsers());

		}

		return badRequest();
	}
}