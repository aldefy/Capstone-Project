package techgravy.nextstop.ui.search;

import java.util.List;

import techgravy.nextstop.ui.search.model.SearchResults;

/**
 * Created by aditlal on 26/12/16.
 */

public interface SearchContract {
    public interface View {
        void showResults(List<SearchResults> results);

        void searchError(String errorMsg);

        void showProgress();

        void hideProgress();
    }

    public interface Presenter {
        void queryForString(String query);
    }
}
