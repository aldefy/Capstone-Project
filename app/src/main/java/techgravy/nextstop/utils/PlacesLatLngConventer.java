package techgravy.nextstop.utils;

import com.bluelinelabs.logansquare.typeconverters.TypeConverter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;

import techgravy.nextstop.ui.home.model.PlaceLatLng;

/**
 * Created by aditlal on 25/12/16.
 */

public class PlacesLatLngConventer implements TypeConverter<PlaceLatLng> {
    double lat = 0, lng = 0;

    @Override
    public PlaceLatLng parse(JsonParser jsonParser) throws IOException {
        String name = jsonParser.getCurrentName();

        if (name.equals("lat")) {
            jsonParser.nextToken();
            lat = jsonParser.getDoubleValue();
        } else if (name.equals("lng")) {
            jsonParser.nextToken();
            lng = jsonParser.getDoubleValue();
        }
        return PlaceLatLng.builder().lat(lat).lng(lng).build();

    }

    @Override
    public void serialize(PlaceLatLng object, String fieldName, boolean writeFieldNameForObject, JsonGenerator jsonGenerator) throws IOException {

    }
}