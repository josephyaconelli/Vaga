package com.josephyaconelli.vaga.com.josephyaconelli.vaga.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.sql.Timestamp;

/**
 * Created by josep on 3/15/2018.
 */

@Entity
public class RecentLocation {

    @PrimaryKey
    @NonNull private String uid;

    @ColumnInfo(name = "time")
    private long time;

    @ColumnInfo(name = "location")
    private String location;

    @ColumnInfo(name = "is_origin")
    private boolean isOrigin;

    @ColumnInfo(name = "location_name")
    private String locationName;

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    @ColumnInfo(name = "location_address")
    private String locationAddress;


    public RecentLocation(String uid, long time, String location, String locationName, String locationAddress, boolean isOrigin){
        setUid(uid);
        setTime(time);
        setLocation(location);
        setOrigin(isOrigin);
        setLocationName(locationName);
        setLocationAddress(locationAddress);

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isOrigin() {
        return isOrigin;
    }

    public void setOrigin(boolean origin) {
        isOrigin = origin;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
