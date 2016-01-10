package nz.ones.ryanj.averagespeed.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import nz.ones.ryanj.averagespeed.R;

public class ActivityNewTrip extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_new_trip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /********Boring UI binding********/
        /*Start Trip Button*/
        Button startButton = (Button)findViewById(R.id.buttonStartTrip);
        startButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v){
                
            }
        });
    }

}
