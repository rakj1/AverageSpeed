package nz.ones.ryanj.averagespeed.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.Format;
import java.text.SimpleDateFormat;

import nz.ones.ryanj.averagespeed.DatabaseHandler;
import nz.ones.ryanj.averagespeed.R;
import nz.ones.ryanj.averagespeed.DataObjects.Trip;

import static android.util.Log.d;

public class ActivityViewTrip extends AppCompatActivity {

    private final String DEBUG_TAG =  "AverageSpeed." + getClass().getCanonicalName();
    private int tripId;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_view_trip);

        db = new DatabaseHandler(getBaseContext());

        Bundle extras = getIntent().getExtras();
        if (extras == null)
        {
            d(DEBUG_TAG, "Can't find trip position");
            end("Can't find trip position");
        }
        Trip thisTrip = db.getTrip(extras.getInt("TRIP_ID"));
        tripId = thisTrip.ID();

        /******** Show the action bar ********/
        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        mActionBar.show();

        refresh();

        /******** Button OnClicks ********/
        Button viewTrip = (Button) findViewById(R.id.buttonViewTripMap);
        viewTrip.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View v){
                Intent i = new Intent(getBaseContext(), ActivityViewTripOnMap.class);
                i.putExtra("TRIP_ID", tripId);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.activity_view_trip, menu);
        return super.onCreateOptionsMenu(menu);
        //return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_delete:
                // Delete the current trip
                confirmDelete();
                return true;

            case R.id.action_edit:
                // Edit the current trip
                editTrip(tripId);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void editTrip(final int TripId)
    {
        Trip t = db.getTrip(TripId);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set trip title");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        // Set the text to the current title
        input.setText(t.Name());
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Trip t = db.getTrip(TripId);
                t.editName(input.getText().toString());
                db.updateTrip(t);
                refresh();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void confirmDelete()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                db = new DatabaseHandler(getBaseContext());
                db.deleteTripAndPoints(tripId);
                dialog.dismiss();
                finish();
            }

        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void refresh()
    {
        Trip thisTrip = db.getTrip(tripId);
        /******** Boring UI binding ********/
        TextView Name = (TextView) findViewById(R.id.textViewName);
        Name.setText(thisTrip.Name());

        Format dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        TextView viewStartTime = (TextView) findViewById(R.id.textViewStartTime);
        viewStartTime.setText(dateFormat.format(thisTrip.StartTime()));
        TextView viewEndTime = (TextView) findViewById(R.id.textViewEndTime);
        viewEndTime.setText(dateFormat.format(thisTrip.EndTime()));
        TextView TripTime = (TextView) findViewById(R.id.textViewTotalTime);
        TripTime.setText(thisTrip.getTimeDifference());

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
