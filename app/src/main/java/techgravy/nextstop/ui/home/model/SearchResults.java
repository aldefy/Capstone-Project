package techgravy.nextstop.ui.home.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

/**
 * Created by aditlal on 27/12/16.
 */

@JsonObject
public class SearchResults {

    @JsonField(name = "photos")
    private List<SearchResultPhotos> mSearchResultPhotosList;
    @JsonField(name = "id")
    private String mId;
    @JsonField(name = "place_id")
    private String mPlaceId;
    @JsonField(name = "icon")
    private String mIcon;
    @JsonField(name = "name")
    private String mName;
    @JsonField(name = "formatted_address")
    private String mFormattedAddress;
    @JsonField(name = "types")
    private List<String> mTypes;
    @JsonField(name = "reference")
    private String mReference;
    @JsonField(name = "geometry")
    private SearchResultGeometry geometry;


    public List<SearchResultPhotos> getSearchResultPhotosList() {
        return mSearchResultPhotosList;
    }

    public void setSearchResultPhotosList(List<SearchResultPhotos> searchResultPhotosList) {
        mSearchResultPhotosList = searchResultPhotosList;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getPlaceId() {
        return mPlaceId;
    }

    public void setPlaceId(String placeId) {
        mPlaceId = placeId;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getFormattedAddress() {
        return mFormattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        mFormattedAddress = formattedAddress;
    }

    public List<String> getTypes() {
        return mTypes;
    }

    public void setTypes(List<String> types) {
        mTypes = types;
    }

    public String getReference() {
        return mReference;
    }

    public void setReference(String reference) {
        mReference = reference;
    }

    public SearchResultGeometry getGeometry() {
        return geometry;
    }

    public void setGeometry(SearchResultGeometry geometry) {
        this.geometry = geometry;
    }

    @Override
    public String toString() {
        return "SearchResults{" +
                "mSearchResultPhotosList=" + mSearchResultPhotosList +
                ", mId='" + mId + '\'' +
                ", mPlaceId='" + mPlaceId + '\'' +
                ", mIcon='" + mIcon + '\'' +
                ", mName='" + mName + '\'' +
                ", mFormattedAddress='" + mFormattedAddress + '\'' +
                ", mTypes=" + mTypes +
                ", mReference='" + mReference + '\'' +
                ", geometry=" + geometry +
                '}';
    }
}
