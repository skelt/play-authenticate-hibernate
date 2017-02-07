package security;

import java.security.SecureRandom;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class PasswordHasher {
	
	private String sha256hex;
	private String salt;
	private String key;
	
	public PasswordHasher()
	{
		Config conf = ConfigFactory.load();
		this.key = conf.getString("play.crypto.secret");
	}
	
	public PasswordHasher(String password)
	{
		Config conf = ConfigFactory.load();
		this.key = conf.getString("play.crypto.secret");
		
		Random r = new SecureRandom();
		byte[] saltBytes = new byte[32];
		r.nextBytes(saltBytes);
		this.salt = Base64.encodeBase64String(saltBytes);
		
		this.sha256hex = DigestUtils.sha256Hex(this.key + password + this.salt);
	}

	public String getSha256hex() {
		return sha256hex;
	}

	public String getSalt() {
		return salt;
	}
	
	public Boolean checkPassword(String storedSha256hex, String storedSalt, String candidate)
	{
		if(DigestUtils.sha256Hex(this.key + candidate + storedSalt).equals(storedSha256hex))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
