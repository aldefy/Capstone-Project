package techgravy.nextstop.data.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;
import java.util.Random;

import techgravy.nextstop.BuildConfig;

/**
 * Created by aditlal on 27/12/16.
 */

@JsonObject
public class SearchResults {
    /*Empty Constructor*/
    public SearchResults() {
    }


    public SearchResults(String id, String reference, String name) {
        mId = id;
        mReference = reference;
        mName = name;
    }

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

    public String generatePhotoUrl() {
        int min = 0;
        int max = getSearchResultPhotosList().size();
        Random rand = new Random();
        SearchResultPhotos photo = getSearchResultPhotosList().get(rand.nextInt((max - min)));
        return BuildConfig.GOOGLE_URL + "photo?maxwidth=" + "800"
                + "&photoreference=" + photo.getPhoto_reference() + "&key=" + BuildConfig.GOOGLE_API_KEY;
    }


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
