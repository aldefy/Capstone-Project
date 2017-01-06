package techgravy.nextstop.ui.home;

import java.util.HashMap;
import java.util.List;

import techgravy.nextstop.ui.home.model.Places;
import techgravy.nextstop.ui.landing.PersonaTags;

/**
 * Created by aditlal on 24/12/16.
 */

public interface HomeContract {

    interface View {
        void attachUserPlaces(List<Places> placesList);

        void attachPlaces(List<Places> placesList);

        void filterPersonaTags(List<PersonaTags> personaTags);

        void showProgress();

        void hideProgress();

        void dataError(Throwable e);
    }

    interface Presenter {
        void filterPlaces(HashMap<String, Boolean> personaTagsList);

        void fetchUserPlaces();

        void fetchUserPersonaTags();
    }
}
