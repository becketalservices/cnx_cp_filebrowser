package de.beas;

import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;

import de.webfilesys.user.TransientUser;
import de.webfilesys.user.UserMgmtException;
import de.webfilesys.user.XmlUserManager;

public class DefaultValueUserManager extends XmlUserManager {
	private static Logger LOG = Logger.getLogger(DefaultValueUserManager.class);
	
	private static final String DEFAULT_CSS = "fmweb";
	
	private static final String DEFAULT_LANGUAGE = "English";
	
	private static final String DEFAULT_ROLE = "webspace";

	private static final String DEFAULT_DOC_ROOT = "*:";
	
	public DefaultValueUserManager() {
		super();
		LOG.info("Init DummyUserManager");
	}

	public ArrayList<String> getListOfUsers() {
		LOG.debug("getListOfUsers(");
		return super.getListOfUsers();
	}

	public ArrayList<String> getAdminUserEmails() {
		LOG.debug("getAdminUserEmails(");
		return super.getAdminUserEmails();
	}

	public ArrayList<String> getMailAddressesByRole(String receiverRole) {
		LOG.debug("getMailAddressesByRole('"+ receiverRole +"')");
		return super.getMailAddressesByRole(receiverRole);
	}

	public ArrayList<String> getAllMailAddresses() {
		LOG.debug("getAllMailAddresses()");
		return super.getAllMailAddresses();
	}

	public void createUser(TransientUser newUser) throws UserMgmtException {
		LOG.debug("createUser()");
		super.createUser(newUser);
	}

	public void updateUser(TransientUser changedUser) throws UserMgmtException {
		LOG.debug("updateUser()");
		if (super.userExists(changedUser.getUserid())) 
			super.updateUser(changedUser);
	}

	public TransientUser getUser(String userId) {
		LOG.debug("getUser('" + userId + "')");
		TransientUser user = super.getUser(userId);
		if (user == null)
			user = buildgetTransientUser(userId);
		return user;
	}

	public boolean addUser(String userId) {
		LOG.debug("addUser('" + userId + "')");
		return super.addUser(userId);
	}

	public boolean removeUser(String userId) {
		LOG.debug("removeUser('" + userId + "')");
		return super.removeUser(userId);
	}

	public boolean userExists(String userId) {
		LOG.debug("userExists('" + userId + "')");
		return super.userExists(userId);
	}

	public String createVirtualUser(String realUser, String docRoot, String role, int expDays, String language) {
		LOG.debug("createVirtualUser(...)");
		return super.createVirtualUser(realUser, docRoot, role, expDays, language);
	}

	public String getUserType(String userId) {
		LOG.debug("getUserType('" + userId + "')");
		return super.getUserType(userId);
	}

	public String getFirstName(String userId) {
		LOG.debug("getFirstName('" + userId + "')");
		return super.getFirstName(userId);
	}

	public String getLastName(String userId) {
		LOG.debug("getLastName('" + userId + "')");
		return super.getLastName(userId);
	}

	public String getEmail(String userId) {
		LOG.debug("getEmail('" + userId + "')");
		return super.getEmail(userId);
	}

	public long getDiskQuota(String userId) {
		LOG.debug("getDiskQuota('" + userId + "')");
		return super.getDiskQuota(userId);
	}

	public int getPageSize(String userId) {
		LOG.debug("getPageSize('" + userId + "')");
		return super.getPageSize(userId);
	}

	public void setPageSize(String userId, int newValue) {
		LOG.debug("setPageSize(...)");
		super.setPageSize(userId, newValue);
	}

	public Date getLastLoginTime(String userId) {
		LOG.debug("getAdminUserEmails('(" + userId + "')");
		return super.getLastLoginTime(userId);
	}

	public void setLastLoginTime(String userId, Date newValue) {
		LOG.debug("setLastLoginTime(...)");
		super.setLastLoginTime(userId, newValue);
	}

	public String getPhone(String userId) {
		LOG.debug("getPhone('" + userId + "')");
		return super.getPhone(userId);
	}

	public String getLanguage(String userId) {
		LOG.debug("getLanguage('" + userId + "')");
		String lang = super.getLanguage(userId);
		if (lang == null || lang.length() == 0)
			return DEFAULT_LANGUAGE;
		return lang;
	}

	public String getRole(String userId) {
		LOG.debug("getRole('" + userId + "')");
		String role= super.getRole(userId);
		if (role == null || role.length() == 0) 
			return DEFAULT_ROLE;
		return role;
	}

	public boolean isReadonly(String userId) {
		LOG.debug("isReadonly('" + userId + "')");
		return super.isReadonly(userId);
	}

	public String getDocumentRoot(String userId) {
		LOG.debug("getDocumentRoot('" + userId + "')");
		LOG.debug(String.format("UserExist: %b", userExists(userId)));
		String docRoot = super.getDocumentRoot(userId);
		if (docRoot == null || docRoot.length() == 0 || !userExists(userId))
			docRoot = DEFAULT_DOC_ROOT;
		LOG.debug("Doc Root: " + docRoot);
		return docRoot;
	}

	public String getLowerCaseDocRoot(String userId) {
		LOG.debug("getLowerCaseDocRoot('" + userId + "')");
		LOG.debug(String.format("UserExist: %b", userExists(userId)));
		String docRoot = super.getLowerCaseDocRoot(userId);
		if (docRoot == null || docRoot.length() == 0 || !userExists(userId))
			docRoot = DEFAULT_DOC_ROOT.toLowerCase();
		LOG.debug("Doc Root: " + docRoot);
		return docRoot;
	}

	public String normalizeDocRoot(String documentRoot) {
		LOG.debug("normalizeDocRoot('" + documentRoot + "')");
		return super.normalizeDocRoot(documentRoot);
	}

	public void setPassword(String userId, String newPassword) {
		LOG.debug("setPassword(...)");
		super.setPassword(userId, newPassword);
	}

	public boolean checkPassword(String userId, String password) {
		LOG.debug("checkPassword(..)");
		return super.checkPassword(userId, password);
	}

	public boolean checkReadonlyPassword(String userId, String password) {
		LOG.debug("checkReadonlyPassword(...)");
		return super.checkReadonlyPassword(userId, password);
	}

	public void setReadonlyPassword(String userId, String newPassword) {
		LOG.debug("setReadonlyPassword(...)");
		super.setReadonlyPassword(userId, newPassword);
	}

	public String getCSS(String userId) {
		LOG.debug("getCSS('" + userId + "')");
		String css = super.getCSS(userId);
		if ( css == null || css.length() == 0) {
			return DEFAULT_CSS;
		}
		return css;
	}

	public ArrayList<TransientUser> getRealUsers() {
		LOG.debug("getRealUsers()");
		return super.getRealUsers();
	}

	public boolean isReadyForShutdown() {
		LOG.debug("isReadyForShutdown()");
		return super.isReadyForShutdown();
	}

	public void activateUser(String activationCode) throws UserMgmtException {
		LOG.debug("activateUser(..)");
		super.activateUser(activationCode);
	}
	
	protected TransientUser buildgetTransientUser(String userId) {
		TransientUser user = new TransientUser();

        user.setUserid(userId);
        
        user.setCss(DEFAULT_CSS);
        
        user.setLanguage(DEFAULT_LANGUAGE);

        user.setDocumentRoot(DEFAULT_DOC_ROOT);
        
        user.setRole(DEFAULT_ROLE);
        
        return user;
	}
}
