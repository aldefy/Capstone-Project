package techgravy.nextstop.ui.home.model;

import java.util.List;

import auto.json.AutoJson;

/**
 * Created by aditlal on 26/12/16.
 */
@AutoJson
public abstract class SearchResults {

    @AutoJson.Field(name = "photos")
    public abstract List<SearchResultPhoto> photos();

    @AutoJson.Field(name = "id")
    public abstract String id();

    @AutoJson.Field(name = "place_id")
    public abstract String place_id();

    @AutoJson.Field(name = "icon")
    public abstract String icon();

    @AutoJson.Field(name = "name")
    public abstract String name();

    @AutoJson.Field(name = "formatted_address")
    public abstract String formatted_address();

    @AutoJson.Field(name = "types")
    public abstract List<String> types();

    @AutoJson.Field(name = "reference")
    public abstract String reference();

    @AutoJson.Field(name = "geometry")
    public abstract SearchResultGeometry geometry();
}
