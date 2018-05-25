package rs.gecko.petar.owmapp.fragments;

import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import rs.gecko.petar.owmapp.R;
import rs.gecko.petar.owmapp.activities.MainActivity;
import rs.gecko.petar.owmapp.data.Constants;
import rs.gecko.petar.owmapp.models.MyLocation;
import rs.gecko.petar.owmapp.rest.WeatherHttpClient;
import rs.gecko.petar.owmapp.rest.data.DataSingleton;
import rs.gecko.petar.owmapp.rest.models.Weather;
import rs.gecko.petar.owmapp.rest.models.WeatherPerCityResponse;
import rs.gecko.petar.owmapp.utils.Utils;

/**
 * Created by Petar on 5/25/18.
 */

public class WeatherDetailsFragment extends Fragment {

    private static final String TAG = WeatherDetailsFragment.class.getSimpleName();

    private static final String ARG_MY_LOCATION = "my_location";

    private MyLocation chosenLocation;
    private WeatherPerCityResponse weatherPerCityResponse;

    private ProgressDialog progressDialog;

    ImageView weatherIconImg;
    TextView temperatureV;
    TextView temperatureMinV;
    TextView temperatureMaxV;
    TextView windV;
    TextView cloudinessV;
    TextView pressureV;
    TextView humidityV;
    TextView geoCoordinatesV;

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

        weatherIconImg = (ImageView) v.findViewById(R.id.ivWeatherIcon);
        temperatureV = (TextView) v.findViewById(R.id.tvTemperature);
        temperatureMinV = (TextView) v.findViewById(R.id.tvTemperatureMin);
        temperatureMaxV = (TextView) v.findViewById(R.id.tvTemperatureMax);
        windV = (TextView) v.findViewById(R.id.tvWind);
        cloudinessV = (TextView) v.findViewById(R.id.tvCloudiness);
        pressureV = (TextView) v.findViewById(R.id.tvPressure);
        humidityV = (TextView) v.findViewById(R.id.tvHumidity);
        geoCoordinatesV = (TextView) v.findViewById(R.id.tvGeoCoordinates);

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

    @SuppressLint("StringFormatInvalid")
    private void updateGui() {

        if (weatherPerCityResponse != null) {

            // Temperature
            temperatureV.setText(getString(R.string.format_temperature,
                    String.valueOf(weatherPerCityResponse.main.temp)));
            temperatureMinV.setText(getString(R.string.format_temperature_min,
                    String.valueOf(weatherPerCityResponse.main.temp_min)));
            temperatureMaxV.setText(getString(R.string.format_temperature_max,
                    String.valueOf(weatherPerCityResponse.main.temp_max)));

            // Wind
            windV.setText(getString(R.string.format_wind,
                    String.valueOf(weatherPerCityResponse.wind.speed),
                    String.valueOf(weatherPerCityResponse.wind.deg)));
            // Pressure
            pressureV.setText(getString(R.string.format_pressure,
                    String.valueOf(weatherPerCityResponse.main.pressure)));
            // Humidity
            humidityV.setText(getString(R.string.format_humidity,
                    String.valueOf(weatherPerCityResponse.main.humidity)));
            // Geo Coordinates
            geoCoordinatesV.setText(getString(R.string.format_geo_coords,
                    weatherPerCityResponse.coord.lon,
                    weatherPerCityResponse.coord.lat));

            List<Weather> weather = weatherPerCityResponse.weather;
            if (weather != null && weather.size() > 0) {
                Weather weatherInfo = weather.get(0);
                // Weather icon

                //TODO: make this a lot better, this is bad bad solution done in absence of time

                int resId = -1;
                switch (weatherInfo.icon){
                    case "01d": resId = R.drawable.weather_01d;
                        break;
                    case "01n": resId = R.drawable.weather_01n;
                        break;
                    case "02d": resId = R.drawable.weather_02d;
                        break;
                    case "02n": resId = R.drawable.weather_02n;
                        break;
                    case "03d": resId = R.drawable.weather_03d;
                        break;
                    case "03n": resId = R.drawable.weather_03n;
                        break;
                    case "04d": resId = R.drawable.weather_04d;
                        break;
                    case "04n": resId = R.drawable.weather_04n;
                        break;
                    case "09d": resId = R.drawable.weather_09d;
                        break;
                    case "09n": resId = R.drawable.weather_09n;
                        break;
                    case "10d": resId = R.drawable.weather_10d;
                        break;
                    case "10n": resId = R.drawable.weather_10n;
                        break;
                    case "11d": resId = R.drawable.weather_11d;
                        break;
                    case "11n": resId = R.drawable.weather_11n;
                        break;
                    case "13d": resId = R.drawable.weather_13d;
                        break;
                    case "13n": resId = R.drawable.weather_13n;
                        break;
                    case "50d": resId = R.drawable.weather_50d;
                        break;
                    case "50n": resId = R.drawable.weather_50n;
                        break;
                }

                weatherIconImg.setImageResource(resId);

                // Cloudiness
                cloudinessV.setText(getString(R.string.format_cloudiness,
                        weatherInfo.description));
            }
        }

    }

    private void requestData(String uri) {
        JSONWeatherTask task = new JSONWeatherTask();
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
