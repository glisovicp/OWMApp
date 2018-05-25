package rs.gecko.petar.owmapp.rest.data;

/**
 * Created by Petar on 5/25/18.
 */

public class ServerAccessData {

    //http://api.openweathermap.org/data/2.5/weather?lat=35&lon=139&appid=b92b3af10d8b98fd09fd3780a0d0d1df&units=metric

    private static final String SERVER_URL = "http://api.openweathermap.org";
    private static final String API_EXTENSION = "data";
    private static final String API_VERSION = "2.5";

    private String serverBaseUrl;

    public String getServerBaseUrlWithApiVersion() {
        return serverBaseUrl + "/" + API_VERSION;
    }

    public String getServerBaseUrlWithApiVersionAndExtension(){
        return serverBaseUrl + "/" + API_EXTENSION +"/" +API_VERSION;
    }

    public ServerAccessData() {
        this.serverBaseUrl = SERVER_URL;
    }

    public String getServerBaseUrl() {
        return serverBaseUrl;
    }

    public void setServerBaseUrl(String serverBaseUrl) {
        this.serverBaseUrl = serverBaseUrl;
    }
}
