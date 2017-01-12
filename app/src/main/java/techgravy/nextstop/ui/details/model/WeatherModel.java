package techgravy.nextstop.ui.details.model;

import com.google.auto.value.AutoValue;

/**
 * Created by aditlal on 12/01/17 - 12.
 */
@AutoValue
public abstract class WeatherModel {

    public abstract int weatherID();

    public abstract double temp();

    public abstract String icon();

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract WeatherModel.Builder weatherID(int id);

        public abstract WeatherModel.Builder temp(double temp);

        public abstract WeatherModel.Builder icon(String icon);

        public abstract WeatherModel build();
    }

    public static WeatherModel.Builder builder() {
        return new AutoValue_WeatherModel.Builder();
    }
}
