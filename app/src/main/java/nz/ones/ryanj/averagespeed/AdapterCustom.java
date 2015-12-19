package nz.ones.ryanj.averagespeed;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import nz.ones.ryanj.averagespeed.Activity.ActivityCustomListViewTrip;
import nz.ones.ryanj.averagespeed.DataObjects.Trip;

/**
 * Created by Ryan Jones on 13/12/2015.
 */
public class AdapterCustom extends BaseAdapter implements View.OnClickListener
{
    /*********** Declare Used Variables *********/
    private Activity activity;
    private ArrayList data;
    private static LayoutInflater inflater=null;
    public Resources res;
    Trip tempValues = null;
    int i=0;

    /*************  AdapterCustom Constructor *****************/
    public AdapterCustom(Activity a, ArrayList d, Resources resLocal) {

        /********** Take passed values **********/
        activity = a;
        data=d;
        res = resLocal;

        /***********  Layout inflator to call external xml layout () ***********/
        inflater = ( LayoutInflater )activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    /******** What is the size of Passed Arraylist Size ************/
    public int getCount() {

        if(data.size()<=0)
            return 1;
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }
    public long getItemId(int position) {
        return position;
    }

    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{

        public TextView name;
        public TextView time;
        public TextView distance;
        public ImageView image;
    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if(convertView == null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.tabitem, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/
            holder = new ViewHolder();
            holder.name = (TextView) vi.findViewById(R.id.tripName);
            holder.time =(TextView)vi.findViewById(R.id.tripTime);
            holder.distance =(TextView)vi.findViewById(R.id.tripDistance);
            holder.image=(ImageView)vi.findViewById(R.id.image);

            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();

        if(data.size()<=0)
        {
            holder.name.setText("No Data");
        }
        else
        {
            /***** Get each Model object from Arraylist ********/
            tempValues=null;
            tempValues = (Trip) data.get( position );

            /************  Set Model values in Holder elements ***********/
            holder.name.setText(tempValues.Name());
            holder.time.setText(tempValues.AverageSpeed());
            holder.distance.setText(tempValues.Distance());
            holder.image.setImageResource(
                    res.getIdentifier("com.androidexample.customlistview:drawable/"+tempValues.Distance(),null,null));

            /******** Set Item Click Listner for LayoutInflater for each row *******/
            vi.setOnClickListener(new OnItemClickListener( position ));
        }
        return vi;
    }

    @Override
    public void onClick(View v) {
        Log.v("AdapterCustom", "=====Row button clicked=====");
    }

    /********* Called when Item click in ListView ************/
    private class OnItemClickListener  implements View.OnClickListener {
        private int mPosition;

        OnItemClickListener(int position){
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {

            ActivityCustomListViewTrip sct = (ActivityCustomListViewTrip)activity;

            /****  Call  onItemClick Method inside ActivityCustomListViewTrip Class ( See Below )****/
            sct.onItemClick(mPosition);
        }
    }
}
