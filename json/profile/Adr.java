
package de.beas.json.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Adr {

    @SerializedName("work")
    @Expose
    private Work work;

    public Work getWork() {
        return work;
    }

    public void setWork(Work work) {
        this.work = work;
    }

}
