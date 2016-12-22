package com.example.ronan.wheremybus;

/**
 * Created by Ronan on 21/12/2016.
 */

public class BusData {

    private String duetime;
    private String route;
    private String destination;
    private String origin;
    private String arrivaldatetime;
    private String operator;

    public BusData(String arrivaldatetime, String destination, String duetime, String operator, String origin, String route) {
        this.arrivaldatetime = arrivaldatetime;
        this.destination = destination;
        this.duetime = duetime;
        this.operator = operator;
        this.origin = origin;
        this.route = route;
    }

   //getters


    public String getArrivaldatetime() {
        return arrivaldatetime;
    }

    public String getDestination() {
        return destination;
    }

    public String getDuetime() {
        return duetime;
    }

    public String getOperator() {
        return operator;
    }

    public String getOrigin() {
        return origin;
    }

    public String getRoute() {
        return route;
    }
}

