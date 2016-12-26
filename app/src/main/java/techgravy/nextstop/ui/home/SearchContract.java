package techgravy.nextstop.ui.home;

import java.util.List;

import techgravy.nextstop.ui.home.model.SearchPlaceModel;

/**
 * Created by aditlal on 26/12/16.
 */

public interface SearchContract {
    public interface View {
        void showResults(List<SearchPlaceModel> results);

        void searchError(String errorMsg);
    }

    public interface Presenter {
        void queryForString(String query);
    }
}
