package example.app.location.locationapp.ui.journeydetails;

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import example.app.location.locationapp.models.Journey;
import example.app.location.locationapp.models.JourneyLocation;

/**
 * Created by Kuba on 22.06.2017.
 */

public class JourneyDetailsActivityPresenter implements JourneyDetailsActivityContract.Presenter {

    private JourneyDetailsActivityContract.View detailsView;
    private JourneyDetailsActivityInteractor interactor;
    private List<LatLng> route = new ArrayList<>();
    private PolylineOptions polyline;

    /**
     * Presenter constructor
     * @param detailsView
     * @param interactor
     */
    public JourneyDetailsActivityPresenter(JourneyDetailsActivityContract.View detailsView, JourneyDetailsActivityInteractor interactor){
        this.detailsView = detailsView;
        this.interactor = interactor;

        polyline = new PolylineOptions();
        polyline.geodesic(true);
        polyline.width(5);
        polyline.color(Color.BLUE);
    }

    /**
     * Get all joureny locatinos from database
     * Draw journey polyline on map
     */
    @Override
    public void drawJourney(int journeyId) {
        if(journeyId > 0) {
            Journey journey = interactor.getJourneyDetails(journeyId);
            String timeStart = "";
            String timeEnd = "";

            if(journey != null) {
                DateFormat simpleDateFormat = new SimpleDateFormat("HH:mm a", Locale.getDefault());
                timeStart = simpleDateFormat.format(new Date(journey.getTimeStart()));
                timeEnd = simpleDateFormat.format(new Date(journey.getTimeEnd()));
            }

            List<JourneyLocation> journeyLocations = interactor.getJourneyLocations(journeyId);
            for (JourneyLocation journeyLocation : journeyLocations) {
                route.add(journeyLocation.getLatLng());
            }
            if(route.size() > 0) {
                polyline.addAll(route);
                detailsView.moveAndZoomCamera(17, route.get(0));
                detailsView.drawPolyline(polyline);
                detailsView.markStartPositionOnMap(route.get(0), timeStart);
                detailsView.markEndPositionOnMap(route.get(route.size()-1), timeEnd);
            }
        }
    }
}
