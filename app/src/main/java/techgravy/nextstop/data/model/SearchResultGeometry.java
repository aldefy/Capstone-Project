package techgravy.nextstop.data.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import techgravy.nextstop.ui.home.model.PlaceLatLng;

/**
 * Created by aditlal on 27/12/16.
 */
@JsonObject
public class SearchResultGeometry {
    @JsonField(name = "location")
    private PlaceLatLng location;

    public PlaceLatLng getLocation() {
        return location;
    }

    public void setLocation(PlaceLatLng location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "SearchResultGeometry{" +
                "location=" + location +
                '}';
    }
}
