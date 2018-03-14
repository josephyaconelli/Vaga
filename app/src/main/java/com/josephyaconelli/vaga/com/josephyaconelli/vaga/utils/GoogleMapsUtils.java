package com.josephyaconelli.vaga.com.josephyaconelli.vaga.utils;

import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.josephyaconelli.vaga.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


/**
 * Created by josep on 3/12/2018.
 */

public class GoogleMapsUtils {


        private static final String TAG = GoogleMapsUtils.class.getSimpleName();

        public static List<Route> currentDirections = null;

        private static final String GOOGLE_MAPS_URL =
                "https://maps.googleapis.com/maps/api/directions/json";

        private static final String DIRECTIONS_BASE_URL = GOOGLE_MAPS_URL;

        final static String ORIGIN_PARAM = "origin";
        final static String DESTINATION_PARAM = "destination";
        final static String API_KEY_PARAM = "key";

        public static URL buildUrl(String origin, String destination, String key) {
            Uri builtUri = Uri.parse(DIRECTIONS_BASE_URL).buildUpon()
                    .appendQueryParameter(ORIGIN_PARAM, origin)
                    .appendQueryParameter(DESTINATION_PARAM, destination)
                    .appendQueryParameter(API_KEY_PARAM, key)
                    .build();

            URL url = null;
            try {
                url = new URL(builtUri.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            Log.v(TAG, "Built URI " + url);

            return url;
        }

        /**
         * Builds the URL used to talk to the weather server using latitude and longitude of a
         * location.
         *
         * @param lat The latitude of the location
         * @param lon The longitude of the location
         * @return The Url to use to query the weather server.
         */
        public static URL buildUrl(Double lat, Double lon) {
            /** This will be implemented in a future lesson **/
            return null;
        }

        /**
         * This method returns the entire result from the HTTP response.
         *
         * @param url The URL to fetch the HTTP response from.
         * @return The contents of the HTTP response.
         * @throws IOException Related to network and stream reading
         */
        public static String getResponseFromHttpUrl(URL url) throws IOException {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                InputStream in = urlConnection.getInputStream();

                Scanner scanner = new Scanner(in);
                scanner.useDelimiter("\\A");

                boolean hasInput = scanner.hasNext();
                if (hasInput) {
                    return scanner.next();
                } else {
                    return null;
                }
            } finally {
                urlConnection.disconnect();
            }
        }

        public static List<Route> getParsed(String jsonJ){



            try {
                List<Route> routes=new ArrayList<>();

                JSONObject mainJSON=new JSONObject(jsonJ);
                JSONArray jarray1=mainJSON.getJSONArray("routes");
                JSONObject jobj1=jarray1.getJSONObject(0);
                JSONArray jarray2=jobj1.getJSONArray("legs");
                JSONObject jobj2=jarray2.getJSONObject(0);
                JSONArray stepArray=jobj2.getJSONArray("steps");



                for (int j=0; j < jarray1.length(); j++ ){
                    Route route=new Route();

                    route.steps = new ArrayList<>();

                    JSONObject jobj5=stepArray.getJSONObject(j);
                    JSONObject disOBJ=jobj5.getJSONObject("distance");
                    JSONObject durOBJ=jobj5.getJSONObject("duration");
                    JSONObject endOBJ=jobj5.getJSONObject("end_location");
                    JSONObject polOBJ=jobj5.getJSONObject("polyline");
                    JSONObject startOBJ=jobj5.getJSONObject("start_location");

                    route.startAddress=jobj2.getString("start_address");
                    route.endAddress=jobj2.getString("end_address");

                    route.distance=new Distance(disOBJ.getString("text"), Integer.valueOf(disOBJ.getString("value")));
                    route.duration=new Duration(durOBJ.getString("text"), Integer.valueOf(durOBJ.getString("value")));
                    route.startLocation=new LatLng(startOBJ.getDouble("lat"),startOBJ.getDouble("lng"));
                    route.endLocation=new LatLng(endOBJ.getDouble("lat"),endOBJ.getDouble("lng"));


                    for (int i=0;i<stepArray.length();i++){
                        Step step = new Step();
                        JSONObject thisStep =stepArray.getJSONObject(i);

                        step.distance = thisStep.getJSONObject("distance").getInt("value");
                        step.duration = thisStep.getJSONObject("duration").getInt("value");
                        step.html_instructions = thisStep.getString("html_instructions");

                        if(thisStep.has("maneuver")) {
                            step.setManeuver(thisStep.getString("maneuver"));
                        } else {
                            step.setManeuver("unknown");
                        }

                        route.steps.add(step);

                    }


                    routes.add(route);
                }


                currentDirections = routes;
                return routes;

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }


}


