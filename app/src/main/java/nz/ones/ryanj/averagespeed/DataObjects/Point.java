package nz.ones.ryanj.averagespeed.DataObjects;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.util.Log.d;

/**
 * Created by Ryan Jones on 15/12/2015.
 */
public class Point
{
    private final String DEBUG_TAG =  "AverageSpeed." + getClass().getCanonicalName();

    private Date _time;
    private Double _longitude;
    private Double _latitude;

    public Point(Date Time, Double Long, Double Lat)
    {
        this._time = Time;
        this._longitude = Long;
        this._latitude = Lat;
    }

    /****** Getter methods *****/
    public Date Time() {return _time;}
    public Double Longitude() {return _longitude;}
    public Double Latitude() {return _latitude;}

}
