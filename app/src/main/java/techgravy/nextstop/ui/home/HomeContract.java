package techgravy.nextstop.ui.home;

import java.util.List;

import techgravy.nextstop.ui.home.model.Places;

/**
 * Created by aditlal on 24/12/16.
 */

public interface HomeContract {

    interface View {
        void attachData(List<Places> placesList);

        void showProgress();

        void hideProgress();

        void dataError(Throwable e);
    }

    interface Presenter {
        void fetchListOfPlaces();

    }
}
