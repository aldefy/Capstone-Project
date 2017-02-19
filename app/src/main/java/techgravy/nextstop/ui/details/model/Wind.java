package techgravy.nextstop.ui.details.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class Wind {

    @JsonField(name = "deg")
    private double deg;

    @JsonField(name = "speed")
    private double speed;

    public void setDeg(double deg) {
        this.deg = deg;
    }

    public double getDeg() {
        return deg;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
    }
}