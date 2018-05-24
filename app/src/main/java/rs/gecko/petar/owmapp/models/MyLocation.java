package rs.gecko.petar.owmapp.models;

import java.io.Serializable;

import rs.gecko.petar.owmapp.rest.models.Coord;

/**
 * Created by Petar on 5/24/18.
 */

public class MyLocation implements Serializable {

    private String locationName;
    private Coord location;

    public MyLocation() {
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Coord getLocation() {
        return location;
    }

    public void setLocation(Coord location) {
        this.location = location;
    }
}
