package com.josephyaconelli.vaga.com.josephyaconelli.vaga.utils;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

/**
 * Created by josep on 3/25/2018.
 */

//@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class UpdateDirectionsJob extends JobService {

    private static final String TAG = "SyncService";

    @Override
    public boolean onStartJob(JobParameters jobParameters) {

        Intent service = new Intent(getApplicationContext(),
                UpdateDirectionsService.class);
        getApplicationContext().startForegroundService(service);


        DirectionUtils.scheduleJob(getApplicationContext());

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }
}
