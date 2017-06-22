package example.app.location.locationapp.ui.journeys;

import java.util.List;

import example.app.location.locationapp.models.Journey;

/**
 * Created by Kuba on 18.06.2017.
 */

public class JourneysActivityPresenter implements JourneysActivityContract.Presenter {

    private JourneysActivityContract.View journeysView;
    private JourneysActivityInteractor interactor;
    private List<Journey> journeys;

    /**
     * Presenter constructor
     * @param journeysView view JourneysActivity
     */
    public JourneysActivityPresenter(JourneysActivityContract.View journeysView, JourneysActivityInteractor interactor) {
        this.journeysView = journeysView;
        this.interactor = interactor;
    }

    /**
     * Get all Journeys from database and
     * send list of journeys to view
     */
    @Override
    public void getJourneys() {
        if(journeys == null) {
            journeysView.toggleProgressBar(true);
            journeys = interactor.getAllJourneys();
            journeysView.showJourneys(journeys);
            journeysView.toggleProgressBar(false);
        }
    }
}
