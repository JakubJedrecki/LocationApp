package example.app.location.locationapp.models;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Kuba on 17.06.2017.
 */

public class JourneyLocation {

    private int id;
    private long journeyId;
    private LatLng latLng;
    private Long timestamp;

    public JourneyLocation() {}

    public JourneyLocation(long journeyId, LatLng latLng, Long timestamp) {
        this.journeyId = journeyId;
        this.latLng = latLng;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(long journeyId) {
        this.journeyId = journeyId;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
