package techgravy.nextstop.ui.search.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

/**
 * Created by aditlal on 27/12/16.
 */
@JsonObject
public class SearchResponse {

    @JsonField(name = "results")
    List<SearchResults> mResultsList;

    public List<SearchResults> getResultsList() {
        return mResultsList;
    }

    public void setResultsList(List<SearchResults> resultsList) {
        mResultsList = resultsList;
    }

    @Override
    public String toString() {
        return "SearchResponse{" +
                "mResultsList=" + mResultsList +
                '}';
    }
}
