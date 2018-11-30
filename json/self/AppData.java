
package de.beas.json.self;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppData {

    @SerializedName("connections")
    @Expose
    private Connections connections;

    public Connections getConnections() {
        return connections;
    }

    public void setConnections(Connections connections) {
        this.connections = connections;
    }

}
