package example.app.location.locationapp.ui.journeydetails;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.app.location.locationapp.R;

import static example.app.location.locationapp.R.string.finish;

public class JourneyDetailsActivity extends AppCompatActivity implements OnMapReadyCallback, JourneyDetailsActivityContract.View {

    @BindView(R.id.toolbar) Toolbar toolbar;
    private GoogleMap map;
    private JourneyDetailsActivityPresenter presenter;
    private int journeyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_details);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.journey_details);
        actionBar.setDisplayHomeAsUpEnabled(true);

        presenter = new JourneyDetailsActivityPresenter(this, new JourneyDetailsActivityInteractor(this));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        journeyId = getIntent().getIntExtra("journeyId", 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        presenter.drawJourney(journeyId);
    }

    /**
     * Draw journey polyline on map
     * @param polyline PolylineOptions journey polyline
     */
    @Override
    public void drawPolyline(PolylineOptions polyline) {
        map.clear();
        map.addPolyline(polyline);
    }

    /**
     * Mark first position on map
     * @param position LatLng first position of journey
     * @param timeStart time when journey started
     */
    @Override
    public void markStartPositionOnMap(LatLng position, String timeStart) {
        MarkerOptions marker = new MarkerOptions();
        marker.position(position);
        marker.title("Start");
        marker.snippet(timeStart);
        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
        map.addMarker(marker);
    }

    /**
     * Mark last position on map
     * @param position LatLng last position
     * @param timeEnd time when journey ended
     */
    @Override
    public void markEndPositionOnMap(LatLng position, String timeEnd) {
        MarkerOptions marker = new MarkerOptions();
        marker.position(position);
        marker.title(getString(finish));
        marker.snippet(timeEnd);
        map.addMarker(marker);
    }

    /**
     * move camera to given position on map and zoom
     * @param zoom zoom value
     * @param position LatLng position on map
     */
    @Override
    public void moveAndZoomCamera(float zoom, LatLng position) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, zoom));
    }
}
