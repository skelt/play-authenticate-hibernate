package service;

import java.util.Date;

import javax.persistence.EntityManager;

import models.User;
import play.Application;
import play.db.jpa.JPA;

import com.feth.play.module.pa.user.AuthUser;
import com.feth.play.module.pa.user.AuthUserIdentity;
import com.feth.play.module.pa.service.UserServicePlugin;
import com.google.inject.Inject;

import constants.JpaConstants;
import sessionfactory.dao.UserHome;

public class HibernateUserServicePlugin extends UserServicePlugin {

	@Inject
	public HibernateUserServicePlugin(Application app) {
		super(app);
	}

	@Override
	public Object save(AuthUser authUser) {
		
		UserHome userDao = new UserHome();
		
		boolean isLinked = userDao.existsByAuthUserIdentity(authUser);
		if (!isLinked) {
			Integer userId = userDao.create(authUser).getId();
			return userId;
		} else {
			// we have this user already, so return null
			return null;
		}
	}

	@Override
	public Object getLocalIdentity(AuthUserIdentity identity) {		
		// For production: Caching might be a good idea here...
		// ...and dont forget to sync the cache when users get deactivated/deleted
		UserHome userDao = new UserHome();
		
		User u = userDao.findByAuthUserIdentity(identity);
		if(u != null) {
			return u.getId();
		} else {
			return null;
		}
	}

	@Override
	public AuthUser merge(AuthUser newUser, AuthUser oldUser) {
		
		UserHome userDao = new UserHome();
		if (!oldUser.equals(newUser)) {
			userDao.merge(oldUser, newUser);
		}
		
		return oldUser;
	}

	@Override
	public AuthUser link(AuthUser oldUser, AuthUser newUser) {
		
		UserHome userDao = new UserHome();
		
		userDao.addLinkedAccount(oldUser, newUser);
		
		return newUser;
	}
	
	@Override
	public AuthUser update(AuthUser knownUser) {
		
		// User logged in again, bump last login date
		UserHome userDao = new UserHome();
		
		User u = userDao.findByAuthUserIdentity(knownUser);
		u.setLastLogin(new Date());
		userDao.merge(u);
		
		return knownUser;
	}

}
