
package de.beas.json.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Email {

    @SerializedName("internet")
    @Expose
    private String internet;
    @SerializedName("X_notes")
    @Expose
    private String xNotes;

    public String getInternet() {
        return internet;
    }

    public void setInternet(String internet) {
        this.internet = internet;
    }

    public String getXNotes() {
        return xNotes;
    }

    public void setXNotes(String xNotes) {
        this.xNotes = xNotes;
    }

}
