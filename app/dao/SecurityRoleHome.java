package dao;

// Generated Jul 4, 2015 5:57:00 PM by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import models.SecurityRole;
import play.Logger;

/**
 * Home object for domain model class SecurityRole.
 * @see models.SecurityRole
 * @author Hibernate Tools
 */
public class SecurityRoleHome {

	public void persist(SecurityRole transientInstance, EntityManager entityManager) {
		
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

	public void remove(SecurityRole persistentInstance, EntityManager entityManager) {
		
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

	public SecurityRole merge(SecurityRole detachedInstance, EntityManager entityManager) {
		
		EntityTransaction tx = null;
		try {
		    tx = entityManager.getTransaction();
		    tx.begin();
		    
		    SecurityRole result = entityManager.merge(detachedInstance);

		    tx.commit();
		    
		    return result;
		}
		catch (RuntimeException e) {
		    if ( tx != null && tx.isActive() ) tx.rollback();
		    throw e; // or display error message
		}
	}

	public SecurityRole findById(Integer id, EntityManager entityManager) {
		Logger.debug("getting SecurityRole instance with id: " + id);
		try {
			SecurityRole instance = entityManager.find(SecurityRole.class, id);
			Logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			Logger.error("get failed", re);
			throw re;
		}
	}
	
	public Set<SecurityRole> findByIds(List<Integer> ids, EntityManager entityManager) {
		//log.debug("getting SecurityRole instance with id: " + id);
		try {
			Query query = entityManager.createQuery("SELECT r FROM SecurityRole r WHERE r.id IN :ids");
			query.setParameter("ids", ids);
			
			Set<SecurityRole> instance = new HashSet<SecurityRole>(query.getResultList());
			Logger.debug("get successful");
			return instance;
		}catch(NoResultException e){
			Logger.info("role not found");
			return null;			
		}catch (Exception re) {
			Logger.error("get failed", re);
			throw re;
		}
	}
	
	public SecurityRole findByRoleName(String name, EntityManager entityManager) {
		try {
			Query query = entityManager.createQuery("SELECT r FROM SecurityRole r WHERE r.roleName = :name");
			query.setParameter("name", name);
			
			SecurityRole instance = (SecurityRole) query.getSingleResult();
			Logger.debug("get successful");
			return instance;
		}catch(NoResultException e){
			Logger.info("role not found");
			return null;			
		}catch (Exception re) {
			Logger.error("get failed", re);
			throw re;
		}
	}
	
	public Boolean hasInitialData(EntityManager entityManager) {
		//log.debug("getting SecurityRole instance with id: " + id);
		try {
			Query query = entityManager.createQuery("SELECT COUNT(*) FROM SecurityRole");
			
			Long count = (Long) query.getSingleResult();
			
			if(count.intValue() == 0)
			{
				return false;
			}
			else
			{
				return true;
			}
			
		} catch (RuntimeException re) {
			Logger.error("get failed", re);
			throw re;
		}
	}
}
