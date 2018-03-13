package com.josephyaconelli.vaga;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Path;
import android.location.Location;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.josephyaconelli.vaga.com.josephyaconelli.vaga.utils.DirectionUtils;
import com.josephyaconelli.vaga.com.josephyaconelli.vaga.utils.GoogleMapsUtils;
import com.josephyaconelli.vaga.com.josephyaconelli.vaga.utils.Route;
import com.josephyaconelli.vaga.com.josephyaconelli.vaga.utils.Step;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView mResultView;
    Button mFromHereBtn;
    Button mGetDirectionsBtn;
    ProgressBar mDirectionsPb;
    PlaceAutocompleteFragment mDestinationAutoComplete;
    PlaceAutocompleteFragment mOriginAutoComplete;

    String mOriginStr, mDestinationStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                PackageManager.PERMISSION_GRANTED);


        mResultView = findViewById(R.id.result_tv);
        mFromHereBtn = findViewById(R.id.fromhere_btn);
        mGetDirectionsBtn = findViewById(R.id.getdirection_btn);
        mDirectionsPb = findViewById(R.id.directions_pb);
        mDestinationAutoComplete = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.destination_auto_complete);
        mOriginAutoComplete = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.origin_auto_complete);

        mOriginAutoComplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                mOriginStr = place.getLatLng().toString();
            }

            @Override
            public void onError(Status status) {

            }
        });

        mDestinationAutoComplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                //Log.i("Place: ", place.getName());
                mDestinationStr = place.getLatLng().toString();
            }

            @Override
            public void onError(Status status) {
                //Log.i(TAG, "ERROR: ", status);
            }
        });


        mGetDirectionsBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                String[] inputs = {mOriginStr, mDestinationStr};
                new FetchDirectionsTask().execute(inputs);
            }
        });


        mFromHereBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                DirectionUtils du = new DirectionUtils(getApplicationContext());

                Location l = du.getLocation();

                String from = l.getLatitude() + "," + l.getLongitude();

                mOriginStr = from;

                mOriginAutoComplete.setHint("Current Location");



            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.directions_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.action_refresh){
            DirectionUtils utils = new DirectionUtils(this);

            Location l = utils.getLocation();
            mResultView.setText("Lat: " + l.getLatitude() + " Long: " + l.getLongitude());

            //URL url = GoogleMapsUtils.buildUrl("Disneyland", "Eugene+OR");
            String[] inputs = {"Disneyland", "Eugene OR"};
            new FetchDirectionsTask().execute(inputs);

            /*
            try {
                String response = GoogleMapsUtils.getResponseFromHttpUrl(url);
                mLocationView.setText(response);
            } catch (IOException e) {
                e.printStackTrace();
            }*/

        }

        return super.onOptionsItemSelected(item);
    }


    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public class FetchDirectionsTask extends AsyncTask<String, Void, List<Route>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressBar();
        }

        @Override
        protected List<Route> doInBackground(String... params) {

            /* If there's no zip code, there's nothing to look up. */
            if (params.length == 0) {
                return null;
            }

            String origin = params[0];
            String destination = params[1];
            String key = getApplicationContext().getResources().getText(R.string.GoogleMapApiKey).toString();
            URL weatherRequestUrl = GoogleMapsUtils.buildUrl(origin, destination, key);

            try {
                String jsonDirections = GoogleMapsUtils
                        .getResponseFromHttpUrl(weatherRequestUrl);


                Log.d("DIRECTIONS_TAG", jsonDirections);

                List<Route> routes = GoogleMapsUtils.getParsed(jsonDirections);

                return routes;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Route> directionData) {
            showResults();
            if (directionData != null) {

                Route route = directionData.get(0);
                mResultView.setText("");
                    for(Step step: route.steps){
                    mResultView.append(Html.fromHtml(step.html_instructions));
                    mResultView.append("\n\n");
                }

                /*
                 * Iterate through the array and append the Strings to the TextView. The reason why we add
                 * the "\n\n\n" after the String is to give visual separation between each String in the
                 * TextView. Later, we'll learn about a better way to display lists of data.
                 */
                //mForecastAdapter.setWeatherData(weatherData);
            } else {
                //showErrorMessage();
            }
        }
    }


    private void showResults(){
        mDirectionsPb.setVisibility(View.GONE);
        mResultView.setVisibility(View.VISIBLE);
    }

    private void showProgressBar(){
        mResultView.setVisibility(View.INVISIBLE);
        mDirectionsPb.setVisibility(View.VISIBLE);
    }

}

