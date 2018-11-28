package com.josephyaconelli.vaga.com.josephyaconelli.vaga.utils;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.app.job.JobWorkItem;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * Created by josep on 8/27/2018.
 */

public class UpdateDirectionsJobScheduler extends JobScheduler {
    @Override
    public int schedule(@NonNull JobInfo jobInfo) {
        return 0;
    }

    @Override
    public int enqueue(@NonNull JobInfo jobInfo, @NonNull JobWorkItem jobWorkItem) {
        return 0;
    }

    @Override
    public void cancel(int i) {

    }

    @Override
    public void cancelAll() {

    }

    @NonNull
    @Override
    public List<JobInfo> getAllPendingJobs() {
        return null;
    }

    @Nullable
    @Override
    public JobInfo getPendingJob(int i) {
        return null;
    }
}
