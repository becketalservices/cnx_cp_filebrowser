
package de.beas;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

/**
 * Servlet Filter implementation class RequestLoggingFilter
 */
public class LtpaTokenAuthFilter implements Filter {
	private static Logger LOG = Logger.getLogger(LtpaTokenAuthFilter.class);

	private String localURL = "http://dummy.beaslabs.com:8080/webfilesys/servlet";
	private String authRedirectURL = "https://cnx63a.beaslabs.com/homepage/login";
	//private String isAdminURL = "https://cnx63a.beaslabs.com/connections/resources/web/user/roles?role=admin";
	//private String userSelfURL = "https://cnx63a.beaslabs.com/connections/opensocial/rest/people/@me/@self";
	//private String userProfileURL = "https://cnx63a.beaslabs.com/profiles/json/profile.do?format=compact&userid=E20141C6-87C2-9EDD-C125-8342004AA53F";
	private String backendURL = "https://cnx63a.beaslabs.com";
	private String cookieDomain = "beaslabs.com";

	public void init(FilterConfig fConfig) throws ServletException {
		LOG.debug("RequestLoggingFilter initialized");

		System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
		System.setProperty("javax.net.ssl.trustStore", "C:/data/keystore.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", "changeme");
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
		System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "DEBUG");
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.wire", "ERROR");
	}

	@SuppressWarnings("unchecked")
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		Cookie LtpaToken = null;
		Cookie LtpaToken2 = null;
		String notAuthorized;


		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession(false);


		if (LOG.isDebugEnabled())  {
			Enumeration<String> params = req.getParameterNames();
			while(params.hasMoreElements()){
				String name = params.nextElement();
				String value = req.getParameter(name);
				LOG.debug("RequestLoggingFilter: " + req.getRemoteAddr() + "::Request Params::{"+name+"="+value+"}");
			}
			params = req.getHeaderNames();
			while(params.hasMoreElements()){
				String name = params.nextElement();
				String value = req.getHeader(name);
				LOG.debug("RequestLoggingFilter: " + req.getRemoteAddr() + "::Request Header::{"+name+"="+value+"}");
			}
		}

		// Check the authentication status
		if (LOG.isDebugEnabled()) {
			String uri = req.getRequestURI();
			LOG.debug("AuthenticationFilter: Requested Resource::"+uri);
		}

		String command = req.getParameter("command");

		String userid = null;
		if ( session != null) {
			userid = (String) session.getAttribute("userid");
		}
		/* for login forms, the userid must be null */
		if ("loginForm".equalsIgnoreCase(command) || "login".equalsIgnoreCase(command)) {
			if (userid != null) {
				userid = null;
				if (session != null) 
					session.removeAttribute("userid");
			}
			LOG.debug("Login Forms. Forward");
			chain.doFilter(request, response);
			return;
		}

		/* getResourceBundle is anonymous */
		if ("getResourceBundle".equalsIgnoreCase(command)) {
			LOG.debug("ResourceBundle. Forward");
			chain.doFilter(request, response);
			return;
		}

		/* in case we have no userid but LtpaToken, we use them */
		if (userid == null) {
			/* We need the cookies */
			Cookie[] cookies = req.getCookies();
			if(cookies != null){
				for(Cookie cookie : cookies){
					if ("LtpaToken".equals(cookie.getName())) {
						LtpaToken = cookie;
						LOG.debug("LtpaToken: ");
						LOG.debug("  " + LtpaToken.getName());
						LOG.debug("  " + LtpaToken.getValue());
						LOG.debug("  " + LtpaToken.getDomain());
						LOG.debug("  " + LtpaToken.getPath());
						LOG.debug("  " + String.valueOf(LtpaToken.getMaxAge()));
					}
					if ("LtpaToken2".equals(cookie.getName())) {
						LtpaToken2 = cookie;
						LOG.debug("LtpaToken2: ");
						LOG.debug("  " + LtpaToken2.getName());
						LOG.debug("  " + LtpaToken2.getValue());
						LOG.debug("  " + LtpaToken2.getDomain());
						LOG.debug("  " + LtpaToken2.getPath());
						LOG.debug("  " + String.valueOf(LtpaToken2.getMaxAge()));
					}
					LOG.debug("RequestLoggingFilter: " + req.getRemoteAddr() + "::Cookie::{"+cookie.getName()+","+cookie.getValue()+"}");
				}
			}

			if (LtpaToken != null || LtpaToken2 != null) {
				BackendClient backendClient = new BackendClient(backendURL, cookieDomain, LtpaToken, LtpaToken2);
				if (backendClient.isAdmin()) {
					userid = backendClient.getUserID();
					if ( userid != null && userid.length() > 0) {
						if (session == null) {
							session = req.getSession(true);

							setSessionInfo(req, session);
						}
						session.setAttribute("userid", userid);
						LOG.debug("UserID " + userid + " from LtpaToken. Forward");
						chain.doFilter(request, response);
						return;
					}
					notAuthorized = "Your user ID could not be determined.";
				} else {
					notAuthorized = "You are not authorized to use this service";
				}
				res.sendError(403, notAuthorized);
				return;
			} // LtpaToken != null
		} // userid == null

		/* User is not authenticated */
		if(session == null || userid == null || userid.length() == 0)  {
			LOG.debug("AuthenticationFilter: Unauthorized access request");
			Cookie cookie = new Cookie("WASReqURL",localURL);
			cookie.setPath("/");
			cookie.setDomain(cookieDomain);
			res.addCookie(cookie);

			LOG.debug("AuthenticationFilter: redirect to authentication page");
			res.sendRedirect(authRedirectURL);
			return;
		}
		// user is already authenticated, pass the request along the filter chain
		LOG.debug("AuthenticationFilter: pass the request along the filter chain");
		chain.doFilter(request, response);

	}

	public void destroy() {
		//we can close resources here
	}

	protected void setSessionInfo(HttpServletRequest req, HttpSession session) {
		session.setAttribute("protocol", req.getScheme());

		session.setAttribute("clientAddress", getClientAddress(req));

		String userAgent = req.getHeader("User-Agent");

		if (userAgent != null) {
			session.setAttribute("userAgent", userAgent);
		}
	}

	private String getClientAddress(HttpServletRequest req) {
		String hostIP = req.getRemoteHost();

		if ((hostIP == null) || (hostIP.trim().length() == 0))
		{
			hostIP = req.getRemoteAddr();
		}

		return(hostIP);
	}

}
