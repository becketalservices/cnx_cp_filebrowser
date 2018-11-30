
package de.beas.json.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Icon {

    @SerializedName("href")
    @Expose
    private String href;
    @SerializedName("alt")
    @Expose
    private String alt;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

}
