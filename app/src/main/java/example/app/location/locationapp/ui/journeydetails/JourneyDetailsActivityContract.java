package example.app.location.locationapp.ui.journeydetails;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import example.app.location.locationapp.models.Journey;
import example.app.location.locationapp.models.JourneyLocation;

/**
 * Created by Kuba on 22.06.2017.
 */

public interface JourneyDetailsActivityContract {

    interface View {
        void drawPolyline(PolylineOptions polyline);
        void moveAndZoomCamera(float zoom, LatLng position);
        void markStartPositionOnMap(LatLng position, String timeStart);
        void markEndPositionOnMap(LatLng position, String timeEnd);
    }

    interface Presenter {
        void drawJourney(int journeyId);
    }

    interface Interactor {
        List<JourneyLocation> getJourneyLocations(int journeyId);
        Journey getJourneyDetails(int journeyId);
    }
}
