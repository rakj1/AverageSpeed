package nz.ones.ryanj.averagespeed.Util;

/**
 * Created by Ryan Jones on 22/01/2016.
 */
public class Constants
{
    private static final int TIME_INTERVAL = 1000 * 20;     //20 Seconds
    private static final int DISTANCE_INTERVAL = 200;       //200 meters

    public static int getTimeInterval()
    {
        return TIME_INTERVAL;
    }

    public static int getDistanceInterval()
    {
        return DISTANCE_INTERVAL;
    }
}
