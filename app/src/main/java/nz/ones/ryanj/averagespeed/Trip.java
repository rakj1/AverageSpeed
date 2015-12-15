package nz.ones.ryanj.averagespeed;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Ryan Jones on 15/12/2015.
 */
public class Trip
{
    private static String Name;
    private static Date StartTime;
    private static Date EndTime;
    private static ArrayList<Point> Points;
    private static String Distance;
    private static String AverageSpeed;

    private Trip(String Name, Point StartingPoint)
    {
        this.Name = Name;
        this.StartTime = StartingPoint.Time();
        Points = new ArrayList<>();
        Points.add(StartingPoint);
    }

    /**Getter methods**/
    public String Name() {return Name;}
    public Date StartTime() {return StartTime;}
    public Date EndTime() {return EndTime;}
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
        EndTime = endingPoint.Time();
    }

    public void addPoint(Point point)
    {
        Points.add(point);
    }

}
