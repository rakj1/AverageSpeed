package nz.ones.ryanj.averagespeed.DataObjects;

import java.util.Date;

import static android.util.Log.d;

/**
 * Created by Ryan Jones on 15/12/2015.
 */
public class Point
{
    private final String DEBUG_TAG =  "AverageSpeed." + getClass().getCanonicalName();

    private int _id;
    private Date _time;
    private Double _latitude;
    private Double _longitude;

    public Point(Date Time, Double Long, Double Lat)
    {
        this._time = Time;
        this._latitude = Lat;
        this._longitude = Long;
    }

    public Point(int ID, Date Time, Double Long, Double Lat)
    {
        this._id = ID;
        this._time = Time;
        this._latitude = Lat;
        this._longitude = Long;
    }

    /****** Getter methods *****/
    public int ID() {return _id;}
    public Date Time() {return _time;}
    public Double Longitude() {return _longitude;}
    public Double Latitude() {return _latitude;}

}
