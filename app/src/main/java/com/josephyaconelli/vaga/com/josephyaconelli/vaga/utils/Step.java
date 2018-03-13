package com.josephyaconelli.vaga.com.josephyaconelli.vaga.utils;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by josep on 3/12/2018.
 */

public class Step {

    public String html_instructions;
    public int distance;
    public int duration;
    public LatLng start_location;
    public LatLng end_location;
    public Maneuver maneuver;


    public void setManeuver(String maneuverToSet){

        Maneuver type;

        switch (maneuverToSet){
            case "turn-slight-left":type = Maneuver.TURN_SLIGHT_LEFT; break;
            case "turn-sharp-left":type = Maneuver.TURN_SHARP_LEFT; break;
            case "uturn-left":type = Maneuver.UTURN_LEFT; break;
            case "turn-left":type = Maneuver.TURN_LEFT; break;
            case "turn-slight-right":type = Maneuver.TURN_SLIGHT_RIGHT; break;
            case "turn-sharp-right":type = Maneuver.TURN_SHARP_RIGHT; break;
            case "uturn-right":type = Maneuver.UTURN_RIGHT; break;
            case "turn-right":type = Maneuver.TURN_RIGHT; break;
            case "straight":type = Maneuver.STRAIGHT; break;
            case "ramp-left":type = Maneuver.RAMP_LEFT; break;
            case "ramp-right":type = Maneuver.RAMP_RIGHT; break;
            case "merge":type = Maneuver.MERGE; break;
            case "fork-left":type = Maneuver.FORK_LEFT; break;
            case "fork-right":type = Maneuver.FORK_RIGHT; break;
            case "ferry":type = Maneuver.FERRY; break;
            case "ferry-train":type = Maneuver.FERRY_TRAIN; break;
            case "roundabout-left":type = Maneuver.ROUNDABOUT_LEFT; break;
            case "roundabout-right":type = Maneuver.ROUNDABOUT_RIGHT; break;
            default:
                type = Maneuver.UNKNOWN;
        }

        maneuver = type;

    }

}
