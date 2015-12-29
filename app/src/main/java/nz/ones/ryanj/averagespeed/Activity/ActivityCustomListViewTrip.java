package nz.ones.ryanj.averagespeed.Activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;

import nz.ones.ryanj.averagespeed.AdapterCustom;
import nz.ones.ryanj.averagespeed.DataObjects.Point;
import nz.ones.ryanj.averagespeed.DatabaseHandler;
import nz.ones.ryanj.averagespeed.R;
import nz.ones.ryanj.averagespeed.DataObjects.Trip;

import static android.util.Log.d;

/**
 * Created by Ryan Jones on 14/12/2015.
 */
public class ActivityCustomListViewTrip extends AppCompatActivity{

    private final String DEBUG_TAG =  "AverageSpeed." + getClass().getCanonicalName();

    ListView list;
    AdapterCustom adapter;
    public ActivityCustomListViewTrip CustomListView = null;
    DatabaseHandler db;
    int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_list_view_trip);
        db = new DatabaseHandler(getBaseContext());

        /********Boring UI binding********/
        /*Floating Button*/
        FloatingActionButton floatingAdd = (FloatingActionButton) findViewById(R.id.floatingAdd);
        floatingAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d(DEBUG_TAG, "Add button clicked");

                Intent i = new Intent(getBaseContext(), ActivityNewTrip.class);
                startActivity(i);
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
        d(DEBUG_TAG, "Fetching " + db.getTripCount() + " trips");
        ArrayList<Trip> trips = db.getAllTrips();
        adapter = new AdapterCustom(CustomListView, trips, res);
        list.setAdapter(adapter);
        list.deferNotifyDataSetChanged();

    }

    /********  This function used by adapter ********/
    public void onItemClick(int mPosition)
    {
        Trip tempTrip = db.getTrip(mPosition);

        d(DEBUG_TAG, "Opening Trip:" + mPosition + " " + tempTrip.Name());
        Intent i = new Intent(getBaseContext(), ActivityDisplayTrip.class);
        i.putExtra("TRIP_ID", mPosition);
        startActivity(i);
    }
}
