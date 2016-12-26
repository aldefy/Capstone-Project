package techgravy.nextstop.ui.home.model;

import auto.json.AutoJson;

/**
 * Created by aditlal on 26/12/16.
 */
@AutoJson
public abstract class SearchResultPhoto {

    @AutoJson.Field(name = "photo_reference")
    public abstract String photo_reference();

    @AutoJson.Field(name = "height")

    public abstract String height();

    @AutoJson.Field(name = "width")
    public abstract String width();
}
