package example.app.location.locationapp.ui.journeys;

import java.util.List;

import example.app.location.locationapp.models.Journey;

/**
 * Created by Kuba on 18.06.2017.
 */

public interface JourneysActivityContract {

    interface View {
        void showJourneys(List<Journey> journeys);
        void toggleProgressBar(boolean show);
    }

    interface Presenter {
        void getJourneys();
    }

    interface Interactor {
        List<Journey> getAllJourneys();
    }
}
