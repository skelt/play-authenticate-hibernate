package reverse.dao;

// Generated Jul 7, 2015 4:46:21 PM by Hibernate Tools 4.3.1

import java.util.List;

import javax.naming.InitialContext;

import models.SecurityRole;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import static org.hibernate.criterion.Example.create;

/**
 * Home object for domain model class SecurityRole.
 * @see reverse.dao.SecurityRole
 * @author Hibernate Tools
 */
public class SecurityRoleHome {

	private static final Log log = LogFactory.getLog(SecurityRoleHome.class);

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

	public void persist(SecurityRole transientInstance) {
		log.debug("persisting SecurityRole instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(SecurityRole instance) {
		log.debug("attaching dirty SecurityRole instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(SecurityRole instance) {
		log.debug("attaching clean SecurityRole instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(SecurityRole persistentInstance) {
		log.debug("deleting SecurityRole instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public SecurityRole merge(SecurityRole detachedInstance) {
		log.debug("merging SecurityRole instance");
		try {
			SecurityRole result = (SecurityRole) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public SecurityRole findById(java.lang.Integer id) {
		log.debug("getting SecurityRole instance with id: " + id);
		try {
			SecurityRole instance = (SecurityRole) sessionFactory
					.getCurrentSession().get("reverse.dao.SecurityRole", id);
			if (instance == null) {
				log.debug("get successful, no instance found");
			} else {
				log.debug("get successful, instance found");
			}
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public Boolean hasInitialData() {
		//log.debug("getting SecurityRole instance with id: " + id);
		try {
			sessionFactory.getCurrentSession().beginTransaction();
			
			Query query = sessionFactory.getCurrentSession().createQuery("SELECT COUNT(*) FROM SecurityRole");
			
			Long count = (Long) query.uniqueResult();
			
			sessionFactory.getCurrentSession().getTransaction().commit();			
			if(count.intValue() == 0)
			{
				return false;
			}
			else
			{
				return true;
			}
			
		} catch (RuntimeException re) {
			sessionFactory.getCurrentSession().getTransaction().rollback();
			throw re;
		}
	}
}
