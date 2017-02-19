package techgravy.nextstop.ui.details.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class Main {
    @JsonField(name = "temp")
    private double temp;

    @JsonField(name = "temp_min")
    private double tempMin;

    @JsonField(name = "grnd_level")
    private double grndLevel;

    @JsonField(name = "temp_kf")
    private double tempKf;

    @JsonField(name = "humidity")
    private int humidity;

    @JsonField(name = "pressure")
    private double pressure;

    @JsonField(name = "sea_level")
    private double seaLevel;

    @JsonField(name = "temp_max")
    private double tempMax;

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getTemp() {
        return temp;
    }

    public void setTempMin(double tempMin) {
        this.tempMin = tempMin;
    }

    public double getTempMin() {
        return tempMin;
    }

    public void setGrndLevel(double grndLevel) {
        this.grndLevel = grndLevel;
    }

    public double getGrndLevel() {
        return grndLevel;
    }

    public void setTempKf(double tempKf) {
        this.tempKf = tempKf;
    }

    public double getTempKf() {
        return tempKf;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getPressure() {
        return pressure;
    }

    public void setSeaLevel(double seaLevel) {
        this.seaLevel = seaLevel;
    }

    public double getSeaLevel() {
        return seaLevel;
    }

    public void setTempMax(double tempMax) {
        this.tempMax = tempMax;
    }

    public double getTempMax() {
        return tempMax;
    }
}