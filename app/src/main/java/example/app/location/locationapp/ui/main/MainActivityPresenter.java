package example.app.location.locationapp.ui.main;

import android.graphics.Color;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import example.app.location.locationapp.R;
import example.app.location.locationapp.services.RouteService;

/**
 * Created by Kuba on 15.06.2017.
 */

public class MainActivityPresenter implements MainActivityContract.Presenter {

    private MainActivityContract.View mainView;
    private PolylineOptions polyline;
    private boolean recordJourney;
    private boolean startingJourneysActivity;
    private boolean isFirstLocation = true;

    /**
     * Presenter constructor
     * @param mainView view MainActivity
     */
    public MainActivityPresenter(MainActivityContract.View mainView) {
        this.mainView = mainView;

        polyline = new PolylineOptions();
        polyline.geodesic(true);
        polyline.width(5);
        polyline.color(Color.BLUE);
    }

    /**
     * Change recordJourney status, TrackingButton text
     * Start RouteService with new recordJourney status
     */
    @Override
    public void toggleTracking() {
        if(mainView.checkPermissions()) {
            recordJourney = !recordJourney;
            if (recordJourney) {
                mainView.setTrackingButtonText(R.string.stop_tracking);
            } else {
                mainView.setTrackingButtonText(R.string.start_tracking);
            }
            mainView.startRouteService(recordJourney);
        } else {
            mainView.requestPermissions();
        }
    }

    /**
     * Updates journey polyline with new location
     * Draw updated polyline on map and mark new location
     * If it's first location on map zoom and move camera
     * @param location new location
     */
    @Override
    public void updateJourneyPolyline(Location location) {
        if(location != null) {
            LatLng locationLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            if(isFirstLocation) {
                isFirstLocation = false;
                mainView.moveAndZoomCamera(17, locationLatLng);
            }
            polyline.add(locationLatLng);
            mainView.drawPolyline(polyline);
            mainView.markPositionOnMap(locationLatLng);
        }
    }

    /**
     * If service is off start it with recordJourney status false
     */
    @Override
    public void activityOnStart() {
        if(!RouteService.isServiceRunning && mainView.checkPermissions()){
            mainView.startRouteService(false);
        }
    }

    /**
     * If MainActivity is stopping, it's not recording Journey
     * and it's not starting JourneysActivity( app is going into background )
     * stop RouteService.
     */
    @Override
    public void activityOnStop() {
        if(!recordJourney && !startingJourneysActivity){
            mainView.stopRouteService();
        }
    }

    /**
     * Set startingJourneysActivity flag.
     * @param isStarting JourneysActivity flag
     */
    @Override
    public void startingJourneysActivity(boolean isStarting){
        startingJourneysActivity = isStarting;
    }

    /**
     * Stop the service when activity is destroyed
     */
    @Override
    public void onDestroy() {
        mainView.stopRouteService();
    }
}
