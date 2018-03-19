package com.josephyaconelli.vaga.com.josephyaconelli.vaga.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by josep on 3/15/2018.
 */

@Dao
public interface RecentLocationsDao {

    @Query("SELECT * FROM RecentLocation ORDER BY time DESC, location_name ASC")
    List<RecentLocation> getAll();

    @Query("SELECT * FROM RecentLocation ORDER BY time DESC LIMIT 1")
    RecentLocation getMostRecent();

    @Query("SELECT * FROM RecentLocation ORDER BY time DESC, location_name ASC LIMIT :limit")
    List<RecentLocation> getXMostRecent(int limit);

    @Query("SELECT * FROM RecentLocation WHERE time > :cutoff ORDER BY time DESC, location_name ASC")
    List<RecentLocation> getUpdate(long cutoff);

    @Update
    public int updateRecentLocations(RecentLocation... recentLocations);

    @Insert
    void insertAll(RecentLocation... recentLocations);

    @Delete
    void delete(RecentLocation recentLocation);


}
