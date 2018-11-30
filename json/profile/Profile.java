
package de.beas.json.profile;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Profile {

    @SerializedName("X_lconn_userid")
    @Expose
    private String xLconnUserid;
    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("fn")
    @Expose
    private String fn;
    @SerializedName("dn")
    @Expose
    private String dn;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("X_bizCardShowPhoto")
    @Expose
    private Boolean xBizCardShowPhoto;
    @SerializedName("X_timezoneOffset")
    @Expose
    private String xTimezoneOffset;
    @SerializedName("X_isExternal")
    @Expose
    private String xIsExternal;
    @SerializedName("X_bizCardMainHtml")
    @Expose
    private String xBizCardMainHtml;
    @SerializedName("X_bizCardSTAwareness")
    @Expose
    private Boolean xBizCardSTAwareness;
    @SerializedName("X_bizCardSecureSTAwareness")
    @Expose
    private Boolean xBizCardSecureSTAwareness;
    @SerializedName("X_bizCardLocation")
    @Expose
    private XBizCardLocation xBizCardLocation;
    @SerializedName("X_bizCardSTInputType")
    @Expose
    private String xBizCardSTInputType;
    @SerializedName("X_bizCardSTStatusMsg")
    @Expose
    private Boolean xBizCardSTStatusMsg;
    @SerializedName("X_stLinks")
    @Expose
    private String xStLinks;
    @SerializedName("X_STChatAction")
    @Expose
    private Boolean xSTChatAction;
    @SerializedName("X_STCallAction")
    @Expose
    private Boolean xSTCallAction;
    @SerializedName("X_bizCardServiceLinks")
    @Expose
    private List<Object> xBizCardServiceLinks = null;
    @SerializedName("X_allowEvalLabel")
    @Expose
    private Boolean xAllowEvalLabel;
    @SerializedName("X_loggedInUserId")
    @Expose
    private String xLoggedInUserId;
    @SerializedName("X_loggedInUserKey")
    @Expose
    private String xLoggedInUserKey;
    @SerializedName("X_loggedInUserDn")
    @Expose
    private String xLoggedInUserDn;
    @SerializedName("X_bizCardActions")
    @Expose
    private List<XBizCardAction> xBizCardActions = null;
    @SerializedName("X_extension_attrs")
    @Expose
    private XExtensionAttrs xExtensionAttrs;
    @SerializedName("X_inDirectory")
    @Expose
    private String xInDirectory;
    @SerializedName("X_isActiveUser")
    @Expose
    private String xIsActiveUser;
    @SerializedName("adr")
    @Expose
    private Adr adr;
    @SerializedName("tel")
    @Expose
    private Tel tel;
    @SerializedName("email")
    @Expose
    private Email email;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("employeeTypeDesc")
    @Expose
    private String employeeTypeDesc;
    @SerializedName("org")
    @Expose
    private String org;
    @SerializedName("X_blogUrl")
    @Expose
    private String xBlogUrl;
    @SerializedName("X_building_name")
    @Expose
    private String xBuildingName;
    @SerializedName("X_building_floor")
    @Expose
    private String xBuildingFloor;
    @SerializedName("X_office")
    @Expose
    private String xOffice;
    @SerializedName("X_isFollowed")
    @Expose
    private String xIsFollowed;
    @SerializedName("X_isFollowedEnabled")
    @Expose
    private String xIsFollowedEnabled;
    @SerializedName("mcode")
    @Expose
    private String mcode;

    public String getXLconnUserid() {
        return xLconnUserid;
    }

    public void setXLconnUserid(String xLconnUserid) {
        this.xLconnUserid = xLconnUserid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFn() {
        return fn;
    }

    public void setFn(String fn) {
        this.fn = fn;
    }

    public String getDn() {
        return dn;
    }

    public void setDn(String dn) {
        this.dn = dn;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Boolean getXBizCardShowPhoto() {
        return xBizCardShowPhoto;
    }

    public void setXBizCardShowPhoto(Boolean xBizCardShowPhoto) {
        this.xBizCardShowPhoto = xBizCardShowPhoto;
    }

    public String getXTimezoneOffset() {
        return xTimezoneOffset;
    }

    public void setXTimezoneOffset(String xTimezoneOffset) {
        this.xTimezoneOffset = xTimezoneOffset;
    }

    public String getXIsExternal() {
        return xIsExternal;
    }

    public void setXIsExternal(String xIsExternal) {
        this.xIsExternal = xIsExternal;
    }

    public String getXBizCardMainHtml() {
        return xBizCardMainHtml;
    }

    public void setXBizCardMainHtml(String xBizCardMainHtml) {
        this.xBizCardMainHtml = xBizCardMainHtml;
    }

    public Boolean getXBizCardSTAwareness() {
        return xBizCardSTAwareness;
    }

    public void setXBizCardSTAwareness(Boolean xBizCardSTAwareness) {
        this.xBizCardSTAwareness = xBizCardSTAwareness;
    }

    public Boolean getXBizCardSecureSTAwareness() {
        return xBizCardSecureSTAwareness;
    }

    public void setXBizCardSecureSTAwareness(Boolean xBizCardSecureSTAwareness) {
        this.xBizCardSecureSTAwareness = xBizCardSecureSTAwareness;
    }

    public XBizCardLocation getXBizCardLocation() {
        return xBizCardLocation;
    }

    public void setXBizCardLocation(XBizCardLocation xBizCardLocation) {
        this.xBizCardLocation = xBizCardLocation;
    }

    public String getXBizCardSTInputType() {
        return xBizCardSTInputType;
    }

    public void setXBizCardSTInputType(String xBizCardSTInputType) {
        this.xBizCardSTInputType = xBizCardSTInputType;
    }

    public Boolean getXBizCardSTStatusMsg() {
        return xBizCardSTStatusMsg;
    }

    public void setXBizCardSTStatusMsg(Boolean xBizCardSTStatusMsg) {
        this.xBizCardSTStatusMsg = xBizCardSTStatusMsg;
    }

    public String getXStLinks() {
        return xStLinks;
    }

    public void setXStLinks(String xStLinks) {
        this.xStLinks = xStLinks;
    }

    public Boolean getXSTChatAction() {
        return xSTChatAction;
    }

    public void setXSTChatAction(Boolean xSTChatAction) {
        this.xSTChatAction = xSTChatAction;
    }

    public Boolean getXSTCallAction() {
        return xSTCallAction;
    }

    public void setXSTCallAction(Boolean xSTCallAction) {
        this.xSTCallAction = xSTCallAction;
    }

    public List<Object> getXBizCardServiceLinks() {
        return xBizCardServiceLinks;
    }

    public void setXBizCardServiceLinks(List<Object> xBizCardServiceLinks) {
        this.xBizCardServiceLinks = xBizCardServiceLinks;
    }

    public Boolean getXAllowEvalLabel() {
        return xAllowEvalLabel;
    }

    public void setXAllowEvalLabel(Boolean xAllowEvalLabel) {
        this.xAllowEvalLabel = xAllowEvalLabel;
    }

    public String getXLoggedInUserId() {
        return xLoggedInUserId;
    }

    public void setXLoggedInUserId(String xLoggedInUserId) {
        this.xLoggedInUserId = xLoggedInUserId;
    }

    public String getXLoggedInUserKey() {
        return xLoggedInUserKey;
    }

    public void setXLoggedInUserKey(String xLoggedInUserKey) {
        this.xLoggedInUserKey = xLoggedInUserKey;
    }

    public String getXLoggedInUserDn() {
        return xLoggedInUserDn;
    }

    public void setXLoggedInUserDn(String xLoggedInUserDn) {
        this.xLoggedInUserDn = xLoggedInUserDn;
    }

    public List<XBizCardAction> getXBizCardActions() {
        return xBizCardActions;
    }

    public void setXBizCardActions(List<XBizCardAction> xBizCardActions) {
        this.xBizCardActions = xBizCardActions;
    }

    public XExtensionAttrs getXExtensionAttrs() {
        return xExtensionAttrs;
    }

    public void setXExtensionAttrs(XExtensionAttrs xExtensionAttrs) {
        this.xExtensionAttrs = xExtensionAttrs;
    }

    public String getXInDirectory() {
        return xInDirectory;
    }

    public void setXInDirectory(String xInDirectory) {
        this.xInDirectory = xInDirectory;
    }

    public String getXIsActiveUser() {
        return xIsActiveUser;
    }

    public void setXIsActiveUser(String xIsActiveUser) {
        this.xIsActiveUser = xIsActiveUser;
    }

    public Adr getAdr() {
        return adr;
    }

    public void setAdr(Adr adr) {
        this.adr = adr;
    }

    public Tel getTel() {
        return tel;
    }

    public void setTel(Tel tel) {
        this.tel = tel;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmployeeTypeDesc() {
        return employeeTypeDesc;
    }

    public void setEmployeeTypeDesc(String employeeTypeDesc) {
        this.employeeTypeDesc = employeeTypeDesc;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getXBlogUrl() {
        return xBlogUrl;
    }

    public void setXBlogUrl(String xBlogUrl) {
        this.xBlogUrl = xBlogUrl;
    }

    public String getXBuildingName() {
        return xBuildingName;
    }

    public void setXBuildingName(String xBuildingName) {
        this.xBuildingName = xBuildingName;
    }

    public String getXBuildingFloor() {
        return xBuildingFloor;
    }

    public void setXBuildingFloor(String xBuildingFloor) {
        this.xBuildingFloor = xBuildingFloor;
    }

    public String getXOffice() {
        return xOffice;
    }

    public void setXOffice(String xOffice) {
        this.xOffice = xOffice;
    }

    public String getXIsFollowed() {
        return xIsFollowed;
    }

    public void setXIsFollowed(String xIsFollowed) {
        this.xIsFollowed = xIsFollowed;
    }

    public String getXIsFollowedEnabled() {
        return xIsFollowedEnabled;
    }

    public void setXIsFollowedEnabled(String xIsFollowedEnabled) {
        this.xIsFollowedEnabled = xIsFollowedEnabled;
    }

    public String getMcode() {
        return mcode;
    }

    public void setMcode(String mcode) {
        this.mcode = mcode;
    }

}
