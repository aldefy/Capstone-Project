package techgravy.nextstop.ui.home.model;


import com.google.firebase.database.PropertyName;

import java.util.List;

/**
 * Created by aditlal on 23/12/16.
 */

public class Places {
    private String place_id;
    private String picturesque;
    private String desc;
    @PropertyName("location")
    private PlaceLatLng location;

    @PropertyName("photos")
    private List<String> photos;

    private String tag;

    private String art;

    private String romantic;

    private String sports;

    private String cityscape;

    private String enterainment;

    private String sun;

    private String currency;

    private String island;

    private String country;

    private String history;

    private String luxury;

    private String beaches;

    private String adventure;

    private String landmarks;

    private String family;

    private String food;

    private String language;

    private String shopping;

    private String place;

    private String nightlife;

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getPicturesque() {
        return picturesque;
    }

    public void setPicturesque(String picturesque) {
        this.picturesque = picturesque;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public PlaceLatLng getLocation() {
        return location;
    }

    public void setLocation(PlaceLatLng location) {
        this.location = location;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getArt() {
        return art;
    }

    public void setArt(String art) {
        this.art = art;
    }

    public String getRomantic() {
        return romantic;
    }

    public void setRomantic(String romantic) {
        this.romantic = romantic;
    }

    public String getSports() {
        return sports;
    }

    public void setSports(String sports) {
        this.sports = sports;
    }

    public String getCityscape() {
        return cityscape;
    }

    public void setCityscape(String cityscape) {
        this.cityscape = cityscape;
    }

    public String getEnterainment() {
        return enterainment;
    }

    public void setEnterainment(String enterainment) {
        this.enterainment = enterainment;
    }

    public String getSun() {
        return sun;
    }

    public void setSun(String sun) {
        this.sun = sun;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getIsland() {
        return island;
    }

    public void setIsland(String island) {
        this.island = island;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public String getLuxury() {
        return luxury;
    }

    public void setLuxury(String luxury) {
        this.luxury = luxury;
    }

    public String getBeaches() {
        return beaches;
    }

    public void setBeaches(String beaches) {
        this.beaches = beaches;
    }

    public String getAdventure() {
        return adventure;
    }

    public void setAdventure(String adventure) {
        this.adventure = adventure;
    }

    public String getLandmarks() {
        return landmarks;
    }

    public void setLandmarks(String landmarks) {
        this.landmarks = landmarks;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getShopping() {
        return shopping;
    }

    public void setShopping(String shopping) {
        this.shopping = shopping;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getNightlife() {
        return nightlife;
    }

    public void setNightlife(String nightlife) {
        this.nightlife = nightlife;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    @Override
    public String toString() {
        return "ClassPojo [place_id = " + place_id + ", picturesque = " + picturesque + ", desc = " + desc + ", location = " + location + ", tag = " + tag + ", art = " + art + ", romantic = " + romantic + ", sports = " + sports + ", cityscape = " + cityscape + ", enterainment = " + enterainment + ", sun = " + sun + ", currency = " + currency + ", island = " + island + ", country = " + country + ", history = " + history + ", luxury = " + luxury + ", beaches = " + beaches + ", adventure = " + adventure + ", landmarks = " + landmarks + ", family = " + family + ", food = " + food + ", language = " + language + ", shopping = " + shopping + ", place = " + place + ", nightlife = " + nightlife + ", photos =" + photos + "]";
    }

}