package com.josephyaconelli.vaga;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.location.*;

import com.josephyaconelli.vaga.com.josephyaconelli.vaga.DirectionsAdapter;
import com.josephyaconelli.vaga.com.josephyaconelli.vaga.data.Directions;
import com.josephyaconelli.vaga.com.josephyaconelli.vaga.data.RecentLocation;
import com.josephyaconelli.vaga.com.josephyaconelli.vaga.utils.DirectionUtils;
import com.josephyaconelli.vaga.com.josephyaconelli.vaga.utils.Route;
import com.josephyaconelli.vaga.com.josephyaconelli.vaga.utils.Step;
import com.josephyaconelli.vaga.com.josephyaconelli.vaga.utils.UpdateDirectionsJob;
import com.josephyaconelli.vaga.com.josephyaconelli.vaga.utils.UpdateDirectionsService;

import java.util.List;
import java.util.NoSuchElementException;

public class DirectionsActivity extends AppCompatActivity {


    RecyclerView mDirectionsRecyclerView;
    DirectionsAdapter mDirectionsAdapter;
    BottomNavigationView mBottomNavigationView;
    Menu mBottomNavigationInfo;
    MenuItem mTotalDistance, mTotalDuration, mExitRoute;
    TextView mCurrentDistance, mCurrentMainDirection, mCurrentSecondaryDirection;
    Toolbar mCurrentDirectionBar;
    LocationManager mLocationManager;
    LocationListener mLocationLister;
    Context mContext;

    Route mRoute = Directions.getRoute();

    int mDistanceTraveled = 0, mTimeShouldHaveElapsed = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);


        Intent i = new Intent(this, UpdateDirectionsService.class);
        i.putExtra("KEY1", "value to be used later");
        this.startService(i);

        mBottomNavigationView = findViewById(R.id.bottom_navigation);
        mBottomNavigationInfo = mBottomNavigationView.getMenu();
        mTotalDistance = mBottomNavigationInfo.findItem(R.id.total_distance_menuitem);
        mTotalDuration = mBottomNavigationInfo.findItem(R.id.total_duration_menuitem);
        mExitRoute = mBottomNavigationInfo.findItem(R.id.end_navigation_menuitem);

        mCurrentDirectionBar = (Toolbar) findViewById(R.id.current_directions_toolbar);

        mCurrentDistance = findViewById(R.id.current_direction_view).findViewById(R.id.distance_tv);
        mCurrentMainDirection = findViewById(R.id.current_direction_view).findViewById(R.id.main_direction_tv);
        mCurrentSecondaryDirection = findViewById(R.id.current_direction_view).findViewById(R.id.secondary_direction_tv);


        mDirectionsRecyclerView = findViewById(R.id.directions_rv);
        mDirectionsAdapter = new DirectionsAdapter();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mDirectionsRecyclerView.setLayoutManager(layoutManager);

        mDirectionsRecyclerView.setHasFixedSize(true);

        mDirectionsAdapter = new DirectionsAdapter();

        mDirectionsRecyclerView.setAdapter(mDirectionsAdapter);




        setTotalDurDist();

        setCurrentDirection(0);


        mExitRoute.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                finish();
                return true;
            }
        });

        mContext = this;

        /* Create the location update listener to update directions */

        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        mLocationLister = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Step currentStep = mRoute.steps.get(0);

                if (DirectionUtils.isCloseOrPast(currentStep, location)) {
                    nextDirection();
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationLister);


    }



    private void setTotalDurDist(){
        int totalDuration = 0, totalDistance = 0;
        for(Step step : mRoute.steps){
            totalDuration += step.duration.value;
            totalDistance += step.distance.value;
        }
        mRoute.distance.value = totalDistance;
        mRoute.duration.value = totalDuration;
    }

    //TODO: This logic doesn't look right to me, but could be because I'm not remembering what getItemCount is
    private void nextDirection(){
        int stepsLen = mRoute.steps.size() - mDirectionsAdapter.getItemCount() + 1;

        if (mDirectionsAdapter.getItemCount() == 1){
            Intent i = new Intent(this, UpdateDirectionsService.class);
            i.putExtra("KEY1", "value to be used later");
            this.startService(i);
            setDone();
            mLocationManager.removeUpdates(mLocationLister);
        } else {
            setCurrentDirection(mRoute.steps.size() - mDirectionsAdapter.getItemCount() + 1);
        }
    }


    private void setDone(){
        mCurrentMainDirection.setText("Arrived!");
    }

    private void setCurrentDirection(int stepIndex){
        mDirectionsRecyclerView.getChildAt(0);
        mTotalDistance.setTitle(mRoute.distance.text);

        mTotalDuration.setTitle(mRoute.duration.text);

        mCurrentDistance.setText(mRoute.steps.get(stepIndex).distance.text);
        mCurrentMainDirection.setText(Html.fromHtml(mRoute.steps.get(stepIndex).html_instructions));

        try {

            int subArraySize = mRoute.steps.size() - stepIndex;
            List<Step> steps = mRoute.steps.subList(stepIndex, mRoute.steps.size());

            mDirectionsAdapter.setSteps(steps.toArray(new Step[subArraySize]));

        } catch (NoSuchElementException e){
            Toast error = Toast.makeText(this, "Something went wrong... so sorry!", Toast.LENGTH_LONG);
            error.show();

        }

    }


}
