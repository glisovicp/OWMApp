package rs.gecko.petar.owmapp.data;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import rs.gecko.petar.owmapp.models.MyLocation;

/**
 * Created by Petar on 5/24/18.
 */

public class LocalCache {

    // keys:
    public static final String KEY_MYLOCATIONS = "rs.gecko.petar.owmapp.data.KEY_MYLOCATIONS";

    // properties
    private List<MyLocation> myLocations;

    private Application application;
    private static LocalCache instance;
    private final DataCachingManager cachingManager;

    private static final String PREFS_DEFAULT = "rs.gecko.petar.owmapp.data.PREFS_DEFAULT";

    public LocalCache(Application application) {
        this.application = application;
        SharedPreferences sharedPreferences = application.getSharedPreferences(PREFS_DEFAULT, Context.MODE_PRIVATE);
        ObjectMapper mapper = new ObjectMapper();

        cachingManager = new DataCachingManager(sharedPreferences, mapper);
    }

    public static LocalCache getInstance(Application application) {
        if (instance == null) {
            instance = new LocalCache(application);
        }
        return instance;
    }

    public List<MyLocation> getMyLocations() {

        if (myLocations == null) {
            myLocations = cachingManager.get(new TypeReference<List<String>>() {
            }, KEY_MYLOCATIONS);
        }
        return myLocations;
    }

//    public void addMyLocations(List<MyLocation> myLocations) {
//        if (getMyLocations() == null) {
//            this.myLocations = new ArrayList<>();
//
//        }
//        this.myLocations.addAll(myLocations);
//        cachingManager.save(KEY_MYLOCATIONS, this.myLocations);
//    }
//
//    public void removeMyLocations(List<MyLocation> myLocations) {
//        if (getMyLocations() != null) {
//            this.myLocations.removeAll(myLocations);
//            cachingManager.save(KEY_MYLOCATIONS, this.myLocations);
//        }
//    }

    public void setMyLocations(List<MyLocation> myLocations) {

        this.myLocations = myLocations;
        cachingManager.save(KEY_MYLOCATIONS, this.myLocations);
    }
}
