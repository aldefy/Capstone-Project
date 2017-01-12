package techgravy.nextstop.ui.details;

import java.util.List;

import techgravy.nextstop.ui.details.model.WeatherModel;
import techgravy.nextstop.ui.home.model.Places;

/**
 * Created by aditlal on 24/12/16.
 */

public interface DetailsContract {

    interface View {
        void loadPlaceTags(List<String> placeTags);
        void loadWeather(WeatherModel model);
    }

    interface Presenter {
        void computePlaceTags(Places places);
        void getWeather(String cityName);
        void onStop();
    }
}
