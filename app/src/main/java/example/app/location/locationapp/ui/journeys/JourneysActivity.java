package example.app.location.locationapp.ui.journeys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.app.location.locationapp.R;
import example.app.location.locationapp.models.Journey;
import example.app.location.locationapp.ui.journeydetails.JourneyDetailsActivity;

public class JourneysActivity extends AppCompatActivity implements JourneysActivityContract.View, JourneysListAdapter.ClickListener{

    @BindView(R.id.journeysRecyclerView) RecyclerView journeysRecyclerView;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.progressBarLayout) RelativeLayout progressBarLayout;
    private List<Journey> journeys;
    private JourneysListAdapter adapter;
    private JourneysActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journeys);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.your_journeys);
        }

        presenter = new JourneysActivityPresenter(this, new JourneysActivityInteractor(this));

        LinearLayoutManager llm = new LinearLayoutManager(this);
        journeys = new ArrayList<>();
        adapter = new JourneysListAdapter(journeys, this, this);
        journeysRecyclerView.setLayoutManager(llm);
        journeysRecyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.journeys_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.goToMaps:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.getJourneys();
    }

    /**
     * Update journeys list and notify adapter about changes.
     * @param journeys list of journeys to display
     */
    @Override
    public void showJourneys(List<Journey> journeys) {
        this.journeys.addAll(journeys);
        adapter.notifyDataSetChanged();
    }

    /**
     * show or hide progress bar
     * @param show
     */
    @Override
    public void toggleProgressBar(boolean show) {
        if(show) {
            progressBarLayout.setVisibility(View.VISIBLE);
        } else {
            progressBarLayout.setVisibility(View.GONE);
        }
    }

    /**
     * Journeys list item click
     * @param view item view
     * @param position item position on list
     */
    @Override
    public void selectedJourney(View view, int position) {
        Intent intent = new Intent(this, JourneyDetailsActivity.class);
        intent.putExtra("journeyId", journeys.get(position).getId());
        startActivity(intent);
    }
}
