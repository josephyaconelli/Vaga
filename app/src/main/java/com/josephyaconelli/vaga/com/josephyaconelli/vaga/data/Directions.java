package com.josephyaconelli.vaga.com.josephyaconelli.vaga.data;


import com.josephyaconelli.vaga.com.josephyaconelli.vaga.utils.Distance;
import com.josephyaconelli.vaga.com.josephyaconelli.vaga.utils.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by josep on 3/18/2018.
 */
public class Directions {

    private static List<Route> routes = new ArrayList<>();

    private Directions(){

    }

    public static void setRoutes(List<Route> routes){
        Directions.routes = routes;
    }

    public static void setRoute(Route route){
        Directions.routes.add(route);
    }


    public static void setMainRoute(int index){
        Route newMainRoute = Directions.routes.get(index);
        Route oldMainRoute = Directions.routes.get(0);

        Directions.routes.set(index, oldMainRoute);
        Directions.routes.set(0, newMainRoute);
    }

    public static Route getRoute(){
            return getRoute(0);
    }

    public static Route getRoute(int index){
        if(!routes.isEmpty() && routes.size() >= index){
            return routes.get(index);
        }
        throw new NoSuchElementException();
    }


}
