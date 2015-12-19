package nz.ones.ryanj.averagespeed.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.Format;
import java.text.SimpleDateFormat;

import nz.ones.ryanj.averagespeed.DatabaseHandler;
import nz.ones.ryanj.averagespeed.R;
import nz.ones.ryanj.averagespeed.DataObjects.Trip;

import static android.util.Log.d;

public class ActivityDisplayTrip extends AppCompatActivity {

    private final String DEBUG_TAG =  "AverageSpeed." + getClass().getCanonicalName();

    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_display_trip);
        db = new DatabaseHandler(getBaseContext());

        Bundle extras = getIntent().getExtras();
        if (extras == null)
        {
            d(DEBUG_TAG, "Can't find trip position");
            end("Can't find trip position");
        }
        Trip thisTrip = db.getTrip(extras.getInt("TRIP_ID"));

        /********Boring UI binding********/
        TextView Name = (TextView) findViewById(R.id.textViewName);
        Name.setText(thisTrip.Name());

        Format dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        TextView StartTime = (TextView) findViewById(R.id.textViewStartTime);
        //StartTime.setText(dateFormat.format(thisTrip.StartTime()));
        TextView EndTime = (TextView) findViewById(R.id.textViewEndTime);
        //EndTime.setText(dateFormat.format(thisTrip.EndTime()));
        TextView TripTime = (TextView) findViewById(R.id.tripTime);
        //TripTime.setText(TODO);

        TextView AverageSpeed = (TextView) findViewById(R.id.textViewAverage);
        //AverageSpeed.setText(TODO);
        TextView Distance = (TextView) findViewById(R.id.textViewDistance);
        //Distance.setText(TODO);

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
