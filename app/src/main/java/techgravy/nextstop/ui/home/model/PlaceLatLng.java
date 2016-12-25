package techgravy.nextstop.ui.home.model;

/**
 * Created by aditlal on 23/12/16.
 */
class PlaceLatLng {
    private Double lng;

    private Double lat;

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return "ClassPojo [lng = " + lng + ", lat = " + lat + "]";
    }
}
