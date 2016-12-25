package techgravy.nextstop.ui.home.model;

import android.os.Parcelable;

import auto.json.AutoJson;

/**
 * Created by aditlal on 23/12/16.
 */
@AutoJson
public abstract class PlaceLatLng implements Parcelable {
    @AutoJson.Field
    public abstract Double lng();

    @AutoJson.Field
    public abstract Double lat();

    @AutoJson.Builder
    public static abstract class Builder {
        public abstract Builder lat(Double lat);

        public abstract Builder lng(Double lng);

        public abstract PlaceLatLng build();
    }

    public static Builder builder() {
        return new AutoJson_PlaceLatLng.Builder();
    }
}
