package nz.ones.ryanj.averagespeed.Activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;
import java.util.Date;

import nz.ones.ryanj.averagespeed.DataObjects.Point;
import nz.ones.ryanj.averagespeed.DataObjects.Trip;
import nz.ones.ryanj.averagespeed.DatabaseHandler;
import nz.ones.ryanj.averagespeed.R;

import static android.util.Log.d;

public class ActivityNewTrip extends AppCompatActivity {

    private final String DEBUG_TAG = "AverageSpeed." + getClass().getCanonicalName();

    private final static int INTERVAL = 1000 * 20;     //20 Seconds
    final Handler h = new Handler();

    private Trip currentTrip;
    private long tripId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_new_trip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Final may break this
        final DatabaseHandler db = new DatabaseHandler(getBaseContext());

        /********Boring UI binding********/
        /*Start Trip Button*/
        Button endButton = (Button) findViewById(R.id.buttonEndTrip);
        endButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                h.removeCallbacksAndMessages(null);
                endTrip();
            }
        });

        //Create a new trip and add to the db getting the ID of the trip
        String tripName = "trip: " + Calendar.getInstance().getTime().toString();
        d(DEBUG_TAG, "Starting trip \"" + tripName + "\" and adding to Database");
        Point startingPoint = getCurrentPoint();
        currentTrip = new Trip(tripName, startingPoint);

        tripId = db.addTrip(currentTrip);
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Get co-ordinates and time to add it as a point
                h.postDelayed(this, INTERVAL);
                d(DEBUG_TAG, "Trip " + tripId + ": Getting current point and adding to Database");
                Point p = getCurrentPoint();
                db.addPoint(new Point(tripId, p.Time(), p.Longitude(), p.Latitude()));
                currentTrip.addPoint(p);
            }
        }, INTERVAL);
    }

    @Override
    public void onBackPressed() {
        d(DEBUG_TAG, "User pressed back button");
        // Double check the user wants to exit
        new AlertDialog.Builder(this)
                .setTitle("End Trip")
                .setMessage("Do you really want end this trip?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        endTrip();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    public void endTrip() {
        final DatabaseHandler db = new DatabaseHandler(getBaseContext());
        d(DEBUG_TAG, "Ending trip:" + currentTrip.ID() + ": Getting last point and adding to database");
        Point p = getCurrentPoint();
        db.addPoint(new Point(tripId, p.Time(), p.Longitude(), p.Latitude()));
        currentTrip = db.getTrip(tripId);
        currentTrip.endTrip(p);
        db.updateTrip(currentTrip);
        finish();
    }


    public Point getCurrentPoint() {
        Date startTime = (Calendar.getInstance().getTime());

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                error("Cannot get last location. Cannot start trip");
                finish();
                return null;
            }
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        //TODO: Deal with the LocationManager being null
        Double latitude = location.getLatitude();
        Double longitude = location.getLongitude();
        Point point = new Point(startTime, latitude, longitude);

        return point;
    }

    private void error(String errorMessage)
    {
        d(DEBUG_TAG, errorMessage);
        finish();
    }
}
