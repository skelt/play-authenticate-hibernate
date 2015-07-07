package global;
import java.util.Arrays;

import javax.persistence.EntityManager;

import models.SecurityRole;

import org.hibernate.SessionFactory;
import org.hibernate.jpa.HibernateEntityManagerFactory;

import play.Application;
import play.GlobalSettings;
import play.db.jpa.JPA;
import play.mvc.Call;
import sessionfactory.dao.SecurityRoleHome;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.PlayAuthenticate.Resolver;
import com.feth.play.module.pa.exceptions.AccessDeniedException;
import com.feth.play.module.pa.exceptions.AuthException;

import constants.JpaConstants;
import controllers.routes;

public class Global extends GlobalSettings {

	private static SessionFactory sessionFactory;
	
	public static SessionFactory getSessionFactory(){
		return sessionFactory;
	}

	@Override
	public void onStart(Application app) {
		
		HibernateEntityManagerFactory emFactory = (HibernateEntityManagerFactory) JPA.em(JpaConstants.DB).getEntityManagerFactory();
		
		sessionFactory = emFactory.getSessionFactory();
		
		PlayAuthenticate.setResolver(new Resolver() {

			@Override
			public Call login() {
				// Your login page
				return routes.Application.login();
			}

			@Override
			public Call afterAuth() {
				// The user will be redirected to this page after authentication
				// if no original URL was saved
				return routes.Application.index();
			}

			@Override
			public Call afterLogout() {
				return routes.Application.index();
			}

			@Override
			public Call auth(String provider) {
				// You can provide your own authentication implementation,
				// however the default should be sufficient for most cases
				return com.feth.play.module.pa.controllers.routes.AuthenticateDI.authenticate(provider);
			}

			@Override
			public Call askMerge() {
				return routes.Account.askMerge();
			}

			@Override
			public Call askLink() {
				return routes.Account.askLink();
			}

			@Override
			public Call onException(AuthException e) {
				if (e instanceof AccessDeniedException) {
					return routes.Signup
							.oAuthDenied(((AccessDeniedException) e)
									.getProviderKey());
				}

				// more custom problem handling here...
				return super.onException(e);
			}
		});

		initialData();
	}
	
	

	@Override
	public void onStop(Application app) {
		sessionFactory.close();
		super.onStop(app);
	}

	private void initialData() {
		
		EntityManager em = JPA.em(JpaConstants.DB);
		
		SecurityRoleHome dao = new SecurityRoleHome();
		
		if (!dao.hasInitialData()) {
			for (String roleName : Arrays
					.asList(controllers.Application.USER_ROLE)) {
				SecurityRole role = new SecurityRole();
				role.setRoleName(roleName);
				dao.persist(role);
			}
		}
		
		em.close();
	}
}