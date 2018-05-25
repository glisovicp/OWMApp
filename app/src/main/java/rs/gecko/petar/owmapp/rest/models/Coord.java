package rs.gecko.petar.owmapp.rest.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Created by Petar on 5/24/18.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Coord extends BaseModel implements Serializable {

    public double lon;
    public double lat;

    public Coord(){

    }

    public Coord(double lon, double lat) {
        this.lon = lon;
        this.lat = lat;
    }
}
