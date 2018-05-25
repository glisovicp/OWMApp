package rs.gecko.petar.owmapp.fragments;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

import rs.gecko.petar.owmapp.R;
import rs.gecko.petar.owmapp.activities.MainActivity;
import rs.gecko.petar.owmapp.data.LocalCache;
import rs.gecko.petar.owmapp.models.MyLocation;
import rs.gecko.petar.owmapp.rest.models.Coord;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by Petar on 5/25/18.
 */

public class AddLocationFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = AddLocationFragment.class.getSimpleName();

    private static final float DEFAULTZOOM = 14;

    private EditText nameEt;

    private GoogleMap mMap;
    MapView mMapView;
    View mView;

    double lat;
    double lng;

    List<MyLocation> bookmarkedLocations;
    private FusedLocationProviderClient mFusedLocationClient;

    CameraUpdate update;

    Marker marker;

    public AddLocationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SavedLocationsFragment.
     */
    public static SavedLocationsFragment newInstance() {
        SavedLocationsFragment fragment = new SavedLocationsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView) mView.findViewById(R.id.map);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_map, container, false);

        nameEt = (EditText) mView.findViewById(R.id.nameET);

        getActivity().setTitle(getString(R.string.title_save_location));

        setHasOptionsMenu(true);

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_location, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_location) {
            if (getActivity() instanceof MainActivity) {
                MainActivity main = (MainActivity) getActivity();
                String name = nameEt.getText().toString().trim();
                if (!name.isEmpty()) {
                    hideSoftKeyboard(main.parentLayout);
                    saveLocation(name);

                    //close current fragment
                    main.replaceFragment(SavedLocationsFragment.class);
                    main.showFab(true);


                } else {
                    Snackbar.make(main.parentLayout, this.getString(R.string.empty_name), Snackbar.LENGTH_LONG)
                            .show();
                }
            }
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        MapsInitializer.initialize(getContext());

        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng ll) {

                lat = ll.latitude;
                lng = ll.longitude;

                setMarkerCurrentlocation(lat, lng);
            }
        });

        //position on current location
        gotoCurrentLocation();
    }

    /**
     * Method hides keyboard from screen
     * @param v		view
     */
    private void hideSoftKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromInputMethod(v.getWindowToken(), 0);
    }

    @SuppressLint("MissingPermission")
    protected void gotoCurrentLocation () {

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mFusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    // Logic to handle location object

                    LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                    lat = ll.latitude;
                    lng = ll.longitude;
                    setMarkerCurrentlocation(ll.latitude, ll.longitude);
                    update = CameraUpdateFactory.newLatLngZoom(ll, DEFAULTZOOM);
                    mMap.animateCamera(update);

                } else {
                    Toast.makeText(getActivity(), "Current location not found!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void setMarkerCurrentlocation (double lat, double lng) {

        /**
         * remove previously set marker on map
         */
        if (marker != null) {
            marker.remove();
        }


        /**
         * set marker
         */
        MarkerOptions options = new MarkerOptions()
                .position(new LatLng(lat, lng))
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_mapmarker));																//ako se postavlja nas marker; moze da stoji u assets folderu, ili u drawable folderima
		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));														//ako se postavlja default android marker

        marker = mMap.addMarker(options);
    }

    private void saveLocation(String name) {

        MyLocation myLocation = new MyLocation();
        myLocation.setLocationName(name);

        Coord coord = new Coord();
        coord.lat = lat;
        coord.lon = lng;
        myLocation.setLocation(coord);

        bookmarkedLocations = LocalCache.getInstance(getActivity().getApplication()).getMyLocations();
        if (bookmarkedLocations == null) {
            bookmarkedLocations = new ArrayList<>();
        }
        bookmarkedLocations.add(myLocation);
        LocalCache.getInstance(getActivity().getApplication()).setMyLocations(bookmarkedLocations);

    }

}
