
package de.beas.json.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class XBizCardAction {

    @SerializedName("urlPattern")
    @Expose
    private String urlPattern;
    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("liClass")
    @Expose
    private String liClass;
    @SerializedName("icon")
    @Expose
    private Icon icon;

    public String getUrlPattern() {
        return urlPattern;
    }

    public void setUrlPattern(String urlPattern) {
        this.urlPattern = urlPattern;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLiClass() {
        return liClass;
    }

    public void setLiClass(String liClass) {
        this.liClass = liClass;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

}
