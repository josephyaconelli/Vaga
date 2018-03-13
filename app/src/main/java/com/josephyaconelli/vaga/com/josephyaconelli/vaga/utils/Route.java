package com.josephyaconelli.vaga.com.josephyaconelli.vaga.utils;


import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by josep on 3/12/2018.
 * Helper for Google maps directions api data
 */
public class Route {

    public Distance distance;
    public Duration duration;
    public String endAddress;
    public LatLng endLocation;
    public String startAddress;
    public LatLng startLocation;
    public List<Step> steps;

}
