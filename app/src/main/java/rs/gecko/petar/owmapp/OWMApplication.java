package rs.gecko.petar.owmapp;

import android.app.Application;
import android.content.Context;

/**
 * Created by Petar on 5/25/18.
 */

public class OWMApplication extends Application {

    private static final String TAG = OWMApplication.class.getSimpleName();

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context=getApplicationContext();

        // TEMP: mock some saved locations
//        List<MyLocation> list = new ArrayList<MyLocation>();
//
//        MyLocation ml = new MyLocation();
//        ml.setLocationName("London");
//        Coord coord = new Coord(-0.13,51.51);
//        ml.setLocation(coord);
//
//        list.add(ml);
//
//        ml = new MyLocation();
//        ml.setLocationName("Belgrade");
//        coord = new Coord(44.818798, 20.414320);
//        ml.setLocation(coord);
//
//        list.add(ml);
//
//        ml = new MyLocation();
//        ml.setLocationName("Lisbon");
//        coord = new Coord(38.727009, -9.139275);
//        ml.setLocation(coord);
//
//        list.add(ml);
//
//        Log.d(TAG, "onCreate: Mocked list size = "+ list.size());
//
//        LocalCache.getInstance(this).setMyLocations(list);
    }
}
