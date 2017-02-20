package techgravy.nextstop.data;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import techgravy.nextstop.utils.Constants;

/**
 * Created by aditlal on 17/12/16 - 17.
 */
@JsonObject
public class FacebookPicture {
    @JsonField
    private FacebookPictureData data;

    public FacebookPictureData getData() {
        return data;
    }

    public void setData(FacebookPictureData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return Constants.CLASS_POJO + " [data = " + data + "]";
    }
}