
package de.beas.json.self;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SelfEntry {

    @SerializedName("entry")
    @Expose
    private Entry entry;

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

}
