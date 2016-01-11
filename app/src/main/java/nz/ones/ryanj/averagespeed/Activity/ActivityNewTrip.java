package nz.ones.ryanj.averagespeed.Activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
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

public class ActivityNewTrip extends AppCompatActivity {

    private final String DEBUG_TAG = "AverageSpeed." + getClass().getCanonicalName();

    private final static int INTERVAL = 1000 * 20;     //20 Seconds
    private static final int REQUEST_GPS = 0;
    final Handler h = new Handler();
    private Trip currentTrip;
    private long tripId;
    private View mLayout;

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
                Log.d(DEBUG_TAG, "Ending trip. Getting last point and adding to database");
                Point p = getCurrentPoint();
                db.addPoint(new Point(tripId, p.Time(), p.Longitude(), p.Latitude()));
                currentTrip.endTrip(p);
                finish();
            }
        });

        //Create a new trip and add to the db getting the ID of the trip
        String tripName = "trip: " + Calendar.getInstance().getTime().toString();
        Log.d(DEBUG_TAG, "Starting trip \"" + tripName + "\" and adding to Database");
        Point startingPoint = getCurrentPoint();
        currentTrip = new Trip(tripName, startingPoint);

        tripId = db.addTrip(currentTrip);
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Get co-ordinates and time to add it as a point
                h.postDelayed(this, INTERVAL);
                Log.d(DEBUG_TAG, "Getting current point and adding to Database");
                Point p = getCurrentPoint();
                db.addPoint(new Point(tripId, p.Time(), p.Longitude(), p.Latitude()));
                currentTrip.addPoint(p);
            }
        }, INTERVAL);
    }

    @Override
    public void onBackPressed()
    {
        // Double check the user wants to exit
        
    }

    public Point getCurrentPoint() {
        Date startTime = (Calendar.getInstance().getTime());

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                requestGPSPermissions(Manifest.permission.ACCESS_FINE_LOCATION);
                requestGPSPermissions(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Double latitude = location.getLatitude();
        Double longitude = location.getLongitude();

        Point point = new Point(startTime, latitude, longitude);

        return point;
    }

    private void requestGPSPermissions(final String permission) {
        Log.d(DEBUG_TAG, "GPS Permission has not been granted. Requesting permission");

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            Snackbar.make(mLayout, "The App needs access to the GPS to get the length of the trip",
                    Snackbar.LENGTH_INDEFINITE).setAction("Ok", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityCompat.requestPermissions(ActivityNewTrip.this, new String[]{permission}, REQUEST_GPS);
                }
            }).show();

        } else {
            // Permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{permission},
                    REQUEST_GPS);
        }
    }
}
