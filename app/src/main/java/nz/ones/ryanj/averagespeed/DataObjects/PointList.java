package nz.ones.ryanj.averagespeed.DataObjects;

import java.util.ArrayList;

/**
 * Created by Ryan Jones on 17/12/2015.
 */
public class PointList
{
    private ArrayList<Point> _points;

    public PointList()
    {
        _points = new ArrayList<>();
    }

    public void add(Point point)
    {
        _points.add(point);
    }

    @Override
    public String toString()
    {
        String s = "[";
        for (Point p: _points)
        {
            s += p.toJSONString() + ", ";
        }
        s = s.substring(0, s.length()-2);
        s += "]";
        return s;
    }
}
