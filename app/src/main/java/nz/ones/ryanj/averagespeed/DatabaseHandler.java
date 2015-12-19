package nz.ones.ryanj.averagespeed;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import nz.ones.ryanj.averagespeed.DataObjects.Trip;

/**
 * Created by Ryan Jones on 20/12/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "tripAverage";
    // Table Name
    private static final String TABLE_TRIP = "trip";
    // Table Columns
    private static final String TRIP_ID = "id";
    private static final String TRIP_NAME = "name";
    private static final String TRIP_START_TIME = "starttime";
    private static final String TRIP_END_TIME = "endtime";
    private static final String TRIP_AVERAGE = "averagespeed";
    private static final String TRIP_DISTANCE = "distance";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_TRIP_TABLE = "CREATE TABLE " + TABLE_TRIP + "("
                + TRIP_ID + " INTEGER PRIMARY KEY, "
                + TRIP_NAME + " TEXT NOT NULL, "
                + TRIP_START_TIME + " TEXT NOT NULL, "
                + TRIP_END_TIME + " TEXT, "
                + TRIP_AVERAGE + " REAL, "
                + TRIP_DISTANCE + " REAL" + ")";

        db.execSQL(CREATE_TRIP_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIP);

        // Create tables again
        onCreate(db);
    }

    /******** Trip DB Operations ********/
    public void addTrip(Trip t)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.sss");

        ContentValues values = new ContentValues();
        values.put(TRIP_NAME, t.Name());
        values.put(TRIP_START_TIME, dateFormat.format(t.StartTime()));

        db.insert(TABLE_TRIP, null, values);
        db.close();
    }
    public Trip getTrip(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TRIP, new String[]{TRIP_ID, TRIP_NAME, TRIP_START_TIME,
                        TRIP_END_TIME, TRIP_AVERAGE, TRIP_DISTANCE}, TRIP_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);

        if(cursor != null)
            cursor.moveToFirst();

        Trip t = new Trip(Integer.parseInt((cursor.getString(0))), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(4), cursor.getString(5));
        return t;
    }
    public ArrayList<Trip> getAllTrips()
    {
        ArrayList<Trip> tripList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TRIP;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst())
        {
            do
            {
                Trip t = new Trip(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3)
                        , cursor.getString(4), cursor.getString(5));
                tripList.add(t);
            } while (cursor.moveToNext());
        }

        return tripList;
    }
    public int getTripCount()
    {
        String countQuery = "SELECT * FROM " + TABLE_TRIP;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int c = cursor.getCount();
        cursor.close();

        return c;
    }
    public int updateTrip(Trip t)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

        ContentValues values = new ContentValues();
        values.put(TRIP_NAME, t.Name());
        values.put(TRIP_START_TIME, dateFormat.format(t.StartTime()));
        values.put(TRIP_END_TIME, dateFormat.format(t.EndTime()));
        values.put(TRIP_AVERAGE, t.AverageSpeed());
        values.put(TRIP_DISTANCE, t.Distance());

        return db.update(TABLE_TRIP, values, TRIP_ID + "=?",
                new String[] {(String.valueOf(t.ID()))});
    }
    public void deleteTrip(Trip t)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRIP, TRIP_ID + " =?",
                new String[] {(String.valueOf(t.ID()))});
        db.close();
    }

}
