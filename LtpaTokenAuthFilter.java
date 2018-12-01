
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

	private static String AUTH_URL = "%s/login";

	private boolean initOK = false;

	private String icHomepageURL;
	private String icConnectionsURL;
	private String icProfilesURL;
	private String icHost;
	
	
	public void init(FilterConfig fConfig) throws ServletException {
		LOG.debug("RequestLoggingFilter initialized");

		initOK = true;
		try {
			icHomepageURL = System.getProperty("de.beas.LtpaTokenAuthFilter.homepage");
			if (icHomepageURL != null) {
				LOG.debug("Homepage URL: " + icHomepageURL);
			} else {
				LOG.error("Property de.beas.LtpaTokenAuthFilter.homepage not set. Authentication not possible.");
				initOK = false;
			}
			icConnectionsURL = System.getProperty("de.beas.LtpaTokenAuthFilter.connections");
			if (icConnectionsURL != null) {
				LOG.debug("Connections URL: " + icConnectionsURL);
			} else {
				LOG.error("Property de.beas.LtpaTokenAuthFilter.connections not set. Authentication not possible.");
				initOK = false;
			}
			icProfilesURL = System.getProperty("de.beas.LtpaTokenAuthFilter.profiles");
			if (icProfilesURL != null) {
				LOG.debug("Profiles URL: " + icProfilesURL);
			} else {
				LOG.error("Property de.beas.LtpaTokenAuthFilter.profiles not set. Authentication not possible.");
				initOK = false;
			}
			icHost = System.getProperty("de.beas.LtpaTokenAuthFilter.host");
			if (icHost != null) {
				LOG.debug("IC Host: " + icHost);
			} else {
				LOG.error("Property de.beas.LtpaTokenAuthFilter.host not set. Authentication not possible.");
				initOK = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			initOK = false;
		}
	}

	@SuppressWarnings("unchecked")
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		Cookie LtpaToken = null;
		Cookie LtpaToken2 = null;
		String notAuthorized = null;

		if (!initOK) {
			LOG.error("LtpaTokenAuthFilter not initialized proprly. Forward traffic without interception.");
			chain.doFilter(request, response);
			return;
		}
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession(false);

		if (LOG.isDebugEnabled())  {
			LOG.debug("URI: " + req.getRequestURI().toString());
			LOG.debug("URL: " + req.getRequestURL().toString());
			LOG.debug("QS:  " + req.getQueryString());
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
					}
					if ("LtpaToken2".equals(cookie.getName())) {
						LtpaToken2 = cookie;
						LOG.debug("LtpaToken2: ");
						LOG.debug("  " + LtpaToken2.getName());
						LOG.debug("  " + LtpaToken2.getValue());
					}
					LOG.debug("RequestLoggingFilter: " + req.getRemoteAddr() + "::Cookie::{"+cookie.getName()+","+cookie.getValue()+"}");
				}
			}

			if (LtpaToken != null || LtpaToken2 != null) {
				BackendClient backendClient = new BackendClient(icConnectionsURL, icProfilesURL, icHost, LtpaToken, LtpaToken2);
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
			String localURL = req.getRequestURL().toString();
			if (req.getQueryString() != null) 
				localURL += "?" + req.getQueryString();
			Cookie cookie = new Cookie("WASReqURL",localURL);
			cookie.setPath("/");
			cookie.setDomain(icHost);
			res.addCookie(cookie);

			LOG.debug("AuthenticationFilter: redirect to authentication page");
			res.sendRedirect(String.format(AUTH_URL, icHomepageURL));
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
