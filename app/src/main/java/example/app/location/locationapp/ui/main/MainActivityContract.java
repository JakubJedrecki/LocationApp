package example.app.location.locationapp.ui.main;

import android.content.BroadcastReceiver;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

public interface MainActivityContract {
    interface View {
        void requestPermissions();
        void showMessage(int stringResourceId);
        void markPositionOnMap(LatLng location);
        void startRouteService(boolean recordJourney);
        void stopRouteService();
        void drawPolyline(PolylineOptions polyline);
        void moveAndZoomCamera(float zoom, LatLng position);
        void setTrackingButtonText(int stringResourceId);
        BroadcastReceiver createLocationBroadcastReceiver();
        boolean checkPermissions();
    }

    interface Presenter {
        void toggleTracking();
        void updateJourneyPolyline(Location location);
        void activityOnStop();
        void startingJourneysActivity(boolean isStarting);
        void activityOnStart();
        void onDestroy();
    }
}
