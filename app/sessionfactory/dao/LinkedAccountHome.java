package sessionfactory.dao;

// Generated Jul 7, 2015 4:46:21 PM by Hibernate Tools 4.3.1

import java.util.List;

import javax.naming.InitialContext;

import models.LinkedAccount;
import models.User;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import com.feth.play.module.pa.user.AuthUser;

import static org.hibernate.criterion.Example.create;

/**
 * Home object for domain model class LinkedAccount.
 * @see reverse.dao.LinkedAccount
 * @author Hibernate Tools
 */
public class LinkedAccountHome {

	private static final Log log = LogFactory.getLog(LinkedAccountHome.class);

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

	public void persist(LinkedAccount transientInstance) {
		log.debug("persisting LinkedAccount instance");
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

	public void attachDirty(LinkedAccount instance) {
		log.debug("attaching dirty LinkedAccount instance");
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

	public void attachClean(LinkedAccount instance) {
		log.debug("attaching clean LinkedAccount instance");
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

	public void delete(LinkedAccount persistentInstance) {
		log.debug("deleting LinkedAccount instance");
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

	public LinkedAccount merge(LinkedAccount detachedInstance) {
		log.debug("merging LinkedAccount instance");
		try {
			sessionFactory.getCurrentSession().beginTransaction();
			LinkedAccount result = (LinkedAccount) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			sessionFactory.getCurrentSession().getTransaction().commit();
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			sessionFactory.getCurrentSession().getTransaction().rollback();
			throw re;
		}
	}

	public LinkedAccount findById(java.lang.Integer id) {
		log.debug("getting LinkedAccount instance with id: " + id);
		try {
			sessionFactory.getCurrentSession().beginTransaction();
			LinkedAccount instance = (LinkedAccount) sessionFactory
					.getCurrentSession().get("models.LinkedAccount", id);
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
	
	public LinkedAccount findByProviderKey(User user, String key) {
		
		try {
			sessionFactory.getCurrentSession().beginTransaction();
			
			Query query = sessionFactory.getCurrentSession().createQuery("SELECT l FROM LinkedAccount l WHERE l.providerKey = :pKey AND l.user = :user");
			query.setParameter("pKey", key);
			query.setParameter("user", user);
			
			LinkedAccount linkedAccount = (LinkedAccount) query.uniqueResult();
			
			sessionFactory.getCurrentSession().getTransaction().commit();
			return linkedAccount;
		} catch (RuntimeException re) {
			sessionFactory.getCurrentSession().getTransaction().rollback();
			throw re;
		}
	}
	
	public LinkedAccount create(User userAccount, String providerKey, String providerUserId) {
		LinkedAccount ret = new LinkedAccount();
		ret.setUser(userAccount);
		ret.setProviderKey(providerKey);
		ret.setProviderUserId(providerUserId);
		
		return this.merge(ret);
	}
	
	public void update(LinkedAccount linkedAccount, AuthUser authUser) {
		
		linkedAccount.setProviderKey(authUser.getProvider());
		linkedAccount.setProviderUserId(authUser.getId());
		
		this.merge(linkedAccount);
	}
}
