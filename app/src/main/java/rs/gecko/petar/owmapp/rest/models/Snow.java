package rs.gecko.petar.owmapp.rest.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Created by Petar on 5/24/18.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Snow extends BaseModel  implements Serializable  {

    private double snowVolume;

    public double getSnowVolume() {
        return snowVolume;
    }

    public void setSnowVolume(double snowVolume) {
        this.snowVolume = snowVolume;
    }
}
