package com.josephyaconelli.vaga.com.josephyaconelli.vaga.utils;

import android.Manifest;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

/**
 * Created by josep on 3/11/2018.
 */


public class DirectionUtils {

    Context mContext;
    LocationManager mLocationManager;
    Location mLastLocation;
    LocationListener mLocationListener;


    public static boolean isCloseOrPast(Step currentStep, Location currentLocation){
        double thresh = 9*(10^(-6)); // approximately 1 meter in LatLng degrees
        thresh *= 50; // 50 meters threshold

        if ((Math.abs(currentStep.end_location.longitude - currentLocation.getLongitude()) <= thresh && Math.abs(currentStep.end_location.latitude - currentLocation.getLatitude()) <= thresh)

                || (((currentLocation.getLatitude() > Math.max(currentStep.end_location.latitude, currentStep.start_location.latitude))
                || (currentLocation.getLatitude() < Math.min(currentStep.end_location.latitude, currentStep.start_location.latitude)))

                && ((currentLocation.getLongitude() > Math.max(currentStep.end_location.longitude, currentStep.start_location.longitude))
                || ((currentLocation.getLongitude() < Math.min(currentStep.end_location.longitude, currentStep.start_location.longitude)))))){
            return true;
        }

        return false;




    }

    public DirectionUtils(Context context){
        mContext = context;

        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mLastLocation = location;
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

    }




    public Location getLocation(){
        if(ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            //mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 500, mLocationListener);


                // No explanation needed; request the permission


                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request

            Toast t = new Toast(mContext);

            t.setText("Location Permission Not Granted. App won't work.");
            t.show();

            return null;

        } else {

            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, mLocationListener);
            double acc1 = 0;
            double acc2 = 0;
            double accDiff = 1;
            double accDiffCutOff = 0.2;
            mLastLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            while(mLastLocation == null){
                mLastLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }

            while(accDiff > 0.2){
                acc1 = mLastLocation.getAccuracy();
                mLastLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                acc2 = mLastLocation.getAccuracy();
                accDiff = Math.abs(acc1 - acc2);
            }
            mLocationManager.removeUpdates(mLocationListener);

            return mLastLocation;


        }
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void scheduleJob(Context context){
        ComponentName serviceComponent = new ComponentName(context, UpdateDirectionsService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        builder.setMinimumLatency(1 * 1000);
        builder.setOverrideDeadline(3 * 1000);

        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        jobScheduler.schedule(builder.build());
    }



}
