package actions;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.commons.codec.binary.Base64;

import com.feth.play.module.pa.providers.password.UsernamePasswordAuthProvider;

import constants.JpaConstants;
import models.LinkedAccount;
import play.Logger;
import play.db.jpa.JPAApi;
//import play.libs.F;
//import play.libs.F.Promise;
import play.mvc.Action;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import security.PasswordHasher;

public class BasicAuthAction extends Action<Controller> {

    private static final String AUTHORIZATION = "authorization";
    private static final String WWW_AUTHENTICATE = "WWW-Authenticate";
    private static final String REALM = "Basic realm=\"Your Realm Here\"";
    private final JPAApi jpaApi;
    
    @Inject
	public BasicAuthAction(final JPAApi api) {
		this.jpaApi = api;
	}
    
    @Override
	public CompletionStage<Result> call(Context context) {
    	String authHeader = context.request().getHeader(AUTHORIZATION);
    	
    	if (authHeader == null) {
            context.response().setHeader(WWW_AUTHENTICATE, REALM);
            return CompletableFuture.completedFuture((Result)unauthorized());
        }
    	
    	String[] credString = this.decodeAuthDetails(authHeader);
    	if (credString == null || credString.length != 2) {
    		return CompletableFuture.completedFuture((Result)unauthorized());
        }
    	
    	String username = credString[0];
    	String password = credString[1];
    	
    	LinkedAccount linkedAccount = this.findUser(username);
    	
    	if(this.validateUser(linkedAccount, password))
    	{
    		context.args.put("api-user", linkedAccount.getUser());
    		return delegate.call(context);
    	}
    	else
    	{
    		return CompletableFuture.completedFuture((Result)unauthorized());
    	}
	}
    
    private boolean validateUser(LinkedAccount linkedAccount, String password)
    {
    	if(linkedAccount == null || password == null) {
			return false;
		}
    	
    	PasswordHasher hasher = new PasswordHasher();
		
		String[] hashSalt = linkedAccount.getProviderUserId().split("#");
		String storedSha256hex = hashSalt[0];
		String storedSalt = hashSalt[1];
		
		return hasher.checkPassword(storedSha256hex, storedSalt, password);
    }
    
    private String[] decodeAuthDetails(String authHeader)
    {
        String auth = authHeader.substring(6);
        byte[] decodedAuth = Base64.decodeBase64(auth.getBytes());
        String[] credString = null;
        
		try {
			credString = new String(decodedAuth, "UTF-8").split(":");
		} catch (UnsupportedEncodingException e) {
			Logger.info("Could not decode base64 for BasicAuth", e);
		}
		
		return credString;
    }
    
    private LinkedAccount findUser(String username)
    {
    	EntityManager em = this.jpaApi.em(JpaConstants.DB);
    	
    	try {
			Query query = em.createQuery("SELECT DISTINCT l FROM LinkedAccount l JOIN l.user u WHERE lower(l.providerKey) = :pKey AND lower(u.email) = :email AND u.active = true");
			query.setParameter("pKey", UsernamePasswordAuthProvider.PROVIDER_KEY.toLowerCase());
			query.setParameter("email", username.toLowerCase());
			
			LinkedAccount linkedAccount = (LinkedAccount) query.getSingleResult();
			
			em.close();
			return linkedAccount;
		} catch (NoResultException nr) {
			Logger.info("User not found using basic auth: ", nr);
			em.close();
			return null;
		} catch (RuntimeException re) {
			Logger.error("something went really bad when looking up the user using basic auth: ", re);
			em.close();
			throw re;
		}
    }
}