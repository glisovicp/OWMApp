package rs.gecko.petar.owmapp.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import rs.gecko.petar.owmapp.R;
import rs.gecko.petar.owmapp.activities.MainActivity;
import rs.gecko.petar.owmapp.data.Constants;
import rs.gecko.petar.owmapp.models.MyLocation;
import rs.gecko.petar.owmapp.rest.WeatherHttpClient;
import rs.gecko.petar.owmapp.rest.data.DataSingleton;
import rs.gecko.petar.owmapp.rest.models.WeatherPerCityResponse;
import rs.gecko.petar.owmapp.utils.Utils;

/**
 * Created by Petar on 5/25/18.
 */

public class WeatherDetailsFragment extends Fragment {

    private static final String TAG = WeatherDetailsFragment.class.getSimpleName();

    private static final String ARG_MY_LOCATION = "my_location";

    private MyLocation chosenLocation;

    private ProgressDialog progressDialog;

    private WeatherPerCityResponse weatherPerCityResponse;

    public WeatherDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment WeatherDetailsFragment.
     */
    public static WeatherDetailsFragment newInstance(MyLocation myLocation) {
        WeatherDetailsFragment fragment = new WeatherDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MY_LOCATION, myLocation);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            chosenLocation = (MyLocation) getArguments().getSerializable(ARG_MY_LOCATION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_weather_details, container, false);


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getActivity() instanceof MainActivity) {

            MainActivity main = (MainActivity) getActivity();
            if (Utils.isOnline(main)) {

                //ex. http://api.openweathermap.org/data/2.5/weather?lat=35&lon=139&appid=b92b3af10d8b98fd09fd3780a0d0d1df&units=metric
                String baseUrlStr = DataSingleton.getInstance().getServerAccessData().getServerBaseUrlWithApiVersionAndExtension();
                String urlStr = baseUrlStr + "/weather?lat=" + chosenLocation.getLocation().lat + "&lon=" + chosenLocation.getLocation().lon + "&appid=" + Constants.APP_ID + "&units=metric";
                requestData(urlStr);
            } else {
                Snackbar.make(main.parentLayout, "There is no internet connection", Snackbar.LENGTH_LONG)
                        .show();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();


        updateGui();
    }

    private void updateGui() {

    }

    private void requestData(String uri) {
        JSONWeatherTask task = new JSONWeatherTask();
        //		task.execute("Param 1", "Param 2", "Param 3");										//ovi parametri moraju da odgovaraju po tipu parametrima u doInBackground metodi AsyncTask klase; kada se ovako pozove asynctask nit onda se aktivnosti izvrsavaju serijski
        //		task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "Param 1", "Param 2", "Param 3");							//kada se ovako pozove asynctask nit onda se aktivnosti izvrsavaju paralelno
        task.execute(uri);
    }

    private class JSONWeatherTask extends AsyncTask<String, Void, Void> {
        boolean get_weather_ok = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Downloading data...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface arg0) {
                    JSONWeatherTask.this.cancel(true);
                }
            });
        }

        @Override
        protected Void doInBackground(String... params) {
            String url = params[0];

            WeatherHttpClient weatherClient = new WeatherHttpClient();

            // Making a request to url and getting response
            String jsonStr = weatherClient.getWeatherData(url);
            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {

                //Parsing response from url
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    weatherPerCityResponse = objectMapper.readValue(jsonStr, WeatherPerCityResponse.class);

                    if (weatherPerCityResponse.cod == 200) {
                        get_weather_ok = true;  // Set response from url to true
                    }
                }catch (JsonParseException e) {
                    e.printStackTrace();
                } catch (JsonMappingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (progressDialog.isShowing())
                progressDialog.dismiss();

            // Updating parsed JSON data into List
            if (get_weather_ok) {
                updateGui();
            }

        }

    }
}
