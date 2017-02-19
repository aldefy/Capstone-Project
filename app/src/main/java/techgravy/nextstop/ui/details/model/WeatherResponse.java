package techgravy.nextstop.ui.details.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

@JsonObject
public class WeatherResponse {

    @JsonField(name = "city")
    private City city;

    @JsonField(name = "cnt")
    private int cnt;

    @JsonField(name = "cod")
    private String cod;

    @JsonField(name = "message")
    private double message;

    @JsonField(name = "list")
    private List<ListItem> list;

    public void setCity(City city) {
        this.city = city;
    }

    public City getCity() {
        return city;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getCod() {
        return cod;
    }

    public void setMessage(double message) {
        this.message = message;
    }

    public double getMessage() {
        return message;
    }

    public void setList(List<ListItem> list) {
        this.list = list;
    }

    public List<ListItem> getList() {
        return list;
    }
}