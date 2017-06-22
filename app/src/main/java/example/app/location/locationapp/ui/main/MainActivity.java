package example.app.location.locationapp.ui.main;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import example.app.location.locationapp.R;
import example.app.location.locationapp.services.RouteService;
import example.app.location.locationapp.ui.journeys.JourneysActivity;
import example.app.location.locationapp.util.Constants;

public class MainActivity extends AppCompatActivity implements MainActivityContract.View, OnMapReadyCallback {

    @BindView(R.id.toggleTrackingButton)
    Button toggleTrackingButton;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private static final String TAG = MainActivity.class.getSimpleName();
    private MainActivityPresenter presenter;
    private static final int PERMISSION_REQUEST_LOCATION = 7;
    private GoogleMap map;
    private BroadcastReceiver locationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        requestPermissions();
        presenter = new MainActivityPresenter(this);
        locationBroadcastReceiver = createLocationBroadcastReceiver();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.goToJourneys:
                presenter.startingJourneysActivity(true);
                startActivity(new Intent(this, JourneysActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.activityOnStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(locationBroadcastReceiver, new IntentFilter(Constants.NEW_LOCATION));
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.startingJourneysActivity(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.activityOnStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
    }

    /**
     * Check app permissions and
     * request permissions if not granted
     */
    @Override
    public void requestPermissions() {
        if(!checkPermissions()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                startRouteService(false);
            } else {
                showMessage(R.string.permission_rationale);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * TrackingButton onClick
     */
    @OnClick(R.id.toggleTrackingButton)
    public void toggleTracking() {
        presenter.toggleTracking();
    }

    /**
     * Change button text
     * @param stringResourceId String resourceId
     */
    @Override
    public void setTrackingButtonText(int stringResourceId) {
        toggleTrackingButton.setText(getString(stringResourceId));
    }

    /**
     * Show Toast message to user
     * @param stringResourceId String resourceId
     */
    @Override
    public void showMessage(int stringResourceId) {
        Toast.makeText(this, getString(stringResourceId), Toast.LENGTH_LONG).show();
    }

    /**
     *  Mark the given position on map
     * @param position LatLng location to mark
     */
    @Override
    public void markPositionOnMap(LatLng position) {
        map.addMarker(new MarkerOptions().position(position).title(getString(R.string.current_location)));
    }

    /**
     * zoom map and move to position
     * @param zoom zoom value
     * @param position LatLng
     */
    @Override
    public void moveAndZoomCamera(float zoom, LatLng position) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, zoom));
    }

    /**
     * Start RouteService or pass new Intent if its running.
     * Intent tells service to record or do not record locations.
     * @param recordJourney boolean - should save locations to database
     */
    @Override
    public void startRouteService(boolean recordJourney) {
        Intent serviceIntent = new Intent(this, RouteService.class);
        serviceIntent.putExtra(Constants.RECORD_JOURNEY, recordJourney);
        startService(serviceIntent);
    }

    /**
     * Stops RouteService.
     */
    @Override
    public void stopRouteService() {
        stopService(new Intent(this, RouteService.class));
    }

    /**
     * Clear map and draw new polyline on map.
     * @param polyline PolylineOptions polyline to draw
     */
    @Override
    public void drawPolyline(PolylineOptions polyline) {
        map.clear();
        map.addPolyline(polyline);
    }

    /**
     * Create BroadcastReceiver to receive location updates from RouteService.
     * @return BroadcastReceiver
     */
    @Override
    public BroadcastReceiver createLocationBroadcastReceiver() {
        BroadcastReceiver newLocationReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle newLocationBundle = intent.getBundleExtra("location");
                presenter.updateJourneyPolyline((Location) newLocationBundle.getParcelable("location"));
            }
        };

        return newLocationReceiver;
    }

    /**
     * check if app has required permissions
     * @return boolean
     */
    @Override
    public boolean checkPermissions(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }
}
