package nz.ones.ryanj.averagespeed;

import java.util.ArrayList;

/**
 * Created by Ryan Jones on 16/12/2015.
 */
public class AllTrips
{
    private static ArrayList<Trip> Trips = new ArrayList<>();

    public static void add(Trip trip)
    {
        Trips.add(trip);
    }

    public static ArrayList<Trip> getTrips()
    {
        return Trips;
    }

    public static Trip get(int index)
    {
        return Trips.get(index);
    }

    public void saveTrips()
    {

    }

    public void loadTrips()
    {

    }
}
