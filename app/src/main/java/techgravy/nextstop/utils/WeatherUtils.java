package techgravy.nextstop.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.meteocons_typeface_library.Meteoconcs;
import com.mikepenz.weather_icons_typeface_library.WeatherIcons;

import techgravy.nextstop.R;
import timber.log.Timber;

/**
 * Created by aditlal on 12/01/17 - 12.
 */

public class WeatherUtils {
    public final static String ICON_PACK_METEOCONCS = "Meteoconcs Pack";
    public final static String ICON_PACK_DEFAULT = "Default Pack";

    public static IconicsDrawable getWeatherIconFromWeather(Context context, int weatherId, String iconPack) {
        Timber.tag("WeatherIcon");
        Timber.d(weatherId + "");
        switch (iconPack) {
            case ICON_PACK_METEOCONCS:
                return getMeteoconcsIcons(context, weatherId);
            case ICON_PACK_DEFAULT:
                return getDefaultIcons(context, weatherId);
            default:
                return new IconicsDrawable(context)
                        .icon(Meteoconcs.Icon.met_temperature)
                        .color(ContextCompat.getColor(context, R.color.white))
                        .sizeDp(36);
        }
    }

    private static IconicsDrawable getDefaultIcons(Context context, int weatherId) {
        if (weatherId >= 200 && weatherId <= 232) {
            return new IconicsDrawable(context)
                    .icon(WeatherIcons.Icon.wic_storm_warning)
                    .color(ContextCompat.getColor(context, R.color.white))
                    .sizeDp(36);
        } else if (weatherId >= 300 && weatherId <= 321) {
            return new IconicsDrawable(context)
                    .icon(WeatherIcons.Icon.wic_rain)
                    .color(ContextCompat.getColor(context, R.color.white))
                    .sizeDp(36);
        } else if (weatherId >= 500 && weatherId <= 504) {
            return new IconicsDrawable(context)
                    .icon(WeatherIcons.Icon.wic_rain)
                    .color(ContextCompat.getColor(context, R.color.white))
                    .sizeDp(36);
        } else if (weatherId == 511) {
            return new IconicsDrawable(context)
                    .icon(WeatherIcons.Icon.wic_snow)
                    .color(ContextCompat.getColor(context, R.color.white))
                    .sizeDp(36);
        } else if (weatherId >= 520 && weatherId <= 531) {
            return new IconicsDrawable(context)
                    .icon(WeatherIcons.Icon.wic_rain)
                    .color(ContextCompat.getColor(context, R.color.white))
                    .sizeDp(36);
        } else if (weatherId >= 600 && weatherId <= 622) {
            return new IconicsDrawable(context)
                    .icon(WeatherIcons.Icon.wic_forecast_io_snow)
                    .color(ContextCompat.getColor(context, R.color.white))
                    .sizeDp(36);
        } else if (weatherId >= 701 && weatherId <= 761) {
            return new IconicsDrawable(context)
                    .icon(WeatherIcons.Icon.wic_fog)
                    .color(ContextCompat.getColor(context, R.color.white))
                    .sizeDp(36);
        } else if (weatherId == 761 || weatherId == 781) {
            return new IconicsDrawable(context)
                    .icon(WeatherIcons.Icon.wic_storm_warning)
                    .color(ContextCompat.getColor(context, R.color.white))
                    .sizeDp(36);
        } else if (weatherId == 800) {
            return new IconicsDrawable(context)
                    .icon(WeatherIcons.Icon.wic_day_sunny_overcast)
                    .color(ContextCompat.getColor(context, R.color.white))
                    .sizeDp(36);
        } else if (weatherId == 801) {
            return new IconicsDrawable(context)
                    .icon(WeatherIcons.Icon.wic_cloudy_windy)
                    .color(ContextCompat.getColor(context, R.color.white))
                    .sizeDp(36);
        } else if (weatherId >= 802 && weatherId <= 804) {
            return new IconicsDrawable(context)
                    .icon(WeatherIcons.Icon.wic_cloudy)
                    .color(ContextCompat.getColor(context, R.color.white))
                    .sizeDp(36);
        } else {
            return new IconicsDrawable(context)
                    .icon(WeatherIcons.Icon.wic_thermometer)
                    .color(ContextCompat.getColor(context, R.color.white))
                    .sizeDp(36);
        }
    }

    private static IconicsDrawable getMeteoconcsIcons(Context context, int weatherId) {
        if (weatherId >= 200 && weatherId <= 232) {
            return new IconicsDrawable(context)
                    .icon(Meteoconcs.Icon.met_cloud_flash)
                    .color(ContextCompat.getColor(context, R.color.white))
                    .sizeDp(36);
        } else if (weatherId >= 300 && weatherId <= 321) {
            return new IconicsDrawable(context)
                    .icon(Meteoconcs.Icon.met_rain_inv)
                    .color(ContextCompat.getColor(context, R.color.white))
                    .sizeDp(36);
        } else if (weatherId >= 500 && weatherId <= 504) {
            return new IconicsDrawable(context)
                    .icon(Meteoconcs.Icon.met_rain)
                    .color(ContextCompat.getColor(context, R.color.white))
                    .sizeDp(36);
        } else if (weatherId == 511) {
            return new IconicsDrawable(context)
                    .icon(Meteoconcs.Icon.met_snow)
                    .color(ContextCompat.getColor(context, R.color.white))
                    .sizeDp(36);
        } else if (weatherId >= 520 && weatherId <= 531) {
            return new IconicsDrawable(context)
                    .icon(Meteoconcs.Icon.met_rain)
                    .color(ContextCompat.getColor(context, R.color.white))
                    .sizeDp(36);
        } else if (weatherId >= 600 && weatherId <= 622) {
            return new IconicsDrawable(context)
                    .icon(Meteoconcs.Icon.met_snow_heavy)
                    .color(ContextCompat.getColor(context, R.color.white))
                    .sizeDp(36);
        } else if (weatherId >= 701 && weatherId <= 761) {
            return new IconicsDrawable(context)
                    .icon(Meteoconcs.Icon.met_fog)
                    .color(ContextCompat.getColor(context, R.color.white))
                    .sizeDp(36);
        } else if (weatherId == 761 || weatherId == 781) {
            return new IconicsDrawable(context)
                    .icon(Meteoconcs.Icon.met_cloud_flash_alt)
                    .color(ContextCompat.getColor(context, R.color.white))
                    .sizeDp(36);
        } else if (weatherId == 800) {
            return new IconicsDrawable(context)
                    .icon(Meteoconcs.Icon.met_sun)
                    .color(ContextCompat.getColor(context, R.color.white))
                    .sizeDp(36);
        } else if (weatherId == 801) {
            return new IconicsDrawable(context)
                    .icon(Meteoconcs.Icon.met_sun_inv)
                    .color(ContextCompat.getColor(context, R.color.white))
                    .sizeDp(36);
        } else if (weatherId >= 802 && weatherId <= 804) {
            return new IconicsDrawable(context)
                    .icon(Meteoconcs.Icon.met_cloud)
                    .color(ContextCompat.getColor(context, R.color.white))
                    .sizeDp(36);
        } else {
            return new IconicsDrawable(context)
                    .icon(Meteoconcs.Icon.met_compass)
                    .color(ContextCompat.getColor(context, R.color.white))
                    .sizeDp(36);
        }

    }

    public static String formatTemperature(Context context, double temperature, String isMetric) {
        // Data stored in Celsius by default.  If user prefers to see in Fahrenheit, convert
        // the values here.
        String suffix = "\u00B0";
        if (!isMetric.equalsIgnoreCase(context.getString(R.string.value_units_metric))) {
            temperature = (temperature * 1.8) + 32;
        }


        // For presentation, assume the user doesn't care about tenths of a degree.
        String returnedString = String.format(context.getString(R.string.format_temperature), temperature);
        if (!isMetric.equalsIgnoreCase(context.getString(R.string.value_units_metric))) {
            returnedString = returnedString.concat(" \u2109");//F
        } else {
            returnedString = returnedString.concat(" \u2103");//C

        }
        Timber.d("temperature=" + temperature + "->" + returnedString);
        return returnedString;
    }


}
