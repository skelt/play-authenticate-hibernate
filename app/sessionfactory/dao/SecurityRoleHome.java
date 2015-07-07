package sessionfactory.dao;

// Generated Jul 7, 2015 4:46:21 PM by Hibernate Tools 4.3.1

import java.util.List;

import javax.naming.InitialContext;
import javax.persistence.EntityManager;

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

	public void attachDirty(SecurityRole instance) {
		log.debug("attaching dirty SecurityRole instance");
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

	public void attachClean(SecurityRole instance) {
		log.debug("attaching clean SecurityRole instance");
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

	public void delete(SecurityRole persistentInstance) {
		log.debug("deleting SecurityRole instance");
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

	public SecurityRole merge(SecurityRole detachedInstance) {
		log.debug("merging SecurityRole instance");
		try {
			sessionFactory.getCurrentSession().beginTransaction();
			SecurityRole result = (SecurityRole) sessionFactory
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

	public SecurityRole findById(java.lang.Integer id) {
		log.debug("getting SecurityRole instance with id: " + id);
		try {
			sessionFactory.getCurrentSession().beginTransaction();
			SecurityRole instance = (SecurityRole) sessionFactory
					.getCurrentSession().get("models.SecurityRole", id);
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
	
	public SecurityRole findByRoleName(String name) {
		try {
			sessionFactory.getCurrentSession().beginTransaction();
			Query query = sessionFactory.getCurrentSession().createQuery("SELECT r FROM SecurityRole r WHERE r.roleName = :name");
			query.setParameter("name", name);
			
			SecurityRole instance = (SecurityRole) query.uniqueResult();
			sessionFactory.getCurrentSession().getTransaction().commit();
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			sessionFactory.getCurrentSession().getTransaction().rollback();
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
