package nz.ones.ryanj.averagespeed.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import nz.ones.ryanj.averagespeed.DataObjects.Point;
import nz.ones.ryanj.averagespeed.DataObjects.Trip;
import nz.ones.ryanj.averagespeed.DatabaseHandler;
import nz.ones.ryanj.averagespeed.R;

import static android.util.Log.d;

public class ActivityViewTripOnMap extends FragmentActivity implements OnMapReadyCallback {

    private final String DEBUG_TAG =  "AverageSpeed." + getClass().getCanonicalName();
    private DatabaseHandler db;
    private GoogleMap mMap;
    private int tripId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trip_on_map);

        db = new DatabaseHandler(getBaseContext());
        Bundle extras = getIntent().getExtras();
        if (extras == null)
        {
            d(DEBUG_TAG, "Can't find trip position");
            end("Can't find trip position");
        }
        tripId = extras.getInt("TRIP_ID");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add all the points to the map
        List<Point> points = db.getAllPoints(tripId);
        LatLng latLng = null;
        for(Point p: points)
        {
            latLng = new LatLng(p.Latitude(), p.Longitude());
            mMap.addMarker(new MarkerOptions().position(latLng));
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));
    }

    private void end(String errMsg)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(errMsg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        end();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

        end();
    }

    private void end()
    {
        finish();
    }
}
