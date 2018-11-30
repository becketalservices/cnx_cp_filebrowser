
package de.beas.json.self;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Connections {

    @SerializedName("organizationId")
    @Expose
    private String organizationId;
    @SerializedName("isExternal")
    @Expose
    private String isExternal;

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getIsExternal() {
        return isExternal;
    }

    public void setIsExternal(String isExternal) {
        this.isExternal = isExternal;
    }

}
