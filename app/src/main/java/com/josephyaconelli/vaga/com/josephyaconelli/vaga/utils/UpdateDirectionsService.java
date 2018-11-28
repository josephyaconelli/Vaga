package com.josephyaconelli.vaga.com.josephyaconelli.vaga.utils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.josephyaconelli.vaga.DirectionsActivity;
import com.josephyaconelli.vaga.com.josephyaconelli.vaga.DirectionsAdapter;
import com.josephyaconelli.vaga.com.josephyaconelli.vaga.data.RecentLocation;

/**
 * Created by josep on 3/25/2018.
 */

public class UpdateDirectionsService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO get new gps coordinate

        Toast.makeText(this, "Service called!", Toast.LENGTH_SHORT).show();



        return Service.START_NOT_STICKY;
    }



    @Override
    public IBinder onBind(Intent intent){

        return null;
    }

}
