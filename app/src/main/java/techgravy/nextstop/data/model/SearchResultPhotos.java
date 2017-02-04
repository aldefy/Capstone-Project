package techgravy.nextstop.data.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by aditlal on 27/12/16.
 */
@JsonObject
class SearchResultPhotos {

    /*Empty Constructor*/
    SearchResultPhotos() {
    }

    @JsonField(name = "photo_reference")
    private String photo_reference;

    @JsonField(name = "height")
    public Double height;

    @JsonField(name = "width")
    public Double width;

    public String getPhoto_reference() {
        return photo_reference;
    }

    public void setPhoto_reference(String photo_reference) {
        this.photo_reference = photo_reference;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    @Override
    public String toString() {
        return "SearchResultPhotos{" +
                "photo_reference='" + photo_reference + '\'' +
                ", height='" + height + '\'' +
                ", width='" + width + '\'' +
                '}';
    }
}
