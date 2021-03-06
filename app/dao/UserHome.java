package dao;

// Generated Jul 4, 2015 5:57:00 PM by Hibernate Tools 4.3.1

import java.util.Date;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import models.LinkedAccount;
import models.SecurityRole;
import models.User;
import models.UserProfile;
import play.Logger;
import providers.MyUsernamePasswordAuthUser;

import com.feth.play.module.pa.providers.password.UsernamePasswordAuthUser;
import com.feth.play.module.pa.user.AuthUser;
import com.feth.play.module.pa.user.AuthUserIdentity;
import com.feth.play.module.pa.user.EmailIdentity;
import com.feth.play.module.pa.user.FirstLastNameIdentity;
import com.feth.play.module.pa.user.NameIdentity;

import exceptions.PlayAuthHibernateServiceException;

/**
 * Home object for domain model class User.
 * @see models.User
 * @author Hibernate Tools
 */
public class UserHome {
	
	public void persist(User transientInstance, EntityManager entityManager) {
		
		EntityTransaction tx = null;
		try {
		    tx = entityManager.getTransaction();
		    tx.begin();
		    
		    entityManager.persist(transientInstance);

		    tx.commit();
		}
		catch (RuntimeException e) {
		    if ( tx != null && tx.isActive() ) tx.rollback();
		    throw e; // or display error message
		}
	}

	public void remove(User persistentInstance, EntityManager entityManager) {
		
		EntityTransaction tx = null;
		try {
		    tx = entityManager.getTransaction();
		    tx.begin();
		    
		    entityManager.remove(persistentInstance);

		    tx.commit();
		}
		catch (RuntimeException e) {
		    if ( tx != null && tx.isActive() ) tx.rollback();
		    throw e; // or display error message
		}
	}

	public User merge(User detachedInstance, EntityManager entityManager) {
		
		EntityTransaction tx = null;
		try {
		    tx = entityManager.getTransaction();
		    tx.begin();
		    
		    User result = entityManager.merge(detachedInstance);

		    tx.commit();
		    
		    return result;
		}
		catch (RuntimeException e) {
		    if ( tx != null && tx.isActive() ) tx.rollback();
		    throw e; // or display error message
		}
	}

	public User findById(Integer id, EntityManager entityManager) {
		
		try {
			Query query = entityManager.createQuery("SELECT u FROM User u LEFT JOIN FETCH u.linkedAccounts LEFT JOIN FETCH u.securityRoles WHERE u.id = :id");
			query.setParameter("id", id);
			
			User user = (User) query.getSingleResult();
			
			return user;
		}catch (NoResultException e) {
			Logger.info("user not found");
			return null;
		}
		catch (Exception re) {
			Logger.error("error: ", re);
			throw re;
		}
		
		/*log.debug("getting User instance with id: " + id);
		try {
			User instance = entityManager.find(User.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}*/
	}
	
	public LinkedAccount getAccountByProvider(User user, String providerKey, EntityManager entityManager) {
		LinkedAccountHome dao = new LinkedAccountHome();
		
		return dao.findByProviderKey(user, providerKey, entityManager);
	}
	
	public void changePassword(User user, MyUsernamePasswordAuthUser authUser, boolean create, EntityManager entityManager) {
		LinkedAccount a = this.getAccountByProvider(user, authUser.getProvider(), entityManager);
		
		LinkedAccountHome dao = new LinkedAccountHome();
		
		if (a == null) {
			if (create) {
				a = dao.create(user, authUser.getProvider(), authUser.getId(), entityManager);
				a.setUser(user);
			} else {
				throw new PlayAuthHibernateServiceException("Account not enabled for password usage");
			}
		}
		
		a.setProviderUserId(authUser.getHashedPassword());
		a.getUser().setEmailValidated(true);
		
		dao.merge(a, entityManager);
	}
	
	public void resetPassword(User user, MyUsernamePasswordAuthUser authUser, boolean create, EntityManager entityManager) {
		// You might want to wrap this into a transaction
		this.changePassword(user, authUser, create, entityManager);
		
		TokenActionHome tokenDao = new TokenActionHome();
		
		tokenDao.deleteByUser(user, "PASSWORD_RESET", entityManager);
	}
	
	public boolean existsByAuthUserIdentity( AuthUserIdentity identity, EntityManager entityManager) {
		User exp = null;
		if (identity instanceof UsernamePasswordAuthUser) {
			exp = findByUsernamePasswordIdentity((UsernamePasswordAuthUser) identity, entityManager);
		} else {
			exp = getAuthUserFind(identity, entityManager);
		}
		
		if(exp == null)
		{
			return false;
		}
		else
		{
			return true;
		}
		//return exp.findRowCount() > 0;
	}
	
	public User findByEmail(String email, EntityManager entityManager) {
		
		try {
			Query query = entityManager.createQuery("SELECT DISTINCT u FROM User u WHERE lower(u.email) = :email AND u.active = true");
			query.setParameter("email", email.toLowerCase());
			
			User user = (User) query.getSingleResult();
			
			return user;
		}catch (NoResultException e) {
			Logger.info("user not found");
			return null;
		}
		catch (Exception re) {
			Logger.error("error: ", re);
			throw re;
		}
	}
	
	public User findByUsernamePasswordIdentity(UsernamePasswordAuthUser identity, EntityManager entityManager) {
		
		try {
			Query query = entityManager.createQuery("SELECT DISTINCT u FROM LinkedAccount l JOIN l.user u LEFT JOIN FETCH u.securityRoles LEFT JOIN FETCH u.userPermissions WHERE lower(l.providerKey) = :pKey AND lower(u.email) = :email AND u.active = true");
			query.setParameter("pKey", identity.getProvider().toLowerCase());
			query.setParameter("email", identity.getEmail().toLowerCase());
			
			User user = (User) query.getSingleResult();
			
			return user;
		}catch (NoResultException e) {
			Logger.info("user not found");
			return null;
		}
		catch (Exception re) {
			Logger.error("error: ", re);
			throw re;
		}
	}    
	
	private User getAuthUserFind(AuthUserIdentity identity, EntityManager entityManager) {
		
		try {
			Query query = entityManager.createQuery("SELECT DISTINCT u FROM LinkedAccount l JOIN l.user u LEFT JOIN FETCH u.securityRoles LEFT JOIN FETCH u.userPermissions WHERE lower(l.providerKey) = :pKey AND lower(l.providerUserId) = :userId AND u.active = true");
			query.setParameter("pKey", identity.getProvider().toLowerCase());
			query.setParameter("userId", identity.getId().toLowerCase());
			
			User user = (User) query.getSingleResult();
			
			return user;
		}catch (NoResultException e) {
			Logger.info("user not found");
			return null;
		}
		catch (Exception re) {
			Logger.error("error: ", re);
			throw re;
		}
	}

	public User findByAuthUserIdentity(AuthUserIdentity identity, EntityManager entityManager) {
		if (identity == null) {
			return null;
		}
		if (identity instanceof UsernamePasswordAuthUser) {
			return findByUsernamePasswordIdentity((UsernamePasswordAuthUser) identity, entityManager);
		} else {
			return getAuthUserFind(identity, entityManager);
		}
	}
	
	public User create(AuthUser authUser, EntityManager entityManager) {
		return this.createByRole(authUser, controllers.Application.USER_ROLE, null, entityManager);
	}
	
	public User createByRole(AuthUser authUser, String roleName, String tiUserId, EntityManager entityManager) {
		User user = new User();
		
		SecurityRoleHome roleDao = new SecurityRoleHome();
		
		SecurityRole role = roleDao.findByRoleName(roleName, entityManager);
		
		Set<SecurityRole> roles = user.getSecurityRoles();
		roles.add(role);
		
		UserProfileHome userProfileDao = new UserProfileHome();
		
		UserProfile userProfile = new UserProfile();
		userProfile.setTiUserId(tiUserId);
		
		userProfile = userProfileDao.merge(userProfile, entityManager);
		
		user.setSecurityRoles(roles);
		user.setUserProfile(userProfile);
		
		user.setActive(true);
		user.setLastLogin(new Date());
		user.setCreatedOn(new Date());
		user.setUpdatedOn(new Date());

		if (authUser instanceof EmailIdentity) {
			EmailIdentity identity = (EmailIdentity) authUser;
			// Remember, even when getting them from FB & Co., emails should be
			// verified within the application as a security breach there might
			// break your security as well!
			user.setEmail(identity.getEmail());
			user.setEmailValidated(false);
		}

		if (authUser instanceof NameIdentity) {
			NameIdentity identity = (NameIdentity) authUser;
			String name = identity.getName();
			if (name != null) {
				user.setName(name);
			}
		}
		
		if (authUser instanceof FirstLastNameIdentity) {
		  FirstLastNameIdentity identity = (FirstLastNameIdentity) authUser;
		  String firstName = identity.getFirstName();
		  String lastName = identity.getLastName();
		  if (firstName != null) {
		    user.setFirstName(firstName);
		  }
		  if (lastName != null) {
		    user.setLastName(lastName);
		  }
		}
		
		user = this.merge(user, entityManager);
		
        LinkedAccountHome accountsDao = new LinkedAccountHome();
		
		accountsDao.create(user, authUser.getProvider(), authUser.getId(), entityManager);

		return user;
	}
	
	public void verify(User unverified, EntityManager entityManager) {
		// You might want to wrap this into a transaction
		unverified.setEmailValidated(true);
		this.merge(unverified, entityManager);
		
		TokenActionHome tokenDao = new TokenActionHome();
		
		tokenDao.deleteByUser(unverified, "EMAIL_VERIFICATION", entityManager);
	}
	
	public void merge(User currentUser, User otherUser, EntityManager entityManager) {
		
		Set<LinkedAccount> currentUserAccounts = currentUser.getLinkedAccounts();
		
		LinkedAccountHome linkedAccountDao = new LinkedAccountHome();
		
		for (LinkedAccount acc : otherUser.getLinkedAccounts()) {
			currentUserAccounts.add(linkedAccountDao.create(currentUser, acc.getProviderKey(), acc.getProviderUserId(), entityManager));
		}
		
		// do all other merging stuff here - like resources, etc.
		currentUser.setLinkedAccounts(currentUserAccounts);
		
		Set<SecurityRole> roles = otherUser.getSecurityRoles();
		
		for(SecurityRole role: roles){
			currentUser.getSecurityRoles().add(role);
		}
		
		// deactivate the merged user that got added to this one
		otherUser.setActive(false);
		
		this.merge(otherUser, entityManager);
		this.merge(currentUser, entityManager);
	}
	
	public void merge(AuthUser oldUser, AuthUser newUser, EntityManager entityManager) {
		
		User oldUserDb = this.findByAuthUserIdentity(oldUser, entityManager);
		User newUserDb = this.findByAuthUserIdentity(newUser, entityManager);
		
		this.merge(oldUserDb, newUserDb, entityManager);
	}
	
	public void addLinkedAccount(AuthUser oldUser, AuthUser newUser, EntityManager entityManager) {
		User u = this.findByAuthUserIdentity(oldUser, entityManager);
		
		LinkedAccountHome linkedAccountsDao = new LinkedAccountHome();
		
		Set<LinkedAccount> linkedAccounts = u.getLinkedAccounts();
		
		linkedAccounts.add(linkedAccountsDao.create(u, newUser.getProvider(), newUser.getId(), entityManager));
		
		u.setLinkedAccounts(linkedAccounts);
		
		this.merge(u, entityManager);
	}
}
