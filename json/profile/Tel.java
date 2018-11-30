
package de.beas.json.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tel {

    @SerializedName("work")
    @Expose
    private String work;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("fax")
    @Expose
    private String fax;

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

}
