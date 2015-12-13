package nz.ones.ryanj.averagespeed;

/**
 * Created by Ryan Jones on 13/12/2015.
 */
public class ListModel
{
    private String TripTime = "";
    private String TripDistance = "";
    private String TripName = "";

    /*********** Set Methods ******************/
    public void setTripTime(String Time)
    {
        this.TripTime = Time;
    }

    public void setTripDistance(String Distance)
    {
        this.TripDistance = Distance;
    }

    public void setTripName(String Name)
    {
        this.TripName = Name;
    }

    /*********** Get Methods ****************/
    public String getTripTime()
    {
        return TripTime;
    }

    public String getTripDistance()
    {
        return TripDistance;
    }

    public String getTripName()
    {
        return TripName;
    }
}
