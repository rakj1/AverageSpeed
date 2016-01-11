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
import nz.ones.ryanj.averagespeed.R;

public class ActivityNewTrip extends AppCompatActivity {

    private final String DEBUG_TAG = "AverageSpeed." + getClass().getCanonicalName();

    private final static int INTERVAL = 1000 * 20;     //20 Seconds
    private static final int REQUEST_GPS = 0;
    private View mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_new_trip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /********Boring UI binding********/
        /*Start Trip Button*/
        Button endButton = (Button) findViewById(R.id.buttonEndTrip);
        endButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

            }
        });

        //Create a new trip and add to the db getting the ID of the trip
        String tripName = "tripX";

        Point startingPoint = getCurrentPoint();
        Trip currentTrip = new Trip(tripName, startingPoint);

        final Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Get co-ordinates and time to add it as a point
                h.postDelayed(this, INTERVAL);
            }
        }, INTERVAL);

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
        Log.i(DEBUG_TAG, "GPS Permission has not been granted. Requesting permission");

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
