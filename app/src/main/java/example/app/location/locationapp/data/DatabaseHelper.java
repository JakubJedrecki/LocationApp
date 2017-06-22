package example.app.location.locationapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import example.app.location.locationapp.util.Constants;

import static example.app.location.locationapp.util.Constants.TIME_END;
import static example.app.location.locationapp.util.Constants.TIME_START;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Journeys.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_JOURNEYS = "CREATE TABLE %1$s ( %2$s INTEGER PRIMARY KEY AUTOINCREMENT, %3$s INTEGER, %4$s INTEGER)";
    private static final String CREATE_TABLE_LATLNGS = "CREATE TABLE %1$s ( %2$s INTEGER PRIMARY KEY AUTOINCREMENT, %3$s INTEGER, %4$s REAL, %5$s REAL, %6$s INTEGER)";
    private static DatabaseHelper instance;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Get DatabaseHelper instance
     * @param context application context
     * @return DatabaseHelper instance
     */
    public static synchronized DatabaseHelper getInstance(Context context) {
        if(instance == null) {
            instance = new DatabaseHelper(context);
        }

        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(String.format(CREATE_TABLE_JOURNEYS, Constants.JOURNEYS_TABLE, Constants.ID, TIME_START, TIME_END));
        sqLiteDatabase.execSQL(String.format(CREATE_TABLE_LATLNGS, Constants.LOCATIONS_TABLE, Constants.ID, Constants.JOURNEY_ID, Constants.LAT_COLUMN, Constants.LNG_COLUMN, Constants.TIMESTAMP));
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Constants.JOURNEYS_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Constants.LOCATIONS_TABLE);

        onCreate(sqLiteDatabase);
    }
}
