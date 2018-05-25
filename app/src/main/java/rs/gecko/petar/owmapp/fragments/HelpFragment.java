package rs.gecko.petar.owmapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import rs.gecko.petar.owmapp.R;

/**
 * Created by Petar on 5/23/18.
 */

public class HelpFragment extends Fragment {

    private static final String TAG = "HelpFragment";

    WebView webView;

    public HelpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SavedLocationsFragment.
     */
    public static HelpFragment newInstance() {
        HelpFragment fragment = new HelpFragment();
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

        View v = inflater.inflate(R.layout.fragment_help, container, false);
        webView = (WebView) v.findViewById(R.id.file_viewer_wv);

        webView.getSettings().setBuiltInZoomControls(true);
        webView.loadUrl("file:///android_asset/help.html");

        webView.setBackgroundColor(0x00000000);
        webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);

        getActivity().setTitle(getString(R.string.title_help));

        return v;

    }

    @Override
    public void onStart() {
        super.onStart();

        Log.d(TAG, "[onStart]");
    }
}
