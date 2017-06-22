package example.app.location.locationapp.ui.journeydetails;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import example.app.location.locationapp.data.DatabaseHelper;
import example.app.location.locationapp.models.Journey;
import example.app.location.locationapp.models.JourneyLocation;
import example.app.location.locationapp.util.Constants;

public class JourneyDetailsActivityInteractor implements JourneyDetailsActivityContract.Interactor {

    private SQLiteDatabase database;

    /**
     * Interactor constructor
     * @param context
     */
    public JourneyDetailsActivityInteractor(Context context) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        database = dbHelper.getWritableDatabase();
    }

    /**
     * Get all locations for journey
     * @param journeyId id of journey to get locations
     * @return List<JourneyLocation> list of locations for journey
     */
    @Override
    public List<JourneyLocation> getJourneyLocations(int journeyId) {
        List<JourneyLocation> locations = new ArrayList<>();
        String selectQuery = String.format("SELECT * FROM %1$s WHERE %2$s = %3$d order by %4$s", Constants.LOCATIONS_TABLE, Constants.JOURNEY_ID, journeyId, Constants.TIMESTAMP);

        Cursor cursor = database.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do {
                JourneyLocation location = new JourneyLocation();
                location.setId(cursor.getInt(cursor.getColumnIndex(Constants.ID)));
                location.setJourneyId(cursor.getInt(cursor.getColumnIndex(Constants.JOURNEY_ID)));
                location.setLatLng(new LatLng(cursor.getDouble(cursor.getColumnIndex(Constants.LAT_COLUMN)), cursor.getDouble(cursor.getColumnIndex(Constants.LNG_COLUMN))));
                location.setTimestamp(cursor.getLong(cursor.getColumnIndex(Constants.TIMESTAMP)));

                locations.add(location);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return locations;
    }

    /**
     * Get all details of journey
     * @param journeyId
     * @return Journey data
     */
    @Override
    public Journey getJourneyDetails(int journeyId) {
        String selectQuery = String.format("SELECT * FROM %1$s WHERE %2$s = %3$d", Constants.JOURNEYS_TABLE, Constants.ID, journeyId);

        Cursor cursor = database.rawQuery(selectQuery, null);

        if(cursor.getCount() >0){
            cursor.moveToFirst();
            return new Journey(cursor.getLong(cursor.getColumnIndex(Constants.TIME_START)), cursor.getLong(cursor.getColumnIndex(Constants.TIME_END)));
        }

        return null;
    }


}
