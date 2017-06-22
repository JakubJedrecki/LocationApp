package example.app.location.locationapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import example.app.location.locationapp.models.Journey;
import example.app.location.locationapp.models.JourneyLocation;
import example.app.location.locationapp.util.Constants;

public class DatabaseManager{

    private SQLiteDatabase database;

    /**
     * Interactor constructor
     * @param context
     */
    public DatabaseManager(Context context) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        database = dbHelper.getWritableDatabase();
    }

    /**
     * Adds passed journey to database.
     * @param journey journey to add
     * @return journey id
     */
    public long createJourney(Journey journey){
        ContentValues values = new ContentValues();
        values.put(Constants.TIME_START, journey.getTimeStart());
        if(journey.getTimeEnd() != null){
            values.put(Constants.TIME_END, journey.getTimeEnd());
        }

        long journeyId = database.insert(Constants.JOURNEYS_TABLE, null, values);
        return journeyId;
    }

    /**
     * update journey end time
     * @param journeyId id of journey to update
     * @param endTimestamp timestamp
     */
    public void updateJourneyEndTime(long journeyId, long endTimestamp) {
        ContentValues values = new ContentValues();
        values.put(Constants.TIME_END, endTimestamp);

        database.update(Constants.JOURNEYS_TABLE, values, Constants.ID + "=" + journeyId, null );
    }

    /**
     * add new location to database
     * @param location JourneyLocation object to save to database
     * @return id of added location
     */
    public long addLocation(JourneyLocation location){
        ContentValues values = new ContentValues();
        values.put(Constants.JOURNEY_ID, location.getJourneyId());
        values.put(Constants.LAT_COLUMN, location.getLatLng().latitude);
        values.put(Constants.LNG_COLUMN, location.getLatLng().longitude);
        values.put(Constants.TIMESTAMP, location.getTimestamp());

        long locationId = database.insert(Constants.LOCATIONS_TABLE, null, values);
        return locationId;
    }
}
