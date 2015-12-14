package nz.ones.ryanj.averagespeed;

/**
 * Created by Ryan Jones on 13/12/2015.
 */
public class ListTrip
{
    private  String Name ="";
    private  String TripDistance ="";
    private  String TripTime ="";

    /*********** Set Methods ******************/

    public void setName(String Name)
    {
        this.Name = Name;
    }

    public void setTripDistance(String TripDistance)
    {
        this.TripDistance = TripDistance;
    }

    public void setTripTime(String TripTime)
    {
        this.TripTime = TripTime;
    }

    /*********** Get Methods ****************/

    public String getName()
    {
        return this.Name;
    }

    public String getTripDistance()
    {
        return this.TripDistance;
    }

    public String getTripTime()
    {
        return this.TripTime;
    }
}
