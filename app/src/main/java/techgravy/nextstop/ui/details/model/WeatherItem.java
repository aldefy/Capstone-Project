package techgravy.nextstop.ui.details.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
@JsonObject
public class WeatherItem {

    @JsonField(name = "icon")
    private String icon;

    @JsonField(name = "description")
    private String description;

    @JsonField(name = "main")
    private String main;

    @JsonField(name = "id")
    private int id;

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getMain() {
        return main;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}