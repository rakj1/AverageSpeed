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
public class Trip
{
    private final String DEBUG_TAG =  "AverageSpeed." + getClass().getCanonicalName();

    private int _id;
    private String _name;
    private Date _startTime;
    private Date _endTime;
    private PointList Points;
    private String _averageSpeed;
    private String _distance;

    public Trip(String Name, Point StartingPoint)
    {
        this._name = Name;
        this._startTime = StartingPoint.Time();
        Points = new PointList();
        Points.add(StartingPoint);
    }

    public Trip(int ID ,String Name, Point StartingPoint)
    {
        this._id = ID;
        this._name = Name;
        this._startTime = StartingPoint.Time();
        Points = new PointList();
        Points.add(StartingPoint);
    }

    public Trip(int ID, String Name, String StartTime, String EndTime, String AverageSpeed, String Distance)
    {
        this._id = ID;
        this._name = Name;

        this._averageSpeed = AverageSpeed;
        this._distance = Distance;
    }

    public Trip(String json)
    {
        try {
            // Parse the string into a JSON object
            JSONObject obj = new JSONObject(json);

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

            d(DEBUG_TAG, "Getting stored prefs");
            d(DEBUG_TAG, obj.toString());
            // Extract the data of the trip from the JSON object
            _name = obj.getString("name");
            _startTime = format.parse(obj.getString("startTime"));
            _endTime = format.parse(obj.getString("endTime"));
            //_distance = obj.getString("_distance");
            //_averageSpeed = obj.getString("averageSpeed");
            Points = (PointList)obj.get("points");
        } catch (JSONException | ParseException e) {
            // If any fields or JSON is invalid print the stack trace and throw a new Illegal arg exception
            d(DEBUG_TAG, e.toString());
            e.printStackTrace();
            throw new IllegalArgumentException("Invalid serialised string, can't create trip ...");
        }
    }

    public String toJSONString() {
        JSONObject obj = new JSONObject();

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

        try {
            obj.put("name", _name);
            obj.put("startTime", format.format(_startTime));
            obj.put("endTime", format.format(_endTime));
            //obj.put("_distance",_distance);
            //obj.put("averageSpeed", _averageSpeed);
        } catch (JSONException e) {
            e.printStackTrace();
            d(DEBUG_TAG, "Error occurred while constructing serialised string...");
        }
        String t = obj.toString().substring(0, obj.toString().length()-1) + ",\"points\":" +  Points.toString() + "}";
        //d(DEBUG_TAG, t);
        return t ;
    }

    /**Getter methods**/
    public int ID() {return _id;}
    public String Name() {return _name;}
    public Date StartTime() {return _startTime;}
    public Date EndTime() {return _endTime;}
    public String Distance()
    {
        return "Null";
    }
    public String AverageSpeed()
    {
        return "Null";
    }

    /**Setter Methods**/
    public void endTrip(Point endingPoint)
    {
        Points.add(endingPoint);
        _endTime = endingPoint.Time();
    }

    public void addPoint(Point point)
    {
        Points.add(point);
    }

}
