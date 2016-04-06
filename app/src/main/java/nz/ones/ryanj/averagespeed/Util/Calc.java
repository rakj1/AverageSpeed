package nz.ones.ryanj.averagespeed.Util;

import java.util.Date;

/**
 * Created by Ryan Jones on 12/03/2016.
 */
public class Calc
{
    // Code from
    // https://stackoverflow.com/questions/837872/calculate-distance-in-meters-when-you-know-longitude-and-latitude-in-java
    // Returns in meters
    public static float distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        float dist = (float) (earthRadius * c);

        return dist;
    }

    // Calculates the average speed
    // Takes the distance in metres
    public static double averageSpeed(double distance, Date startTime, Date endTime)
    {
        // Time the trip took in seconds
        long deltaTime = (endTime.getTime() - startTime.getTime())/1000;
        return ((distance/deltaTime)*3.6);
    }
}
