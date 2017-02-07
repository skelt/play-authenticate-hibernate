package service;

import java.util.Arrays;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import constants.JpaConstants;
import dao.SecurityRoleHome;
import dao.UserHome;
import forms.RegistrationForm;
import models.SecurityRole;
import models.User;
import play.db.jpa.JPAApi;
import providers.MyUsernamePasswordAuthUser;

/**
 * Data initializer class.
 */
public class DataInitializer {
	
	private final JPAApi jpaApi;

    @Inject
    public DataInitializer(final JPAApi api) {
        this.jpaApi = api;
        
        EntityManager em = this.jpaApi.em(JpaConstants.DB);
		
		SecurityRoleHome dao = new SecurityRoleHome();
		
		if (!dao.hasInitialData(em)) {
			for (String roleName : Arrays.asList(controllers.Application.USER_ROLE, controllers.Api.USER_ROLE, controllers.Admin.USER_ROLE)) {
				SecurityRole role = new SecurityRole();
				role.setRoleName(roleName);
				dao.persist(role, em);
			}
		}
		
		UserHome userDao = new UserHome();
		
		RegistrationForm input = new RegistrationForm("admin", "", "admin@scet.co");
		
		input.setPassword1("admin");
		
		MyUsernamePasswordAuthUser authUser = new MyUsernamePasswordAuthUser(input);
		
		User u = userDao.findByAuthUserIdentity(authUser, em);
		
		if(u == null)
		{
			User newUser = userDao.createByRole(authUser, controllers.Admin.USER_ROLE, null, em);
			newUser.setActive(true);
			newUser.setEmailValidated(true);
			
			userDao.merge(newUser, em);
		}
		
		em.close();
    }
}
