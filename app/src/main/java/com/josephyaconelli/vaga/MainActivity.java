package com.josephyaconelli.vaga;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.josephyaconelli.vaga.com.josephyaconelli.vaga.RecentLocationAdapter;
import com.josephyaconelli.vaga.com.josephyaconelli.vaga.data.AppDatabase;
import com.josephyaconelli.vaga.com.josephyaconelli.vaga.data.AppDatabaseManager;
import com.josephyaconelli.vaga.com.josephyaconelli.vaga.data.Directions;
import com.josephyaconelli.vaga.com.josephyaconelli.vaga.data.RecentLocation;
import com.josephyaconelli.vaga.com.josephyaconelli.vaga.utils.DirectionUtils;
import com.josephyaconelli.vaga.com.josephyaconelli.vaga.utils.GoogleMapsUtils;
import com.josephyaconelli.vaga.com.josephyaconelli.vaga.utils.Route;
import com.josephyaconelli.vaga.com.josephyaconelli.vaga.utils.UpdateDirectionsService;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static final boolean TESTING = true;

    RecyclerView mRecentLocationsRv;
    RecentLocationAdapter mRecentLocationAdapter;
    Button mFromHereBtn;
    Button mGetDirectionsBtn;
    ProgressBar mDirectionsPb;
    PlaceAutocompleteFragment mDestinationAutoComplete;
    PlaceAutocompleteFragment mOriginAutoComplete;
    AppDatabase database;

    long lastUpdate;

    List<RecentLocation> mRecentLocations;

    String mOriginStr, mDestinationStr, mOriginName, mOriginAddress, mDestinationName, mDestinationAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        if(TESTING){
            Intent i = new Intent(this, UpdateDirectionsService.class);
            i.putExtra("KEY1", "value to be used later");
            this.startService(i);
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                PackageManager.PERMISSION_GRANTED);


        database = AppDatabaseManager.getInstance(this);

        lastUpdate = System.currentTimeMillis();

        mFromHereBtn = findViewById(R.id.fromhere_btn);
        mGetDirectionsBtn = findViewById(R.id.getdirection_btn);
        mDirectionsPb = findViewById(R.id.directions_pb);
        mDestinationAutoComplete = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.destination_auto_complete);
        mOriginAutoComplete = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.origin_auto_complete);

        mRecentLocationsRv = (RecyclerView) findViewById(R.id.recent_location_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecentLocationsRv.setLayoutManager(layoutManager);

        mRecentLocationsRv.setHasFixedSize(true);

        mRecentLocationAdapter = new RecentLocationAdapter();

        mRecentLocationsRv.setAdapter(mRecentLocationAdapter);

        new FetchRecentLocationsLast().execute();


        mOriginAutoComplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                mOriginAddress = place.getAddress().toString();
                mOriginName = place.getName().toString();

                if(mOriginAddress.contains(mOriginName)){
                    mOriginAddress = mOriginAddress.substring(mOriginName.length() + 1);
                }

                mOriginStr = place.getAddress().toString();
            }

            @Override
            public void onError(Status status) {

            }
        });

        mDestinationAutoComplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                mDestinationAddress = place.getAddress().toString();
                mDestinationName = place.getName().toString();

                if(mDestinationAddress.contains(mDestinationName)){
                    mDestinationAddress = mDestinationAddress.substring(mDestinationName.length() + 1);
                }
                mDestinationStr = place.getAddress().toString();
            }

            @Override
            public void onError(Status status) {
                //Log.i(TAG, "ERROR: ", status);
            }
        });


        mGetDirectionsBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                String[] inputs = {mOriginStr, mOriginName, mDestinationStr, mDestinationName};
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


    private void loadRecentLocations(){
        mRecentLocationAdapter.setRecentLocations(mRecentLocations.toArray(new RecentLocation[0]));
    }

    private  void getRecentLocations(){
        mRecentLocations = database.recentLocationsDao().getXMostRecent(10);
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


        return super.onOptionsItemSelected(item);
    }

    public class FetchRecentLocationsLast extends  AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            showProgressBar();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            getRecentLocations();
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            showResults();
            loadRecentLocations();

        }
    }

    public class AddRecentLocationsTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {

            long currentTime = System.currentTimeMillis();
            lastUpdate = currentTime;

            RecentLocation originL = new RecentLocation(mOriginStr,
                    currentTime,
                    mOriginStr,
                    mOriginName,
                    mOriginAddress,
                    true);
            RecentLocation destinationL = new RecentLocation(mDestinationStr,
                    currentTime + 1,
                    mDestinationStr,
                    mDestinationName,
                    mDestinationAddress,
                    false);

            if(database.recentLocationsDao().updateRecentLocations(originL) == 0){
                database.recentLocationsDao().insertAll(originL);
            }
            if(database.recentLocationsDao().updateRecentLocations(destinationL) == 0){
                database.recentLocationsDao().insertAll(destinationL);
            }

            getRecentLocations();

            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            loadRecentLocations();

        }
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
            String destination = params[2];

            String key = getApplicationContext().getResources().getText(R.string.GoogleMapApiKey).toString();
            URL weatherRequestUrl = GoogleMapsUtils.buildUrl(origin, destination, key);

            new AddRecentLocationsTask().execute(params);



            try {
                String jsonDirections = GoogleMapsUtils
                        .getResponseFromHttpUrl(weatherRequestUrl);


                Log.d("DIRECTIONS_TAG", jsonDirections);

                List<Route> routes = GoogleMapsUtils.getParsed(jsonDirections);

                Directions.setRoutes(routes);

                return routes;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Route> directionData) {
            showResults();
            if (directionData == null) {
                showErrorMessage();
            }

            Class directionsActivity = DirectionsActivity.class;

            Intent loadDirectionsActivity = new Intent(MainActivity.this, directionsActivity);
            startActivity(loadDirectionsActivity);

        }
    }

    private void showErrorMessage() {
     //   mResultView.setText("Oops! Something went wrong :/");
     //   mResultView.setTextColor(Color.LTGRAY);
    }


    private void showResults(){
        mDirectionsPb.setVisibility(View.GONE);
        mRecentLocationsRv.setVisibility(View.VISIBLE);
    }

    private void showProgressBar(){
     //   mResultView.setVisibility(View.INVISIBLE);
        mRecentLocationsRv.setVisibility(View.INVISIBLE);
        mDirectionsPb.setVisibility(View.VISIBLE);
    }

}

