package com.josephyaconelli.vaga.com.josephyaconelli.vaga.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by josep on 6/20/2018.
 */

public class DirectionServiceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DirectionUtils.scheduleJob(context);
    }
}
