package techgravy.nextstop.ui.home.model;

import auto.json.AutoJson;

/**
 * Created by aditlal on 26/12/16.
 */
@AutoJson
public abstract class SearchResultGeometry {
    @AutoJson.Field(name = "location")
    public abstract PlaceLatLng location();
}
