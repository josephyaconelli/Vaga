package com.josephyaconelli.vaga.com.josephyaconelli.vaga.data;

import android.arch.persistence.room.Room;
import android.content.Context;

/**
 *
 * Singleton class for accessing database, making sure there is only one instance created.
 * */
public class AppDatabaseManager {

    private static AppDatabase database = null;

    /**
     * To protect against multiple instances
     */
    private AppDatabaseManager(){
        // Do nothing :)
    }

    public static AppDatabase getInstance(Context context) {
        if(database == null) {
            database = Room.databaseBuilder(context, AppDatabase.class, "recentlocation")
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return database;
    }

}
