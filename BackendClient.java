package de.beas;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import de.beas.json.self.SelfEntry;
import de.beas.json.profile.Profile;

public class BackendClient {
	private static Logger LOG = Logger.getLogger(BackendClient.class);

	private String backendServerURL;

	private static String ADMIN_URL = "/connections/resources/web/user/roles?role=admin";
	private static String USER_SELF_URL = "/connections/opensocial/rest/people/@me/@self";
	private static String USER_PROFILE_URL = "/profiles/json/profile.do?format=compact&userid=%s";

	private CookieStore cookieStore;
	private CloseableHttpClient httpclient;

	public BackendClient(String backendServerURL, String cookieDomain, Cookie ltpaToken, Cookie ltpaToken2) {
		cookieStore = new BasicCookieStore();
		if (ltpaToken != null) {
			BasicClientCookie ltpaCookie = new BasicClientCookie(ltpaToken.getName(), ltpaToken.getValue());
			ltpaCookie.setDomain(cookieDomain);
			ltpaCookie.setAttribute(ClientCookie.DOMAIN_ATTR, "true");
			ltpaCookie.setPath("/");
			//ltpaCookie.setExpiryDate(ltpaToken.getMaxAge());
			cookieStore.addCookie(ltpaCookie);
		}
		if (ltpaToken2 != null) {
			BasicClientCookie ltpaCookie2 = new BasicClientCookie(ltpaToken2.getName(), ltpaToken2.getValue());
			ltpaCookie2.setDomain(cookieDomain);
			ltpaCookie2.setAttribute(ClientCookie.DOMAIN_ATTR, "true");
			ltpaCookie2.setPath("/");
			//ltpaCookie.setExpiryDate(ltpaToken.getMaxAge());
			cookieStore.addCookie(ltpaCookie2);
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("Contents of Cookie store");
			List<org.apache.http.cookie.Cookie> cookies = cookieStore.getCookies();
			for(org.apache.http.cookie.Cookie cookie : cookies) {
				LOG.debug("Cookie: " + cookie.toString());
			}
		}

		httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();

		this.backendServerURL = backendServerURL;

	}
	/**
	 *  Do a request to the backend to check if the user has the admin role
	 * @param ltpaToken
	 * @param ltpaToken2
	 * @return true in case user has admin role.
	 */
	public boolean isAdmin() {
		boolean isAdmin = false;
		CloseableHttpResponse response1 = null;
		try {
			HttpGet httpGet = new HttpGet(backendServerURL + ADMIN_URL);
			response1 = httpclient.execute(httpGet);
			LOG.debug("ADMIN FETCH: " + response1.getStatusLine());

			HttpEntity entity1 = response1.getEntity();

			if (response1.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				Gson gson = new Gson();
				Type type = new TypeToken<Map<String, String>>(){}.getType();
				Map<String, String> resultMap = gson.fromJson(new InputStreamReader(entity1.getContent()), type);
				if (LOG.isDebugEnabled()) {
					LOG.debug("Result Map:");
					for (Map.Entry<String, String> entry : resultMap.entrySet())
					{
						LOG.debug("  " + entry.getKey() + "=" + entry.getValue());
					}
				}
				if (resultMap.containsKey("admin")) {
					if ("true".equalsIgnoreCase(resultMap.get("admin"))) {
						isAdmin = true;
					}
				}
			}
			EntityUtils.consume(entity1);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (response1 != null)
				try {
					response1.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return isAdmin;
	}

	/**
	 * Do a request to the backend to get the user id
	 * @return userid
	 */
	public String getUserID() {
		// 1. Do a request to the self URL to get the profile ID.
		// 2. Do a request to the profile URL to get the uid.
		
		String userid = null;
		String profileId = getProfileID();
		if (profileId != null && profileId.length() > 0) {
			// Extract ID
			try {
				String id = profileId.substring("urn:lsid:lconn.ibm.com:profiles.person:".length());
				if (id.length() > 0) {
					userid = getUID(id);
				}
			} catch (Exception e) {
				// Nothing to do here.
			}
		}
		return userid;
	}
	
	/**
	 * Do a request to the backend to get the user profile id
	 * @return profileid
	 */
	private String getProfileID() {
		// 1. Do a request to the self URL to get the profile ID.
		// 2. Do a request to the profile URL to get the uid.

		String userid = null;

		CloseableHttpResponse response1 = null;
		try {
			HttpGet httpGet = new HttpGet(backendServerURL + USER_SELF_URL);
			response1 = httpclient.execute(httpGet);
			LOG.debug("SELF_URL FETCH: " + response1.getStatusLine());

			HttpEntity entity1 = response1.getEntity();
			
			if (response1.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				/*
				BufferedReader reader = new BufferedReader(new InputStreamReader(entity1.getContent()));
				String line = null;
				while ((line=reader.readLine())!=null) {
					LOG.debug(line);
				} */
				Gson gson = new Gson();
				SelfEntry selfEntry = gson.fromJson(new InputStreamReader(entity1.getContent()), SelfEntry.class);
				if (LOG.isDebugEnabled() && selfEntry != null && selfEntry.getEntry() != null) {
					LOG.debug("Result SelfEntry:");
					LOG.debug("  " + selfEntry.getEntry().getDisplayName());
					LOG.debug("  " + selfEntry.getEntry().getId());
				}
				if (selfEntry != null && selfEntry.getEntry() != null && selfEntry.getEntry().getId() != null && selfEntry.getEntry().getId().length() > 0)
					userid = selfEntry.getEntry().getId();
			}
			EntityUtils.consume(entity1);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (response1 != null)
				try {
					response1.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return userid;
	}

	/**
	 * Do a request to the backend to get the user profile id
	 * @return profileid
	 */
	private String getUID(String profileId) {

		String userid = null;

		CloseableHttpResponse response1 = null;
		try {
			HttpGet httpGet = new HttpGet(backendServerURL + String.format(USER_PROFILE_URL, profileId));
			response1 = httpclient.execute(httpGet);
			LOG.debug("PROFILE_URL FETCH: " + response1.getStatusLine());

			HttpEntity entity1 = response1.getEntity();
			
			if (response1.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				/*
				BufferedReader reader = new BufferedReader(new InputStreamReader(entity1.getContent()));
				String line = null;
				while ((line=reader.readLine())!=null) {
					LOG.debug(line);
				} */
				
				Gson gson = new Gson();
				Profile profile = gson.fromJson(new InputStreamReader(entity1.getContent()), Profile.class);
				if (LOG.isDebugEnabled() && profile != null && profile.getUid() != null) {
					LOG.debug("Result Profile:");
					LOG.debug("  " + profile.getKey());
					LOG.debug("  " + profile.getUid());
					LOG.debug("  " + profile.getFn());
				}
				if (profile != null && profile.getUid() != null && profile.getUid().length() > 0)
					userid = profile.getUid();
			}
			EntityUtils.consume(entity1);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (response1 != null)
				try {
					response1.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return userid;
	}
}
