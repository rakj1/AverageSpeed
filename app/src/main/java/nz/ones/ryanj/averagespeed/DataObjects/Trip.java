package nz.ones.ryanj.averagespeed.DataObjects;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import nz.ones.ryanj.averagespeed.DatabaseHandler;

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
    private String _averageSpeed;
    private String _distance;

    public Trip(String Name, Point StartingPoint)
    {
        this._name = Name;
        this._startTime = StartingPoint.Time();
        //Points.add(StartingPoint);
    }

    public Trip(int ID ,String Name, Point StartingPoint)
    {
        this._id = ID;
        this._name = Name;
        this._startTime = StartingPoint.Time();
        //Points.add(StartingPoint);
    }

    public Trip(int ID, String Name, String StartTime, String EndTime, String AverageSpeed, String Distance)
    {
        Log.d(DEBUG_TAG, "Creating Trip: " + ID + ", " + Name + ", " + StartTime + ", " + EndTime + ", " + AverageSpeed + ", " + Distance);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        try {
            this._id = ID;
            this._name = Name;
            this._startTime = dateFormat.parse(StartTime);
            if (EndTime != null)
                this._endTime = dateFormat.parse(EndTime);
            this._averageSpeed = AverageSpeed;
            this._distance = Distance;
        }
        catch (ParseException ex)
        {
            d(DEBUG_TAG, ex.getMessage());
        }
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
        //Points.add(endingPoint);
        _endTime = endingPoint.Time();
    }

    public void addPoint(Point point)
    {
        //Points.add(point);
    }

    /** General Methods **/
    public String getTimeDifference()
    {
        long duration  = _endTime.getTime() - _startTime.getTime();

        long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);

        return diffInHours + "h:"+diffInMinutes + "m:" + diffInSeconds + "s";
    }

}
