
package de.beas.json.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class XBizCardLocation {

    @SerializedName("unsecure")
    @Expose
    private String unsecure;
    @SerializedName("secure")
    @Expose
    private String secure;

    public String getUnsecure() {
        return unsecure;
    }

    public void setUnsecure(String unsecure) {
        this.unsecure = unsecure;
    }

    public String getSecure() {
        return secure;
    }

    public void setSecure(String secure) {
        this.secure = secure;
    }

}
