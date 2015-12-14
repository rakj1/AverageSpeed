package nz.ones.ryanj.averagespeed;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Ryan Jones on 14/12/2015.
 */
public class CustomListViewTrip extends Activity {

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

                Toast.makeText(getBaseContext(), "add clicked:", Toast.LENGTH_SHORT).show();

            }

        });

        CustomListView = this;

        /******** Take some data in Arraylist ( CustomListViewValuesArr ) ***********/
        Resources res = getResources();
        list= (ListView)findViewById(R.id.list);  // List defined in XML ( See Below )

        /**************** Create Custom Adapter *********/
        adapter = new CustomAdapter( CustomListView, CustomListViewValuesArr,res );
        list.setAdapter(adapter);

    }

    public void refresh()
    {
        
    }

    /*****************  This function used by adapter ****************/
    public void onItemClick(int mPosition)
    {
        ListModel tempValues = (ListModel) CustomListViewValuesArr.get(mPosition);

        // SHOW ALERT
        Toast.makeText(CustomListView, tempValues.getCompanyName() + " Image:"+tempValues.getImage()+"Url:"+tempValues.getUrl(),Toast.LENGTH_SHORT).show();
    }
}
