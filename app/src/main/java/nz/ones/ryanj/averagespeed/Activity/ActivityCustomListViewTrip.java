package nz.ones.ryanj.averagespeed.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import nz.ones.ryanj.averagespeed.CustomTripAdapter;
import nz.ones.ryanj.averagespeed.DatabaseHandler;
import nz.ones.ryanj.averagespeed.R;
import nz.ones.ryanj.averagespeed.DataObjects.Trip;

import static android.util.Log.d;

/**
 * Created by Ryan Jones on 14/12/2015.
 */
public class ActivityCustomListViewTrip extends AppCompatActivity{

    private final String DEBUG_TAG =  "AverageSpeed." + getClass().getCanonicalName();
    private final int REQUEST_GPS = 0;

    private ListView list;
    private CustomTripAdapter adapter;
    public ActivityCustomListViewTrip CustomListView = null;
    private ArrayList<Trip> trips = null;
    private DatabaseHandler db;
    private View mLayout;

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

                boolean b = checkForPermission(Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_GPS);
                if (b)
                {
                    d(DEBUG_TAG, "Starting new trip Intent");
                    Intent i = new Intent(getBaseContext(), ActivityNewTrip.class);
                    startActivity(i);
                }
            }
        });

        CustomListView = this;
        refresh();
    }

    @Override
    public void onRestart()
    {
        refresh();
        super.onRestart();
    }

    public void refresh()
    {
        d(DEBUG_TAG, "Refreshing");
        /******** Take some data in Array list ********/
        Resources res = getResources();
        list = (ListView)findViewById(R.id.tripList);

        /******** Create Custom Adapter *********/
        d(DEBUG_TAG, "Fetching " + db.getTripCount() + " trips");
        trips = db.getAllTrips();
        adapter = new CustomTripAdapter(CustomListView, trips, res);
        list.setAdapter(adapter);
        list.deferNotifyDataSetChanged();
    }

    /********  This function used by adapter ********/
    public void onItemClick(int mPosition)
    {
        Trip tempTrip = trips.get(mPosition);
        d(DEBUG_TAG, "Opening Trip:" + mPosition + " " + tempTrip.Name());
        Intent i = new Intent(getBaseContext(), ActivityDisplayTrip.class);
        i.putExtra("TRIP_ID", tempTrip.ID());
        startActivity(i);
    }

    private boolean checkForPermission(final String permission, final int RequestCode)
    {
        boolean b = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            d(DEBUG_TAG, "We are running on 6.0");
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
            {
                d(DEBUG_TAG, "Don't have " + permission +". Asking for it.");
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    d(DEBUG_TAG, "Asking for permission with rational.");
                    Snackbar.make(mLayout, "The App needs " + permission + " to work correctly.",
                            Snackbar.LENGTH_INDEFINITE).setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(ActivityCustomListViewTrip.this, new String[]{permission}, RequestCode);
                        }
                    }).show();
                    b = false;
                } else {
                    d(DEBUG_TAG, "Asking for permission directly.");
                    // Permission has not been granted yet. Request it directly.
                    b = false;      // Don't return value directly after this as gets skipped over
                    ActivityCompat.requestPermissions(this, new String[]{permission},
                            RequestCode);
                }
            }
        }
        return b;
    }
}
