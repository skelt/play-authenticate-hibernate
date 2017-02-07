package titanium;

import jackson.titanium.CreateUserResponse;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

import play.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import constants.ScetConstants;

public class TitaniumCloud {
	
	private Config conf;
	private String tiKey;
	private String tiPushAdminUser;
	private String tiPushAdminUserPassword;
	
	public TitaniumCloud() {
		conf = ConfigFactory.load();
		tiKey = conf.getString("titanium.apiKey");
		tiPushAdminUser = conf.getString("titanium.tiPushAdminUser");
		tiPushAdminUserPassword = conf.getString("titanium.tiPushAdminUserPassword");
	}
	
	public boolean subscribePush(String tiUserId, String channel, String deviceToken, String os){

		CreateUserResponse admin = null;

		try {
			admin = this.loginAdmin(tiPushAdminUser, tiPushAdminUserPassword);
		} catch (Exception e) {
			Logger.error("Error logging in titanium admin, check config file and titanium dashboard for correct credentials ", e);
		}

		String sessionId = admin.getMeta().getSession_id();

		String url = "https://api.cloud.appcelerator.com/v1/push_notification/subscribe.json?key=" + tiKey + "&_session_id=" + sessionId;
		
		String urlParameters = null;
		
		try {
			urlParameters = "channel=" + URLEncoder.encode(channel, "UTF-8")
					+ "&device_token=" + URLEncoder.encode(deviceToken, "UTF-8")
					+ "&type=" + URLEncoder.encode(os, "UTF-8") + "&su_id="
					+ URLEncoder.encode(tiUserId, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			Logger.error("Titanium cloud could not encode url", e);
		}
		
		Response response = null;
		
		try {
			response = this.httpRequest(url, "POST", urlParameters);
		} catch (MalformedURLException e) {
			Logger.error("Titanium cloud could not encode url", e);
		} catch (IOException e) {
			Logger.error("Titanium cloud could not subscribe to push ", e);
		}
		
		if (response.getResponseCode() == 200) {
			return true;
		} else {
			return false;
		}
		
	}
	
	public boolean unsubscibePush(String tiUserId, String channel, String deviceToken){

		CreateUserResponse admin = null;

		try {
			admin = this.loginAdmin(tiPushAdminUser, tiPushAdminUserPassword);
		} catch (Exception e) {
			Logger.error("Error logging in titanium admin, check config file and titanium dashboard for correct credentials ", e);
		}

		String sessionId = admin.getMeta().getSession_id();

		String url = "https://api.cloud.appcelerator.com/v1/push_notification/unsubscribe.json?key="
				+ tiKey
				+ "&_session_id="
				+ sessionId
				+ "&channel="
				+ channel
				+ "&device_token=" + deviceToken + "&su_id=" + tiUserId;
		
		Response response = null;
		
		try {
			response = this.httpRequest(url, ScetConstants.DELETE, null);
		} catch (MalformedURLException e) {
			Logger.error("Titanium cloud could not encode url", e);
		} catch (IOException e) {
			Logger.error("Titanium cloud could not unsubscribe to push ", e);
		}

		if (response.getResponseCode() == 200) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean sendPush(String channel, String payload){
		
		CreateUserResponse admin = null;
		
		try {
			admin = this.loginAdmin(tiPushAdminUser, tiPushAdminUserPassword);
		} catch (Exception e) {
			Logger.error("Titanium login failed for admin - check config file", e);
		}

		String sessionId = admin.getMeta().getSession_id();

		String url = "https://api.cloud.appcelerator.com/v1/push_notification/notify_tokens.json?key=" + tiKey + "&_session_id=" + sessionId;
		String formParameters = null;
		
		try {
			formParameters = "channel=" + URLEncoder.encode(channel, "UTF-8")
					+ "&payload=" + URLEncoder.encode(payload, "UTF-8")
					+ "&to_tokens=everyone";
		} catch (UnsupportedEncodingException e1) {
			Logger.error("Titanium cloud could not encode url", e1);
		}
		
		Response response = null;
		
		try {
			response = this.httpRequest(url, ScetConstants.POST, formParameters);
		} catch (MalformedURLException e) {
			Logger.error("Titanium cloud could not encode url", e);
		} catch (IOException e) {
			Logger.error("Titanium cloud could not send to push ", e);
		}
		
		if (response != null && response.getResponseCode() == 200) {
			return true;
		} else {
			return false;
		}
		
	}
	
	public CreateUserResponse createTiUser(String email, String firstName, String lastName, String password) {

		Config conf = ConfigFactory.load();
		String tiKey = conf.getString("titanium.apiKey");

		String url = "https://api.cloud.appcelerator.com/v1/users/create.json?key="
				+ tiKey;
		String urlParameters = null;
		
		try {
			urlParameters = "email=" + URLEncoder.encode(email, "UTF-8")
					+ "&first_name=" + URLEncoder.encode(firstName, "UTF-8")
					+ "&last_name=" + URLEncoder.encode(lastName, "UTF-8")
					+ "&password=" + URLEncoder.encode(password, "UTF-8")
					+ "&password_confirmation="
					+ URLEncoder.encode(password, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			Logger.error("Titanium cloud could not encode url ", e);
		}
		
		Response response = null;
		
		try {
			response = this.httpRequest(url, ScetConstants.POST, urlParameters);
		} catch (MalformedURLException e) {
			Logger.error("Titanium cloud could not encode url", e);
		} catch (IOException e) {
			Logger.error("Titanium cloud could not create user ", e);
		}

		ObjectMapper mapper = new ObjectMapper();
		CreateUserResponse createResponse = null;
		
		try {
			createResponse = mapper.readValue(response.getResponseBody(), CreateUserResponse.class);
		} catch (JsonParseException e) {
			Logger.error("Titanium cloud could not parse json from create user ", e);
		} catch (JsonMappingException e) {
			Logger.error("Titanium cloud could not parse map json from create user ", e);
		} catch (IOException e) {
			Logger.error("Titanium cloud could not create user ", e);
		}

		return createResponse;

	}
	
	public CreateUserResponse loginAdmin(String userNameEmail, String password) {

		String url = "https://api.cloud.appcelerator.com/v1/users/login.json?key=" + tiKey;
		
		String formParameters = null;
		
		try {
			formParameters = "login=" + URLEncoder.encode(userNameEmail, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			Logger.error("Titanium cloud could not encode url", e1);
		}
		
		Response response = null;
		
		try {
			response = this.httpRequest(url, ScetConstants.POST, formParameters);
		} catch (IOException e) {
			Logger.error("Titanium cloud could not login admin ", e);
		}

		// print result
		Logger.info(response.getResponseBody());

		ObjectMapper mapper = new ObjectMapper();
		CreateUserResponse createResponse = null;
		try {
			createResponse = mapper.readValue(response.getResponseBody(), CreateUserResponse.class);
		} catch (JsonParseException e) {
			Logger.error("Titanium cloud could not parse json from login user ", e);
		} catch (JsonMappingException e) {
			Logger.error("Titanium cloud could not parse map json from login user ", e);
		} catch (IOException e) {
			Logger.error("Titanium cloud could not login user ", e);
		}

		return createResponse;
	}
	
	private Response httpRequest(String url, String method, String parameters) throws MalformedURLException, IOException
	{
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		// add reuqest header
		con.setRequestMethod(method);
		con.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");

		if(ScetConstants.POST.equals(method))
		{
			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(parameters);
			wr.flush();
			wr.close();
		}

		int responseCode = con.getResponseCode();
		Logger.info("\nSending '" + method + "' request to URL : " + url);
		Logger.info("Post parameters : " + parameters);
		Logger.info("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuilder response = new StringBuilder();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		return new Response(responseCode, response.toString());
	}

}
