package com.josephyaconelli.vaga.com.josephyaconelli.vaga.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by josep on 3/15/2018.
 */

@Database(entities = {RecentLocation.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract RecentLocationsDao recentLocationsDao();
}
