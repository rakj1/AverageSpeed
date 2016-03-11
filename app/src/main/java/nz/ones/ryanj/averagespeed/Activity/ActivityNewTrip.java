package nz.ones.ryanj.averagespeed.Activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import nz.ones.ryanj.averagespeed.DataObjects.Point;
import nz.ones.ryanj.averagespeed.DataObjects.Trip;
import nz.ones.ryanj.averagespeed.DatabaseHandler;
import nz.ones.ryanj.averagespeed.R;
import nz.ones.ryanj.averagespeed.Util.Constants;

import static android.util.Log.d;

public class ActivityNewTrip extends AppCompatActivity implements LocationListener {

    private final String DEBUG_TAG = "AverageSpeed." + getClass().getCanonicalName();

    private TextView latitude;
    private TextView longitude;
    private LocationManager locationManager;
    private Location lastLocation;
    private String provider;

    private Trip currentTrip;
    private long tripId;
    private Point lastPoint;
    private boolean firstPoint = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_new_trip);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        latitude = (TextView) findViewById(R.id.textViewLat);
        longitude = (TextView) findViewById(R.id.textViewLong);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /********Boring UI binding********/
        /*End Trip Button*/
        Button endButton = (Button) findViewById(R.id.buttonEndTrip);
        endButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                endTrip();
            }
        });
        /********Boring UI binding********/

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            d(DEBUG_TAG, "Can't get GPS permission");
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            d(DEBUG_TAG, "Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
            latitude.setText("Location not available");
            longitude.setText("Location not available");
            ExitWithError("Location not available");
        }

        DatabaseHandler db = new DatabaseHandler(getBaseContext());
        String tripName = "trip: " + Calendar.getInstance().getTime().toString();
        d(DEBUG_TAG, "Starting trip \"" + tripName + "\" and adding to Database");
        Point startingPoint = getCurrentPoint(Calendar.getInstance().getTime(), location);
        currentTrip = new Trip(tripName, startingPoint);
        tripId = db.addTrip(currentTrip);
    }

    private Point getCurrentPoint(Date time, Location location) {
        Double latitude = location.getLatitude();
        Double longitude = location.getLongitude();
        Point point = new Point(time, latitude, longitude);
        lastPoint = point;
        return point;
    }

    private void addPoint(DatabaseHandler databaseHandler, Location location)
    {
        Date time = Calendar.getInstance().getTime();
        lastLocation = location;
        //Check if the previous point was added within 20 seconds of this one. If it was don't add the new one.
        if (lastPoint != null &&((time.getTime() - lastPoint.Time().getTime()/1000) < 20)) {
            d(DEBUG_TAG, "Point was within 20 seconds of last one not adding it");
            return;
        }
        d(DEBUG_TAG, "Trip " + tripId + ": Getting current point and adding to Database");
        Point p = getCurrentPoint(time ,location);
        databaseHandler.addPoint(new Point(tripId, p.Time(), p.Longitude(), p.Latitude()));
        if (!firstPoint)
            currentTrip.addPoint(p);
    }

    private void endTrip() {
        DatabaseHandler db = new DatabaseHandler(getBaseContext());
        d(DEBUG_TAG, "Ending trip:" + currentTrip.ID() + ": Getting last point and adding to database");
        Point p = getCurrentPoint(Calendar.getInstance().getTime(), lastLocation);
        db.addPoint(new Point(tripId, p.Time(), p.Longitude(), p.Latitude()));
        currentTrip = db.getTrip(tripId);
        currentTrip.endTrip(p);
        db.updateTrip(currentTrip);
        finish();
    }

    private void ExitWithError(String message)
    {
        d(DEBUG_TAG, message);
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void onBackPressed() {
        d(DEBUG_TAG, "User pressed back button");
        // Double check the user wants to exit
        new AlertDialog.Builder(this)
                .setTitle("End Trip")
                .setMessage("Do you really want to end this trip?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        endTrip();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(provider, Constants.TIME_INTERVAL, 20, this);
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        float lat = (float) (location.getLatitude());
        float lng = (float) (location.getLongitude());
        latitude.setText(String.valueOf(lat));
        longitude.setText(String.valueOf(lng));
        addPoint(new DatabaseHandler(this), location);
        firstPoint = false;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }
}
