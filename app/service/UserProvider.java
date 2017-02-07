package service;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.user.AuthUser;

import constants.JpaConstants;
import dao.UserHome;
import models.User;
import play.db.jpa.JPAApi;
import play.mvc.Http.Session;

/**
 * Service layer for User DB entity
 */
public class UserProvider {

    private final PlayAuthenticate auth;
    private final JPAApi jpaApi;

    @Inject
    public UserProvider(final PlayAuthenticate auth, final JPAApi api) {
        this.auth = auth;
        this.jpaApi = api;
    }

    @Nullable
    public User getUser(Session session) {
        final AuthUser currentAuthUser = this.auth.getUser(session);
        //final User localUser = User.findByAuthUserIdentity(currentAuthUser);
        
        EntityManager em = this.jpaApi.em(JpaConstants.DB);
		
		// User logged in again, bump last login date
		UserHome userDao = new UserHome();
		
		User u = userDao.findByAuthUserIdentity(currentAuthUser, em);
		//u.setLastLogin(new Date());
		//userDao.merge(u, em);

		em.close();
        return u;
    }
}
