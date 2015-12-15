package nz.ones.ryanj.averagespeed;

import java.util.Date;

/**
 * Created by Ryan Jones on 15/12/2015.
 */
public class Point
{
    private Date Time;
    private Double Longitude;
    private Double Latitude;

    public Point(Date Time, Double Long, Double Lat)
    {
        this.Time = Time;
        this.Longitude = Long;
        this.Latitude = Lat;
    }

    public Date Time() {return Time;}
    public Double Longitude() {return Longitude;}
    public Double Latitude() {return Latitude;}

}
