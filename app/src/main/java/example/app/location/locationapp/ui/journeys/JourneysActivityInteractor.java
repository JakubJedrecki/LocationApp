package example.app.location.locationapp.ui.journeys;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import example.app.location.locationapp.data.DatabaseHelper;
import example.app.location.locationapp.models.Journey;
import example.app.location.locationapp.util.Constants;

/**
 * Created by Kuba on 18.06.2017.
 */

public class JourneysActivityInteractor implements JourneysActivityContract.Interactor {

    private SQLiteDatabase database;

    /**
     * Interactor constructor
     * @param context
     */
    public JourneysActivityInteractor(Context context) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        database = dbHelper.getWritableDatabase();
    }

    /**
     * Get all Journeys from database
     * @return List<Journey> list of journeys
     */
    public List<Journey> getAllJourneys(){
        List<Journey> journeys = new ArrayList<>();
        String selectQuery = String.format("SELECT * FROM %1$s order by %2$s desc ", Constants.JOURNEYS_TABLE, Constants.ID);

        Cursor cursor = database.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do {
                Journey journey = new Journey();
                journey.setId(cursor.getInt(cursor.getColumnIndex(Constants.ID)));
                journey.setTimeStart(cursor.getLong(cursor.getColumnIndex(Constants.TIME_START)));
                journey.setTimeEnd(cursor.getLong(cursor.getColumnIndex(Constants.TIME_END)));

                journeys.add(journey);
            } while (cursor.moveToNext());

        }
        cursor.close();
        return journeys;
    }
}
