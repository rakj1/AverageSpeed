package nz.ones.ryanj.averagespeed;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import static android.util.Log.d;

/**
 * Created by Ryan Jones on 14/12/2015.
 */
public class CustomListViewTrip extends AppCompatActivity{

    private final String DEBUG_TAG =  "AverageSpeed." + getClass().getCanonicalName();

    ListView list;
    CustomAdapter adapter;
    public CustomListViewTrip CustomListView = null;
    public  ArrayList<Trip> CustomListViewValuesArr = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_list_view_trip);

        /********Boring UI binding********/
        /*Floating Button*/
        FloatingActionButton floatingAdd = (FloatingActionButton) findViewById(R.id.floatingAdd);
        floatingAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                d(DEBUG_TAG, "Add button clicked");

                Calendar c = Calendar.getInstance();
                Point p = new Point(c.getTime(), 1343.6456, 4326.432);
                Trip trip = new Trip("Trip", p);

                c = Calendar.getInstance();
                trip.addPoint(new Point(c.getTime(), 1345.6456, 4328.432));

                c = Calendar.getInstance();
                trip.endTrip(new Point(c.getTime(), 1349.6456, 4329.432));

                CustomListViewValuesArr.add(trip);

                refresh();
            }
        });

        CustomListView = this;
        refresh();
    }

    public void refresh()
    {
        d(DEBUG_TAG, "Refreshing");

        /******** Take some data in Array list ********/
        Resources res = getResources();
        list = (ListView)findViewById(R.id.tripList);

        /******** Create Custom Adapter *********/
        adapter = new CustomAdapter( CustomListView, CustomListViewValuesArr,res );
        list.setAdapter(adapter);
        list.deferNotifyDataSetChanged();
    }

    /********  This function used by adapter ********/
    public void onItemClick(int mPosition)
    {
        Trip tempValues = CustomListViewValuesArr.get(mPosition);

        // SHOW ALERT
        Toast.makeText(CustomListView, tempValues.Name() + " Distance: "+tempValues.Distance()+"Average Speed: "+tempValues.AverageSpeed(),Toast.LENGTH_SHORT).show();
    }
}
