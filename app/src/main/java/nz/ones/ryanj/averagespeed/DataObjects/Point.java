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

    private Date Time;
    private Double Longitude;
    private Double Latitude;

    public Point(Date Time, Double Long, Double Lat)
    {
        this.Time = Time;
        this.Longitude = Long;
        this.Latitude = Lat;
    }

    public Point(String json)
    {
        try {
            // Parse the string into a JSON object
            JSONObject obj = new JSONObject(json);

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

            // Extract the data of the trip from the JSON object
            Time = format.parse(obj.getString("time"));
            Longitude = obj.getDouble("long");
            Latitude = obj.getDouble("lat");
        } catch (JSONException | ParseException e) {
            // If any fields or JSON is invalid print the stack trace and throw a new Illegal arg exception
            e.printStackTrace();
            throw new IllegalArgumentException("Invalid serialised string, can't create trip ...");
        }
    }

    public Date Time() {return Time;}
    public Double Longitude() {return Longitude;}
    public Double Latitude() {return Latitude;}

    public String toJSONString()
    {
        JSONObject obj = new JSONObject();
        try {
            obj.put("time", Time);
            obj.put("long", Longitude);
            obj.put("lat", Latitude);
        } catch (JSONException e) {
            e.printStackTrace();
            d(DEBUG_TAG, "Error occurred while constructing serialised string...");
        }
        return obj.toString();
    }

}
