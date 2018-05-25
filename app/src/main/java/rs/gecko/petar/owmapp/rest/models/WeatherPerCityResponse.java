package rs.gecko.petar.owmapp.rest.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Petar on 5/24/18.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WeatherPerCityResponse extends BaseModel implements Serializable {

    public Coord coord;
    public List<Weather> weather;
    public String base;
    public Main main;
    public Wind wind;
    public Rain rain;
    public Snow snow;
    public Clouds clouds;
    public long dt;
    public Sys sys;
    public long id;
    public String name;
    public long cod;
    public long visibility;

}
