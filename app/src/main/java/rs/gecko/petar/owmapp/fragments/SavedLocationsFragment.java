package rs.gecko.petar.owmapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import rs.gecko.petar.owmapp.R;
import rs.gecko.petar.owmapp.data.LocalCache;
import rs.gecko.petar.owmapp.models.MyLocation;

/**
 * Created by Petar on 5/23/18.
 */

public class SavedLocationsFragment extends Fragment {

    private static final String TAG = "SavedLocFragment";

    private List<MyLocation> myLocations;

    public SavedLocationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SavedLocationsFragment.
     */
    public static SavedLocationsFragment newInstance(LocalCache localCache) {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_savedlocations, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        // get my saved locations
        myLocations = LocalCache.getInstance(getActivity().getApplication()).getMyLocations();
    }

    @Override
    public void onResume() {
        super.onResume();
        
        if (myLocations != null) {
            // show recyclerView with locations
        }
    }
}
