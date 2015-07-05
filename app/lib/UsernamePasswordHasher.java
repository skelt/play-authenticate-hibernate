/*
 * Written by Stewart Kelt Copyright SCET
 */

package lib;

import java.security.SecureRandom;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class UsernamePasswordHasher {
	
	private String sha256;
	private String globalSalt;
	private String salt;
	private String password;
	
	public UsernamePasswordHasher(String password)
	{
		Config conf = ConfigFactory.load();
		this.globalSalt = conf.getString("play.crypto.secret");
		this.password = password;
		
		Random r = new SecureRandom();
		byte[] saltBytes = new byte[32];
		r.nextBytes(saltBytes);
		this.salt = Base64.encodeBase64String(saltBytes);
		
		this.sha256 = DigestUtils.sha256Hex(globalSalt + this.password + this.salt);
	}

	public String getSalt() {
		return salt;
	}
	
	public String getSha256() {
		return sha256;
	}

	public Boolean isEqualTo(String sha256Hex, String salt)
	{
		if(DigestUtils.sha256Hex(globalSalt + this.password + salt).equals(sha256Hex))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
}
