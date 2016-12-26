package techgravy.nextstop.ui.home.model;

import java.util.List;

import auto.json.AutoJson;

/**
 * Created by aditlal on 26/12/16.
 */
@AutoJson
public abstract class SearchPlaceModel {

    @AutoJson.Field(name = "results")
    public abstract List<SearchResults> mResultsList();

}
