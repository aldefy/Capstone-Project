package techgravy.nextstop.ui.details;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import techgravy.nextstop.ui.details.model.WeatherResponse;

/**
 * Created by aditlal on 12/01/17 - 12.
 */

public interface WeatherApi {
    @GET("forecast")
    Observable<WeatherResponse> getTodaysForecast(@Query("q") String city, @Query("APPID") String apiKey,@Query("units") String units);
}
