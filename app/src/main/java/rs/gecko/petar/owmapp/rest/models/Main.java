package rs.gecko.petar.owmapp.rest.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Created by Petar on 5/24/18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Main extends BaseModel  implements Serializable {

    public double temp;
    public double pressure;
    public long humidity;
    public double temp_min;
    public double temp_max;
    public double sea_level;
    public double grnd_level;

    public Main() {
    }

    public Main(double temp, double pressure, long humidity, double temp_min, double temp_max, double sea_level, double grnd_level) {
        this.temp = temp;
        this.pressure = pressure;
        this.humidity = humidity;
        this.temp_min = temp_min;
        this.temp_max = temp_max;
        this.sea_level = sea_level;
        this.grnd_level = grnd_level;
    }


}
