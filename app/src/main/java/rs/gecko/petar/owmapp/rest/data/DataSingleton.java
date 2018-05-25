package rs.gecko.petar.owmapp.rest.data;

/**
 * Created by Petar on 5/25/18.
 */

public class DataSingleton {

    private static DataSingleton instance;
    private ServerAccessData serverAccessData;


    public ServerAccessData getServerAccessData() {
        return serverAccessData;
    }

    private DataSingleton() {
        serverAccessData= new ServerAccessData();
    }

    public static DataSingleton getInstance() {
        if (instance == null) {
            instance = new DataSingleton();
        }
        return instance;
    }

}
