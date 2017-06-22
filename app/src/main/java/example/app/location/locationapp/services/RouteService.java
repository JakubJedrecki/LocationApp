package example.app.location.locationapp.services;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import example.app.location.locationapp.models.Journey;
import example.app.location.locationapp.models.JourneyLocation;
import example.app.location.locationapp.data.DatabaseManager;
import example.app.location.locationapp.util.Constants;

public class RouteService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = RouteService.class.getSimpleName();
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private boolean recordLocations;
    private DatabaseManager interactor;
    private static Long currentJourneyId = null;
    public static boolean isServiceRunning;
    private PowerManager.WakeLock wakeLock;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: Service is running");
        interactor = new DatabaseManager(this);
        setGoogleApiClient();
        googleApiClient.connect();
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "RouteServiceWakeLock");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isServiceRunning = true;
        if(intent != null){
            recordLocations = intent.getBooleanExtra(Constants.RECORD_JOURNEY, false);
            if(recordLocations && currentJourneyId == null) {
                wakeLock.acquire();
                Journey journey = new Journey(System.currentTimeMillis());
                currentJourneyId = interactor.createJourney(journey);
            } else if(!recordLocations && currentJourneyId != null) {
                interactor.updateJourneyEndTime(currentJourneyId, System.currentTimeMillis());
                currentJourneyId = null;
                wakeLock.release();
            }
        }

        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * If journey was recorded update end time
     * remove location updates
     * release wakelock
     * set isServiceRunning flag to false
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(recordLocations && currentJourneyId != null) {
            interactor.updateJourneyEndTime(currentJourneyId, System.currentTimeMillis());
            currentJourneyId = null;
        }
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        if(wakeLock.isHeld()) {
            wakeLock.release();
        }
        isServiceRunning = false;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        requestLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended: connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: connection failed");
    }

    @Override
    public void onLocationChanged(Location location) {
        addLocationToDatabase(location);
        sendLocationToActivity(location);
    }

    /**
     * Use LocationServices to request location updates
     * if GoogleApiClient is connected
     */
    private void requestLocationUpdates(){
        if(googleApiClient.isConnected()){
            try{
                setLocationRequest();
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
            } catch (SecurityException ex) {
                Log.e(TAG, "requestLocationUpdates: no permissions", ex);
            }
        }
    }

    /**
     * set new LocationRequest for LocationServices
     */
    private void setLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000); // how often we ask provider for location
        locationRequest.setFastestInterval(5000); // 'steal' location from other app
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); // can pick balanced power accuracy
    }

    /**
     * Build GoogleApiClient instance
     */
    public void setGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * Send new location to Activity via LocalBroadcastManager
     * @param location Location
     */
    private void sendLocationToActivity(Location location){
        Intent newLocationIntent = new Intent(Constants.NEW_LOCATION);
        Bundle locationBundle = new Bundle();
        locationBundle.putParcelable("location", location);
        newLocationIntent.putExtra("location", locationBundle);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(newLocationIntent);
    }

    /**
     * Add location to database if recordLocations is true
     * @param location Location
     */
    private void addLocationToDatabase(Location location){
        if(recordLocations){
            JourneyLocation journeyLocation = new JourneyLocation(currentJourneyId, new LatLng(location.getLatitude(), location.getLongitude()), System.currentTimeMillis());
            interactor.addLocation(journeyLocation);
        }
    }
}
