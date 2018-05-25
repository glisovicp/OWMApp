package rs.gecko.petar.owmapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import rs.gecko.petar.owmapp.R;
import rs.gecko.petar.owmapp.data.LocalCache;
import rs.gecko.petar.owmapp.models.MyLocation;

/**
 * Created by Petar on 5/23/18.
 */

public class SettingsFragment extends Fragment {

    private static final String TAG = SettingsFragment.class.getSimpleName();

    private List<MyLocation> myLocations;

    private Button clearSavedLocationsBtn;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SettingsFragment.
     */
    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
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
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        clearSavedLocationsBtn = (Button) v.findViewById(R.id.settings_localstorage_clear_btn);

        clearSavedLocationsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myLocations != null && myLocations.size() > 0) {

                    LocalCache.getInstance(getActivity().getApplication()).clearCache();
                    clearSavedLocationsBtn.setEnabled(false);
                }
            }
        });

        getActivity().setTitle(getString(R.string.title_settings));

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        // get my saved locations
        myLocations = LocalCache.getInstance(getActivity().getApplication()).getMyLocations();

        if (myLocations != null && myLocations.size() > 0) {
            clearSavedLocationsBtn.setEnabled(true);
        } else {
            clearSavedLocationsBtn.setEnabled(false);
        }
    }


}
