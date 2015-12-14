package nz.ones.ryanj.averagespeed;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.util.Log.d;

/**
 * Created by Ryan Jones on 14/12/2015.
 */
public class CustomListViewTrip extends Activity {

    private final String DEBUG_TAG =  "AverageSpeed." + getClass().getCanonicalName();

    ListView list;
    CustomAdapter adapter;
    public CustomListViewTrip CustomListView = null;
    public  ArrayList<ListModel> CustomListViewValuesArr = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_list_view_trip);

        /***Boring UI binding**/
        /*Floating Button*/
        FloatingActionButton floatingAdd = (FloatingActionButton) findViewById(R.id.floatingAdd);
        floatingAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                d(DEBUG_TAG, "Add button clicked");

                ListModel sched = new ListModel();
                sched.setCompanyName("Company ");
                sched.setImage("image");
                sched.setUrl("http:\\www.test.com");
                CustomListViewValuesArr.add(sched);

                refresh();
            }
        });

        CustomListView = this;
        refresh();
    }

    public void refresh()
    {
        d(DEBUG_TAG, "Refreshing");

        /******** Take some data in Arraylist ( CustomListViewValuesArr ) ***********/
        Resources res = getResources();
        list = (ListView)findViewById(R.id.tripList);

        /******** Create Custom Adapter *********/
        adapter = new CustomAdapter( CustomListView, CustomListViewValuesArr,res );
        list.setAdapter(adapter);
        list.deferNotifyDataSetChanged();
    }

    /*****************  This function used by adapter ****************/
    public void onItemClick(int mPosition)
    {
        ListModel tempValues = (ListModel) CustomListViewValuesArr.get(mPosition);

        // SHOW ALERT
        Toast.makeText(CustomListView, tempValues.getCompanyName() + " Image:"+tempValues.getImage()+"Url:"+tempValues.getUrl(),Toast.LENGTH_SHORT).show();
    }
}
