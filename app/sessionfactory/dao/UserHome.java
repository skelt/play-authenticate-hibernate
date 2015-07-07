package sessionfactory.dao;

// Generated Jul 7, 2015 4:46:21 PM by Hibernate Tools 4.3.1

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.naming.InitialContext;

import models.LinkedAccount;
import models.SecurityRole;
import models.User;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import com.feth.play.module.pa.providers.password.UsernamePasswordAuthUser;
import com.feth.play.module.pa.user.AuthUser;
import com.feth.play.module.pa.user.AuthUserIdentity;
import com.feth.play.module.pa.user.EmailIdentity;
import com.feth.play.module.pa.user.FirstLastNameIdentity;
import com.feth.play.module.pa.user.NameIdentity;

import sessionfactory.dao.LinkedAccountHome;
import sessionfactory.dao.SecurityRoleHome;
import sessionfactory.dao.TokenActionHome;
import static org.hibernate.criterion.Example.create;

/**
 * Home object for domain model class User.
 * @see reverse.dao.User
 * @author Hibernate Tools
 */
public class UserHome {

	private static final Log log = LogFactory.getLog(UserHome.class);

	private final SessionFactory sessionFactory = global.Global.getSessionFactory();

	protected SessionFactory getSessionFactory() {
		try {
			return (SessionFactory) new InitialContext()
					.lookup("SessionFactory");
		} catch (Exception e) {
			log.error("Could not locate SessionFactory in JNDI", e);
			throw new IllegalStateException(
					"Could not locate SessionFactory in JNDI");
		}
	}

	public void persist(User transientInstance) {
		log.debug("persisting User instance");
		try {
			sessionFactory.getCurrentSession().beginTransaction();
			sessionFactory.getCurrentSession().persist(transientInstance);
			sessionFactory.getCurrentSession().getTransaction().commit();
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			sessionFactory.getCurrentSession().getTransaction().rollback();
			throw re;
		}
	}

	public void attachDirty(User instance) {
		log.debug("attaching dirty User instance");
		try {
			sessionFactory.getCurrentSession().beginTransaction();
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			sessionFactory.getCurrentSession().getTransaction().commit();
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			sessionFactory.getCurrentSession().getTransaction().rollback();
			throw re;
		}
	}

	public void attachClean(User instance) {
		log.debug("attaching clean User instance");
		try {
			sessionFactory.getCurrentSession().beginTransaction();
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			sessionFactory.getCurrentSession().getTransaction().commit();
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			sessionFactory.getCurrentSession().getTransaction().rollback();
			throw re;
		}
	}

	public void delete(User persistentInstance) {
		log.debug("deleting User instance");
		try {
			sessionFactory.getCurrentSession().beginTransaction();
			sessionFactory.getCurrentSession().delete(persistentInstance);
			sessionFactory.getCurrentSession().getTransaction().commit();
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			sessionFactory.getCurrentSession().getTransaction().rollback();
			throw re;
		}
	}

	public User merge(User detachedInstance) {
		log.debug("merging User instance");
		try {
			sessionFactory.getCurrentSession().beginTransaction();
			User result = (User) sessionFactory.getCurrentSession().merge(
					detachedInstance);
			sessionFactory.getCurrentSession().getTransaction().commit();
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			sessionFactory.getCurrentSession().getTransaction().rollback();
			throw re;
		}
	}

	public User findById(java.lang.Integer id) {
		log.debug("getting User instance with id: " + id);
		try {
			sessionFactory.getCurrentSession().beginTransaction();
			User instance = (User) sessionFactory.getCurrentSession().get(
					"models.User", id);
			sessionFactory.getCurrentSession().getTransaction().commit();
			if (instance == null) {
				log.debug("get successful, no instance found");
			} else {
				log.debug("get successful, instance found");
			}
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			sessionFactory.getCurrentSession().getTransaction().rollback();
			throw re;
		}
	}
	
	public LinkedAccount getAccountByProvider(User user, String providerKey) {
		LinkedAccountHome dao = new LinkedAccountHome();
		
		return dao.findByProviderKey(user, providerKey);
	}
	
	public void changePassword(User user, UsernamePasswordAuthUser authUser, boolean create) {
		LinkedAccount a = this.getAccountByProvider(user, authUser.getProvider());
		
		LinkedAccountHome dao = new LinkedAccountHome();
		
		if (a == null) {
			if (create) {
				a = dao.create(user, authUser.getProvider(), authUser.getId());
				a.setUser(user);
			} else {
				throw new RuntimeException("Account not enabled for password usage");
			}
		}
		
		a.setProviderUserId(authUser.getHashedPassword());
		
		dao.merge(a);
	}
	
	public void resetPassword(User user, UsernamePasswordAuthUser authUser, boolean create) {
		// You might want to wrap this into a transaction
		this.changePassword(user, authUser, create);
		
		TokenActionHome tokenDao = new TokenActionHome();
		
		tokenDao.deleteByUser(user, "PASSWORD_RESET");
	}
	
	public boolean existsByAuthUserIdentity( AuthUserIdentity identity) {
		User exp = null;
		if (identity instanceof UsernamePasswordAuthUser) {
			exp = findByUsernamePasswordIdentity((UsernamePasswordAuthUser) identity);
		} else {
			exp = getAuthUserFind(identity);
		}
		
		if(exp == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public User findByUsernamePasswordIdentity(UsernamePasswordAuthUser identity) {
		
		try {
			sessionFactory.getCurrentSession().beginTransaction();
		    Query query = sessionFactory.getCurrentSession().createQuery("SELECT DISTINCT u FROM LinkedAccount l JOIN l.user u WHERE l.providerKey = :pKey AND u.email = :email AND u.active = true");
			query.setParameter("pKey", identity.getProvider());
			query.setParameter("email", identity.getEmail());
			
			User user = (User) query.uniqueResult();
			sessionFactory.getCurrentSession().getTransaction().commit();		
			
			return user;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			sessionFactory.getCurrentSession().getTransaction().rollback();
			throw re;
		} 
	}
	
    public User findByEmail(String email) {
		
		try {
			sessionFactory.getCurrentSession().beginTransaction();
			Query query = sessionFactory.getCurrentSession().createQuery("SELECT DISTINCT u FROM User u WHERE u.email = :email AND u.active = true");
			query.setParameter("email", email);
			
			User user = (User) query.uniqueResult();
			sessionFactory.getCurrentSession().getTransaction().commit();
			
			return user;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			sessionFactory.getCurrentSession().getTransaction().rollback();
			throw re;
		}
	}
	
	private User getAuthUserFind(AuthUserIdentity identity) {
		
		try {
			sessionFactory.getCurrentSession().beginTransaction();
			Query query = sessionFactory.getCurrentSession().createQuery("SELECT DISTINCT u FROM LinkedAccount l JOIN l.user u WHERE l.providerKey = :pKey AND l.providerUserId = :userId AND u.active = true");
			query.setParameter("pKey", identity.getProvider());
			query.setParameter("userId", identity.getId());
			
			User user = (User) query.uniqueResult();
			sessionFactory.getCurrentSession().getTransaction().commit();
			
			return user;
		}
		catch (RuntimeException re) {
			log.error("get failed", re);
			sessionFactory.getCurrentSession().getTransaction().rollback();
			throw re;
		}
	}

	public User findByAuthUserIdentity(AuthUserIdentity identity) {
		if (identity == null) {
			return null;
		}
		if (identity instanceof UsernamePasswordAuthUser) {
			return findByUsernamePasswordIdentity((UsernamePasswordAuthUser) identity);
		} else {
			return getAuthUserFind(identity);
		}
	}
	
	public User create(AuthUser authUser) {
		User user = new User();
		
		SecurityRoleHome roleDao = new SecurityRoleHome();
		
		SecurityRole role = roleDao.findByRoleName(controllers.Application.USER_ROLE);
		
		List<SecurityRole> roles = user.getSecurityRoles();
		roles.add(role);
		
		user.setSecurityRoles(roles);
		
		user.setActive(true);
		user.setLastLogin(new Date());

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
		
		user = this.merge(user);
		
        LinkedAccountHome accountsDao = new LinkedAccountHome();
		
		accountsDao.create(user, authUser.getProvider(), authUser.getId());

		return user;
	}
	
	public void verify(User unverified) {
		// You might want to wrap this into a transaction
		unverified.setEmailValidated(true);
		this.merge(unverified);
		
		TokenActionHome tokenDao = new TokenActionHome();
		
		tokenDao.deleteByUser(unverified, "EMAIL_VERIFICATION");
	}
	
	public void merge(User currentUser, User otherUser) {
		
		Set<LinkedAccount> currentUserAccounts = currentUser.getLinkedAccounts();
		
		LinkedAccountHome linkedAccountDao = new LinkedAccountHome();
		
		for (LinkedAccount acc : otherUser.getLinkedAccounts()) {
			currentUserAccounts.add(linkedAccountDao.create(currentUser, acc.getProviderKey(), acc.getProviderUserId()));
		}
		
		// do all other merging stuff here - like resources, etc.
		currentUser.setLinkedAccounts(currentUserAccounts);
		
		// deactivate the merged user that got added to this one
		otherUser.setActive(false);
		
		this.merge(otherUser);
		this.merge(currentUser);
	}
	
	public void merge(AuthUser oldUser, AuthUser newUser) {
		
		User oldUserDb = this.findByAuthUserIdentity(oldUser);
		User newUserDb = this.findByAuthUserIdentity(newUser);
		
		this.merge(oldUserDb, newUserDb);
	}
	
	public void addLinkedAccount(AuthUser oldUser, AuthUser newUser) {
		User u = this.findByAuthUserIdentity(oldUser);
		
		LinkedAccountHome linkedAccountsDao = new LinkedAccountHome();
		
		Set<LinkedAccount> linkedAccounts = u.getLinkedAccounts();
		
		linkedAccounts.add(linkedAccountsDao.create(u, newUser.getProvider(), newUser.getId()));
		
		u.setLinkedAccounts(linkedAccounts);
		
		this.merge(u);
	}
}
