package techgravy.nextstop.ui.search;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import techgravy.nextstop.BuildConfig;
import techgravy.nextstop.ui.search.model.SearchResponse;

/**
 * Created by aditlal on 26/12/16.
 */

public interface SearchApiInterface {
    @GET("textsearch/json?key=" + BuildConfig.GOOGLE_API_KEY)
    Observable<SearchResponse> textSearch(@Query("query") String query);
}
