package techgravy.nextstop.ui.home.model;


import android.os.Parcelable;

import com.google.firebase.database.PropertyName;

import java.util.List;

import auto.json.AutoJson;
import techgravy.nextstop.utils.PlacesLatLngConverter;

/**
 * Created by aditlal on 23/12/16.
 */
@AutoJson
public abstract class Places implements Parcelable {
    @AutoJson.Field
    public abstract String place_id();

    @AutoJson.Field
    public abstract String picturesque();

    @AutoJson.Field
    public abstract String desc();

    @AutoJson.Field(name = "location", typeConverter = PlacesLatLngConverter.class)
    @PropertyName("location")
    public abstract PlaceLatLng location();

    @AutoJson.Field(name = "photos")
    @PropertyName("photos")
    public abstract List<String> photos();

    @AutoJson.Field
    public abstract String tag();

    @AutoJson.Field
    public abstract String art();

    @AutoJson.Field
    public abstract String romantic();

    @AutoJson.Field
    public abstract String sports();

    @AutoJson.Field
    public abstract String cityscape();

    @AutoJson.Field
    public abstract String enterainment();

    @AutoJson.Field
    public abstract String sun();

    @AutoJson.Field
    public abstract String currency();

    @AutoJson.Field
    public abstract String island();

    @AutoJson.Field
    public abstract String country();

    @AutoJson.Field
    public abstract String history();

    @AutoJson.Field
    public abstract String luxury();

    @AutoJson.Field
    public abstract String beaches();

    @AutoJson.Field
    public abstract String adventure();

    @AutoJson.Field

    public abstract String landmarks();

    @AutoJson.Field
    public abstract String family();

    @AutoJson.Field
    public abstract String food();

    @AutoJson.Field
    public abstract String language();

    @AutoJson.Field
    public abstract String shopping();

    @AutoJson.Field
    public abstract String place();

    @AutoJson.Field
    public abstract String nightlife();


}