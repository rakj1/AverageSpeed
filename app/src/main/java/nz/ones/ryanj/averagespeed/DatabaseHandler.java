package nz.ones.ryanj.averagespeed;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import nz.ones.ryanj.averagespeed.DataObjects.Point;
import nz.ones.ryanj.averagespeed.DataObjects.Trip;

import static android.util.Log.d;

/**
 * Created by Ryan Jones on 20/12/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper
{
    private final String DEBUG_TAG =  "AverageSpeed." + getClass().getCanonicalName();

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "tripAverage";
    // Table Name
    private static final String TABLE_TRIP = "trip";
    private static final String TABLE_POINT = "point";
    // Trip Table Columns
    private static final String TRIP_ID = "id";
    private static final String TRIP_NAME = "name";
    private static final String TRIP_START_TIME = "starttime";
    private static final String TRIP_END_TIME = "endtime";
    private static final String TRIP_AVERAGE = "averagespeed";
    private static final String TRIP_DISTANCE = "distance";
    // Point Table Names
    private static final String POINT_ID = "id";
    private static final String POINT_TRIP_ID = "tripid";
    private static final String POINT_TIME = "time";
    private static final String POINT_LAT = "lat";
    private static final String POINT_LONG = "long";
    // Trip Point Table Names


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

        String CREATE_POINT_TABLE = "CREATE TABLE " + TABLE_POINT + "("
                + POINT_ID + " INTEGER PRIMARY KEY, "
                + POINT_TRIP_ID + " INTEGER, "
                + POINT_TIME + " TEXT NOT NULL, "
                + POINT_LAT + " REAL NOT NULL, "
                + POINT_LONG + " REAL NOT NULL" + ")";

        db.execSQL(CREATE_TRIP_TABLE);
        db.execSQL(CREATE_POINT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POINT);

        // Create tables again
        onCreate(db);
    }

    /******** Trip DB Operations ********/
    public long addTrip(Trip t)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.sss");

        ContentValues values = new ContentValues();
        values.put(TRIP_NAME, t.Name());
        values.put(TRIP_START_TIME, dateFormat.format(t.StartTime()));

        long id = db.insert(TABLE_TRIP, null, values);
        db.close();
        return id;
    }
    public Trip getTrip(long id)
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
            do {
                Trip t = new Trip(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                        cursor.getString(4), cursor.getString(5));
                tripList.add(t);
            } while (cursor.moveToNext());

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
                new String[]{(String.valueOf(t.ID()))});
        db.close();
    }

    /***** Points DB Operations *****/
    public void addPoint(Point p)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.sss");

        ContentValues values = new ContentValues();
        values.put(POINT_TRIP_ID, p.ID());
        values.put(POINT_TIME, dateFormat.format(p.Time()));
        values.put(POINT_LAT, p.Latitude());
        values.put(POINT_LONG, p.Longitude());

        db.insert(TABLE_POINT, null, values);
        db.close();
    }
    public Point getPoint(long id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.sss");

        Cursor cursor = db.query(TABLE_POINT, new String[]{POINT_ID, POINT_TIME, POINT_LAT,
                        POINT_LONG}, POINT_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);

        if(cursor != null)
            cursor.moveToFirst();

        Point p = null;
        try {
            p = new Point(Long.parseLong(cursor.getString(0)), dateFormat.parse(cursor.getString(1)), Double.parseDouble(cursor.getString(2))
                    , Double.parseDouble(cursor.getString(3)));
        }
        catch (ParseException ex)
        {
            d(DEBUG_TAG, ex.getMessage());
        }
        return p;
    }
    public ArrayList<Point> getAllPoints(int tripId)
    {
        ArrayList<Point> pointList = new ArrayList<>();
        String selectQuery = "SELECT + " + POINT_ID + ", " + POINT_TIME + ", " + POINT_LAT + ", " + POINT_LONG + "  FROM " + TABLE_POINT + " WHERE " + POINT_TRIP_ID + " = " + tripId;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.sss");

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst())
        {
            do
            {
                try {
                    d(DEBUG_TAG, "Getting point from DB: " + cursor.getString(0) + ": " + cursor.getString(1) + ": " + cursor.getString(2)+ ": " + cursor.getString(3) + "");
                    Point p = new Point(Integer.parseInt(cursor.getString(0)), dateFormat.parse(cursor.getString(1)), Double.parseDouble(cursor.getString(2)),
                            Double.parseDouble(cursor.getString(3)));
                    pointList.add(p);
                }
                catch (ParseException ex)
                {
                    d(DEBUG_TAG, ex.getMessage());
                }
            } while (cursor.moveToNext());
        }

        return pointList;
    }
    public int getPointCount(int tripId)
    {
        String countQuery = "SELECT * FROM " + TABLE_POINT+ "WHERE " + POINT_TRIP_ID + " = " + tripId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int c = cursor.getCount();
        cursor.close();

        return c;
    }
    public int updatePoint(Point p)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

        ContentValues values = new ContentValues();
        values.put(POINT_TIME, dateFormat.format(p.Time()));
        values.put(POINT_LAT, p.Latitude());
        values.put(POINT_LONG, p.Longitude());

        return db.update(TABLE_POINT, values, POINT_ID + "=?",
                new String[] {(String.valueOf(p.ID()))});
    }
    public void deletePoint(Point p)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_POINT, POINT_ID+ " =?",
                new String[]{(String.valueOf(p.ID()))});
        db.close();
    }

}
