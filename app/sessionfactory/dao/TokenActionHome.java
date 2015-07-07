package sessionfactory.dao;

// Generated Jul 7, 2015 4:46:21 PM by Hibernate Tools 4.3.1

import java.util.Date;
import java.util.List;

import javax.naming.InitialContext;

import models.TokenAction;
import models.User;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import sessionfactory.dao.UserHome;
import static org.hibernate.criterion.Example.create;

/**
 * Home object for domain model class TokenAction.
 * @see reverse.dao.TokenAction
 * @author Hibernate Tools
 */
public class TokenActionHome {

	private static final Log log = LogFactory.getLog(TokenActionHome.class);
	
	private final static long VERIFICATION_TIME = 7 * 24 * 3600;

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

	public void persist(TokenAction transientInstance) {
		log.debug("persisting TokenAction instance");
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

	public void attachDirty(TokenAction instance) {
		log.debug("attaching dirty TokenAction instance");
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

	public void attachClean(TokenAction instance) {
		log.debug("attaching clean TokenAction instance");
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

	public void delete(TokenAction persistentInstance) {
		log.debug("deleting TokenAction instance");
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

	public TokenAction merge(TokenAction detachedInstance) {
		log.debug("merging TokenAction instance");
		try {
			sessionFactory.getCurrentSession().beginTransaction();
			TokenAction result = (TokenAction) sessionFactory
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

	public TokenAction findById(java.lang.Integer id) {
		log.debug("getting TokenAction instance with id: " + id);
		try {
			sessionFactory.getCurrentSession().beginTransaction();
			TokenAction instance = (TokenAction) sessionFactory
					.getCurrentSession().get("models.TokenAction", id);
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
	
	public User findUserByTokenId(Integer id) {
		try {
			sessionFactory.getCurrentSession().beginTransaction();
			Query query = sessionFactory.getCurrentSession().createQuery("SELECT u FROM TokenAction t JOIN t.user u WHERE t.id = :id");
			query.setParameter("id", id);
			
			User instance = (User) query.uniqueResult();
			sessionFactory.getCurrentSession().getTransaction().commit();
			log.debug("get successful");
			return instance;
		}
		catch (RuntimeException re) {
			log.error("get failed", re);
			sessionFactory.getCurrentSession().getTransaction().rollback();
			throw re;
		}
	}
	
	public TokenAction create(String type, String token, User targetUser) {
		
		UserHome userDao = new UserHome();
		
		User user = userDao.findById(targetUser.getId());
		
		TokenAction ua = new TokenAction();
		ua.setUser(user);
		ua.setToken(token);
		ua.setType(type);
		Date created = new Date();
		ua.setCreatedOn(created);
		ua.setExpiresOn(new Date(created.getTime() + VERIFICATION_TIME * 1000));
		
		this.persist(ua);
		return ua;
	}
	
	public TokenAction findByToken(String token, String type) {
		try {
			sessionFactory.getCurrentSession().beginTransaction();
			Query query = sessionFactory.getCurrentSession().createQuery("SELECT t FROM TokenAction t WHERE t.token = :token AND t.type = :type");
			query.setParameter("token", token);
			query.setParameter("type", type);
			
			TokenAction instance = (TokenAction) query.uniqueResult();
			sessionFactory.getCurrentSession().getTransaction().commit();
			log.debug("get successful");
			return instance;
		}/*catch (NoResultException e){
			sessionFactory.getCurrentSession().getTransaction().rollback();
			return null;
		}*/
		catch (RuntimeException re) {
			log.error("get failed", re);
			sessionFactory.getCurrentSession().getTransaction().rollback();
			throw re;
		}
	}
	
	public void deleteByUser(User u, String type) {
		
		try {
			sessionFactory.getCurrentSession().beginTransaction();
			
			Query query = sessionFactory.getCurrentSession().createQuery("SELECT t FROM TokenAction t JOIN t.user u WHERE u = :user AND t.type = :type");
			query.setParameter("user", u);
			query.setParameter("type", type);
			
			List<TokenAction> tokens = (List<TokenAction>) query.list();
			
			for(TokenAction token: tokens)
			{
				//this.delete(token);
				sessionFactory.getCurrentSession().delete(token);
			}
			
			sessionFactory.getCurrentSession().getTransaction().commit();
			
		} catch (RuntimeException re) {
			sessionFactory.getCurrentSession().getTransaction().rollback();
			throw re;
		}
	}
}